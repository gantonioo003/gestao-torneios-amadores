package com.torneios.dominio.engajamento.desafio;

public record ResultadoAmistoso(int golsDesafiante, int golsDesafiado) {

    public ResultadoAmistoso {
        if (golsDesafiante < 0 || golsDesafiado < 0) {
            throw new IllegalArgumentException("O resultado do amistoso nao pode ter gols negativos.");
        }
    }
}
