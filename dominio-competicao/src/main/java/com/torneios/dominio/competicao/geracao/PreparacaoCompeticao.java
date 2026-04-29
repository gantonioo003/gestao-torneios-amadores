package com.torneios.dominio.competicao.geracao;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.rodada.Rodada;

public class PreparacaoCompeticao {

    private final TorneioId torneioId;
    private final List<Partida> partidas;
    private final List<Rodada> rodadas;

    public PreparacaoCompeticao(TorneioId torneioId, List<Partida> partidas, List<Rodada> rodadas) {
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio da preparacao e obrigatorio.");
        this.partidas = List.copyOf(Objects.requireNonNull(partidas, "As partidas da preparacao sao obrigatorias."));
        this.rodadas = List.copyOf(Objects.requireNonNull(rodadas, "As rodadas da preparacao sao obrigatorias."));
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public List<Rodada> getRodadas() {
        return rodadas;
    }
}
