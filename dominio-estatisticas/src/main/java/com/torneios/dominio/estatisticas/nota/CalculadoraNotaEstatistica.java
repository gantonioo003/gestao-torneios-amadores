package com.torneios.dominio.estatisticas.nota;

import java.util.Collection;
import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;

public class CalculadoraNotaEstatistica {

    private static final double NOTA_BASE = 6.0;
    private static final double PESO_GOL = 2.0;
    private static final double PESO_ASSISTENCIA = 1.5;
    private static final double PESO_CARTAO_AMARELO = -1.0;
    private static final double PESO_CARTAO_VERMELHO = -2.0;

    public NotaEstatistica calcular(TorneioId torneioId,
                                    PartidaId partidaId,
                                    long jogadorId,
                                    Collection<EventoEstatistico> eventos) {
        Objects.requireNonNull(torneioId, "O torneio e obrigatorio para calcular a nota.");
        Objects.requireNonNull(partidaId, "A partida e obrigatoria para calcular a nota.");
        Objects.requireNonNull(eventos, "Os eventos sao obrigatorios para calcular a nota.");
        if (jogadorId <= 0) {
            throw new IllegalArgumentException("O jogador deve ser informado para calcular a nota.");
        }

        double valor = NOTA_BASE;

        for (EventoEstatistico evento : eventos) {
            if (evento.getJogadorId() != jogadorId) {
                continue;
            }
            if (!evento.getPartidaId().equals(partidaId)) {
                continue;
            }
            valor += peso(evento.getTipo());
        }

        return new NotaEstatistica(torneioId, partidaId, jogadorId, limitar(valor));
    }

    private double peso(TipoEventoEstatistico tipo) {
        return switch (tipo) {
            case GOL -> PESO_GOL;
            case ASSISTENCIA -> PESO_ASSISTENCIA;
            case CARTAO_AMARELO -> PESO_CARTAO_AMARELO;
            case CARTAO_VERMELHO -> PESO_CARTAO_VERMELHO;
        };
    }

    private double limitar(double valor) {
        if (valor < 0.0) {
            return 0.0;
        }
        if (valor > 10.0) {
            return 10.0;
        }
        return valor;
    }
}
