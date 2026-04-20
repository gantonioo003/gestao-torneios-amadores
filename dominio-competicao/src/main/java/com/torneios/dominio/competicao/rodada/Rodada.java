package com.torneios.dominio.competicao.rodada;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public class Rodada {

    private final TorneioId torneioId;
    private final int numero;
    private final List<PartidaId> partidas;

    public Rodada(TorneioId torneioId, int numero) {
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio da rodada e obrigatorio.");
        if (numero <= 0) {
            throw new IllegalArgumentException("O numero da rodada deve ser maior que zero.");
        }
        this.numero = numero;
        this.partidas = new ArrayList<>();
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public int getNumero() {
        return numero;
    }

    public List<PartidaId> getPartidas() {
        return List.copyOf(partidas);
    }

    public void adicionarPartida(PartidaId partidaId) {
        partidas.add(Objects.requireNonNull(partidaId, "A partida da rodada e obrigatoria."));
    }
}
