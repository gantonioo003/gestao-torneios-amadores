package com.torneios.dominio.competicao.partida;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.evento.EventoBarramento;
import com.torneios.dominio.compartilhado.evento.ResultadoRegistradoEvento;
import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.chaveamento.Chaveamento;
import com.torneios.dominio.competicao.chaveamento.ChaveamentoServico;
import com.torneios.dominio.competicao.classificacao.Classificacao;
import com.torneios.dominio.competicao.classificacao.ClassificacaoServico;
import com.torneios.dominio.competicao.geracao.GeradorPartidasServico;
import com.torneios.dominio.competicao.geracao.ModoPreparacaoCompeticao;
import com.torneios.dominio.competicao.geracao.PreparacaoCompeticao;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;
import com.torneios.dominio.competicao.rodada.Rodada;

public class PartidaServico {

    private final PartidaRepositorio partidaRepositorio;
    private final ConsultaCompeticaoTorneio consultaCompeticaoTorneio;
    private final GeradorPartidasServico geradorPartidasServico;
    private final ClassificacaoServico classificacaoServico;
    private final ChaveamentoServico chaveamentoServico;
    private final EventoBarramento barramento;

    public PartidaServico(PartidaRepositorio partidaRepositorio,
                          ConsultaCompeticaoTorneio consultaCompeticaoTorneio,
                          GeradorPartidasServico geradorPartidasServico,
                          ClassificacaoServico classificacaoServico,
                          ChaveamentoServico chaveamentoServico,
                          EventoBarramento barramento) {
        notNull(partidaRepositorio, "O repositorio de partidas e obrigatorio.");
        notNull(consultaCompeticaoTorneio, "A consulta de competicao do torneio e obrigatoria.");
        notNull(geradorPartidasServico, "O gerador de partidas e obrigatorio.");
        notNull(classificacaoServico, "O servico de classificacao e obrigatorio.");
        notNull(chaveamentoServico, "O servico de chaveamento e obrigatorio.");
        notNull(barramento, "O barramento de eventos e obrigatorio.");

        this.partidaRepositorio = partidaRepositorio;
        this.consultaCompeticaoTorneio = consultaCompeticaoTorneio;
        this.geradorPartidasServico = geradorPartidasServico;
        this.classificacaoServico = classificacaoServico;
        this.chaveamentoServico = chaveamentoServico;
        this.barramento = barramento;
    }

    public void salvar(Partida partida) {
        partidaRepositorio.salvar(partida);
    }

    public List<Partida> gerarPartidas(TorneioId torneioId, UsuarioId usuarioId) {
        return prepararCompeticao(torneioId, usuarioId).getPartidas();
    }

    public PreparacaoCompeticao prepararCompeticao(TorneioId torneioId, UsuarioId usuarioId) {
        return prepararCompeticao(torneioId, usuarioId, ModoPreparacaoCompeticao.SORTEIO, List.of());
    }

    public PreparacaoCompeticao prepararCompeticaoPorSorteio(TorneioId torneioId, UsuarioId usuarioId) {
        return prepararCompeticao(torneioId, usuarioId, ModoPreparacaoCompeticao.SORTEIO, List.of());
    }

    public PreparacaoCompeticao prepararCompeticaoManual(TorneioId torneioId,
                                                         UsuarioId usuarioId,
                                                         List<TimeId> ordemManualParticipantes) {
        return prepararCompeticao(torneioId, usuarioId, ModoPreparacaoCompeticao.MANUAL, ordemManualParticipantes);
    }

    private PreparacaoCompeticao prepararCompeticao(TorneioId torneioId,
                                                    UsuarioId usuarioId,
                                                    ModoPreparacaoCompeticao modoPreparacao,
                                                    List<TimeId> ordemManualParticipantes) {
        validarOrganizador(torneioId, usuarioId);
        if (!consultaCompeticaoTorneio.estruturaGerada(torneioId)) {
            throw new OperacaoNaoPermitidaException("Nao e permitido preparar a competicao sem estrutura previa.");
        }

        FormatoTorneio formatoTorneio = consultaCompeticaoTorneio.obterFormato(torneioId);
        int quantidadeJogadoresPorEquipe = consultaCompeticaoTorneio.obterQuantidadeJogadoresPorEquipe(torneioId);
        List<TimeId> participantes = consultaCompeticaoTorneio.listarParticipantesAprovados(torneioId);
        List<List<TimeId>> grupos = consultaCompeticaoTorneio.listarGrupos(torneioId);
        List<TimeId> participantesDaPreparacao = modoPreparacao == ModoPreparacaoCompeticao.MANUAL
                ? validarOrdemManual(participantes, ordemManualParticipantes)
                : participantes;

        List<Partida> partidas = geradorPartidasServico.gerar(
                torneioId,
                formatoTorneio,
                quantidadeJogadoresPorEquipe,
                participantesDaPreparacao,
                grupos,
                modoPreparacao);
        partidas.forEach(partidaRepositorio::salvar);
        List<Rodada> rodadas = geradorPartidasServico.distribuirEmRodadas(torneioId, partidas);
        return new PreparacaoCompeticao(torneioId, modoPreparacao, partidas, rodadas);
    }

