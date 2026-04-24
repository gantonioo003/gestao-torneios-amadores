package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;

public class PartidaRepositorioMemoria implements PartidaRepositorio {

    private final Map<PartidaId, Partida> dados = new HashMap<>();

    @Override
    public void salvar(Partida partida) {
        dados.put(partida.getId(), partida);
    }

    @Override
    public Optional<Partida> buscarPorId(PartidaId partidaId) {
        return Optional.ofNullable(dados.get(partidaId));
    }

    @Override
    public List<Partida> listarPorTorneio(TorneioId torneioId) {
        return dados.values().stream()
                .filter(partida -> partida.getTorneioId().equals(torneioId))
                .toList();
    }

    public void limpar() {
        dados.clear();
    }
}
