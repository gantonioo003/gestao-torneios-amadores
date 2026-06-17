package com.torneios.dominio.estatisticas.nota;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public record NotaMediaJogador(TorneioId torneioId,
                               JogadorId jogadorId,
                               double media,
                               int partidasConsideradas) {

    public NotaMediaJogador {
        if (torneioId == null) {
            throw new IllegalArgumentException("O torneio da media de nota e obrigatorio.");
        }
        if (jogadorId == null) {
            throw new IllegalArgumentException("O jogador da media de nota e obrigatorio.");
        }
        if (media < 0.0 || media > 10.0) {
            throw new IllegalArgumentException("A media de nota deve estar entre 0 e 10.");
        }
        if (partidasConsideradas <= 0) {
            throw new IllegalArgumentException("A quantidade de partidas consideradas deve ser positiva.");
        }
    }
}
