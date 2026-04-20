package com.torneios.dominio.compartilhado.evento;

import com.torneios.dominio.compartilhado.partida.PartidaId;

public class ResultadoRegistradoEvento extends EventoDominio {

    private final PartidaId partidaId;

    public ResultadoRegistradoEvento(PartidaId partidaId) {
        super("ResultadoRegistrado");
        this.partidaId = partidaId;
    }

    public PartidaId getPartidaId() {
        return partidaId;
    }
}
