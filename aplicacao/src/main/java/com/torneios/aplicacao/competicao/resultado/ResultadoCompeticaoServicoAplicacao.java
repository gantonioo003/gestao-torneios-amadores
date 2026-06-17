package com.torneios.aplicacao.competicao.resultado;

import static org.apache.commons.lang3.Validate.notNull;

import java.time.LocalDateTime;
import java.util.List;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.chaveamento.Chaveamento;
import com.torneios.dominio.competicao.classificacao.Classificacao;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultado;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultadoId;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultadoServico;
import com.torneios.dominio.competicao.contestacao.DecisaoContestacaoResultado;
import com.torneios.dominio.competicao.contestacao.HistoricoDecisaoContestacao;
import com.torneios.dominio.competicao.partida.AtualizacaoCompeticao;
import com.torneios.dominio.competicao.partida.PartidaServico;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

/**
 * Casos de uso de resultado oficial, andamento automatico e contestacao.
 */
public class ResultadoCompeticaoServicoAplicacao {

    private final PartidaServico partidaServico;
    private final ContestacaoResultadoServico contestacaoResultadoServico;

    public ResultadoCompeticaoServicoAplicacao(PartidaServico partidaServico,
                                               ContestacaoResultadoServico contestacaoResultadoServico) {
        notNull(partidaServico, "O servico de partida e obrigatorio.");
        notNull(contestacaoResultadoServico, "O servico de contestacao e obrigatorio.");
        this.partidaServico = partidaServico;
        this.contestacaoResultadoServico = contestacaoResultadoServico;
    }

    public AtualizacaoCompeticaoResumo registrarResultado(long torneioId,
                                                          long partidaId,
                                                          long organizadorId,
                                                          int golsMandante,
                                                          int golsVisitante) {
        AtualizacaoCompeticao atualizacaoCompeticao = partidaServico.registrarResultado(
                new TorneioId(torneioId),
                new PartidaId(partidaId),
                new UsuarioId(organizadorId),
                new ResultadoPartida(golsMandante, golsVisitante));
        return converter(atualizacaoCompeticao);
    }

    public AtualizacaoCompeticaoResumo gerenciarAndamento(long torneioId) {
        return converter(partidaServico.gerenciarAndamento(new TorneioId(torneioId)));
    }

    public List<ClassificacaoResumo> visualizarClassificacao(long torneioId) {
        return partidaServico.visualizarClassificacao(new TorneioId(torneioId)).stream()
                .map(this::converterClassificacao)
                .toList();
    }

    public ChaveamentoResumo visualizarChaveamento(long torneioId) {
        return converterChaveamento(partidaServico.visualizarChaveamento(new TorneioId(torneioId)));
    }

    public ContestacaoResumo abrirContestacao(long contestacaoId,
                                              long partidaId,
                                              long timeSolicitanteId,
                                              long usuarioSolicitanteId,
                                              String motivo,
                                              String justificativa,
                                              List<String> evidencias,
                                              LocalDateTime dataHoraAbertura) {
        return converterContestacao(contestacaoResultadoServico.abrirContestacao(
                new ContestacaoResultadoId(contestacaoId),
                new PartidaId(partidaId),
                new TimeId(timeSolicitanteId),
                new UsuarioId(usuarioSolicitanteId),
                motivo,
                justificativa,
                evidencias,
                dataHoraAbertura));
    }

    public ContestacaoResumo analisarContestacao(long contestacaoId,
                                                 long organizadorId,
                                                 String decisao,
                                                 String observacao,
                                                 Integer golsMandanteCorrigido,
                                                 Integer golsVisitanteCorrigido,
                                                 LocalDateTime dataHoraDecisao) {
        ResultadoPartida resultadoCorrigido = golsMandanteCorrigido == null || golsVisitanteCorrigido == null
                ? null
                : new ResultadoPartida(golsMandanteCorrigido, golsVisitanteCorrigido);
        return converterContestacao(contestacaoResultadoServico.analisarContestacao(
                new ContestacaoResultadoId(contestacaoId),
                new UsuarioId(organizadorId),
                DecisaoContestacaoResultado.valueOf(decisao),
                observacao,
                resultadoCorrigido,
                dataHoraDecisao));
    }

    public List<ContestacaoResumo> listarContestacoesDoTorneio(long torneioId) {
        return contestacaoResultadoServico.listarContestacoesDoTorneio(new TorneioId(torneioId)).stream()
                .map(this::converterContestacao)
                .toList();
    }

