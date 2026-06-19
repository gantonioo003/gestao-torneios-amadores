package com.torneios.dominio.torneio.torneio;

import com.torneios.dominio.compartilhado.torneio.TorneioId;

public interface PreparacaoCompeticaoInvalidador {

    void invalidar(TorneioId torneioId);
}
