package com.torneios.dominio.estatisticas.evento;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public final class CartaoAmarelo extends EventoEstatistico {

    public CartaoAmarelo(long id, TorneioId torneioId, PartidaId partidaId, long jogadorId) {
        super(id, torneioId, partidaId, jogadorId, TipoEventoEstatistico.CARTAO_AMARELO);
    }
}
