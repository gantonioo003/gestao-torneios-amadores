package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.estatisticas.evento.ConsultaEstatisticaCompeticao;

public class ConsultaEstatisticaCompeticaoMemoria implements ConsultaEstatisticaCompeticao {

    private final Map<TorneioId, UsuarioId> organizadores = new HashMap<>();
    private final Map<PartidaId, TorneioId> partidaTorneio = new HashMap<>();
    private final Map<PartidaId, Set<JogadorId>> jogadoresPorPartida = new HashMap<>();
    private final Set<PartidaId> partidasEncerradas = new HashSet<>();
    private final Map<PartidaId, Set<JogadorId>> titularesPorPartida = new HashMap<>();
    private final Map<PartidaId, Set<JogadorId>> reservasPorPartida = new HashMap<>();
    private final Map<PartidaId, Map<JogadorId, TimeId>> timeDoJogadorPorPartida = new HashMap<>();

    public void registrarOrganizador(TorneioId torneioId, UsuarioId organizadorId) {
        organizadores.put(torneioId, organizadorId);
    }

    public void registrarPartida(PartidaId partidaId, TorneioId torneioId) {
        partidaTorneio.put(partidaId, torneioId);
    }

    public void registrarJogadorNaPartida(PartidaId partidaId, JogadorId jogadorId) {
        jogadoresPorPartida.computeIfAbsent(partidaId, k -> new HashSet<>()).add(jogadorId);
    }

    public void registrarPartidaEncerrada(PartidaId partidaId) {
        partidasEncerradas.add(partidaId);
    }

    public void registrarTitular(PartidaId partidaId, JogadorId jogadorId, TimeId timeId) {
        titularesPorPartida.computeIfAbsent(partidaId, k -> new HashSet<>()).add(jogadorId);
        registrarJogadorNaPartida(partidaId, jogadorId);
        timeDoJogadorPorPartida.computeIfAbsent(partidaId, k -> new HashMap<>())
                .put(jogadorId, timeId);
    }

    public void registrarReserva(PartidaId partidaId, JogadorId jogadorId, TimeId timeId) {
        reservasPorPartida.computeIfAbsent(partidaId, k -> new HashSet<>()).add(jogadorId);
        registrarJogadorNaPartida(partidaId, jogadorId);
        timeDoJogadorPorPartida.computeIfAbsent(partidaId, k -> new HashMap<>())
                .put(jogadorId, timeId);
    }

    @Override
    public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        UsuarioId organizador = organizadores.get(torneioId);
        return organizador != null && organizador.equals(usuarioId);
    }

    @Override
    public boolean partidaPertenceAoTorneio(PartidaId partidaId, TorneioId torneioId) {
        TorneioId torneio = partidaTorneio.get(partidaId);
        return torneio != null && torneio.equals(torneioId);
    }

    @Override
    public boolean jogadorPertenceAosTimesDaPartida(PartidaId partidaId, JogadorId jogadorId) {
        Set<JogadorId> jogadores = jogadoresPorPartida.get(partidaId);
        return jogadores != null && jogadores.contains(jogadorId);
    }

    @Override
    public boolean partidaEncerrada(PartidaId partidaId) {
        return partidasEncerradas.contains(partidaId);
    }

    @Override
    public boolean jogadorEhTitularNaEscalacao(PartidaId partidaId, JogadorId jogadorId) {
        Set<JogadorId> titulares = titularesPorPartida.get(partidaId);
        return titulares != null && titulares.contains(jogadorId);
    }

    @Override
    public boolean jogadorEhReservaNaEscalacao(PartidaId partidaId, JogadorId jogadorId) {
        Set<JogadorId> reservas = reservasPorPartida.get(partidaId);
        return reservas != null && reservas.contains(jogadorId);
    }

    @Override
    public boolean jogadoresPertencemAoMesmoTimeNaPartida(PartidaId partidaId,
                                                           JogadorId primeiroJogadorId,
                                                           JogadorId segundoJogadorId) {
        Map<JogadorId, TimeId> timesPorJogador = timeDoJogadorPorPartida.get(partidaId);
        if (timesPorJogador == null) {
            return false;
        }
        TimeId timePrimeiro = timesPorJogador.get(primeiroJogadorId);
        TimeId timeSegundo = timesPorJogador.get(segundoJogadorId);
        return timePrimeiro != null && timePrimeiro.equals(timeSegundo);
    }

    public void limpar() {
        organizadores.clear();
        partidaTorneio.clear();
        jogadoresPorPartida.clear();
        partidasEncerradas.clear();
        titularesPorPartida.clear();
        reservasPorPartida.clear();
        timeDoJogadorPorPartida.clear();
    }
}
