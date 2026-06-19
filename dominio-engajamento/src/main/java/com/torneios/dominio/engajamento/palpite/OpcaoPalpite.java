package com.torneios.dominio.engajamento.palpite;

import java.util.Objects;

public record OpcaoPalpite(long valor) {

    public static final long EMPATE = 0L;

    public OpcaoPalpite {
        if (valor < 0) {
            throw new IllegalArgumentException("O valor da opcao do palpite nao pode ser negativo.");
        }
    }

    public boolean correspondeA(long resultadoReal) {
        return Objects.equals(this.valor, resultadoReal);
    }
}


