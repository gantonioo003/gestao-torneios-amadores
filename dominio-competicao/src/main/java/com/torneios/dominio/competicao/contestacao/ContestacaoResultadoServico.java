package com.torneios.dominio.competicao.contestacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.competicao.partida.ConsultaCompeticaoTorneio;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

public class ContestacaoResultadoServico {

    private final PartidaRepositorio partidaRepositorio;
    private final ContestacaoResultadoRepositorio contestacaoRepositorio;
    private final ConsultaCompeticaoTorneio consultaCompeticaoTorneio;
    private final ConsultaContestacaoResultado consultaContestacaoResultado;

    public ContestacaoResultadoServico(PartidaRepositorio partidaRepositorio,
                                       ContestacaoResultadoRepositorio contestacaoRepositorio,
                                       ConsultaCompeticaoTorneio consultaCompeticaoTorneio,
                                       ConsultaContestacaoResultado consultaContestacaoResultado) {
        this.partidaRepositorio = Objects.requireNonNull(partidaRepositorio,
                "O repositorio de partidas e obrigatorio.");
        this.contestacaoRepositorio = Objects.requireNonNull(contestacaoRepositorio,
                "O repositorio de contestacoes e obrigatorio.");
        this.consultaCompeticaoTorneio = Objects.requireNonNull(consultaCompeticaoTorneio,
                "A consulta de competicao e obrigatoria.");
        this.consultaContestacaoResultado = Objects.requireNonNull(consultaContestacaoResultado,
                "A consulta de contestacao e obrigatoria.");
    }

    public ContestacaoResultado abrirContestacao(ContestacaoResultadoId contestacaoId,
                                                 PartidaId partidaId,
                                                 TimeId timeSolicitanteId,
                                                 UsuarioId usuarioSolicitanteId,
                                                 String motivo,
                                                 String justificativa,
                                                 List<String> evidencias,
                                                 LocalDateTime dataHoraAbertura) {
        Objects.requireNonNull(contestacaoId, "O id da contestacao e obrigatorio.");
        Objects.requireNonNull(partidaId, "A partida da contestacao e obrigatoria.");
        Objects.requireNonNull(timeSolicitanteId, "O time solicitante e obrigatorio.");
        Objects.requireNonNull(usuarioSolicitanteId, "O usuario solicitante e obrigatorio.");
        LocalDateTime abertura = dataHoraAbertura == null ? LocalDateTime.now() : dataHoraAbertura;

        Partida partida = obterPartida(partidaId);
        validarResultadoOficial(partida);
        validarTimeDaPartida(partida, timeSolicitanteId);
        validarTimesPertencemAoTorneio(partida);
        validarResponsavel(timeSolicitanteId, usuarioSolicitanteId);

        int prazoHoras = consultaContestacaoResultado.prazoContestacaoEmHoras(partida.getTorneioId());
        LocalDateTime prazoLimite = partida.getDataHoraRegistroResultado().plusHours(prazoHoras);
        if (abertura.isAfter(prazoLimite)) {
            throw new RegraDeNegocioException("O prazo para contestar o resultado da partida foi encerrado.");
        }
        if (contestacaoRepositorio.existePendentePorPartidaETime(partidaId, timeSolicitanteId)) {
            throw new RegraDeNegocioException(
                    "Ja existe contestacao pendente para este time nesta partida.");
        }

        ContestacaoResultado contestacao = new ContestacaoResultado(
                contestacaoId, partida.getTorneioId(), partidaId, timeSolicitanteId, usuarioSolicitanteId,
                motivo, justificativa, evidencias, abertura, prazoLimite);
        contestacaoRepositorio.salvar(contestacao);
        return contestacao;
    }

    public ContestacaoResultado analisarContestacao(ContestacaoResultadoId contestacaoId,
                                                    UsuarioId organizadorId,
                                                    DecisaoContestacaoResultado decisao,
                                                    String observacao,
                                                    ResultadoPartida resultadoCorrigido,
                                                    LocalDateTime dataHoraDecisao) {
        Objects.requireNonNull(contestacaoId, "A contestacao e obrigatoria.");
        Objects.requireNonNull(organizadorId, "O organizador e obrigatorio.");
        Objects.requireNonNull(decisao, "A decisao e obrigatoria.");
        LocalDateTime decisaoEm = dataHoraDecisao == null ? LocalDateTime.now() : dataHoraDecisao;

        ContestacaoResultado contestacao = contestacaoRepositorio.buscarPorId(contestacaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Contestacao nao encontrada."));
        if (!contestacao.pendente()) {
            throw new RegraDeNegocioException("A contestacao ja foi analisada.");
        }
        if (!consultaCompeticaoTorneio.usuarioEhOrganizador(contestacao.getTorneioId(), organizadorId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o organizador do torneio pode analisar a contestacao.");
        }
        Partida partida = obterPartida(contestacao.getPartidaId());
        ResultadoPartida resultadoAnterior = partida.getResultado();
        if (DecisaoContestacaoResultado.ACEITAR.equals(decisao)) {
            if (resultadoCorrigido == null) {
                throw new RegraDeNegocioException(
                        "A aceitacao da contestacao com correcao exige novo resultado oficial.");
            }
            partida.corrigirResultadoOficial(resultadoCorrigido, decisaoEm);
            partidaRepositorio.salvar(partida);
        }

        contestacao.registrarDecisao(
                organizadorId, decisao, observacao, resultadoAnterior, resultadoCorrigido, decisaoEm);
        contestacaoRepositorio.salvar(contestacao);
        return contestacao;
    }

    public List<ContestacaoResultado> listarContestacoesDoTorneio(com.torneios.dominio.compartilhado.torneio.TorneioId torneioId) {
        Objects.requireNonNull(torneioId, "O torneio e obrigatorio.");
        return contestacaoRepositorio.listarContestacoesPorTorneio(torneioId);
    }

    private Partida obterPartida(PartidaId partidaId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Partida nao encontrada."));
    }

    private void validarResultadoOficial(Partida partida) {
        if (!partida.estaEncerrada() || partida.getResultado() == null || partida.getDataHoraRegistroResultado() == null) {
            throw new RegraDeNegocioException(
                    "A contestacao so pode ser aberta para partida com resultado oficial registrado.");
        }
    }

    private void validarTimeDaPartida(Partida partida, TimeId timeSolicitanteId) {
        if (!partida.getMandante().equals(timeSolicitanteId) && !partida.getVisitante().equals(timeSolicitanteId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas times envolvidos na partida podem contestar o resultado.");
        }
    }

    private void validarTimesPertencemAoTorneio(Partida partida) {
        List<TimeId> participantes = consultaCompeticaoTorneio.listarParticipantesAprovados(partida.getTorneioId());
        if (!participantes.contains(partida.getMandante()) || !participantes.contains(partida.getVisitante())) {
            throw new RegraDeNegocioException("Os times da partida devem pertencer ao torneio.");
        }
    }

    private void validarResponsavel(TimeId timeSolicitanteId, UsuarioId usuarioSolicitanteId) {
        if (!consultaContestacaoResultado.usuarioEhResponsavelDoTime(timeSolicitanteId, usuarioSolicitanteId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o responsavel pelo time participante pode contestar o resultado.");
        }
    }
}
