package com.torneios.dominio.estatisticas.evento;

import java.util.Objects;
import java.util.Optional;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.evento.EventoDominio;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public abstract class EventoEstatistico extends EventoDominio {

    private final long id;
    private final TorneioId torneioId;
    private final PartidaId partidaId;
    private final JogadorId jogadorId;
    private final TipoEventoEstatistico tipo;
    private final boolean automatico;
    private final Long eventoOrigemId;

    protected EventoEstatistico(long id,
                                TorneioId torneioId,
                                PartidaId partidaId,
                                JogadorId jogadorId,
                                TipoEventoEstatistico tipo) {
        this(id, torneioId, partidaId, jogadorId, tipo, false, null);
    }

    protected EventoEstatistico(long id,
                                TorneioId torneioId,
                                PartidaId partidaId,
                                JogadorId jogadorId,
                                TipoEventoEstatistico tipo,
                                boolean automatico,
                                Long eventoOrigemId) {
        super(tipo.name());
        if (id <= 0) {
            throw new IllegalArgumentException("O id do evento estatistico deve ser maior que zero.");
        }
        if (eventoOrigemId != null && eventoOrigemId <= 0) {
            throw new IllegalArgumentException("O evento de origem deve ser maior que zero.");
        }
        this.id = id;
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio do evento e obrigatorio.");
        this.partidaId = Objects.requireNonNull(partidaId, "A partida do evento e obrigatoria.");
        this.jogadorId = Objects.requireNonNull(jogadorId, "O jogador do evento e obrigatorio.");
        this.tipo = Objects.requireNonNull(tipo, "O tipo do evento e obrigatorio.");
        this.automatico = automatico;
        this.eventoOrigemId = eventoOrigemId;
    }

    public long getId() {
        return id;
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public PartidaId getPartidaId() {
        return partidaId;
    }

    public JogadorId getJogadorId() {
        return jogadorId;
    }

    public TipoEventoEstatistico getTipo() {
        return tipo;
    }

    public boolean isAutomatico() {
        return automatico;
    }

    public Optional<Long> getEventoOrigemId() {
        return Optional.ofNullable(eventoOrigemId);
    }

    public boolean foiGeradoPor(long origemId) {
        return Objects.equals(eventoOrigemId, origemId);
    }
}