    public Partida obterPartida(PartidaId partidaId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Partida nao encontrada."));
    }

    public AtualizacaoCompeticao registrarResultado(TorneioId torneioId,
                                                    PartidaId partidaId,
                                                    UsuarioId usuarioId,
                                                    ResultadoPartida resultadoPartida) {
        validarOrganizador(torneioId, usuarioId);
        Partida partida = obterPartida(partidaId);
        if (!partida.getTorneioId().equals(torneioId)) {
            throw new OperacaoNaoPermitidaException("A partida informada nao pertence ao torneio.");
        }
        validarParticipantesDaPartida(torneioId, partida);
        partida.registrarResultado(resultadoPartida);
        partidaRepositorio.salvar(partida);
        barramento.postar(new ResultadoRegistradoEvento(partidaId));
        return atualizarCompeticaoAposResultado(torneioId);
    }

    public List<Partida> listarPorTorneio(TorneioId torneioId) {
        return partidaRepositorio.listarPorTorneio(torneioId);
    }

    public List<Classificacao> visualizarClassificacao(TorneioId torneioId) {
        if (!consultaCompeticaoTorneio.estruturaGerada(torneioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Nao e permitido visualizar a classificacao antes da geracao da estrutura.");
        }
        FormatoTorneio formatoTorneio = consultaCompeticaoTorneio.obterFormato(torneioId);
        if (!formatoTorneio.exigeClassificacao()) {
            throw new OperacaoNaoPermitidaException("O formato do torneio nao utiliza classificacao.");
        }
        return classificacaoServico.gerar(
                torneioId,
                consultaCompeticaoTorneio.listarParticipantesAprovados(torneioId),
                listarPorTorneio(torneioId));
    }

    public Chaveamento visualizarChaveamento(TorneioId torneioId) {
        if (!consultaCompeticaoTorneio.estruturaGerada(torneioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Nao e permitido visualizar o chaveamento antes da geracao da estrutura.");
        }
        FormatoTorneio formatoTorneio = consultaCompeticaoTorneio.obterFormato(torneioId);
        if (!formatoTorneio.exigeChaveamento()) {
            throw new OperacaoNaoPermitidaException("O formato do torneio nao utiliza chaveamento.");
        }
        return chaveamentoServico.gerar(torneioId, formatoTorneio, listarPorTorneio(torneioId));
    }

    public AtualizacaoCompeticao gerenciarAndamento(TorneioId torneioId) {
        return atualizarCompeticaoAposResultado(torneioId);
    }

    private AtualizacaoCompeticao atualizarCompeticaoAposResultado(TorneioId torneioId) {
        FormatoTorneio formatoTorneio = consultaCompeticaoTorneio.obterFormato(torneioId);
        if (formatoTorneio.exigeClassificacao()) {
            return AtualizacaoCompeticao.comClassificacao(visualizarClassificacao(torneioId));
        }
        return AtualizacaoCompeticao.comChaveamento(visualizarChaveamento(torneioId));
    }

    private void validarOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        if (!consultaCompeticaoTorneio.usuarioEhOrganizador(torneioId, usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o organizador do torneio pode executar esta operacao.");
        }
    }

    private void validarParticipantesDaPartida(TorneioId torneioId, Partida partida) {
        List<TimeId> participantesAprovados = consultaCompeticaoTorneio.listarParticipantesAprovados(torneioId);
        if (!participantesAprovados.contains(partida.getMandante())
                || !participantesAprovados.contains(partida.getVisitante())) {
            throw new RegraDeNegocioException(
                    "Nao e permitido registrar resultado para times que nao pertencem ao torneio.");
        }
    }

    private List<TimeId> validarOrdemManual(List<TimeId> participantes, List<TimeId> ordemManualParticipantes) {
        if (ordemManualParticipantes == null || ordemManualParticipantes.size() != participantes.size()) {
            throw new RegraDeNegocioException(
                    "A preparacao manual deve informar todos os participantes aprovados.");
        }
        if (!participantes.containsAll(ordemManualParticipantes)
                || !ordemManualParticipantes.containsAll(participantes)) {
            throw new RegraDeNegocioException(
                    "A preparacao manual so pode usar participantes aprovados.");
        }
        return ordemManualParticipantes;
    }
}
