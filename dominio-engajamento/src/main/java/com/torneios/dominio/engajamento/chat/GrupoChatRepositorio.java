package com.torneios.dominio.engajamento.chat;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface GrupoChatRepositorio {
    void salvar(GrupoChat grupo);
    Optional<GrupoChat> buscarPorId(GrupoChatId grupoId);
    List<GrupoChat> listarPorUsuario(UsuarioId usuarioId);
}
