package com.torneios.dominio.engajamento.palpite;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConsultaSuportePalpite {

    boolean usuarioEstaAutenticado(UsuarioId usuarioId);

    boolean partidaIniciada(PartidaId partidaId);

    boolean partidaEncerrada(PartidaId partidaId);

    boolean torneioIniciado(TorneioId torneioId);

    boolean torneioFinalizado(TorneioId torneioId);

    boolean opcaoValidaParaEvento(EventoAlvo eventoAlvo, OpcaoPalpite opcao);
}
