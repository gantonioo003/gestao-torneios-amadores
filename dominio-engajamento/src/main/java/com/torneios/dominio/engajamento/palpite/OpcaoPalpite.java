package com.torneios.dominio.engajamento.palpite;

import java.util.Objects;

public record OpcaoPalpite(long valor) {

    public OpcaoPalpite {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da opcao do palpite deve ser maior que zero.");
        }
    }

    public boolean correspondeA(long resultadoReal) {
        return Objects.equals(this.valor, resultadoReal);
    }
}


