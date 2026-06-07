package com.torneios.dominio.competicao.escalacao;

import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.Posicao;
import com.torneios.dominio.compartilhado.jogador.JogadorId;

public record JogadorPosicionadoMesaTatica(
        JogadorId jogadorId,
        Posicao posicao,
        CoordenadaMesaTatica coordenada,
        int ordemNaLinha) {

    public JogadorPosicionadoMesaTatica {
        Objects.requireNonNull(jogadorId, "O jogador posicionado na mesa tatica e obrigatorio.");
        Objects.requireNonNull(posicao, "A posicao taticamente representada e obrigatoria.");
        Objects.requireNonNull(coordenada, "A coordenada da mesa tatica e obrigatoria.");
        if (ordemNaLinha < 1) {
            throw new IllegalArgumentException("A ordem do jogador na linha tática deve ser positiva.");
        }
    }
}
