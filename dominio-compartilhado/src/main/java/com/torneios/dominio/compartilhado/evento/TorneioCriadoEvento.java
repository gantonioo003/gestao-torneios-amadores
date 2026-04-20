package com.torneios.dominio.compartilhado.evento;

import com.torneios.dominio.compartilhado.torneio.TorneioId;

public class TorneioCriadoEvento extends EventoDominio {

    private final TorneioId torneioId;

    public TorneioCriadoEvento(TorneioId torneioId) {
        super("TorneioCriado");
        this.torneioId = torneioId;
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }
}
