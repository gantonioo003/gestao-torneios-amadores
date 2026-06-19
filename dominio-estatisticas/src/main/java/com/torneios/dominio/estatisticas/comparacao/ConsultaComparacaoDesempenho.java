package com.torneios.dominio.estatisticas.comparacao;

import java.util.Optional;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.time.TimeId;

public interface ConsultaComparacaoDesempenho {

    boolean jogadorExiste(JogadorId jogadorId);

    String nomeJogador(JogadorId jogadorId);

    String nomeTime(TimeId timeId);

    Optional<TimeId> timeDoJogador(JogadorId jogadorId);
}
