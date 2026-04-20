package com.torneios.dominio.torneio.estrutura;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.torneio.torneio.TorneioId;

public class EstruturaCompeticao {

    private final TorneioId torneioId;
    private final TipoEstruturaCompeticao tipo;
    private final List<String> etapas;

    public EstruturaCompeticao(TorneioId torneioId, TipoEstruturaCompeticao tipo) {
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio da estrutura e obrigatorio.");
        this.tipo = Objects.requireNonNull(tipo, "O tipo da estrutura e obrigatorio.");
        this.etapas = new ArrayList<>();
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public TipoEstruturaCompeticao getTipo() {
        return tipo;
    }

    public List<String> getEtapas() {
        return List.copyOf(etapas);
    }

    public boolean foiGerada() {
        return !etapas.isEmpty();
    }

    public void adicionarEtapa(String etapa) {
        if (etapa == null || etapa.isBlank()) {
            throw new IllegalArgumentException("A etapa da estrutura nao pode ser vazia.");
        }
        etapas.add(etapa.trim());
    }
}
