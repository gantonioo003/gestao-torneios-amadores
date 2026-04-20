package com.torneios.dominio.estatisticas.desempenho;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public class EstatisticaJogador {

    private final TorneioId torneioId;
    private final long jogadorId;
    private int gols;
    private int assistencias;
    private int cartoesAmarelos;
    private int cartoesVermelhos;

    public EstatisticaJogador(TorneioId torneioId, long jogadorId) {
        if (torneioId == null) {
            throw new IllegalArgumentException("O torneio da estatistica e obrigatorio.");
        }
        if (jogadorId <= 0) {
            throw new IllegalArgumentException("O jogador da estatistica e obrigatorio.");
        }
        this.torneioId = torneioId;
        this.jogadorId = jogadorId;
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public long getJogadorId() {
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

    public void registrarEvento(TipoEventoEstatistico tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de evento e obrigatorio.");
        }

        switch (tipo) {
            case GOL -> gols++;
            case ASSISTENCIA -> assistencias++;
            case CARTAO_AMARELO -> cartoesAmarelos++;
            case CARTAO_VERMELHO -> cartoesVermelhos++;
        }
    }
}
