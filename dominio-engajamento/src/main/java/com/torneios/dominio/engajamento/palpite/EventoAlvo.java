package com.torneios.dominio.engajamento.palpite;

import java.util.Objects;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

/**
 * Identifica de forma unica o evento sobre o qual um palpite e registrado.
 * Combina o tipo de palpite com o torneio e, quando aplicavel, com a partida.
 */
public final class EventoAlvo {

    private final TipoPalpite tipo;
    private final TorneioId torneioId;
    private final PartidaId partidaId;

    private EventoAlvo(TipoPalpite tipo, TorneioId torneioId, PartidaId partidaId) {
        this.tipo = Objects.requireNonNull(tipo, "O tipo do palpite e obrigatorio.");
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio do evento alvo e obrigatorio.");
        this.partidaId = partidaId;
    }

    public static EventoAlvo paraPartida(TorneioId torneioId, PartidaId partidaId) {
        Objects.requireNonNull(partidaId, "A partida do evento alvo e obrigatoria.");
        return new EventoAlvo(TipoPalpite.VENCEDOR_PARTIDA, torneioId, partidaId);
    }

    public static EventoAlvo paraCampeao(TorneioId torneioId) {
        return new EventoAlvo(TipoPalpite.CAMPEAO_TORNEIO, torneioId, null);
    }

    public static EventoAlvo paraArtilheiro(TorneioId torneioId) {
        return new EventoAlvo(TipoPalpite.ARTILHEIRO_TORNEIO, torneioId, null);
    }

    public static EventoAlvo paraLiderAssistencias(TorneioId torneioId) {
        return new EventoAlvo(TipoPalpite.LIDER_ASSISTENCIAS_TORNEIO, torneioId, null);
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
        if (!(o instanceof EventoAlvo)) {
            return false;
        }
        EventoAlvo other = (EventoAlvo) o;
        return tipo == other.tipo
                && torneioId.equals(other.torneioId)
                && Objects.equals(partidaId, other.partidaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, torneioId, partidaId);
    }
}