    private AtualizacaoCompeticaoResumo converter(AtualizacaoCompeticao atualizacaoCompeticao) {
        List<ClassificacaoResumo> classificacao = atualizacaoCompeticao.classificacaoAtualizada() == null
                ? List.of()
                : atualizacaoCompeticao.classificacaoAtualizada().stream()
                        .map(this::converterClassificacao)
                        .toList();
        ChaveamentoResumo chaveamento = atualizacaoCompeticao.chaveamentoAtualizado() == null
                ? null
                : converterChaveamento(atualizacaoCompeticao.chaveamentoAtualizado());
        return new AtualizacaoCompeticaoResumo(classificacao, chaveamento);
    }

    private ClassificacaoResumo converterClassificacao(Classificacao classificacao) {
        return new ClassificacaoResumo(
                classificacao.getTorneioId().valor(),
                classificacao.getTimeId().valor(),
                classificacao.getPontos(),
                classificacao.getVitorias(),
                classificacao.getEmpates(),
                classificacao.getDerrotas(),
                classificacao.getGolsPro(),
                classificacao.getGolsContra(),
                classificacao.getSaldoGols());
    }

    private ChaveamentoResumo converterChaveamento(Chaveamento chaveamento) {
        return new ChaveamentoResumo(
                chaveamento.getTorneioId().valor(),
                chaveamento.getFases(),
                chaveamento.getPartidas().stream().map(PartidaId::valor).toList());
    }

    private ContestacaoResumo converterContestacao(ContestacaoResultado contestacaoResultado) {
        return new ContestacaoResumo(
                contestacaoResultado.getId().valor(),
                contestacaoResultado.getTorneioId().valor(),
                contestacaoResultado.getPartidaId().valor(),
                contestacaoResultado.getTimeSolicitanteId().valor(),
                contestacaoResultado.getUsuarioSolicitanteId().valor(),
                contestacaoResultado.getMotivo(),
                contestacaoResultado.getJustificativa(),
                contestacaoResultado.getEvidencias(),
                contestacaoResultado.getDataHoraAbertura(),
                contestacaoResultado.getPrazoLimite(),
                contestacaoResultado.getStatus().name(),
                contestacaoResultado.getHistorico().stream().map(this::converterHistorico).toList());
    }

    private HistoricoContestacaoResumo converterHistorico(HistoricoDecisaoContestacao historicoDecisaoContestacao) {
        ResultadoPartida resultadoAnterior = historicoDecisaoContestacao.resultadoAnterior();
        ResultadoPartida resultadoCorrigido = historicoDecisaoContestacao.resultadoCorrigido();
        return new HistoricoContestacaoResumo(
                historicoDecisaoContestacao.organizadorId().valor(),
                historicoDecisaoContestacao.decisao().name(),
                historicoDecisaoContestacao.observacao(),
                resultadoAnterior == null ? null : resultadoAnterior.golsMandante(),
                resultadoAnterior == null ? null : resultadoAnterior.golsVisitante(),
                resultadoCorrigido == null ? null : resultadoCorrigido.golsMandante(),
                resultadoCorrigido == null ? null : resultadoCorrigido.golsVisitante(),
                historicoDecisaoContestacao.dataHoraDecisao());
    }

    public record AtualizacaoCompeticaoResumo(List<ClassificacaoResumo> classificacao,
                                              ChaveamentoResumo chaveamento) {
    }

    public record ClassificacaoResumo(long torneioId,
                                      long timeId,
                                      int pontos,
                                      int vitorias,
                                      int empates,
                                      int derrotas,
                                      int golsPro,
                                      int golsContra,
                                      int saldoGols) {
    }

    public record ChaveamentoResumo(long torneioId, List<String> fases, List<Long> partidas) {
    }

    public record ContestacaoResumo(long id,
                                    long torneioId,
                                    long partidaId,
                                    long timeSolicitanteId,
                                    long usuarioSolicitanteId,
                                    String motivo,
                                    String justificativa,
                                    List<String> evidencias,
                                    LocalDateTime dataHoraAbertura,
                                    LocalDateTime prazoLimite,
                                    String status,
                                    List<HistoricoContestacaoResumo> historico) {
    }

    public record HistoricoContestacaoResumo(long organizadorId,
                                             String decisao,
                                             String observacao,
                                             Integer golsMandanteAnterior,
                                             Integer golsVisitanteAnterior,
                                             Integer golsMandanteCorrigido,
                                             Integer golsVisitanteCorrigido,
                                             LocalDateTime dataHoraDecisao) {
    }
}
