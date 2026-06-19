package com.torneios.infraestrutura.persistencia.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.enumeracao.EsquemaTatico;
import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.Posicao;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.competicao.escalacao.Escalacao;
import com.torneios.dominio.competicao.escalacao.EscalacaoId;
import com.torneios.dominio.competicao.escalacao.EscalacaoRepositorio;
import com.torneios.dominio.competicao.escalacao.JogadorEscalado;
import com.torneios.dominio.competicao.escalacao.TipoVisualizacaoEscalacao;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "ESCALACAO")
class EscalacaoJpa {
    @Id
    Long id;
    Long partidaId;
    Long timeId;
    String formatoEquipe;
    String tipoVisualizacao;
    String esquemaTatico;
    boolean congelada;

    @OneToMany(mappedBy = "escalacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ORDEM")
    List<EscalacaoTitularJpa> titulares = new ArrayList<>();

    @OneToMany(mappedBy = "escalacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ORDEM")
    List<EscalacaoReservaJpa> reservas = new ArrayList<>();
}

@Entity
@Table(name = "ESCALACAO_TITULAR")
class EscalacaoTitularJpa {
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "ESCALACAO_ID")
    EscalacaoJpa escalacao;

    Long jogadorId;
    String posicao;
}

@Entity
@Table(name = "ESCALACAO_RESERVA")
class EscalacaoReservaJpa {
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "ESCALACAO_ID")
    EscalacaoJpa escalacao;

    Long jogadorId;
}

interface EscalacaoJpaRepository extends JpaRepository<EscalacaoJpa, Long> {
    Optional<EscalacaoJpa> findByPartidaIdAndTimeId(Long partidaId, Long timeId);
    List<EscalacaoJpa> findByPartidaIdOrderByTimeIdAsc(Long partidaId);
}

@Repository
class EscalacaoRepositorioJpa implements EscalacaoRepositorio {

    @Autowired
    EscalacaoJpaRepository repositorio;

    @Override
    public void salvar(Escalacao escalacao) {
        EscalacaoJpa jpa = repositorio
                .findByPartidaIdAndTimeId(escalacao.getPartidaId().valor(), escalacao.getTimeId().valor())
                .orElse(new EscalacaoJpa());
        jpa.id = jpa.id == null ? escalacao.getId().valor() : jpa.id;
        jpa.partidaId = escalacao.getPartidaId().valor();
        jpa.timeId = escalacao.getTimeId().valor();
        jpa.formatoEquipe = escalacao.getFormatoEquipe().name();
        jpa.tipoVisualizacao = escalacao.getTipoVisualizacao().name();
        jpa.esquemaTatico = escalacao.getEsquemaTatico() == null ? null : escalacao.getEsquemaTatico().name();
        jpa.congelada = escalacao.estaCongelada();

        jpa.titulares.clear();
        long sequencia = 1L;
        for (JogadorEscalado titular : escalacao.getTitulares()) {
            EscalacaoTitularJpa item = new EscalacaoTitularJpa();
            item.id = jpa.id * 1000 + sequencia++;
            item.escalacao = jpa;
            item.jogadorId = titular.jogadorId().valor();
            item.posicao = titular.posicao().name();
            jpa.titulares.add(item);
        }

        jpa.reservas.clear();
        sequencia = 501L;
        for (JogadorId reserva : escalacao.getReservas()) {
            EscalacaoReservaJpa item = new EscalacaoReservaJpa();
            item.id = jpa.id * 1000 + sequencia++;
            item.escalacao = jpa;
            item.jogadorId = reserva.valor();
            jpa.reservas.add(item);
        }
        repositorio.save(jpa);
    }

    @Override
    public Optional<Escalacao> buscarPorPartidaETime(PartidaId partidaId, TimeId timeId) {
        return repositorio.findByPartidaIdAndTimeId(partidaId.valor(), timeId.valor()).map(this::toDomain);
    }

    @Override
    public List<Escalacao> listarPorPartida(PartidaId partidaId) {
        return repositorio.findByPartidaIdOrderByTimeIdAsc(partidaId.valor()).stream()
                .map(this::toDomain)
                .toList();
    }

    private Escalacao toDomain(EscalacaoJpa jpa) {
        List<JogadorEscalado> titulares = jpa.titulares.stream()
                .map(item -> new JogadorEscalado(
                        new JogadorId(item.jogadorId),
                        Posicao.valueOf(item.posicao)))
                .toList();
        List<JogadorId> reservas = jpa.reservas.stream()
                .map(item -> new JogadorId(item.jogadorId))
                .toList();
        Escalacao escalacao = new Escalacao(
                new EscalacaoId(jpa.id),
                new PartidaId(jpa.partidaId),
                new TimeId(jpa.timeId),
                FormatoEquipe.valueOf(jpa.formatoEquipe),
                TipoVisualizacaoEscalacao.valueOf(jpa.tipoVisualizacao),
                jpa.esquemaTatico == null ? null : EsquemaTatico.valueOf(jpa.esquemaTatico),
                titulares,
                reservas);
        if (jpa.congelada) {
            escalacao.congelar();
        }
        return escalacao;
    }
}
