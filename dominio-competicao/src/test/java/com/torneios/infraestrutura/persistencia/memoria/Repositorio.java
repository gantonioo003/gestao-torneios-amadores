package com.torneios.infraestrutura.persistencia.memoria;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.escalacao.Escalacao;
import com.torneios.dominio.competicao.escalacao.EscalacaoRepositorio;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;

public class Repositorio implements PartidaRepositorio, EscalacaoRepositorio {

    /*-----------------------------------------------------------------------*/
    private final Map<PartidaId, Partida> partidas = new HashMap<>();

    @Override
    public void salvar(Partida partida) {
        notNull(partida, "A partida nao pode ser nula.");
        partidas.put(partida.getId(), partida);
    }

    @Override
    public Optional<Partida> buscarPorId(PartidaId partidaId) {
        notNull(partidaId, "O id da partida nao pode ser nulo.");
        return Optional.ofNullable(partidas.get(partidaId));
    }

    @Override
    public List<Partida> listarPorTorneio(TorneioId torneioId) {
        notNull(torneioId, "O id do torneio nao pode ser nulo.");
        return partidas.values().stream()
                .filter(p -> p.getTorneioId().equals(torneioId))
                .toList();
    }
    /*-----------------------------------------------------------------------*/

    /*-----------------------------------------------------------------------*/
    private final Map<String, Escalacao> escalacoes = new LinkedHashMap<>();

    @Override
    public void salvar(Escalacao escalacao) {
        notNull(escalacao, "A escalacao nao pode ser nula.");
        escalacoes.put(chave(escalacao.getPartidaId(), escalacao.getTimeId()), escalacao);
    }

    @Override
    public Optional<Escalacao> buscarPorPartidaETime(PartidaId partidaId, TimeId timeId) {
        return Optional.ofNullable(escalacoes.get(chave(partidaId, timeId)));
    }

    @Override
    public List<Escalacao> listarPorPartida(PartidaId partidaId) {
        notNull(partidaId, "O id da partida nao pode ser nulo.");
        return escalacoes.values().stream()
                .filter(e -> e.getPartidaId().equals(partidaId))
                .toList();
    }

    private String chave(PartidaId partidaId, TimeId timeId) {
        return partidaId.valor() + ":" + timeId.valor();
    }
    /*-----------------------------------------------------------------------*/

    public void limpar() {
        partidas.clear();
        escalacoes.clear();
    }
}
