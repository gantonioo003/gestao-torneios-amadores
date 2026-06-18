package com.torneios.dominio.engajamento.palpite;

import java.util.Objects;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public final class EventoAlvoPalpite {

    private final TipoPalpite tipo;
    private final TorneioId torneioId;
    private final PartidaId partidaId;

    private EventoAlvoPalpite(TipoPalpite tipo, TorneioId torneioId, PartidaId partidaId) {
        this.tipo = Objects.requireNonNull(tipo, "O tipo do palpite e obrigatorio.");
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio do evento alvo e obrigatorio.");
        this.partidaId = partidaId;
    }

    public static EventoAlvoPalpite paraPartida(TorneioId torneioId, PartidaId partidaId) {
        Objects.requireNonNull(partidaId, "A partida do evento alvo e obrigatoria.");
        return new EventoAlvoPalpite(TipoPalpite.VENCEDOR_PARTIDA, torneioId, partidaId);
    }

    public static EventoAlvoPalpite paraCampeao(TorneioId torneioId) {
        return new EventoAlvoPalpite(TipoPalpite.CAMPEAO_TORNEIO, torneioId, null);
    }

    public static EventoAlvoPalpite paraArtilheiro(TorneioId torneioId) {
        return new EventoAlvoPalpite(TipoPalpite.ARTILHEIRO_TORNEIO, torneioId, null);
    }

    public static EventoAlvoPalpite paraLiderAssistencias(TorneioId torneioId) {
        return new EventoAlvoPalpite(TipoPalpite.LIDER_ASSISTENCIAS_TORNEIO, torneioId, null);
    }

    public TipoPalpite getTipo() {
        return tipo;
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public PartidaId getPartidaId() {
        return partidaId;
    }

    public boolean ehPorPartida() {
        return tipo == TipoPalpite.VENCEDOR_PARTIDA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventoAlvoPalpite)) {
            return false;
        }
        EventoAlvoPalpite other = (EventoAlvoPalpite) o;
        return tipo == other.tipo
                && torneioId.equals(other.torneioId)
                && Objects.equals(partidaId, other.partidaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, torneioId, partidaId);
    }
}


