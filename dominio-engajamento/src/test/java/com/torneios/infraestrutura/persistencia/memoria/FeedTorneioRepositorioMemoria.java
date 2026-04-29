package com.torneios.infraestrutura.persistencia.memoria;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.engajamento.feed.FeedTorneioRepositorio;
import com.torneios.dominio.engajamento.feed.PublicacaoFeed;
import com.torneios.dominio.engajamento.feed.PublicacaoFeedId;

public class FeedTorneioRepositorioMemoria implements FeedTorneioRepositorio {

    private final Map<PublicacaoFeedId, PublicacaoFeed> publicacoes = new LinkedHashMap<>();

    @Override
    public void salvar(PublicacaoFeed publicacao) {
        publicacoes.put(publicacao.getId(), publicacao);
    }

    @Override
    public Optional<PublicacaoFeed> buscarPorId(PublicacaoFeedId publicacaoId) {
        return Optional.ofNullable(publicacoes.get(publicacaoId));
    }

    @Override
    public List<PublicacaoFeed> listarPorTorneio(TorneioId torneioId) {
        return publicacoes.values().stream()
                .filter(publicacao -> publicacao.getTorneioId().equals(torneioId))
                .toList();
    }

    public List<PublicacaoFeed> listarTodos() {
        return new ArrayList<>(publicacoes.values());
    }
}
