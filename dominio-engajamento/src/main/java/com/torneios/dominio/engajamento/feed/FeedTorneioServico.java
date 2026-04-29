package com.torneios.dominio.engajamento.feed;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

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

    public List<PublicacaoFeed> listarFeed(TorneioId torneioId) {
        Objects.requireNonNull(torneioId, "O torneio do feed e obrigatorio.");
        return feedTorneioRepositorio.listarPorTorneio(torneioId).stream()
                .filter(publicacao -> !publicacao.estaRemovida())
                .toList();
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
