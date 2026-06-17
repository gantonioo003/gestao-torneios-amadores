package com.torneios.dominio.estatisticas.desempenho;

import com.torneios.dominio.compartilhado.jogador.JogadorId;

public record EstatisticaCarreiraJogador(JogadorId jogadorId,
                                         int gols,
                                         int assistencias,
                                         int cartoesAmarelos,
                                         int cartoesVermelhos,
                                         int torneiosComEventos) {

    public EstatisticaCarreiraJogador {
        if (jogadorId == null) {
            throw new IllegalArgumentException("O jogador da carreira e obrigatorio.");
        }
        if (gols < 0 || assistencias < 0 || cartoesAmarelos < 0 || cartoesVermelhos < 0 || torneiosComEventos < 0) {
            throw new IllegalArgumentException("Os totais da carreira nao podem ser negativos.");
        }
    }
}
