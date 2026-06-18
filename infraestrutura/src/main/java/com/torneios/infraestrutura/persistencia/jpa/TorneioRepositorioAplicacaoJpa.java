package com.torneios.infraestrutura.persistencia.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.torneios.aplicacao.torneio.criacao.TorneioRepositorioAplicacao;
import com.torneios.aplicacao.torneio.criacao.TorneioResumo;

@Repository
class TorneioRepositorioAplicacaoImpl implements TorneioRepositorioAplicacao {

    @Autowired
    TorneioJpaRepository repositorio;

    @Override
    public List<TorneioResumo> pesquisarResumos() {
        return repositorio.findAll().stream()
                .map(jpa -> (TorneioResumo) new TorneioJpaResumo(jpa))
                .toList();
    }

    @Override
    public List<TorneioResumo> pesquisarResumosPorNome(String nome) {
        return repositorio.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome).stream()
                .map(jpa -> (TorneioResumo) new TorneioJpaResumo(jpa))
                .toList();
    }

    @Override
    public List<TorneioResumo> pesquisarResumosPorOrganizador(long organizadorId) {
        return repositorio.findByOrganizadorId(organizadorId).stream()
                .map(jpa -> (TorneioResumo) new TorneioJpaResumo(jpa))
                .toList();
    }
}

record TorneioJpaResumo(TorneioJpa jpa) implements TorneioResumo {
    @Override
    public Long getId() {
        return jpa.id;
    }

    @Override
    public String getNome() {
        return jpa.nome;
    }

    @Override
    public String getFormato() {
        return jpa.formato != null ? jpa.formato.name() : null;
    }

    @Override
    public String getFormatoEquipe() {
        return jpa.formatoEquipe != null ? jpa.formatoEquipe.name() : null;
    }

    @Override
    public Long getOrganizadorId() {
        return jpa.organizadorId;
    }

    @Override
    public String getStatus() {
        return jpa.status != null ? jpa.status.name() : null;
    }

    @Override
    public boolean aceitaSolicitacoes() {
        return jpa.aceitaSolicitacoes;
    }
}
