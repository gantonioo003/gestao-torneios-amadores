package com.torneios.dominio.estatisticas.evento;

import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public final class Substituicao extends EventoEstatistico {

    private final JogadorId jogadorSaiuId;
    private final JogadorId jogadorEntrouId;

    public Substituicao(long id,
                        TorneioId torneioId,
                        PartidaId partidaId,
                        JogadorId jogadorSaiuId,
                        JogadorId jogadorEntrouId) {
        super(id, torneioId, partidaId, jogadorEntrouId, TipoEventoEstatistico.SUBSTITUICAO);
        this.jogadorSaiuId = Objects.requireNonNull(jogadorSaiuId, "O jogador substituido e obrigatorio.");
        this.jogadorEntrouId = Objects.requireNonNull(jogadorEntrouId, "O jogador substituto e obrigatorio.");
        if (jogadorSaiuId.equals(jogadorEntrouId)) {
            throw new IllegalArgumentException("Jogador substituido e substituto devem ser diferentes.");
        }
    }

    public JogadorId getJogadorSaiuId() {
        return jogadorSaiuId;
    }

    public JogadorId getJogadorEntrouId() {
        return jogadorEntrouId;
    }
}
