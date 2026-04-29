package com.torneios.dominio.estatisticas.evento;

import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;

public final class Substituicao extends EventoEstatistico {

    private final JogadorId jogadorQueSaiu;

    public Substituicao(long id,
                        TorneioId torneioId,
                        PartidaId partidaId,
                        JogadorId jogadorQueSaiu,
                        JogadorId jogadorQueEntrou) {
        super(id, torneioId, partidaId, jogadorQueEntrou, TipoEventoEstatistico.SUBSTITUICAO);
        this.jogadorQueSaiu = Objects.requireNonNull(jogadorQueSaiu,
                "O jogador que saiu na substituicao e obrigatorio.");
        if (jogadorQueSaiu.equals(jogadorQueEntrou)) {
            throw new IllegalArgumentException(
                    "O jogador que saiu e o jogador que entrou nao podem ser o mesmo.");
        }
    }

    public JogadorId getJogadorQueSaiu() {
        return jogadorQueSaiu;
    }

    public JogadorId getJogadorQueEntrou() {
        return getJogadorId();
    }
}
