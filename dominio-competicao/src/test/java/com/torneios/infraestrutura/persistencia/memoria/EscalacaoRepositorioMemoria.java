package com.torneios.infraestrutura.persistencia.memoria;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.competicao.escalacao.Escalacao;
import com.torneios.dominio.competicao.escalacao.EscalacaoRepositorio;

public class EscalacaoRepositorioMemoria implements EscalacaoRepositorio {

    private final Map<String, Escalacao> escalacoes = new LinkedHashMap<>();

    @Override
    public void salvar(Escalacao escalacao) {
        escalacoes.put(chave(escalacao.getPartidaId(), escalacao.getTimeId()), escalacao);
    }

    @Override
    public Optional<Escalacao> buscarPorPartidaETime(PartidaId partidaId, TimeId timeId) {
        return Optional.ofNullable(escalacoes.get(chave(partidaId, timeId)));
    }

    @Override
    public List<Escalacao> listarPorPartida(PartidaId partidaId) {
        return escalacoes.values().stream()
                .filter(escalacao -> escalacao.getPartidaId().equals(partidaId))
                .toList();
    }

    private String chave(PartidaId partidaId, TimeId timeId) {
        return partidaId.valor() + ":" + timeId.valor();
    }
}
