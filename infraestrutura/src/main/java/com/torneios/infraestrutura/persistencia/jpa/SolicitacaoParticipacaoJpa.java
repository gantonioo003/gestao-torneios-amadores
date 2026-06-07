package com.torneios.infraestrutura.persistencia.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.enumeracao.StatusSolicitacao;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacao;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoRepositorio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SOLICITACAO_PARTICIPACAO")
class SolicitacaoParticipacaoJpa {
    @Id
    Long id;
    Long solicitanteId;
    Long timeId;
    Long torneioId;
    String status;
}

interface SolicitacaoJpaRepository extends JpaRepository<SolicitacaoParticipacaoJpa, Long> {
    List<SolicitacaoParticipacaoJpa> findByTorneioIdAndStatus(Long torneioId, String status);
    List<SolicitacaoParticipacaoJpa> findBySolicitanteId(Long solicitanteId);
    boolean existsByTimeIdAndTorneioIdAndStatus(Long timeId, Long torneioId, String status);
}

@Repository
class SolicitacaoParticipacaoRepositorioImpl implements SolicitacaoParticipacaoRepositorio {

    @Autowired
    SolicitacaoJpaRepository repositorio;

    @Override
    public void salvar(SolicitacaoParticipacao sol) {
        var jpa = repositorio.findById(sol.getId().valor()).orElse(new SolicitacaoParticipacaoJpa());
        jpa.id = sol.getId().valor();
        jpa.solicitanteId = sol.getSolicitante().valor();
        jpa.timeId = sol.getTimeId().valor();
        jpa.torneioId = sol.getTorneioId().valor();
        jpa.status = sol.getStatus().name();
        repositorio.save(jpa);
    }

    @Override
    public Optional<SolicitacaoParticipacao> buscarPorId(SolicitacaoParticipacaoId id) {
        return repositorio.findById(id.valor()).map(this::toDomain);
    }

    @Override
    public List<SolicitacaoParticipacao> listarPendentesPorTorneio(TorneioId torneioId) {
        return repositorio
            .findByTorneioIdAndStatus(torneioId.valor(), StatusSolicitacao.PENDENTE.name())
            .stream().map(this::toDomain).toList();
    }

    @Override
    public List<SolicitacaoParticipacao> listarPorSolicitante(UsuarioId usuarioId) {
        return repositorio.findBySolicitanteId(usuarioId.valor())
            .stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existePendentePorTimeETorneio(TimeId timeId, TorneioId torneioId) {
        return repositorio.existsByTimeIdAndTorneioIdAndStatus(
            timeId.valor(), torneioId.valor(), StatusSolicitacao.PENDENTE.name());
    }

    private SolicitacaoParticipacao toDomain(SolicitacaoParticipacaoJpa jpa) {
        var sol = new SolicitacaoParticipacao(
            new SolicitacaoParticipacaoId(jpa.id),
            new UsuarioId(jpa.solicitanteId),
            new TimeId(jpa.timeId),
            new TorneioId(jpa.torneioId)
        );
        var status = StatusSolicitacao.valueOf(jpa.status);
        switch (status) {
            case APROVADA -> sol.aprovar();
            case REJEITADA -> sol.rejeitar();
            case CANCELADA -> sol.cancelar(new UsuarioId(jpa.solicitanteId));
            default -> { }
        }
        return sol;
    }
}
