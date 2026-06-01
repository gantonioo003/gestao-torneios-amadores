package com.torneios.aplicacao.participacao.conta;

import java.util.Optional;

public interface ContaRepositorioAplicacao {
    Optional<ContaUsuarioResumo> pesquisarPorId(long usuarioId);
}
