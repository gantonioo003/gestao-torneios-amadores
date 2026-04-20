package com.torneios.dominio.competicao.partida;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public interface PartidaRepositorio {

    void salvar(Partida partida);

    Optional<Partida> buscarPorId(PartidaId partidaId);

    List<Partida> listarPorTorneio(TorneioId torneioId);
}
