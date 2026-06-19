package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.feed.Denuncia;
import com.torneios.dominio.engajamento.feed.DenunciaId;
import com.torneios.dominio.engajamento.feed.DenunciaRepositorio;
import com.torneios.dominio.engajamento.feed.StatusDenuncia;
import com.torneios.dominio.engajamento.feed.TipoAlvoDenuncia;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DENUNCIA")
class DenunciaJpa {
    @Id Long id;
    Long denuncianteId;
    String tipoAlvo;
    Long alvoId;
    String motivo;
    String status;
    LocalDateTime criadaEm;
}

interface DenunciaJpaRepository extends JpaRepository<DenunciaJpa, Long> {
    boolean existsByDenuncianteIdAndTipoAlvoAndAlvoIdAndStatus(
            Long denuncianteId, String tipoAlvo, Long alvoId, String status);
    List<DenunciaJpa> findByStatusOrderByCriadaEmAsc(String status);
}

@Repository
class DenunciaRepositorioImpl implements DenunciaRepositorio {
    @Autowired DenunciaJpaRepository repositorio;

    @Override
    public void salvar(Denuncia denuncia) {
        DenunciaJpa jpa = repositorio.findById(denuncia.getId().valor()).orElse(new DenunciaJpa());
        jpa.id = denuncia.getId().valor();
        jpa.denuncianteId = denuncia.getDenuncianteId().valor();
        jpa.tipoAlvo = denuncia.getTipoAlvo().name();
        jpa.alvoId = denuncia.getAlvoId();
        jpa.motivo = denuncia.getMotivo();
        jpa.status = denuncia.getStatus().name();
        jpa.criadaEm = denuncia.getCriadaEm();
        repositorio.save(jpa);
    }

    @Override
    public Optional<Denuncia> buscarPorId(DenunciaId id) {
        return repositorio.findById(id.valor()).map(this::paraDominio);
    }

    @Override
    public boolean existePendente(UsuarioId denuncianteId, TipoAlvoDenuncia tipoAlvo, long alvoId) {
        return repositorio.existsByDenuncianteIdAndTipoAlvoAndAlvoIdAndStatus(
                denuncianteId.valor(), tipoAlvo.name(), alvoId, StatusDenuncia.PENDENTE.name());
    }

    @Override
    public List<Denuncia> listarPendentes() {
        return repositorio.findByStatusOrderByCriadaEmAsc(StatusDenuncia.PENDENTE.name()).stream()
                .map(this::paraDominio)
                .toList();
    }

    private Denuncia paraDominio(DenunciaJpa jpa) {
        Denuncia denuncia = new Denuncia(
                new DenunciaId(jpa.id),
                new UsuarioId(jpa.denuncianteId),
                TipoAlvoDenuncia.valueOf(jpa.tipoAlvo),
                jpa.alvoId,
                jpa.motivo);
        ReflexaoDominioJpa.definirCampo(denuncia, "criadaEm", jpa.criadaEm);
        ReflexaoDominioJpa.definirCampo(denuncia, "status", StatusDenuncia.valueOf(jpa.status));
        return denuncia;
    }
}
