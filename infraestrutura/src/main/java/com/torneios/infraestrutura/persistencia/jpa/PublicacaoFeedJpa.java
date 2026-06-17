package com.torneios.infraestrutura.persistencia.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            case COMENTARIO -> PublicacaoFeed.comentario(
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
        if (jpa.removida) {
            ReflexaoDominioJpa.definirCampo(publicacao, "removida", true);
        }
        return publicacao;
    }

    private List<String> serializarReacao(Map.Entry<UsuarioId, TipoReacaoFeed> entrada) {
        List<String> linha = new ArrayList<>();
        linha.add(String.valueOf(entrada.getKey().valor()));
        linha.add(entrada.getValue().name());
        return linha;
    }
}
