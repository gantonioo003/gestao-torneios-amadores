package com.torneios.dominio.torneio.torneio;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
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

    public TorneioServico(TorneioRepositorio torneioRepositorio,
                          OrganizadorTorneioServico organizadorTorneioServico,
                          GeradorEstruturaCompeticaoServico geradorEstruturaCompeticaoServico,
                          ConsultaElegibilidadeParticipanteTorneio consultaElegibilidadeParticipanteTorneio) {
        this.torneioRepositorio = Objects.requireNonNull(torneioRepositorio,
                "O repositorio de torneios e obrigatorio.");
        this.organizadorTorneioServico = Objects.requireNonNull(organizadorTorneioServico,
                "O servico de organizador do torneio e obrigatorio.");
        this.geradorEstruturaCompeticaoServico = Objects.requireNonNull(geradorEstruturaCompeticaoServico,
                "O gerador de estrutura da competicao e obrigatorio.");
        this.consultaElegibilidadeParticipanteTorneio = Objects.requireNonNull(consultaElegibilidadeParticipanteTorneio,
                "A consulta de elegibilidade do participante e obrigatoria.");
    }

    public Torneio criarTorneio(TorneioId id,
                                String nome,
                                FormatoTorneio formato,
                                FormatoEquipe formatoEquipe,
                                UsuarioId organizadorId,
                                boolean aceitaSolicitacoes) {
        Torneio torneio = new Torneio(id, nome, formato, formatoEquipe, organizadorId, aceitaSolicitacoes);
        torneioRepositorio.salvar(torneio);
        return torneio;
    }

    public void definirParticipantesIniciais(TorneioId torneioId, UsuarioId organizadorId, Collection<TimeId> timesIds) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        validarParticipantesElegiveis(torneio, timesIds);
        torneio.adicionarParticipantes(timesIds);
        timesIds.forEach(timeId -> consultaElegibilidadeParticipanteTorneio.vincularTimeAoTorneio(timeId, torneioId));
        torneio.fecharSolicitacoes();
        torneioRepositorio.salvar(torneio);
    }

    public void aprovarParticipante(TorneioId torneioId, UsuarioId organizadorId, TimeId timeId) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        validarParticipanteElegivel(torneio, timeId);
        torneio.adicionarParticipante(timeId);
        consultaElegibilidadeParticipanteTorneio.vincularTimeAoTorneio(timeId, torneioId);
        torneioRepositorio.salvar(torneio);
    }

    public void removerParticipante(TorneioId torneioId, UsuarioId organizadorId, TimeId timeId) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        torneio.removerParticipante(timeId);
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

    public EstruturaCompeticao gerarEstruturaCompeticao(TorneioId torneioId, UsuarioId organizadorId) {
        Torneio torneio = obterTorneio(torneioId);
        organizadorTorneioServico.validarPermissao(torneio, organizadorId);
        EstruturaCompeticao estruturaCompeticao = geradorEstruturaCompeticaoServico.gerar(torneio);
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
}
