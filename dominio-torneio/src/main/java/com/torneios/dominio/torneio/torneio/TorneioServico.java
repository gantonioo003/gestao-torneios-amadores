package com.torneios.dominio.torneio.torneio;

import java.util.Collection;
import java.util.Objects;

public class TorneioServico {

    private final TorneioRepositorio torneioRepositorio;

    public TorneioServico(TorneioRepositorio torneioRepositorio) {
        this.torneioRepositorio = Objects.requireNonNull(torneioRepositorio,
                "O repositorio de torneios e obrigatorio.");
    }

    public Torneio criarTorneio(TorneioId id,
                                String nome,
                                FormatoTorneio formato,
                                FormatoEquipe formatoEquipe,
                                long organizadorId,
                                boolean aceitaSolicitacoes) {
        Torneio torneio = new Torneio(id, nome, formato, formatoEquipe, organizadorId, aceitaSolicitacoes);
        torneioRepositorio.salvar(torneio);
        return torneio;
    }

    public void definirParticipantesIniciais(TorneioId torneioId, Collection<Long> timesIds) {
        Torneio torneio = obterTorneio(torneioId);
        torneio.adicionarParticipantes(timesIds);
        torneio.fecharSolicitacoes();
        torneioRepositorio.salvar(torneio);
    }

    public void aprovarParticipante(TorneioId torneioId, long timeId) {
        Torneio torneio = obterTorneio(torneioId);
        torneio.adicionarParticipante(timeId);
        torneioRepositorio.salvar(torneio);
    }

    public void removerParticipante(TorneioId torneioId, long timeId) {
        Torneio torneio = obterTorneio(torneioId);
        torneio.removerParticipante(timeId);
        torneioRepositorio.salvar(torneio);
    }

    public void abrirSolicitacoes(TorneioId torneioId) {
        Torneio torneio = obterTorneio(torneioId);
        torneio.abrirParaSolicitacoes();
        torneioRepositorio.salvar(torneio);
    }

    public void iniciarTorneio(TorneioId torneioId) {
        Torneio torneio = obterTorneio(torneioId);
        torneio.iniciar();
        torneioRepositorio.salvar(torneio);
    }

    public Torneio obterTorneio(TorneioId torneioId) {
        return torneioRepositorio.buscarPorId(torneioId)
                .orElseThrow(() -> new IllegalArgumentException("Torneio nao encontrado."));
    }
}
