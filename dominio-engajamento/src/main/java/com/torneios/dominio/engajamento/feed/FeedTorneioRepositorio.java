package com.torneios.dominio.engajamento.feed;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface FeedTorneioRepositorio {

    void salvar(PublicacaoFeed publicacao);

    Optional<PublicacaoFeed> buscarPorId(PublicacaoFeedId publicacaoId);

    List<PublicacaoFeed> listarPorTorneio(TorneioId torneioId);

    List<PublicacaoFeed> listarTodos();

    List<PublicacaoFeed> listarPorHashtag(String hashtag);

    List<PublicacaoFeed> listarPorAutor(UsuarioId usuarioId);

    List<PublicacaoFeed> listarComentarios(PublicacaoFeedId publicacaoPaiId);
}
