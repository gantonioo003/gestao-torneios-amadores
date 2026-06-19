package com.torneios.dominio.torneio.torneio;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Collection;
import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.evento.EventoBarramento;
import com.torneios.dominio.compartilhado.evento.TorneioCriadoEvento;
import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.estrutura.EstruturaCompeticao;
import com.torneios.dominio.torneio.estrutura.GeradorEstruturaCompeticaoServico;
import com.torneios.dominio.torneio.organizador.OrganizadorTorneioServico;

public class TorneioServico {

    private final TorneioRepositorio torneioRepositorio;
    private final OrganizadorTorneioServico organizadorTorneioServico;
    private final GeradorEstruturaCompeticaoServico geradorEstruturaCompeticaoServico;
    private final ConsultaElegibilidadeParticipanteTorneio consultaElegibilidadeParticipanteTorneio;
    private final PreparacaoCompeticaoInvalidador preparacaoCompeticaoInvalidador;
    private final EventoBarramento barramento;

    public TorneioServico(TorneioRepositorio torneioRepositorio,
                          OrganizadorTorneioServico organizadorTorneioServico,
                          GeradorEstruturaCompeticaoServico geradorEstruturaCompeticaoServico,
                          ConsultaElegibilidadeParticipanteTorneio consultaElegibilidadeParticipanteTorneio,
                          PreparacaoCompeticaoInvalidador preparacaoCompeticaoInvalidador,
                          EventoBarramento barramento) {
        notNull(torneioRepositorio, "O repositorio de torneios e obrigatorio.");
        notNull(organizadorTorneioServico, "O servico de organizador do torneio e obrigatorio.");
        notNull(geradorEstruturaCompeticaoServico, "O gerador de estrutura da competicao e obrigatorio.");
        notNull(consultaElegibilidadeParticipanteTorneio, "A consulta de elegibilidade do participante e obrigatoria.");
        notNull(preparacaoCompeticaoInvalidador, "O invalidador da preparacao e obrigatorio.");
        notNull(barramento, "O barramento de eventos e obrigatorio.");

        this.torneioRepositorio = torneioRepositorio;
        this.organizadorTorneioServico = organizadorTorneioServico;
        this.geradorEstruturaCompeticaoServico = geradorEstruturaCompeticaoServico;
        this.consultaElegibilidadeParticipanteTorneio = consultaElegibilidadeParticipanteTorneio;
        this.preparacaoCompeticaoInvalidador = preparacaoCompeticaoInvalidador;
        this.barramento = barramento;
    }

    public Torneio criarTorneio(TorneioId id,
                                String nome,
                                FormatoTorneio formato,
                                FormatoEquipe formatoEquipe,
                                UsuarioId organizadorId,
                                boolean aceitaSolicitacoes) {
        Torneio torneio = new Torneio(id, nome, formato, formatoEquipe, organizadorId, aceitaSolicitacoes);
        torneioRepositorio.salvar(torneio);
        barramento.postar(new TorneioCriadoEvento(id));
        return torneio;
    }

