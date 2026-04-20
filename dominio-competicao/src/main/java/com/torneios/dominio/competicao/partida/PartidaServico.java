package com.torneios.dominio.competicao.partida;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

public class PartidaServico {

    private final PartidaRepositorio partidaRepositorio;

    public PartidaServico(PartidaRepositorio partidaRepositorio) {
        this.partidaRepositorio = Objects.requireNonNull(partidaRepositorio,
                "O repositorio de partidas e obrigatorio.");
    }

    public void salvar(Partida partida) {
        partidaRepositorio.salvar(partida);
    }

    public Partida obterPartida(PartidaId partidaId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Partida nao encontrada."));
    }

    public void registrarResultado(PartidaId partidaId, ResultadoPartida resultadoPartida) {
        Partida partida = obterPartida(partidaId);
        partida.registrarResultado(resultadoPartida);
        partidaRepositorio.salvar(partida);
    }

    public List<Partida> listarPorTorneio(TorneioId torneioId) {
        return partidaRepositorio.listarPorTorneio(torneioId);
    }
}
