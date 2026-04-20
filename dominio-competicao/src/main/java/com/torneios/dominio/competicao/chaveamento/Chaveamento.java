package com.torneios.dominio.competicao.chaveamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public class Chaveamento {

    private final TorneioId torneioId;
    private final List<String> fases;
    private final List<PartidaId> partidas;

    public Chaveamento(TorneioId torneioId) {
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio do chaveamento e obrigatorio.");
        this.fases = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public List<String> getFases() {
        return List.copyOf(fases);
    }

    public List<PartidaId> getPartidas() {
        return List.copyOf(partidas);
    }

    public void adicionarFase(String fase) {
        if (fase == null || fase.isBlank()) {
            throw new IllegalArgumentException("A fase do chaveamento e obrigatoria.");
        }
        fases.add(fase.trim());
    }

    public void vincularPartida(PartidaId partidaId) {
        partidas.add(Objects.requireNonNull(partidaId, "A partida do chaveamento e obrigatoria."));
    }
}
