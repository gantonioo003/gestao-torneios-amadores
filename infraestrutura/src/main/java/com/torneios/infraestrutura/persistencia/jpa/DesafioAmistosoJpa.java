package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistoso;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoRepositorio;
import com.torneios.dominio.engajamento.desafio.ResultadoAmistoso;
import com.torneios.dominio.engajamento.desafio.StatusDesafioAmistoso;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DESAFIO_AMISTOSO")
class DesafioAmistosoJpa {

    @Id
    Long id;

    Long proponenteId;
    Long timeDesafianteId;
    Long timeDesafiadoId;
    LocalDateTime dataHora;
    String local;
    String status;
    Integer golsDesafiante;
    Integer golsDesafiado;
}

interface DesafioAmistosoJpaRepository extends JpaRepository<DesafioAmistosoJpa, Long> {
    List<DesafioAmistosoJpa> findByTimeDesafianteIdOrTimeDesafiadoId(Long timeDesafianteId, Long timeDesafiadoId);
}

@Repository
class DesafioAmistosoRepositorioImpl implements DesafioAmistosoRepositorio {

    @Autowired
    DesafioAmistosoJpaRepository repositorio;

    @Override
    public void salvar(DesafioAmistoso desafioAmistoso) {
        var jpa = repositorio.findById(desafioAmistoso.getId().valor()).orElse(new DesafioAmistosoJpa());
        jpa.id = desafioAmistoso.getId().valor();
        jpa.proponenteId = desafioAmistoso.getProponenteId().valor();
        jpa.timeDesafianteId = desafioAmistoso.getTimeDesafianteId().valor();
        jpa.timeDesafiadoId = desafioAmistoso.getTimeDesafiadoId().valor();
        jpa.dataHora = desafioAmistoso.getDataHora();
        jpa.local = desafioAmistoso.getLocal();
        jpa.status = desafioAmistoso.getStatus().name();
        jpa.golsDesafiante = desafioAmistoso.getResultado().map(ResultadoAmistoso::golsDesafiante).orElse(null);
        jpa.golsDesafiado = desafioAmistoso.getResultado().map(ResultadoAmistoso::golsDesafiado).orElse(null);
        repositorio.save(jpa);
    }

    @Override
    public Optional<DesafioAmistoso> buscarPorId(DesafioAmistosoId desafioAmistosoId) {
        return repositorio.findById(desafioAmistosoId.valor()).map(this::paraDominio);
    }

    @Override
    public List<DesafioAmistoso> listarHistoricoDoTime(TimeId timeId) {
        return repositorio.findByTimeDesafianteIdOrTimeDesafiadoId(timeId.valor(), timeId.valor()).stream()
                .map(this::paraDominio)
                .toList();
    }

    private DesafioAmistoso paraDominio(DesafioAmistosoJpa jpa) {
        DesafioAmistoso desafioAmistoso = new DesafioAmistoso(
                new DesafioAmistosoId(jpa.id),
                new UsuarioId(jpa.proponenteId),
                new TimeId(jpa.timeDesafianteId),
                new TimeId(jpa.timeDesafiadoId),
                jpa.dataHora,
                jpa.local);
        StatusDesafioAmistoso status = StatusDesafioAmistoso.valueOf(jpa.status);
        switch (status) {
            case PROPOSTO -> {
            }
            case ACEITO -> desafioAmistoso.aceitar();
            case RECUSADO -> desafioAmistoso.recusar();
            case CANCELADO -> desafioAmistoso.cancelar();
            case RESULTADO_REGISTRADO -> {
                desafioAmistoso.aceitar();
                desafioAmistoso.registrarResultado(new ResultadoAmistoso(jpa.golsDesafiante, jpa.golsDesafiado));
            }
        }
        return desafioAmistoso;
    }
}
