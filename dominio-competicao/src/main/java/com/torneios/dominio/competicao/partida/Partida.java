package com.torneios.dominio.competicao.partida;

import java.util.Objects;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

public class Partida {

    private final PartidaId id;
    private final TorneioId torneioId;
    private final TimeId mandante;
    private final TimeId visitante;
    private ResultadoPartida resultado;
    private boolean encerrada;

    public Partida(PartidaId id, TorneioId torneioId, TimeId mandante, TimeId visitante) {
        this.id = Objects.requireNonNull(id, "O id da partida e obrigatorio.");
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio da partida e obrigatorio.");
        this.mandante = Objects.requireNonNull(mandante, "O time mandante e obrigatorio.");
        this.visitante = Objects.requireNonNull(visitante, "O time visitante e obrigatorio.");
        if (mandante.equals(visitante)) {
            throw new IllegalArgumentException("Uma partida nao pode ter o mesmo time nos dois lados.");
        }
        this.encerrada = false;
    }

    public PartidaId getId() {
        return id;
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public TimeId getMandante() {
        return mandante;
    }

    public TimeId getVisitante() {
        return visitante;
    }

    public ResultadoPartida getResultado() {
        return resultado;
    }

    public boolean estaEncerrada() {
        return encerrada;
    }

    public void registrarResultado(ResultadoPartida resultado) {
        if (encerrada) {
            throw new IllegalStateException("Nao e permitido registrar resultado novamente para uma partida encerrada.");
        }
        this.resultado = Objects.requireNonNull(resultado, "O resultado da partida e obrigatorio.");
        this.encerrada = true;
    }
}
