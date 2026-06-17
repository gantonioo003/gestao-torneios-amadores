package com.torneios.dominio.estatisticas.evento;

import java.util.List;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConsultaEstatisticaCompeticao {

    boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId);

    boolean partidaPertenceAoTorneio(PartidaId partidaId, TorneioId torneioId);

    boolean jogadorPertenceAosTimesDaPartida(PartidaId partidaId, JogadorId jogadorId);

    default List<PartidaId> listarPartidasDoJogadorNoTorneio(TorneioId torneioId, JogadorId jogadorId) {
        return List.of();
    }
}
