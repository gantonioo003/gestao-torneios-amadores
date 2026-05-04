package com.torneios.dominio.engajamento.chat;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConversaPrivadaRepositorio {

    void salvar(ConversaPrivada conversaPrivada);

    Optional<ConversaPrivada> buscarPorId(ConversaPrivadaId conversaPrivadaId);

    List<ConversaPrivada> listarPorUsuario(UsuarioId usuarioId);

    List<ConversaPrivada> listarSolicitadasParaUsuario(UsuarioId usuarioId);

    List<ConversaPrivada> listarAprovadasPorUsuario(UsuarioId usuarioId);
}
