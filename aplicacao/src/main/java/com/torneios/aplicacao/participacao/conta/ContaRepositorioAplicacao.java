package com.torneios.aplicacao.participacao.conta;

import java.util.List;
import java.util.Optional;

public interface ContaRepositorioAplicacao {
    Optional<ContaUsuarioResumo> pesquisarPorId(long usuarioId);

    Optional<ContaUsuarioResumo> pesquisarPorNomeUsuario(String nomeUsuario);

    List<ContaUsuarioResumo> pesquisarUsuarios(String termo, long usuarioIdAtual);
}
