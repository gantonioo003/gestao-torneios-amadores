package com.torneios.infraestrutura.persistencia.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.evento.Assistencia;
import com.torneios.dominio.estatisticas.evento.CartaoAmarelo;
import com.torneios.dominio.estatisticas.evento.CartaoVermelho;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoRepositorio;
import com.torneios.dominio.estatisticas.evento.Gol;
import com.torneios.dominio.estatisticas.evento.Substituicao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "EVENTO_ESTATISTICO")
class EventoEstatisticoJpa {

    @Id
    Long id;

    Long torneioId;
    Long partidaId;
    Long jogadorId;
    String tipo;
    boolean automatico;
    Long eventoOrigemId;
    Long jogadorSaiuId;
    Long jogadorEntrouId;
    Integer ordemParadaJogo;
    String descricaoParadaJogo;
}

interface EventoEstatisticoJpaRepository extends JpaRepository<EventoEstatisticoJpa, Long> {
    List<EventoEstatisticoJpa> findByTorneioId(Long torneioId);
    List<EventoEstatisticoJpa> findByPartidaId(Long partidaId);
    List<EventoEstatisticoJpa> findByJogadorIdAndTorneioId(Long jogadorId, Long torneioId);
}

@Repository
class EventoEstatisticoRepositorioImpl implements EventoEstatisticoRepositorio {

    @Autowired
    EventoEstatisticoJpaRepository repositorio;

    @Override
    public void salvar(EventoEstatistico eventoEstatistico) {
        var jpa = repositorio.findById(eventoEstatistico.getId()).orElse(new EventoEstatisticoJpa());
        jpa.id = eventoEstatistico.getId();
        jpa.torneioId = eventoEstatistico.getTorneioId().valor();
        jpa.partidaId = eventoEstatistico.getPartidaId().valor();
        jpa.jogadorId = eventoEstatistico.getJogadorId().valor();
        jpa.tipo = eventoEstatistico.getTipo().name();
        jpa.automatico = eventoEstatistico.isAutomatico();
        jpa.eventoOrigemId = eventoEstatistico.getEventoOrigemId().orElse(null);
        if (eventoEstatistico instanceof Substituicao substituicao) {
            jpa.jogadorSaiuId = substituicao.getJogadorSaiuId().valor();
            jpa.jogadorEntrouId = substituicao.getJogadorEntrouId().valor();
            jpa.ordemParadaJogo = substituicao.getOrdemParadaJogo();
            jpa.descricaoParadaJogo = substituicao.getDescricaoParadaJogo();
        } else {
            jpa.jogadorSaiuId = null;
            jpa.jogadorEntrouId = null;
            jpa.ordemParadaJogo = null;
            jpa.descricaoParadaJogo = null;
        }
        repositorio.save(jpa);
    }

    @Override
    public Optional<EventoEstatistico> buscarPorId(long eventoId) {
        return repositorio.findById(eventoId).map(this::paraDominio);
    }

    @Override
    public void remover(long eventoId) {
        repositorio.deleteById(eventoId);
    }

    @Override
    public List<EventoEstatistico> listarTodos() {
        return repositorio.findAll().stream().map(this::paraDominio).toList();
    }

    @Override
    public List<EventoEstatistico> listarPorTorneio(TorneioId torneioId) {
        return repositorio.findByTorneioId(torneioId.valor()).stream().map(this::paraDominio).toList();
    }

    @Override
    public List<EventoEstatistico> listarPorPartida(PartidaId partidaId) {
        return repositorio.findByPartidaId(partidaId.valor()).stream().map(this::paraDominio).toList();
    }

    @Override
    public List<EventoEstatistico> listarPorJogadorNoTorneio(JogadorId jogadorId, TorneioId torneioId) {
        return repositorio.findByJogadorIdAndTorneioId(jogadorId.valor(), torneioId.valor()).stream()
                .map(this::paraDominio)
                .toList();
    }

    private EventoEstatistico paraDominio(EventoEstatisticoJpa jpa) {
        TorneioId torneioId = new TorneioId(jpa.torneioId);
        PartidaId partidaId = new PartidaId(jpa.partidaId);
        JogadorId jogadorId = new JogadorId(jpa.jogadorId);
        TipoEventoEstatistico tipo = TipoEventoEstatistico.valueOf(jpa.tipo);
        return switch (tipo) {
            case GOL -> new Gol(jpa.id, torneioId, partidaId, jogadorId);
            case ASSISTENCIA -> new Assistencia(jpa.id, torneioId, partidaId, jogadorId);
            case CARTAO_AMARELO -> new CartaoAmarelo(jpa.id, torneioId, partidaId, jogadorId);
            case CARTAO_VERMELHO -> new CartaoVermelho(jpa.id, torneioId, partidaId, jogadorId, jpa.automatico, jpa.eventoOrigemId);
            case SUBSTITUICAO -> new Substituicao(
                    jpa.id,
                    torneioId,
                    partidaId,
                    new JogadorId(jpa.jogadorSaiuId),
                    new JogadorId(jpa.jogadorEntrouId),
                    jpa.ordemParadaJogo,
                    jpa.descricaoParadaJogo);
        };
    }
}
