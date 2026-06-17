package com.torneios.infraestrutura.persistencia.jpa;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.torneios.aplicacao.participacao.conta.ContaRepositorioAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaUsuarioResumo;

@Repository
class ContaRepositorioAplicacaoImpl implements ContaRepositorioAplicacao {

    @Autowired
    ContaUsuarioJpaRepository repositorio;

    @Override
    public Optional<ContaUsuarioResumo> pesquisarPorId(long usuarioId) {
        return repositorio.findById(usuarioId).map(ContaUsuarioJpaResumo::new);
    }
}

record ContaUsuarioJpaResumo(ContaUsuarioJpa jpa) implements ContaUsuarioResumo {
    @Override
    public Long getId() {
        return jpa.id;
    }

    @Override
    public String getNome() {
        return jpa.nome;
    }

    @Override
    public String getEmail() {
        return jpa.email;
    }

    @Override
    public String getTipo() {
        return jpa.tipo;
    }
}
