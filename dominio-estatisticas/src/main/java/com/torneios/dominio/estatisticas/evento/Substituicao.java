package com.torneios.dominio.estatisticas.evento;

import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public final class Substituicao extends EventoEstatistico {

    private final JogadorId jogadorSaiuId;
    private final JogadorId jogadorEntrouId;
    private final Integer ordemParadaJogo;
    private final String descricaoParadaJogo;

    public Substituicao(long id,
                        TorneioId torneioId,
                        PartidaId partidaId,
                        JogadorId jogadorSaiuId,
                        JogadorId jogadorEntrouId) {
        this(id, torneioId, partidaId, jogadorSaiuId, jogadorEntrouId, null, null);
    }

    public Substituicao(long id,
                        TorneioId torneioId,
                        PartidaId partidaId,
                        JogadorId jogadorSaiuId,
                        JogadorId jogadorEntrouId,
                        Integer ordemParadaJogo,
                        String descricaoParadaJogo) {
        super(id, torneioId, partidaId, jogadorEntrouId, TipoEventoEstatistico.SUBSTITUICAO);
        this.jogadorSaiuId = Objects.requireNonNull(jogadorSaiuId, "O jogador substituido e obrigatorio.");
        this.jogadorEntrouId = Objects.requireNonNull(jogadorEntrouId, "O jogador substituto e obrigatorio.");
        if (jogadorSaiuId.equals(jogadorEntrouId)) {
            throw new IllegalArgumentException("Jogador substituido e substituto devem ser diferentes.");
        }
        if (ordemParadaJogo != null && ordemParadaJogo <= 0) {
            throw new IllegalArgumentException("A ordem da parada de jogo deve ser positiva.");
        }
        this.ordemParadaJogo = ordemParadaJogo;
        this.descricaoParadaJogo = descricaoParadaJogo == null || descricaoParadaJogo.isBlank()
                ? null
                : descricaoParadaJogo.trim();
    }

    public JogadorId getJogadorSaiuId() {
        return jogadorSaiuId;
    }

    public JogadorId getJogadorEntrouId() {
        return jogadorEntrouId;
    }

    public Integer getOrdemParadaJogo() {
        return ordemParadaJogo;
    }

    public String getDescricaoParadaJogo() {
        return descricaoParadaJogo;
    }
}
