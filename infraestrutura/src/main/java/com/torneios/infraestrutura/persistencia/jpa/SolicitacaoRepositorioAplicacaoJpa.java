package com.torneios.infraestrutura.persistencia.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.torneios.aplicacao.participacao.candidatura.SolicitacaoRepositorioAplicacao;
import com.torneios.aplicacao.participacao.candidatura.SolicitacaoResumo;
import com.torneios.dominio.compartilhado.enumeracao.StatusSolicitacao;

@Repository
class SolicitacaoRepositorioAplicacaoImpl implements SolicitacaoRepositorioAplicacao {

    @Autowired
    SolicitacaoJpaRepository repositorio;

    @Override
    public List<SolicitacaoResumo> pesquisarPorSolicitante(long solicitanteId) {
        return repositorio.findBySolicitanteId(solicitanteId).stream()
                .map(jpa -> (SolicitacaoResumo) new SolicitacaoJpaResumo(jpa))
                .toList();
    }

    @Override
    public List<SolicitacaoResumo> pesquisarPendentesPorTorneio(long torneioId) {
        return repositorio.findByTorneioIdAndStatus(torneioId, StatusSolicitacao.PENDENTE.name()).stream()
                .map(jpa -> (SolicitacaoResumo) new SolicitacaoJpaResumo(jpa))
                .toList();
    }
}

record SolicitacaoJpaResumo(SolicitacaoParticipacaoJpa jpa) implements SolicitacaoResumo {
    @Override
    public Long getId() {
        return jpa.id;
    }

    @Override
    public Long getTimeId() {
        return jpa.timeId;
    }

    @Override
    public Long getTorneioId() {
        return jpa.torneioId;
    }

    @Override
    public Long getSolicitanteId() {
        return jpa.solicitanteId;
    }

    @Override
    public String getStatus() {
        return jpa.status;
    }
}
