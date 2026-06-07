package com.torneios.dominio.competicao.escalacao;

public record CoordenadaMesaTatica(double eixoX, double eixoY) {

    public CoordenadaMesaTatica {
        validarFaixa("X", eixoX);
        validarFaixa("Y", eixoY);
    }

    private static void validarFaixa(String eixo, double valor) {
        if (valor < 0.0 || valor > 100.0) {
            throw new IllegalArgumentException(
                    "A coordenada " + eixo + " da mesa tatica deve estar entre 0 e 100.");
        }
    }
}
