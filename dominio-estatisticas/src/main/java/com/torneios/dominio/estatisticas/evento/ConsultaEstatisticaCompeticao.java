package com.torneios.dominio.estatisticas.evento;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConsultaEstatisticaCompeticao {

    boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId);

    boolean partidaPertenceAoTorneio(PartidaId partidaId, TorneioId torneioId);

    boolean jogadorPertenceAosTimesDaPartida(PartidaId partidaId, JogadorId jogadorId);

    boolean partidaEncerrada(PartidaId partidaId);

    boolean jogadorEhTitularNaEscalacao(PartidaId partidaId, JogadorId jogadorId);

    boolean jogadorEhReservaNaEscalacao(PartidaId partidaId, JogadorId jogadorId);

    boolean jogadoresPertencemAoMesmoTimeNaPartida(PartidaId partidaId,
                                                   JogadorId primeiroJogadorId,
                                                   JogadorId segundoJogadorId);
}
