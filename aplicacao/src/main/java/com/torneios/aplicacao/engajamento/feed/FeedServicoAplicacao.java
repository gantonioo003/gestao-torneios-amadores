package com.torneios.aplicacao.engajamento.feed;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.ArrayList;
import java.time.LocalDateTime;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.feed.FeedTorneioServico;
import com.torneios.dominio.engajamento.feed.PublicacaoFeed;
import com.torneios.dominio.engajamento.feed.PublicacaoFeedId;
import com.torneios.dominio.engajamento.feed.TipoReacaoFeed;
import com.torneios.dominio.engajamento.feed.TipoIdentidadeFeed;
import com.torneios.aplicacao.participacao.conta.ContaRepositorioAplicacao;
import com.torneios.aplicacao.participacao.time.TimeRepositorioAplicacao;
import com.torneios.aplicacao.torneio.criacao.TorneioRepositorioAplicacao;

/**
 * Casos de uso do feed social e oficial da plataforma.
 */
public class FeedServicoAplicacao {

    private final FeedTorneioServico feedTorneioServico;
    private final ContaRepositorioAplicacao contaRepositorio;
    private final TimeRepositorioAplicacao timeRepositorio;
    private final TorneioRepositorioAplicacao torneioRepositorio;

    public FeedServicoAplicacao(FeedTorneioServico feedTorneioServico) {
        this(feedTorneioServico, null, null, null);
    }

    public FeedServicoAplicacao(FeedTorneioServico feedTorneioServico,
                                ContaRepositorioAplicacao contaRepositorio,
                                TimeRepositorioAplicacao timeRepositorio,
                                TorneioRepositorioAplicacao torneioRepositorio) {
        notNull(feedTorneioServico, "O servico de feed e obrigatorio.");
        this.feedTorneioServico = feedTorneioServico;
        this.contaRepositorio = contaRepositorio;
        this.timeRepositorio = timeRepositorio;
        this.torneioRepositorio = torneioRepositorio;
    }

