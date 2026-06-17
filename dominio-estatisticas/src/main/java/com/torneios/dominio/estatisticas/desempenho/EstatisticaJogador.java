package com.torneios.dominio.estatisticas.desempenho;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public class EstatisticaJogador {

    private final TorneioId torneioId;
    private final JogadorId jogadorId;
    private int gols;
    private int assistencias;
    private int cartoesAmarelos;
    private int cartoesVermelhos;

    public EstatisticaJogador(TorneioId torneioId, JogadorId jogadorId) {
        if (torneioId == null) {
            throw new IllegalArgumentException("O torneio da estatistica e obrigatorio.");
        }
        this.torneioId = torneioId;
        this.jogadorId = java.util.Objects.requireNonNull(jogadorId, "O jogador da estatistica e obrigatorio.");
    }

    private EstatisticaJogador(TorneioId torneioId,
                               JogadorId jogadorId,
                               int gols,
                               int assistencias,
                               int cartoesAmarelos,
                               int cartoesVermelhos) {
        this(torneioId, jogadorId);
        this.gols = gols;
        this.assistencias = assistencias;
        this.cartoesAmarelos = cartoesAmarelos;
        this.cartoesVermelhos = cartoesVermelhos;
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public JogadorId getJogadorId() {
        return jogadorId;
    }

    public int getGols() {
        return gols;
    }

    public int getAssistencias() {
        return assistencias;
    }

    public int getCartoesAmarelos() {
        return cartoesAmarelos;
    }

    public int getCartoesVermelhos() {
        return cartoesVermelhos;
    }

    public EstatisticaJogador copiar() {
        return new EstatisticaJogador(
                torneioId,
                jogadorId,
                gols,
                assistencias,
                cartoesAmarelos,
                cartoesVermelhos);
    }

    public void registrarEvento(TipoEventoEstatistico tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de evento e obrigatorio.");
        }

        switch (tipo) {
            case GOL -> gols++;
            case ASSISTENCIA -> assistencias++;
            case CARTAO_AMARELO -> cartoesAmarelos++;
            case CARTAO_VERMELHO -> cartoesVermelhos++;
            case SUBSTITUICAO -> { }
        }
    }
}
