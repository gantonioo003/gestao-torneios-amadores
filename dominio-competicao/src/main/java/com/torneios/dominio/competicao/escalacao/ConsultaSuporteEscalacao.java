package com.torneios.dominio.competicao.escalacao;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConsultaSuporteEscalacao {

    boolean partidaIniciada(PartidaId partidaId);

    boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId);

    boolean tecnicoEstaAssociadoAoTime(TimeId timeId, TecnicoId tecnicoId);

    List<JogadorId> listarElencoDoTime(TimeId timeId);

    FormatoEquipe obterFormatoEquipeDaPartida(PartidaId partidaId);
}
