package com.torneios.dominio.engajamento.feed;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.torneio.TorneioId;

public interface FeedTorneioRepositorio {

    void salvar(PublicacaoFeed publicacao);

    Optional<PublicacaoFeed> buscarPorId(PublicacaoFeedId publicacaoId);

    List<PublicacaoFeed> listarPorTorneio(TorneioId torneioId);
}
