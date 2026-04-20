package com.torneios.dominio.competicao.classificacao;

import java.util.Objects;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

public class Classificacao {

    private final TorneioId torneioId;
    private final TimeId timeId;
    private int pontos;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int golsPro;
    private int golsContra;

    public Classificacao(TorneioId torneioId, TimeId timeId) {
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio da classificacao e obrigatorio.");
        this.timeId = Objects.requireNonNull(timeId, "O time da classificacao e obrigatorio.");
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public TimeId getTimeId() {
        return timeId;
    }

    public int getPontos() {
        return pontos;
    }

    public int getVitorias() {
        return vitorias;
    }

    public int getEmpates() {
        return empates;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getGolsPro() {
        return golsPro;
    }

    public int getGolsContra() {
        return golsContra;
    }

    public int getSaldoGols() {
        return golsPro - golsContra;
    }

    public void registrarComoMandante(ResultadoPartida resultadoPartida) {
        golsPro += resultadoPartida.golsMandante();
        golsContra += resultadoPartida.golsVisitante();
        atualizarPontos(resultadoPartida.mandanteVenceu(), resultadoPartida.empate());
    }

    public void registrarComoVisitante(ResultadoPartida resultadoPartida) {
        golsPro += resultadoPartida.golsVisitante();
        golsContra += resultadoPartida.golsMandante();
        atualizarPontos(resultadoPartida.visitanteVenceu(), resultadoPartida.empate());
    }

    private void atualizarPontos(boolean venceu, boolean empatou) {
        if (venceu) {
            pontos += 3;
            vitorias++;
            return;
        }
        if (empatou) {
            pontos += 1;
            empates++;
            return;
        }
        derrotas++;
    }
}
