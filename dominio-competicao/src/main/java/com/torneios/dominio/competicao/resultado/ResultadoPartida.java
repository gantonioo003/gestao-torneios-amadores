package com.torneios.dominio.competicao.resultado;

public record ResultadoPartida(int golsMandante, int golsVisitante) {

    public ResultadoPartida {
        if (golsMandante < 0 || golsVisitante < 0) {
            throw new IllegalArgumentException("O placar da partida nao pode ter gols negativos.");
        }
    }

    public boolean empate() {
        return golsMandante == golsVisitante;
    }

    public boolean mandanteVenceu() {
        return golsMandante > golsVisitante;
    }

    public boolean visitanteVenceu() {
        return golsVisitante > golsMandante;
    }

    public int saldoMandante() {
        return golsMandante - golsVisitante;
    }

    public int saldoVisitante() {
        return golsVisitante - golsMandante;
    }
}
