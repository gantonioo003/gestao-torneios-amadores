package com.torneios.infraestrutura.persistencia.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.torneios.aplicacao.competicao.andamento.PartidaRepositorioAplicacao;
import com.torneios.aplicacao.competicao.andamento.PartidaResumo;

@Repository
class PartidaRepositorioAplicacaoImpl implements PartidaRepositorioAplicacao {

    @Autowired
    PartidaJpaRepository repositorio;

    @Override
    public List<PartidaResumo> pesquisarResumosPorTorneio(long torneioId) {
        return repositorio.findByTorneioId(torneioId).stream()
                .map(jpa -> (PartidaResumo) new PartidaJpaResumo(jpa))
                .toList();
    }
}

record PartidaJpaResumo(PartidaJpa jpa) implements PartidaResumo {
    @Override
    public Long getId() {
        return jpa.id;
    }

    @Override
    public Long getTorneioId() {
        return jpa.torneioId;
    }

    @Override
    public Long getMandanteId() {
        return jpa.mandanteId;
    }

    @Override
    public Long getVisitanteId() {
        return jpa.visitanteId;
    }

    @Override
    public String getEtapa() {
        return jpa.etapa;
    }

    @Override
    public boolean isEncerrada() {
        return jpa.encerrada;
    }

    @Override
    public Integer getGolsMandante() {
        return jpa.golsMandante;
    }

    @Override
    public Integer getGolsVisitante() {
        return jpa.golsVisitante;
    }
}
