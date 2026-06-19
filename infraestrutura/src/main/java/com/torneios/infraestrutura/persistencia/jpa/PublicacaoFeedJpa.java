package com.torneios.infraestrutura.persistencia.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.feed.FeedTorneioRepositorio;
import com.torneios.dominio.engajamento.feed.PublicacaoFeed;
import com.torneios.dominio.engajamento.feed.PublicacaoFeedId;
import com.torneios.dominio.engajamento.feed.TipoPublicacaoFeed;
import com.torneios.dominio.engajamento.feed.TipoReacaoFeed;
import com.torneios.dominio.engajamento.feed.TipoIdentidadeFeed;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "PUBLICACAO_FEED")
class PublicacaoFeedJpa {

    @Id
    Long id;

    Long torneioId;
    String tipo;
    Long autorId;
    Long partidaId;
    String tipoIdentidade;
    Long identidadeId;
    Long publicacaoPaiId;
    LocalDateTime criadaEm;
    String conteudo;

    @Lob
    String hashtagsData;

    @Lob
    String midiasData;

    @Lob
    String curtidasData;

    @Lob
    String reacoesData;

    boolean removida;
}

interface PublicacaoFeedJpaRepository extends JpaRepository<PublicacaoFeedJpa, Long> {
    List<PublicacaoFeedJpa> findByTorneioId(Long torneioId);
    List<PublicacaoFeedJpa> findByAutorIdOrderByCriadaEmDesc(Long autorId);
    List<PublicacaoFeedJpa> findByPublicacaoPaiIdOrderByCriadaEmAsc(Long publicacaoPaiId);
}

@Repository
class FeedTorneioRepositorioImpl implements FeedTorneioRepositorio {

    @Autowired
    PublicacaoFeedJpaRepository repositorio;

    @Override
    public void salvar(PublicacaoFeed publicacao) {
        var jpa = repositorio.findById(publicacao.getId().valor()).orElse(new PublicacaoFeedJpa());
        jpa.id = publicacao.getId().valor();
        jpa.torneioId = publicacao.getTorneioIdOptional().map(TorneioId::valor).orElse(null);
        jpa.tipo = publicacao.getTipo().name();
        jpa.autorId = publicacao.getAutorId().map(UsuarioId::valor).orElse(null);
        jpa.partidaId = publicacao.getPartidaId().map(PartidaId::valor).orElse(null);
        jpa.tipoIdentidade = publicacao.getTipoIdentidade().name();
        jpa.identidadeId = publicacao.getIdentidadeId();
        jpa.publicacaoPaiId = publicacao.getPublicacaoPaiId().map(PublicacaoFeedId::valor).orElse(null);
        jpa.criadaEm = publicacao.getCriadaEm();
        jpa.conteudo = publicacao.getConteudo();
        jpa.hashtagsData = PersistenciaTextoUtil.serializarLista(new ArrayList<>(publicacao.getHashtags()));
        jpa.midiasData = PersistenciaTextoUtil.serializarLista(publicacao.getMidias());
        jpa.curtidasData = PersistenciaTextoUtil.serializarLista(
                ReflexaoDominioJpa.conjuntoCampo(publicacao, "curtidas").stream()
                        .map(usuarioId -> ((UsuarioId) usuarioId).valor())
                        .map(String::valueOf)
                        .toList());
        jpa.reacoesData = PersistenciaTextoUtil.serializarLinhas(
                publicacao.getReacoes().entrySet().stream()
                        .map(this::serializarReacao)
                        .toList());
        jpa.removida = publicacao.estaRemovida();
        repositorio.save(jpa);
    }

    @Override
    public Optional<PublicacaoFeed> buscarPorId(PublicacaoFeedId publicacaoId) {
        return repositorio.findById(publicacaoId.valor()).map(this::paraDominio);
    }

