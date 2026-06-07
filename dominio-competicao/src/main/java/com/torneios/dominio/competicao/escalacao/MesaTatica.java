package com.torneios.dominio.competicao.escalacao;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.EsquemaTatico;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;

public class MesaTatica {

    private final PartidaId partidaId;
    private final TimeId timeId;
    private final EsquemaTatico esquemaTatico;
    private final List<JogadorPosicionadoMesaTatica> titularesPosicionados;
    private final List<JogadorId> reservas;

    public MesaTatica(PartidaId partidaId,
                      TimeId timeId,
                      EsquemaTatico esquemaTatico,
                      List<JogadorPosicionadoMesaTatica> titularesPosicionados,
                      List<JogadorId> reservas) {
        this.partidaId = Objects.requireNonNull(partidaId, "A partida da mesa tatica e obrigatoria.");
        this.timeId = Objects.requireNonNull(timeId, "O time da mesa tatica e obrigatorio.");
        this.esquemaTatico = Objects.requireNonNull(esquemaTatico, "O esquema tatico da mesa tatica e obrigatorio.");
        this.titularesPosicionados = List.copyOf(Objects.requireNonNull(
                titularesPosicionados, "Os titulares posicionados da mesa tatica sao obrigatorios."));
        this.reservas = List.copyOf(Objects.requireNonNull(
                reservas, "A lista de reservas da mesa tatica e obrigatoria."));
    }

    public PartidaId getPartidaId() {
        return partidaId;
    }

    public TimeId getTimeId() {
        return timeId;
    }

    public EsquemaTatico getEsquemaTatico() {
        return esquemaTatico;
    }

    public List<JogadorPosicionadoMesaTatica> getTitularesPosicionados() {
        return Collections.unmodifiableList(titularesPosicionados);
    }

    public List<JogadorId> getReservas() {
        return Collections.unmodifiableList(reservas);
    }
}