    public void definirParticipantesIniciais(TorneioId torneioId, UsuarioId organizadorId, Collection<TimeId> timesIds) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        validarParticipantesElegiveis(torneio, timesIds);
        boolean possuiaEstrutura = torneio.getStatus()
                == com.torneios.dominio.compartilhado.enumeracao.StatusTorneio.ESTRUTURA_GERADA;
        torneio.adicionarParticipantes(timesIds);
        invalidarPreparacaoSeNecessario(torneioId, possuiaEstrutura);
        timesIds.forEach(timeId -> consultaElegibilidadeParticipanteTorneio.vincularTimeAoTorneio(timeId, torneioId));
        torneio.fecharSolicitacoes();
        torneioRepositorio.salvar(torneio);
    }

    public void aprovarParticipante(TorneioId torneioId, UsuarioId organizadorId, TimeId timeId) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        validarParticipanteElegivel(torneio, timeId);
        boolean possuiaEstrutura = torneio.getStatus()
                == com.torneios.dominio.compartilhado.enumeracao.StatusTorneio.ESTRUTURA_GERADA;
        torneio.adicionarParticipante(timeId);
        invalidarPreparacaoSeNecessario(torneioId, possuiaEstrutura);
        consultaElegibilidadeParticipanteTorneio.vincularTimeAoTorneio(timeId, torneioId);
        torneioRepositorio.salvar(torneio);
    }

    public void removerParticipante(TorneioId torneioId, UsuarioId organizadorId, TimeId timeId) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        boolean possuiaEstrutura = torneio.getStatus()
                == com.torneios.dominio.compartilhado.enumeracao.StatusTorneio.ESTRUTURA_GERADA;
        torneio.removerParticipante(timeId);
        invalidarPreparacaoSeNecessario(torneioId, possuiaEstrutura);
        consultaElegibilidadeParticipanteTorneio.removerVinculoDoTimeAoTorneio(timeId, torneioId);
        torneioRepositorio.salvar(torneio);
    }

    public void abrirSolicitacoes(TorneioId torneioId, UsuarioId organizadorId) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        torneio.abrirParaSolicitacoes();
        torneioRepositorio.salvar(torneio);
    }

    public void fecharSolicitacoes(TorneioId torneioId, UsuarioId organizadorId) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        torneio.fecharSolicitacoes();
        torneioRepositorio.salvar(torneio);
    }

    public void renomearTorneio(TorneioId torneioId, UsuarioId organizadorId, String novoNome) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        torneio.renomear(novoNome);
        torneioRepositorio.salvar(torneio);
    }

    public void atualizarConfiguracao(TorneioId torneioId,
                                      UsuarioId organizadorId,
                                      String nome,
                                      boolean aceitaSolicitacoes) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        torneio.atualizarConfiguracao(nome, aceitaSolicitacoes);
        torneioRepositorio.salvar(torneio);
    }

    public EstruturaCompeticao gerarEstruturaCompeticao(TorneioId torneioId, UsuarioId organizadorId) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        EstruturaCompeticao estruturaCompeticao = geradorEstruturaCompeticaoServico.gerarPorSorteio(torneio);
        torneioRepositorio.salvar(torneio);
        return estruturaCompeticao;
    }

    public EstruturaCompeticao gerarEstruturaManual(TorneioId torneioId,
                                                    UsuarioId organizadorId,
                                                    List<TimeId> ordemManualParticipantes) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        EstruturaCompeticao estruturaCompeticao = geradorEstruturaCompeticaoServico.gerarManual(
                torneio, ordemManualParticipantes);
        torneioRepositorio.salvar(torneio);
        return estruturaCompeticao;
    }

    public void iniciarTorneio(TorneioId torneioId, UsuarioId organizadorId) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        torneio.iniciar();
        torneioRepositorio.salvar(torneio);
    }

    public void finalizarTorneio(TorneioId torneioId, UsuarioId organizadorId) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        torneio.finalizar();
        torneioRepositorio.salvar(torneio);
    }

    public HistoricoEdicaoTorneio repetirTorneio(TorneioId torneioId,
                                                 UsuarioId organizadorId,
                                                 boolean abrirSolicitacoes) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        HistoricoEdicaoTorneio historico = torneio.repetirComoNovaEdicao(abrirSolicitacoes);
        torneioRepositorio.salvar(torneio);
        return historico;
    }

    public List<Torneio> listarTorneiosDisponiveis() {
        return torneioRepositorio.listarTodos().stream()
                .filter(Torneio::estaDisponivelParaVisualizacao)
                .toList();
    }

    public List<Torneio> listarTorneiosDoOrganizador(UsuarioId organizadorId) {
        return torneioRepositorio.listarPorOrganizador(organizadorId);
    }

    public Torneio obterTorneio(TorneioId torneioId) {
        return torneioRepositorio.buscarPorId(torneioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Torneio nao encontrado."));
    }

    private void validarParticipantesElegiveis(Torneio torneio, Collection<TimeId> timesIds) {
        timesIds.forEach(timeId -> validarParticipanteElegivel(torneio, timeId));
    }

    private void validarParticipanteElegivel(Torneio torneio, TimeId timeId) {
        if (!consultaElegibilidadeParticipanteTorneio.timeExiste(timeId)) {
            throw new RegraDeNegocioException("O time informado nao existe.");
        }
        if (!consultaElegibilidadeParticipanteTorneio.timePossuiTecnico(timeId)) {
            throw new RegraDeNegocioException("Todo time participante do torneio deve possuir tecnico associado.");
        }
        if (consultaElegibilidadeParticipanteTorneio.quantidadeJogadores(timeId)
                < torneio.getFormatoEquipe().getQuantidadeJogadores()) {
            throw new RegraDeNegocioException(
                    "O time nao possui jogadores suficientes para o formato de equipe do torneio.");
        }
    }

    private void invalidarPreparacaoSeNecessario(TorneioId torneioId, boolean possuiaEstrutura) {
        if (possuiaEstrutura) {
            preparacaoCompeticaoInvalidador.invalidar(torneioId);
        }
    }
}
