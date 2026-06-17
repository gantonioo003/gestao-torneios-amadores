package com.torneios.dominio.estatisticas.evento;

import java.util.List;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public record ControleDisciplinarJogador(TorneioId torneioId,
                                         PartidaId partidaId,
                                         JogadorId jogadorId,
                                         int cartoesAmarelosNoTorneio,
                                         int cartoesAmarelosNaPartida,
                                         int cartoesVermelhosNoTorneio,
                                         boolean expulsoAutomaticamenteNaPartida,
                                         boolean suspensaoAutomaticaPendente,
                                         List<Long> eventosAutomaticosGerados) {

    public ControleDisciplinarJogador {
        if (torneioId == null) {
            throw new IllegalArgumentException("O torneio do controle disciplinar e obrigatorio.");
        }
        if (partidaId == null) {
            throw new IllegalArgumentException("A partida do controle disciplinar e obrigatoria.");
        }
        if (jogadorId == null) {
            throw new IllegalArgumentException("O jogador do controle disciplinar e obrigatorio.");
        }
        if (cartoesAmarelosNoTorneio < 0 || cartoesAmarelosNaPartida < 0 || cartoesVermelhosNoTorneio < 0) {
            throw new IllegalArgumentException("As contagens disciplinares nao podem ser negativas.");
        }
        eventosAutomaticosGerados = List.copyOf(eventosAutomaticosGerados == null ? List.of() : eventosAutomaticosGerados);
    }
}
