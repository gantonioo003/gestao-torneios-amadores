package com.torneios.dominio.competicao.escalacao;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;

public interface EscalacaoRepositorio {

    void salvar(Escalacao escalacao);

    Optional<Escalacao> buscarPorPartidaETime(PartidaId partidaId, TimeId timeId);

    List<Escalacao> listarPorPartida(PartidaId partidaId);
}
