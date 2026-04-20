package com.torneios.dominio.torneio.participante;

import java.util.Objects;

import com.torneios.dominio.torneio.torneio.TorneioId;

public final class ParticipanteTorneio {

    private final long timeId;
    private final TorneioId torneioId;

    public ParticipanteTorneio(long timeId, TorneioId torneioId) {
        if (timeId <= 0) {
            throw new IllegalArgumentException("O id do time deve ser maior que zero.");
        }

        this.timeId = timeId;
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio do participante e obrigatorio.");
    }

    public long getTimeId() {
        return timeId;
    }

    public TorneioId getTorneioId() {
        return torneioId;
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
