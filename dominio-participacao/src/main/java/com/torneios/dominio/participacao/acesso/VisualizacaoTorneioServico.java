package com.torneios.dominio.participacao.acesso;

import java.util.List;
import java.util.Objects;

public class VisualizacaoTorneioServico {

    private final CatalogoTorneiosDisponiveis catalogoTorneiosDisponiveis;

    public VisualizacaoTorneioServico(CatalogoTorneiosDisponiveis catalogoTorneiosDisponiveis) {
        this.catalogoTorneiosDisponiveis = Objects.requireNonNull(catalogoTorneiosDisponiveis,
                "O catalogo de torneios disponiveis e obrigatorio.");
    }

    public List<TorneioDisponivel> visualizarTorneiosDisponiveis() {
        return catalogoTorneiosDisponiveis.listarTorneiosDisponiveis();
    }

    public boolean existemTorneiosDisponiveis() {
        return !visualizarTorneiosDisponiveis().isEmpty();
    }
}
