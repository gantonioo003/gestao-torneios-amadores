package com.torneios.dominio.estatisticas.nota;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public record NotaEstatistica(TorneioId torneioId, PartidaId partidaId, JogadorId jogadorId, double valor) {

    public NotaEstatistica {
        if (torneioId == null) {
            throw new IllegalArgumentException("O torneio da nota estatistica e obrigatorio.");
        }
        if (partidaId == null) {
            throw new IllegalArgumentException("A partida da nota estatistica e obrigatoria.");
        }
        if (jogadorId == null) {
            throw new IllegalArgumentException("O jogador da nota estatistica e obrigatorio.");
        }
        if (valor < 0.0 || valor > 10.0) {
            throw new IllegalArgumentException("A nota estatistica deve estar entre 0 e 10.");
        }
    }
}
