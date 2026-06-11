package com.torneios.dominio.competicao.contestacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

public class ContestacaoResultado {

    private final ContestacaoResultadoId id;
    private final TorneioId torneioId;
    private final PartidaId partidaId;
    private final TimeId timeSolicitanteId;
    private final UsuarioId usuarioSolicitanteId;
    private final String motivo;
    private final String justificativa;
    private final List<String> evidencias;
    private final LocalDateTime dataHoraAbertura;
    private final LocalDateTime prazoLimite;
    private final List<HistoricoDecisaoContestacao> historico = new ArrayList<>();
    private StatusContestacaoResultado status;

    public ContestacaoResultado(ContestacaoResultadoId id,
                                TorneioId torneioId,
                                PartidaId partidaId,
                                TimeId timeSolicitanteId,
                                UsuarioId usuarioSolicitanteId,
                                String motivo,
                                String justificativa,
                                List<String> evidencias,
                                LocalDateTime dataHoraAbertura,
                                LocalDateTime prazoLimite) {
        this.id = Objects.requireNonNull(id, "O id da contestacao e obrigatorio.");
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio da contestacao e obrigatorio.");
        this.partidaId = Objects.requireNonNull(partidaId, "A partida da contestacao e obrigatoria.");
        this.timeSolicitanteId = Objects.requireNonNull(timeSolicitanteId, "O time solicitante e obrigatorio.");
        this.usuarioSolicitanteId = Objects.requireNonNull(usuarioSolicitanteId, "O usuario solicitante e obrigatorio.");
        this.motivo = exigirTexto(motivo, "O motivo da contestacao e obrigatorio.");
        this.justificativa = exigirTexto(justificativa, "A justificativa da contestacao e obrigatoria.");
        this.evidencias = List.copyOf(evidencias == null ? List.of() : evidencias);
        this.dataHoraAbertura = Objects.requireNonNull(dataHoraAbertura, "A data de abertura e obrigatoria.");
        this.prazoLimite = Objects.requireNonNull(prazoLimite, "O prazo limite da contestacao e obrigatorio.");
        this.status = StatusContestacaoResultado.PENDENTE;
    }

    public ContestacaoResultadoId getId() { return id; }
    public TorneioId getTorneioId() { return torneioId; }
    public PartidaId getPartidaId() { return partidaId; }
    public TimeId getTimeSolicitanteId() { return timeSolicitanteId; }
    public UsuarioId getUsuarioSolicitanteId() { return usuarioSolicitanteId; }
    public String getMotivo() { return motivo; }
    public String getJustificativa() { return justificativa; }
    public List<String> getEvidencias() { return evidencias; }
    public LocalDateTime getDataHoraAbertura() { return dataHoraAbertura; }
    public LocalDateTime getPrazoLimite() { return prazoLimite; }
    public StatusContestacaoResultado getStatus() { return status; }
    public List<HistoricoDecisaoContestacao> getHistorico() { return List.copyOf(historico); }

    public boolean pendente() {
        return StatusContestacaoResultado.PENDENTE.equals(status);
    }

    public void registrarDecisao(UsuarioId organizadorId,
                                DecisaoContestacaoResultado decisao,
                                String observacao,
                                ResultadoPartida resultadoAnterior,
                                ResultadoPartida resultadoCorrigido,
                                LocalDateTime dataHoraDecisao) {
        if (!pendente()) {
            throw new IllegalStateException("A contestacao ja possui decisao registrada.");
        }
        historico.add(new HistoricoDecisaoContestacao(
                organizadorId, decisao, observacao, resultadoAnterior, resultadoCorrigido, dataHoraDecisao));
        switch (decisao) {
            case ACEITAR -> status = StatusContestacaoResultado.ACEITA;
            case REJEITAR -> status = StatusContestacaoResultado.REJEITADA;
            case SOLICITAR_CORRECAO -> status = StatusContestacaoResultado.CORRECAO_SOLICITADA;
        }
    }

    private String exigirTexto(String texto, String mensagem) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return texto.trim();
    }
}
