package com.torneios.dominio.torneio.participante;

import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.StatusParticipante;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public final class ParticipanteTorneio {

    private final TimeId timeId;
    private final TorneioId torneioId;
    private final StatusParticipante status;

    public ParticipanteTorneio(TimeId timeId, TorneioId torneioId) {
        this.timeId = Objects.requireNonNull(timeId, "O id do time do participante e obrigatorio.");
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio do participante e obrigatorio.");
        this.status = StatusParticipante.APROVADO;
    }

    public TimeId getTimeId() {
        return timeId;
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public StatusParticipante getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticipanteTorneio that)) {
            return false;
        }
        return timeId == that.timeId && torneioId.equals(that.torneioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeId, torneioId);
    }
}
