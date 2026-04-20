package com.torneios.dominio.participacao.solicitacao;

import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.StatusSolicitacao;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class SolicitacaoParticipacao {

    private final SolicitacaoParticipacaoId id;
    private final UsuarioId solicitante;
    private final TimeId timeId;
    private final TorneioId torneioId;
    private StatusSolicitacao status;

    public SolicitacaoParticipacao(SolicitacaoParticipacaoId id,
                                   UsuarioId solicitante,
                                   TimeId timeId,
                                   TorneioId torneioId) {
        this.id = Objects.requireNonNull(id, "O id da solicitacao e obrigatorio.");
        this.solicitante = Objects.requireNonNull(solicitante, "O solicitante e obrigatorio.");
        this.timeId = Objects.requireNonNull(timeId, "O time da solicitacao e obrigatorio.");
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio da solicitacao e obrigatorio.");
        this.status = StatusSolicitacao.PENDENTE;
    }

    public SolicitacaoParticipacaoId getId() {
        return id;
    }

    public UsuarioId getSolicitante() {
        return solicitante;
    }

    public TimeId getTimeId() {
        return timeId;
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public boolean estaPendente() {
        return status == StatusSolicitacao.PENDENTE;
    }

    public void aprovar() {
        validarPendente();
        this.status = StatusSolicitacao.APROVADA;
    }

    public void rejeitar() {
        validarPendente();
        this.status = StatusSolicitacao.REJEITADA;
    }

    private void validarPendente() {
        if (!estaPendente()) {
            throw new IllegalStateException("A solicitacao ja foi avaliada.");
        }
    }
}