    public PublicacaoResumo publicarComunicado(long publicacaoId, long torneioId, long organizadorId, String conteudo) {
        return converter(feedTorneioServico.publicarComunicado(
                new PublicacaoFeedId(publicacaoId),
                new TorneioId(torneioId),
                new UsuarioId(organizadorId),
                conteudo), organizadorId, true);
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
                midias), autorId, true);
    }

    public PublicacaoResumo publicar(long publicacaoId,
                                     long autorId,
                                     String tipoIdentidade,
                                     long identidadeId,
                                     String conteudo,
                                     List<String> hashtags,
                                     List<String> midias) {
        return converter(feedTorneioServico.publicarPostagem(
                new PublicacaoFeedId(publicacaoId),
                new UsuarioId(autorId),
                TipoIdentidadeFeed.valueOf(tipoIdentidade),
                identidadeId,
                conteudo,
                hashtags == null ? List.of() : hashtags,
                midias == null ? List.of() : midias), autorId, true);
    }

    public PublicacaoResumo comentar(long comentarioId,
                                     long publicacaoId,
                                     long autorId,
                                     String conteudo,
                                     List<String> midias) {
        return converter(feedTorneioServico.comentarPublicacao(
                new PublicacaoFeedId(comentarioId),
                new PublicacaoFeedId(publicacaoId),
                new UsuarioId(autorId),
                conteudo,
                midias), autorId, false);
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
                conteudo), usuarioId, false);
    }

    public PublicacaoResumo registrarAtualizacaoAutomatica(long publicacaoId,
                                                           long torneioId,
                                                           long partidaId,
                                                           String conteudo) {
        return converter(feedTorneioServico.registrarAtualizacaoAutomatica(
                new PublicacaoFeedId(publicacaoId),
                new TorneioId(torneioId),
                new PartidaId(partidaId),
                conteudo), null, true);
    }

    public PublicacaoResumo editarPublicacao(long publicacaoId, long usuarioId, String novoConteudo) {
        return converter(feedTorneioServico.editarPublicacao(
                new PublicacaoFeedId(publicacaoId),
                new UsuarioId(usuarioId),
                novoConteudo), usuarioId, true);
    }

    public void removerPublicacao(long publicacaoId, long usuarioId) {
        feedTorneioServico.removerPublicacao(new PublicacaoFeedId(publicacaoId), new UsuarioId(usuarioId));
    }

    public PublicacaoResumo curtirPublicacao(long publicacaoId, long usuarioId) {
        return converter(feedTorneioServico.curtirPublicacao(
                new PublicacaoFeedId(publicacaoId), new UsuarioId(usuarioId)), usuarioId, true);
    }

    public PublicacaoResumo reagirPublicacao(long publicacaoId, long usuarioId, String tipoReacao) {
        return converter(feedTorneioServico.reagirPublicacao(
                new PublicacaoFeedId(publicacaoId),
                new UsuarioId(usuarioId),
                TipoReacaoFeed.valueOf(tipoReacao)), usuarioId, true);
    }

    public List<PublicacaoResumo> listarFeed(long torneioId) {
        return listarFeed(torneioId, null);
    }

    public List<PublicacaoResumo> listarFeed(long torneioId, Long usuarioAtualId) {
        return feedTorneioServico.listarFeed(new TorneioId(torneioId)).stream()
                .filter(publicacao -> publicacao.getPublicacaoPaiId().isEmpty())
                .sorted(Comparator.comparing(PublicacaoFeed::getCriadaEm).reversed())
                .map(publicacao -> converter(publicacao, usuarioAtualId, true))
                .toList();
    }

    public List<PublicacaoResumo> listarFeedGeral() {
        return listarFeedGeral(null);
    }

    public List<PublicacaoResumo> listarFeedGeral(Long usuarioAtualId) {
        return feedTorneioServico.listarFeedGeral().stream()
                .sorted(Comparator.comparing(PublicacaoFeed::getCriadaEm).reversed())
                .map(publicacao -> converter(publicacao, usuarioAtualId, true))
                .toList();
    }

    public List<PublicacaoResumo> buscarPorHashtag(String hashtag) {
        return feedTorneioServico.buscarPorHashtag(hashtag).stream()
                .map(publicacao -> converter(publicacao, null, true))
                .toList();
    }

    public List<PublicacaoResumo> listarPorAutor(long autorId, Long usuarioAtualId) {
        return feedTorneioServico.listarPorAutor(new UsuarioId(autorId)).stream()
                .sorted(Comparator.comparing(PublicacaoFeed::getCriadaEm).reversed())
                .map(publicacao -> converter(publicacao, usuarioAtualId, true))
                .toList();
    }

    public List<PublicacaoResumo> listarPorIdentidade(String tipoIdentidade,
                                                     long identidadeId,
                                                     Long usuarioAtualId) {
        return feedTorneioServico.listarPorIdentidade(
                        TipoIdentidadeFeed.valueOf(tipoIdentidade), identidadeId).stream()
                .sorted(Comparator.comparing(PublicacaoFeed::getCriadaEm).reversed())
                .map(publicacao -> converter(publicacao, usuarioAtualId, true))
                .toList();
    }

    public PublicacaoResumo consultar(long publicacaoId, Long usuarioAtualId) {
        return converter(
                feedTorneioServico.consultarPublicacao(new PublicacaoFeedId(publicacaoId)),
                usuarioAtualId,
                true);
    }

    public List<IdentidadeResumo> listarIdentidades(long usuarioId) {
        List<IdentidadeResumo> identidades = new ArrayList<>();
        contaRepositorio.pesquisarPorId(usuarioId).ifPresent(conta ->
                identidades.add(new IdentidadeResumo("USUARIO", usuarioId, conta.getNome(), "Perfil pessoal")));
        timeRepositorio.pesquisarResumosGerenciaveis(usuarioId).forEach(time ->
                identidades.add(new IdentidadeResumo("TIME", time.getId(), time.getNome(), "Publicar como time")));
        torneioRepositorio.pesquisarResumosPorOrganizador(usuarioId).forEach(torneio ->
                identidades.add(new IdentidadeResumo(
                        "TORNEIO", torneio.getId(), torneio.getNome(), "Publicar como torneio")));
        return identidades;
    }

    public List<AssuntoResumo> listarAssuntosDoMomento() {
        Map<String, Integer> pontuacoes = new LinkedHashMap<>();
        feedTorneioServico.listarFeedGeral().forEach(publicacao -> {
            int comentarios = feedTorneioServico.listarComentarios(publicacao.getId()).size();
            int pontos = 1 + publicacao.getQuantidadeCurtidas() * 2
                    + publicacao.getReacoes().size() + comentarios * 3;
            publicacao.getHashtags().forEach(hashtag ->
                    pontuacoes.merge(hashtag, pontos, Integer::sum));
        });
        return pontuacoes.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(8)
                .map(item -> new AssuntoResumo(item.getKey(), item.getValue()))
                .toList();
    }

    private PublicacaoResumo converter(PublicacaoFeed publicacaoFeed,
                                       Long usuarioAtualId,
                                       boolean incluirComentarios) {
        Map<Long, String> reacoes = new LinkedHashMap<>();
        publicacaoFeed.getReacoes().forEach((usuarioId, tipoReacaoFeed) -> reacoes.put(usuarioId.valor(), tipoReacaoFeed.name()));
        List<PublicacaoResumo> comentarios = incluirComentarios
                ? feedTorneioServico.listarComentarios(publicacaoFeed.getId()).stream()
                        .map(comentario -> converter(comentario, usuarioAtualId, false))
                        .toList()
                : List.of();
        UsuarioId usuarioAtual = usuarioAtualId == null ? null : new UsuarioId(usuarioAtualId);
        return new PublicacaoResumo(
                publicacaoFeed.getId().valor(),
                publicacaoFeed.getTorneioIdOptional().map(TorneioId::valor).orElse(null),
                publicacaoFeed.getTipo().name(),
                publicacaoFeed.getAutorId().map(UsuarioId::valor).orElse(null),
                nomeAutor(publicacaoFeed),
                nomeUsuarioAutor(publicacaoFeed),
                fotoAutor(publicacaoFeed),
                publicacaoFeed.getTipoIdentidade().name(),
                publicacaoFeed.getIdentidadeId(),
                nomeIdentidade(publicacaoFeed),
                publicacaoFeed.getPartidaId().map(PartidaId::valor).orElse(null),
                publicacaoFeed.getPublicacaoPaiId().map(PublicacaoFeedId::valor).orElse(null),
                publicacaoFeed.getConteudo(),
                publicacaoFeed.getHashtags().stream().toList(),
                publicacaoFeed.getMidias(),
                publicacaoFeed.getQuantidadeCurtidas(),
                publicacaoFeed.foiCurtidaPor(usuarioAtual),
                reacoes,
                publicacaoFeed.getCriadaEm(),
                comentarios,
                publicacaoFeed.estaRemovida());
    }

    private String nomeAutor(PublicacaoFeed publicacao) {
        return publicacao.getAutorId()
                .flatMap(id -> contaRepositorio == null
                        ? java.util.Optional.empty()
                        : contaRepositorio.pesquisarPorId(id.valor()))
                .map(conta -> conta.getNome())
                .orElse("Liga Amadora+");
    }

    private String nomeUsuarioAutor(PublicacaoFeed publicacao) {
        return publicacao.getAutorId()
                .flatMap(id -> contaRepositorio == null
                        ? java.util.Optional.empty()
                        : contaRepositorio.pesquisarPorId(id.valor()))
                .map(conta -> conta.getNomeUsuario())
                .orElse("liga_amadora");
    }

    private String fotoAutor(PublicacaoFeed publicacao) {
        return publicacao.getAutorId()
                .flatMap(id -> contaRepositorio == null
                        ? java.util.Optional.empty()
                        : contaRepositorio.pesquisarPorId(id.valor()))
                .map(conta -> conta.getFotoPerfilUrl())
                .orElse(null);
    }

    private String nomeIdentidade(PublicacaoFeed publicacao) {
        if (publicacao.getIdentidadeId() == null) return "Liga Amadora+";
        return switch (publicacao.getTipoIdentidade()) {
            case USUARIO -> contaRepositorio == null ? nomeAutor(publicacao) : contaRepositorio
                    .pesquisarPorId(publicacao.getIdentidadeId()).map(conta -> conta.getNome())
                    .orElse(nomeAutor(publicacao));
            case TIME -> timeRepositorio == null ? "Time" : timeRepositorio.pesquisarResumos("").stream()
                    .filter(time -> time.getId().equals(publicacao.getIdentidadeId()))
                    .findFirst().map(time -> time.getNome()).orElse("Time");
            case TORNEIO -> torneioRepositorio == null ? "Torneio" : torneioRepositorio.pesquisarResumos().stream()
                    .filter(torneio -> torneio.getId().equals(publicacao.getIdentidadeId()))
                    .findFirst().map(torneio -> torneio.getNome()).orElse("Torneio");
            case SISTEMA -> "Atualizacao da partida";
        };
    }

    public record PublicacaoResumo(long id,
                                   Long torneioId,
                                   String tipo,
                                   Long autorId,
                                   String autorNome,
                                   String autorNomeUsuario,
                                   String autorFotoPerfilUrl,
                                   String tipoIdentidade,
                                   Long identidadeId,
                                   String identidadeNome,
                                   Long partidaId,
                                   Long publicacaoPaiId,
                                   String conteudo,
                                   List<String> hashtags,
                                   List<String> midias,
                                   int quantidadeCurtidas,
                                   boolean curtidaPeloUsuario,
                                   Map<Long, String> reacoes,
                                   LocalDateTime criadaEm,
                                   List<PublicacaoResumo> comentarios,
                                   boolean removida) {
    }

    public record IdentidadeResumo(String tipo, long id, String nome, String descricao) {
    }

    public record AssuntoResumo(String hashtag, int pontuacao) {
    }
}
