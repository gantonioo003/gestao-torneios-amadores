package com.torneios.dominio.engajamento.palpite;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ProgressoPalpiteRepositorio {
    void salvar(ProgressoPalpite progresso);
    Optional<ProgressoPalpite> buscarPorUsuario(UsuarioId usuarioId);
    List<ProgressoPalpite> listarRanking();
}
