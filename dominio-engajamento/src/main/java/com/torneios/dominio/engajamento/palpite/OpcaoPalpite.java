package com.torneios.dominio.engajamento.palpite;

import java.util.Objects;

/**
 * Representa a opcao escolhida pelo usuario em um palpite.
 * O significado do valor depende do tipo de palpite:
 * - VENCEDOR_PARTIDA: id de TimeId
 * - CAMPEAO_TORNEIO: id de TimeId
 * - ARTILHEIRO_TORNEIO: id de JogadorId
 * - LIDER_ASSISTENCIAS_TORNEIO: id de JogadorId
 */
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
