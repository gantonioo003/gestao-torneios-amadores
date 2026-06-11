package com.torneios.dominio.competicao.contestacao;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public interface ContestacaoResultadoRepositorio {

    void salvar(ContestacaoResultado contestacao);

    Optional<ContestacaoResultado> buscarPorId(ContestacaoResultadoId contestacaoId);

    List<ContestacaoResultado> listarContestacoesPorTorneio(TorneioId torneioId);

    List<ContestacaoResultado> listarContestacoesPorPartida(PartidaId partidaId);

    boolean existePendentePorPartidaETime(PartidaId partidaId, TimeId timeId);
}