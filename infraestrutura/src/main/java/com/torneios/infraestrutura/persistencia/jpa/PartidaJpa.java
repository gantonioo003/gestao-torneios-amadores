package com.torneios.infraestrutura.persistencia.jpa;

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
    boolean encerrada;

    @Column(nullable = true)
    Integer golsMandante;

    @Column(nullable = true)
    Integer golsVisitante;
}

interface PartidaJpaRepository extends JpaRepository<PartidaJpa, Long> {
    List<PartidaJpa> findByTorneioId(Long torneioId);
}

@Repository
class PartidaRepositorioImpl implements PartidaRepositorio {

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
        jpa.encerrada = partida.estaEncerrada();

        if (partida.getResultado() != null) {
            jpa.golsMandante = partida.getResultado().golsMandante();
            jpa.golsVisitante = partida.getResultado().golsVisitante();
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

    private Partida toDomain(PartidaJpa jpa) {
        var partida = new Partida(
                new PartidaId(jpa.id),
                new TorneioId(jpa.torneioId),
                new TimeId(jpa.mandanteId),
                new TimeId(jpa.visitanteId),
                jpa.etapa,
                jpa.quantidadeJogadoresPorEquipe);

        if (jpa.encerrada && jpa.golsMandante != null && jpa.golsVisitante != null) {
            partida.registrarResultado(new ResultadoPartida(jpa.golsMandante, jpa.golsVisitante));
        }

        return partida;
    }
}
