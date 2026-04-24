package com.torneios.dominio.torneio.torneio;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public interface ConsultaElegibilidadeParticipanteTorneio {

    boolean timeExiste(TimeId timeId);

    boolean timePossuiTecnico(TimeId timeId);

    int quantidadeJogadores(TimeId timeId);

    void vincularTimeAoTorneio(TimeId timeId, TorneioId torneioId);

    void removerVinculoDoTimeAoTorneio(TimeId timeId, TorneioId torneioId);
}
