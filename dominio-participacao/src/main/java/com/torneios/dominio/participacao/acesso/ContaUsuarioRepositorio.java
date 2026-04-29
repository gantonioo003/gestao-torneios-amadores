package com.torneios.dominio.participacao.acesso;

import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ContaUsuarioRepositorio {

    void salvar(ContaUsuario contaUsuario);

    Optional<ContaUsuario> buscarPorId(UsuarioId usuarioId);

    Optional<ContaUsuario> buscarPorEmail(String email);

    void remover(UsuarioId usuarioId);
}
