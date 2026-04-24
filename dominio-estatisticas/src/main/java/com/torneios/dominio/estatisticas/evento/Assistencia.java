package com.torneios.dominio.estatisticas.evento;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public final class Assistencia extends EventoEstatistico {

    public Assistencia(long id, TorneioId torneioId, PartidaId partidaId, JogadorId jogadorId) {
        super(id, torneioId, partidaId, jogadorId, TipoEventoEstatistico.ASSISTENCIA);
    }
}
