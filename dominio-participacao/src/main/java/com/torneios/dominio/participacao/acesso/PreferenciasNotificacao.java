package com.torneios.dominio.participacao.acesso;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class PreferenciasNotificacao {

    private final UsuarioId usuarioId;
    private final EnumSet<CategoriaNotificacao> categoriasAtivas;

    public PreferenciasNotificacao(UsuarioId usuarioId, Set<CategoriaNotificacao> categoriasAtivas) {
        this.usuarioId = Objects.requireNonNull(usuarioId, "O usuario das preferencias e obrigatorio.");
        this.categoriasAtivas = categoriasAtivas == null || categoriasAtivas.isEmpty()
                ? EnumSet.noneOf(CategoriaNotificacao.class)
                : EnumSet.copyOf(categoriasAtivas);
    }

    public static PreferenciasNotificacao todasAtivas(UsuarioId usuarioId) {
        return new PreferenciasNotificacao(usuarioId, EnumSet.allOf(CategoriaNotificacao.class));
    }

    public UsuarioId getUsuarioId() {
        return usuarioId;
    }

    public Set<CategoriaNotificacao> getCategoriasAtivas() {
        return Set.copyOf(categoriasAtivas);
    }

    public boolean permite(CategoriaNotificacao categoria) {
        return categoriasAtivas.contains(categoria);
    }
}
