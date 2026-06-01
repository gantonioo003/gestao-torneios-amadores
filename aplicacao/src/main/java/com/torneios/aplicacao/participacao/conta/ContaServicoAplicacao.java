package com.torneios.aplicacao.participacao.conta;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Optional;

public class ContaServicoAplicacao {

    private final ContaRepositorioAplicacao repositorio;

    public ContaServicoAplicacao(ContaRepositorioAplicacao repositorio) {
        notNull(repositorio, "O repositorio nao pode ser nulo.");
        this.repositorio = repositorio;
    }

    public Optional<ContaUsuarioResumo> pesquisarPorId(long usuarioId) {
        return repositorio.pesquisarPorId(usuarioId);
    }
}
