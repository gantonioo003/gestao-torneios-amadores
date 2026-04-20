package com.torneios.dominio.compartilhado.partida;

public record Resultado(int golsTimeA, int golsTimeB) {

    public Resultado {
        if (golsTimeA < 0 || golsTimeB < 0) {
            throw new IllegalArgumentException("O placar da partida nao pode ter gols negativos.");
        }
    }

    public boolean empate() {
        return golsTimeA == golsTimeB;
    }

    public boolean vitoriaTimeA() {
        return golsTimeA > golsTimeB;
    }

    public boolean vitoriaTimeB() {
        return golsTimeB > golsTimeA;
    }

    public int saldo() {
        return golsTimeA - golsTimeB;
    }
}
