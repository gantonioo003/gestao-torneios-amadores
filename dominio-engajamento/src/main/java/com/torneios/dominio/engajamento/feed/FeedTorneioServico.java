package com.torneios.dominio.engajamento.feed;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.compartilhado.time.TimeId;

public class FeedTorneioServico {

    private final FeedTorneioRepositorio feedTorneioRepositorio;
    private final ConsultaSuporteFeedTorneio consultaSuporte;

    public FeedTorneioServico(FeedTorneioRepositorio feedTorneioRepositorio,
                              ConsultaSuporteFeedTorneio consultaSuporte) {
        this.feedTorneioRepositorio = Objects.requireNonNull(feedTorneioRepositorio,
                "O repositorio do feed e obrigatorio.");
        this.consultaSuporte = Objects.requireNonNull(consultaSuporte,
                "A consulta de suporte do feed e obrigatoria.");
    }

    public PublicacaoFeed publicarComunicado(PublicacaoFeedId publicacaoId,
                                             TorneioId torneioId,
                                             UsuarioId organizadorId,
                                             String conteudo) {
        validarOrganizador(torneioId, organizadorId);
        PublicacaoFeed publicacao = PublicacaoFeed.comunicadoOficial(
                publicacaoId, torneioId, organizadorId, conteudo);
        feedTorneioRepositorio.salvar(publicacao);
        return publicacao;
    }

    public PublicacaoFeed publicarPostagemSocial(PublicacaoFeedId publicacaoId,
                                                 UsuarioId autorId,
                                                 String conteudo,
                                                 List<String> hashtags,
                                                 List<String> midias) {
        validarUsuarioAutenticado(autorId);
        PublicacaoFeed publicacao = PublicacaoFeed.postagemSocial(
                publicacaoId, autorId, conteudo, hashtags, midias);
        feedTorneioRepositorio.salvar(publicacao);
        return publicacao;
    }

    public PublicacaoFeed publicarPostagem(PublicacaoFeedId publicacaoId,
                                           UsuarioId autorId,
                                           TipoIdentidadeFeed tipoIdentidade,
                                           long identidadeId,
                                           String conteudo,
                                           List<String> hashtags,
                                           List<String> midias) {
        validarUsuarioAutenticado(autorId);
        TorneioId torneioId = null;
        switch (tipoIdentidade) {
            case USUARIO -> {
                if (identidadeId != autorId.valor()) {
                    throw new OperacaoNaoPermitidaException("O usuario so pode publicar com a propria identidade.");
                }
            }
            case TIME -> {
                if (!consultaSuporte.usuarioEhResponsavelTime(new TimeId(identidadeId), autorId)) {
                    throw new OperacaoNaoPermitidaException(
                            "Apenas o responsavel do time pode publicar usando esta identidade.");
                }
            }
            case TORNEIO -> {
                torneioId = new TorneioId(identidadeId);
                validarOrganizador(torneioId, autorId);
            }
            case SISTEMA -> throw new OperacaoNaoPermitidaException(
                    "A identidade de sistema e reservada para atualizacoes automaticas.");
        }
        PublicacaoFeed publicacao = PublicacaoFeed.postagem(
                publicacaoId, autorId, tipoIdentidade, identidadeId, torneioId, conteudo, hashtags, midias);
        feedTorneioRepositorio.salvar(publicacao);
        return publicacao;
    }

    public PublicacaoFeed comentarPublicacao(PublicacaoFeedId comentarioId,
                                             PublicacaoFeedId publicacaoId,
                                             UsuarioId autorId,
                                             String conteudo,
                                             List<String> midias) {
        validarUsuarioAutenticado(autorId);
        PublicacaoFeed publicacaoPai = obterPublicacao(publicacaoId);
        if (publicacaoPai.getTipo() == TipoPublicacaoFeed.COMENTARIO || publicacaoPai.estaRemovida()) {
            throw new OperacaoNaoPermitidaException(
                    "Comentarios so podem ser adicionados a uma publicacao ativa do feed.");
        }
        PublicacaoFeed comentario = PublicacaoFeed.comentarioPublicacao(
                comentarioId, publicacaoId, autorId, conteudo,
                midias == null ? List.of() : midias);
        feedTorneioRepositorio.salvar(comentario);
        return comentario;
    }

    public PublicacaoFeed comentarPartida(PublicacaoFeedId publicacaoId,
                                          TorneioId torneioId,
                                          PartidaId partidaId,
                                          UsuarioId usuarioId,
                                          String conteudo) {
        validarUsuarioAutenticado(usuarioId);
        validarPartidaDoTorneio(torneioId, partidaId);
        PublicacaoFeed publicacao = PublicacaoFeed.comentario(
                publicacaoId, torneioId, partidaId, usuarioId, conteudo);
        feedTorneioRepositorio.salvar(publicacao);
        return publicacao;
    }

    public PublicacaoFeed registrarAtualizacaoAutomatica(PublicacaoFeedId publicacaoId,
                                                         TorneioId torneioId,
                                                         PartidaId partidaId,
                                                         String conteudo) {
        validarPartidaDoTorneio(torneioId, partidaId);
        PublicacaoFeed publicacao = PublicacaoFeed.atualizacaoAutomatica(
                publicacaoId, torneioId, partidaId, conteudo);
        feedTorneioRepositorio.salvar(publicacao);
        return publicacao;
    }

