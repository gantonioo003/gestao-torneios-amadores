package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;
import com.torneios.dominio.torneio.torneio.PreparacaoCompeticaoInvalidador;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PARTIDA")
class PartidaJpa {

    @Id
    Long id;

    Long torneioId;
    Long mandanteId;
    Long visitanteId;
    String etapa;
    int quantidadeJogadoresPorEquipe;
    boolean iniciada;
    boolean encerrada;

    @Column(nullable = true)
    Integer golsMandante;

    @Column(nullable = true)
    Integer golsVisitante;

    @Column(nullable = true)
    LocalDateTime dataHoraRegistroResultado;
}

interface PartidaJpaRepository extends JpaRepository<PartidaJpa, Long> {
    List<PartidaJpa> findByTorneioId(Long torneioId);
    void deleteByTorneioId(Long torneioId);
}

@Repository
class PartidaRepositorioImpl implements PartidaRepositorio, PreparacaoCompeticaoInvalidador {

    @Autowired
    PartidaJpaRepository repositorio;

    @Override
    public void salvar(Partida partida) {
        var jpa = repositorio.findById(partida.getId().valor()).orElse(new PartidaJpa());
        jpa.id = partida.getId().valor();
        jpa.torneioId = partida.getTorneioId().valor();
        jpa.mandanteId = partida.getMandante().valor();
        jpa.visitanteId = partida.getVisitante().valor();
        jpa.etapa = partida.getEtapa();
        jpa.quantidadeJogadoresPorEquipe = partida.getQuantidadeJogadoresPorEquipe();
        jpa.iniciada = partida.estaIniciada();
        jpa.encerrada = partida.estaEncerrada();

        if (partida.getResultado() != null) {
            jpa.golsMandante = partida.getResultado().golsMandante();
            jpa.golsVisitante = partida.getResultado().golsVisitante();
            jpa.dataHoraRegistroResultado = partida.getDataHoraRegistroResultado();
        } else {
            jpa.golsMandante = null;
            jpa.golsVisitante = null;
            jpa.dataHoraRegistroResultado = null;
        }

        repositorio.save(jpa);
    }

    @Override
    public Optional<Partida> buscarPorId(PartidaId partidaId) {
        return repositorio.findById(partidaId.valor()).map(this::toDomain);
    }

    @Override
    public List<Partida> listarPorTorneio(TorneioId torneioId) {
        return repositorio.findByTorneioId(torneioId.valor())
                          .stream().map(this::toDomain).toList();
    }

    @Override
    public void invalidar(TorneioId torneioId) {
        repositorio.deleteByTorneioId(torneioId.valor());
    }

    private Partida toDomain(PartidaJpa jpa) {
        var partida = new Partida(
                new PartidaId(jpa.id),
                new TorneioId(jpa.torneioId),
                new TimeId(jpa.mandanteId),
                new TimeId(jpa.visitanteId),
                jpa.etapa,
                jpa.quantidadeJogadoresPorEquipe);

        if (jpa.encerrada && jpa.golsMandante != null && jpa.golsVisitante != null && jpa.dataHoraRegistroResultado != null) {
            partida.registrarResultado(new ResultadoPartida(jpa.golsMandante, jpa.golsVisitante),
                    jpa.dataHoraRegistroResultado);
        } else if (jpa.iniciada) {
            partida.iniciar();
        }

        return partida;
    }
}
