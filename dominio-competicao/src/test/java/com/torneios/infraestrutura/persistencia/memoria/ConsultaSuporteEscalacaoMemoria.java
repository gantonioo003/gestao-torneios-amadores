package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.escalacao.ConsultaSuporteEscalacao;

public class ConsultaSuporteEscalacaoMemoria implements ConsultaSuporteEscalacao {

    private final Set<PartidaId> partidasIniciadas = new HashSet<>();
    private final Map<TimeId, UsuarioId> responsaveisPorTime = new HashMap<>();
    private final Map<TimeId, TecnicoId> tecnicosPorTime = new HashMap<>();
    private final Map<TimeId, List<JogadorId>> elencoPorTime = new HashMap<>();
    private final Map<PartidaId, FormatoEquipe> formatoPorPartida = new HashMap<>();
    private final Map<PartidaId, List<TimeId>> timesPorPartida = new HashMap<>();
    private final Set<PartidaId> partidasComEscalacaoObrigatoria = new HashSet<>();

    public void registrarPartida(PartidaId partidaId, FormatoEquipe formatoEquipe, boolean iniciada) {
        registrarPartida(partidaId, formatoEquipe, iniciada, List.of(), false);
    }

    public void registrarPartida(PartidaId partidaId,
                                 FormatoEquipe formatoEquipe,
                                 boolean iniciada,
                                 List<TimeId> times,
                                 boolean escalacaoObrigatoria) {
        formatoPorPartida.put(partidaId, formatoEquipe);
        timesPorPartida.put(partidaId, List.copyOf(times));
        if (iniciada) {
            partidasIniciadas.add(partidaId);
        } else {
            partidasIniciadas.remove(partidaId);
        }
        if (escalacaoObrigatoria) {
            partidasComEscalacaoObrigatoria.add(partidaId);
        } else {
            partidasComEscalacaoObrigatoria.remove(partidaId);
        }
    }

    public void registrarResponsavel(TimeId timeId, UsuarioId usuarioId) {
        responsaveisPorTime.put(timeId, usuarioId);
    }

    public void registrarTecnico(TimeId timeId, TecnicoId tecnicoId) {
        tecnicosPorTime.put(timeId, tecnicoId);
    }

    public void registrarElenco(TimeId timeId, List<JogadorId> jogadores) {
        elencoPorTime.put(timeId, List.copyOf(jogadores));
    }

    public void iniciarPartida(PartidaId partidaId) {
        partidasIniciadas.add(partidaId);
    }

    @Override
    public boolean partidaIniciada(PartidaId partidaId) {
        return partidasIniciadas.contains(partidaId);
    }

    @Override
    public boolean escalacaoObrigatoriaNaPartida(PartidaId partidaId) {
        return partidasComEscalacaoObrigatoria.contains(partidaId);
    }

    @Override
    public boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId) {
        return usuarioId.equals(responsaveisPorTime.get(timeId));
    }

    @Override
    public boolean tecnicoEstaAssociadoAoTime(TimeId timeId, TecnicoId tecnicoId) {
        return tecnicoId.equals(tecnicosPorTime.get(timeId));
    }

    @Override
    public List<JogadorId> listarElencoDoTime(TimeId timeId) {
        return elencoPorTime.getOrDefault(timeId, List.of());
    }

    @Override
    public List<TimeId> listarTimesDaPartida(PartidaId partidaId) {
        return timesPorPartida.getOrDefault(partidaId, List.of());
    }

    @Override
    public FormatoEquipe obterFormatoEquipeDaPartida(PartidaId partidaId) {
        return formatoPorPartida.get(partidaId);
    }
}
