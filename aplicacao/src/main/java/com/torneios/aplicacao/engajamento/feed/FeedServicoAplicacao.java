package com.torneios.aplicacao.engajamento.feed;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.feed.FeedTorneioServico;
import com.torneios.dominio.engajamento.feed.PublicacaoFeed;
import com.torneios.dominio.engajamento.feed.PublicacaoFeedId;
import com.torneios.dominio.engajamento.feed.TipoReacaoFeed;

/**
 * Casos de uso do feed social e oficial da plataforma.
 */
public class FeedServicoAplicacao {

    private final FeedTorneioServico feedTorneioServico;

    public FeedServicoAplicacao(FeedTorneioServico feedTorneioServico) {
        notNull(feedTorneioServico, "O servico de feed e obrigatorio.");
        this.feedTorneioServico = feedTorneioServico;
    }

    public PublicacaoResumo publicarComunicado(long publicacaoId, long torneioId, long organizadorId, String conteudo) {
        return converter(feedTorneioServico.publicarComunicado(
                new PublicacaoFeedId(publicacaoId),
                new TorneioId(torneioId),
                new UsuarioId(organizadorId),
                conteudo));
    }

    public PublicacaoResumo publicarPostagemSocial(long publicacaoId,
                                                   long autorId,
                                                   String conteudo,
                                                   List<String> hashtags,
                                                   List<String> midias) {
        return converter(feedTorneioServico.publicarPostagemSocial(
                new PublicacaoFeedId(publicacaoId),
                new UsuarioId(autorId),
                conteudo,
                hashtags,
                midias));
    }

    public PublicacaoResumo comentarPartida(long publicacaoId,
                                            long torneioId,
                                            long partidaId,
                                            long usuarioId,
                                            String conteudo) {
        return converter(feedTorneioServico.comentarPartida(
                new PublicacaoFeedId(publicacaoId),
                new TorneioId(torneioId),
                new PartidaId(partidaId),
                new UsuarioId(usuarioId),
                conteudo));
    }

    public PublicacaoResumo registrarAtualizacaoAutomatica(long publicacaoId,
                                                           long torneioId,
                                                           long partidaId,
                                                           String conteudo) {
        return converter(feedTorneioServico.registrarAtualizacaoAutomatica(
                new PublicacaoFeedId(publicacaoId),
                new TorneioId(torneioId),
                new PartidaId(partidaId),
                conteudo));
    }

    public PublicacaoResumo editarPublicacao(long publicacaoId, long usuarioId, String novoConteudo) {
        return converter(feedTorneioServico.editarPublicacao(
                new PublicacaoFeedId(publicacaoId),
                new UsuarioId(usuarioId),
                novoConteudo));
    }

    public void removerPublicacao(long publicacaoId, long usuarioId) {
        feedTorneioServico.removerPublicacao(new PublicacaoFeedId(publicacaoId), new UsuarioId(usuarioId));
    }

    public PublicacaoResumo curtirPublicacao(long publicacaoId, long usuarioId) {
        return converter(feedTorneioServico.curtirPublicacao(new PublicacaoFeedId(publicacaoId), new UsuarioId(usuarioId)));
    }

    public PublicacaoResumo reagirPublicacao(long publicacaoId, long usuarioId, String tipoReacao) {
        return converter(feedTorneioServico.reagirPublicacao(
                new PublicacaoFeedId(publicacaoId),
                new UsuarioId(usuarioId),
                TipoReacaoFeed.valueOf(tipoReacao)));
    }

    public List<PublicacaoResumo> listarFeed(long torneioId) {
        return feedTorneioServico.listarFeed(new TorneioId(torneioId)).stream()
                .map(this::converter)
                .toList();
    }

    public List<PublicacaoResumo> listarFeedGeral() {
        return feedTorneioServico.listarFeedGeral().stream()
                .map(this::converter)
                .toList();
    }

    public List<PublicacaoResumo> buscarPorHashtag(String hashtag) {
        return feedTorneioServico.buscarPorHashtag(hashtag).stream()
                .map(this::converter)
                .toList();
    }

    private PublicacaoResumo converter(PublicacaoFeed publicacaoFeed) {
        Map<Long, String> reacoes = new LinkedHashMap<>();
        publicacaoFeed.getReacoes().forEach((usuarioId, tipoReacaoFeed) -> reacoes.put(usuarioId.valor(), tipoReacaoFeed.name()));
        return new PublicacaoResumo(
                publicacaoFeed.getId().valor(),
                publicacaoFeed.getTorneioIdOptional().map(TorneioId::valor).orElse(null),
                publicacaoFeed.getTipo().name(),
                publicacaoFeed.getAutorId().map(UsuarioId::valor).orElse(null),
                publicacaoFeed.getPartidaId().map(PartidaId::valor).orElse(null),
                publicacaoFeed.getConteudo(),
                publicacaoFeed.getHashtags().stream().toList(),
                publicacaoFeed.getMidias(),
                publicacaoFeed.getQuantidadeCurtidas(),
                reacoes,
                publicacaoFeed.estaRemovida());
    }

    public record PublicacaoResumo(long id,
                                   Long torneioId,
                                   String tipo,
                                   Long autorId,
                                   Long partidaId,
                                   String conteudo,
                                   List<String> hashtags,
                                   List<String> midias,
                                   int quantidadeCurtidas,
                                   Map<Long, String> reacoes,
                                   boolean removida) {
    }
}
