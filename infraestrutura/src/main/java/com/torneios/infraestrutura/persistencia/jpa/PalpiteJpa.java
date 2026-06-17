package com.torneios.infraestrutura.persistencia.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.palpite.EventoAlvo;
import com.torneios.dominio.engajamento.palpite.OpcaoPalpite;
import com.torneios.dominio.engajamento.palpite.Palpite;
import com.torneios.dominio.engajamento.palpite.PalpiteId;
import com.torneios.dominio.engajamento.palpite.PalpiteRepositorio;
import com.torneios.dominio.engajamento.palpite.TipoPalpite;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PALPITE")
class PalpiteJpa {

    @Id
    Long id;

    Long usuarioId;
    String identificadorVotante;
    String tipo;
    Long torneioId;
    Long partidaId;
    long opcao;
    boolean apurado;
    Boolean acertou;
}

interface PalpiteJpaRepository extends JpaRepository<PalpiteJpa, Long> {
    Optional<PalpiteJpa> findByIdentificadorVotanteAndTipoAndTorneioIdAndPartidaId(
            String identificadorVotante,
            String tipo,
            Long torneioId,
            Long partidaId);

    List<PalpiteJpa> findByTipoAndTorneioIdAndPartidaId(String tipo, Long torneioId, Long partidaId);
}

@Repository
class PalpiteRepositorioImpl implements PalpiteRepositorio {

    @Autowired
    PalpiteJpaRepository repositorio;

    @Override
    public void salvar(Palpite palpite) {
        var jpa = repositorio.findById(palpite.getId().valor()).orElse(new PalpiteJpa());
        jpa.id = palpite.getId().valor();
        jpa.usuarioId = palpite.getUsuarioId() == null ? null : palpite.getUsuarioId().valor();
        jpa.identificadorVotante = palpite.getIdentificadorVotante();
        jpa.tipo = palpite.getEventoAlvo().getTipo().name();
        jpa.torneioId = palpite.getEventoAlvo().getTorneioId().valor();
        jpa.partidaId = palpite.getEventoAlvo().getPartidaId() == null ? null : palpite.getEventoAlvo().getPartidaId().valor();
        jpa.opcao = palpite.getOpcao().valor();
        jpa.apurado = palpite.estaApurado();
        jpa.acertou = palpite.acertou().orElse(null);
        repositorio.save(jpa);
    }

    @Override
    public Optional<Palpite> buscarPorUsuarioEEvento(UsuarioId usuarioId, EventoAlvo eventoAlvo) {
        return buscarPorVotanteEEvento("USUARIO:" + usuarioId.valor(), eventoAlvo);
    }

    @Override
    public Optional<Palpite> buscarPorVotanteEEvento(String identificadorVotante, EventoAlvo eventoAlvo) {
        return repositorio.findByIdentificadorVotanteAndTipoAndTorneioIdAndPartidaId(
                        identificadorVotante,
                        eventoAlvo.getTipo().name(),
                        eventoAlvo.getTorneioId().valor(),
                        eventoAlvo.getPartidaId() == null ? null : eventoAlvo.getPartidaId().valor())
                .map(this::paraDominio);
    }

    @Override
    public List<Palpite> listarPorEvento(EventoAlvo eventoAlvo) {
        return repositorio.findByTipoAndTorneioIdAndPartidaId(
                        eventoAlvo.getTipo().name(),
                        eventoAlvo.getTorneioId().valor(),
                        eventoAlvo.getPartidaId() == null ? null : eventoAlvo.getPartidaId().valor()).stream()
                .map(this::paraDominio)
                .toList();
    }

    private Palpite paraDominio(PalpiteJpa jpa) {
        EventoAlvo eventoAlvo = criarEventoAlvo(jpa.tipo, jpa.torneioId, jpa.partidaId);
        Palpite palpite = jpa.usuarioId == null
                ? new Palpite(new PalpiteId(jpa.id), jpa.identificadorVotante.replaceFirst("^VISITANTE:", ""), eventoAlvo,
                        new OpcaoPalpite(jpa.opcao))
                : new Palpite(new PalpiteId(jpa.id), new UsuarioId(jpa.usuarioId), eventoAlvo, new OpcaoPalpite(jpa.opcao));
        ReflexaoDominioJpa.definirCampo(palpite, "apurado", jpa.apurado);
        ReflexaoDominioJpa.definirCampo(palpite, "acertou", jpa.acertou);
        return palpite;
    }

    private EventoAlvo criarEventoAlvo(String tipo, Long torneioId, Long partidaId) {
        TipoPalpite tipoPalpite = TipoPalpite.valueOf(tipo);
        return switch (tipoPalpite) {
            case VENCEDOR_PARTIDA -> EventoAlvo.paraPartida(new TorneioId(torneioId), new PartidaId(partidaId));
            case CAMPEAO_TORNEIO -> EventoAlvo.paraCampeao(new TorneioId(torneioId));
            case ARTILHEIRO_TORNEIO -> EventoAlvo.paraArtilheiro(new TorneioId(torneioId));
            case LIDER_ASSISTENCIAS_TORNEIO -> EventoAlvo.paraLiderAssistencias(new TorneioId(torneioId));
        };
    }
}