    public PublicacaoFeed editarPublicacao(PublicacaoFeedId publicacaoId,
                                           UsuarioId usuarioId,
                                           String novoConteudo) {
        validarUsuarioAutenticado(usuarioId);
        PublicacaoFeed publicacao = obterPublicacao(publicacaoId);
        publicacao.editarConteudo(usuarioId, novoConteudo);
        feedTorneioRepositorio.salvar(publicacao);
        return publicacao;
    }

    public void removerPublicacao(PublicacaoFeedId publicacaoId, UsuarioId usuarioId) {
        validarUsuarioAutenticado(usuarioId);
        PublicacaoFeed publicacao = obterPublicacao(publicacaoId);
        publicacao.remover(usuarioId);
        feedTorneioRepositorio.salvar(publicacao);
    }

    public PublicacaoFeed curtirPublicacao(PublicacaoFeedId publicacaoId, UsuarioId usuarioId) {
        validarUsuarioAutenticado(usuarioId);
        PublicacaoFeed publicacao = obterPublicacao(publicacaoId);
        publicacao.curtir(usuarioId);
        feedTorneioRepositorio.salvar(publicacao);
        return publicacao;
    }

    public PublicacaoFeed reagirPublicacao(PublicacaoFeedId publicacaoId,
                                           UsuarioId usuarioId,
                                           TipoReacaoFeed tipoReacaoFeed) {
        validarUsuarioAutenticado(usuarioId);
        PublicacaoFeed publicacao = obterPublicacao(publicacaoId);
        publicacao.reagir(usuarioId, tipoReacaoFeed);
        feedTorneioRepositorio.salvar(publicacao);
        return publicacao;
    }

    public List<PublicacaoFeed> listarFeed(TorneioId torneioId) {
        Objects.requireNonNull(torneioId, "O torneio do feed e obrigatorio.");
        return feedTorneioRepositorio.listarPorTorneio(torneioId).stream()
                .filter(publicacao -> !publicacao.estaRemovida())
                .toList();
    }

    public List<PublicacaoFeed> listarFeedGeral() {
        return feedTorneioRepositorio.listarTodos().stream()
                .filter(publicacao -> !publicacao.estaRemovida())
                .filter(publicacao -> publicacao.getPublicacaoPaiId().isEmpty())
                .toList();
    }

    public List<PublicacaoFeed> buscarPorHashtag(String hashtag) {
        return feedTorneioRepositorio.listarPorHashtag(hashtag).stream()
                .filter(publicacao -> !publicacao.estaRemovida())
                .toList();
    }

    public List<PublicacaoFeed> listarPorAutor(UsuarioId usuarioId) {
        Objects.requireNonNull(usuarioId, "O autor do perfil e obrigatorio.");
        return feedTorneioRepositorio.listarPorAutor(usuarioId).stream()
                .filter(publicacao -> !publicacao.estaRemovida())
                .filter(publicacao -> publicacao.getPublicacaoPaiId().isEmpty())
                .filter(publicacao -> publicacao.getTipoIdentidade() == TipoIdentidadeFeed.USUARIO)
                .toList();
    }

    public List<PublicacaoFeed> listarComentarios(PublicacaoFeedId publicacaoId) {
        obterPublicacao(publicacaoId);
        return feedTorneioRepositorio.listarComentarios(publicacaoId).stream()
                .filter(publicacao -> !publicacao.estaRemovida())
                .toList();
    }

    public PublicacaoFeed consultarPublicacao(PublicacaoFeedId publicacaoId) {
        PublicacaoFeed publicacao = obterPublicacao(publicacaoId);
        if (publicacao.estaRemovida()) {
            throw new EntidadeNaoEncontradaException("Publicacao do feed nao encontrada.");
        }
        return publicacao;
    }

    private PublicacaoFeed obterPublicacao(PublicacaoFeedId publicacaoId) {
        return feedTorneioRepositorio.buscarPorId(publicacaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Publicacao do feed nao encontrada."));
    }

    private void validarOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        validarUsuarioAutenticado(usuarioId);
        if (!consultaSuporte.usuarioEhOrganizador(torneioId, usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o organizador do torneio pode publicar comunicados oficiais.");
        }
    }

    private void validarUsuarioAutenticado(UsuarioId usuarioId) {
        Objects.requireNonNull(usuarioId, "O usuario da publicacao e obrigatorio.");
        if (!consultaSuporte.usuarioEstaAutenticado(usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas usuarios autenticados podem interagir no feed do torneio.");
        }
    }

    private void validarPartidaDoTorneio(TorneioId torneioId, PartidaId partidaId) {
        Objects.requireNonNull(torneioId, "O torneio da publicacao e obrigatorio.");
        Objects.requireNonNull(partidaId, "A partida da publicacao e obrigatoria.");
        if (!consultaSuporte.partidaPertenceAoTorneio(torneioId, partidaId)) {
            throw new RegraDeNegocioException(
                    "A partida informada nao pertence ao torneio do feed.");
        }
    }
}
