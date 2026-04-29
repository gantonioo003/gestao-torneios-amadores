package com.torneios.dominio.competicao.escalacao;

import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.Posicao;
import com.torneios.dominio.compartilhado.jogador.JogadorId;

public record JogadorEscalado(JogadorId jogadorId, Posicao posicao) {

    public JogadorEscalado {
        Objects.requireNonNull(jogadorId, "O jogador escalado e obrigatorio.");
        Objects.requireNonNull(posicao, "A posicao do jogador escalado e obrigatoria.");
    }
}