    @Override
    public List<PublicacaoFeed> listarPorTorneio(TorneioId torneioId) {
        return repositorio.findByTorneioId(torneioId.valor()).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<PublicacaoFeed> listarTodos() {
        return repositorio.findAll().stream().map(this::paraDominio).toList();
    }

    @Override
    public List<PublicacaoFeed> listarPorHashtag(String hashtag) {
        String hashtagNormalizada = hashtag == null ? "" : hashtag.replace("#", "").trim().toLowerCase();
        return listarTodos().stream()
                .filter(publicacao -> publicacao.getHashtags().contains(hashtagNormalizada))
                .toList();
    }

    @Override
    public List<PublicacaoFeed> listarPorAutor(UsuarioId usuarioId) {
        return repositorio.findByAutorIdOrderByCriadaEmDesc(usuarioId.valor()).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<PublicacaoFeed> listarComentarios(PublicacaoFeedId publicacaoPaiId) {
        return repositorio.findByPublicacaoPaiIdOrderByCriadaEmAsc(publicacaoPaiId.valor()).stream()
                .map(this::paraDominio)
                .toList();
    }

    private PublicacaoFeed paraDominio(PublicacaoFeedJpa jpa) {
        TipoPublicacaoFeed tipo = TipoPublicacaoFeed.valueOf(jpa.tipo);
        PublicacaoFeed publicacao = switch (tipo) {
            case POSTAGEM_SOCIAL -> PublicacaoFeed.postagemSocial(
                    new PublicacaoFeedId(jpa.id),
                    new UsuarioId(jpa.autorId),
                    jpa.conteudo,
                    PersistenciaTextoUtil.desserializarLista(jpa.hashtagsData),
                    PersistenciaTextoUtil.desserializarLista(jpa.midiasData));
            case COMUNICADO_OFICIAL -> PublicacaoFeed.comunicadoOficial(
                    new PublicacaoFeedId(jpa.id),
                    new TorneioId(jpa.torneioId),
                    new UsuarioId(jpa.autorId),
                    jpa.conteudo);
            case COMENTARIO -> jpa.publicacaoPaiId != null
                    ? PublicacaoFeed.comentarioPublicacao(
                            new PublicacaoFeedId(jpa.id),
                            new PublicacaoFeedId(jpa.publicacaoPaiId),
                            new UsuarioId(jpa.autorId),
                            jpa.conteudo,
                            PersistenciaTextoUtil.desserializarLista(jpa.midiasData))
                    : PublicacaoFeed.comentario(
                            new PublicacaoFeedId(jpa.id),
                            new TorneioId(jpa.torneioId),
                            new PartidaId(jpa.partidaId),
                            new UsuarioId(jpa.autorId),
                            jpa.conteudo);
            case ATUALIZACAO_AUTOMATICA -> PublicacaoFeed.atualizacaoAutomatica(
                    new PublicacaoFeedId(jpa.id),
                    new TorneioId(jpa.torneioId),
                    new PartidaId(jpa.partidaId),
                    jpa.conteudo);
        };

        for (String curtida : PersistenciaTextoUtil.desserializarLista(jpa.curtidasData)) {
            publicacao.curtir(new UsuarioId(Long.parseLong(curtida)));
        }
        for (List<String> reacao : PersistenciaTextoUtil.desserializarLinhas(jpa.reacoesData)) {
            publicacao.reagir(new UsuarioId(Long.parseLong(reacao.get(0))), TipoReacaoFeed.valueOf(reacao.get(1)));
        }
        ReflexaoDominioJpa.definirCampo(publicacao, "tipoIdentidade",
                jpa.tipoIdentidade == null ? identidadeLegada(jpa) : TipoIdentidadeFeed.valueOf(jpa.tipoIdentidade));
        ReflexaoDominioJpa.definirCampo(publicacao, "identidadeId",
                jpa.identidadeId == null ? identidadeIdLegada(jpa) : jpa.identidadeId);
        ReflexaoDominioJpa.definirCampo(publicacao, "publicacaoPaiId",
                jpa.publicacaoPaiId == null ? null : new PublicacaoFeedId(jpa.publicacaoPaiId));
        ReflexaoDominioJpa.definirCampo(publicacao, "criadaEm",
                jpa.criadaEm == null ? LocalDateTime.now() : jpa.criadaEm);
        if (jpa.removida) {
            ReflexaoDominioJpa.definirCampo(publicacao, "removida", true);
        }
        return publicacao;
    }

    private TipoIdentidadeFeed identidadeLegada(PublicacaoFeedJpa jpa) {
        if (TipoPublicacaoFeed.ATUALIZACAO_AUTOMATICA.name().equals(jpa.tipo)) return TipoIdentidadeFeed.SISTEMA;
        if (jpa.torneioId != null && TipoPublicacaoFeed.COMUNICADO_OFICIAL.name().equals(jpa.tipo)) {
            return TipoIdentidadeFeed.TORNEIO;
        }
        return TipoIdentidadeFeed.USUARIO;
    }

    private Long identidadeIdLegada(PublicacaoFeedJpa jpa) {
        return switch (identidadeLegada(jpa)) {
            case TORNEIO -> jpa.torneioId;
            case SISTEMA -> jpa.partidaId;
            case USUARIO -> jpa.autorId;
            case TIME -> null;
        };
    }

    private List<String> serializarReacao(Map.Entry<UsuarioId, TipoReacaoFeed> entrada) {
        List<String> linha = new ArrayList<>();
        linha.add(String.valueOf(entrada.getKey().valor()));
        linha.add(entrada.getValue().name());
        return linha;
    }
}
