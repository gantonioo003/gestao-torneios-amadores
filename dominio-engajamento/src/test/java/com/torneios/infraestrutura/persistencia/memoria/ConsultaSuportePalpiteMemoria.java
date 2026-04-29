package com.torneios.infraestrutura.persistencia.memoria;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.palpite.ConsultaSuportePalpite;
import com.torneios.dominio.engajamento.palpite.EventoAlvo;
import com.torneios.dominio.engajamento.palpite.OpcaoPalpite;

public class ConsultaSuportePalpiteMemoria implements ConsultaSuportePalpite {

    private final Set<UsuarioId> usuariosAutenticados = new HashSet<>();
    private final Set<PartidaId> partidasIniciadas = new HashSet<>();
    private final Set<PartidaId> partidasEncerradas = new HashSet<>();
    private final Set<TorneioId> torneiosIniciados = new HashSet<>();
    private final Set<TorneioId> torneiosFinalizados = new HashSet<>();
    private final Map<EventoAlvo, Set<Long>> opcoesValidasPorEvento = new HashMap<>();

    public void autenticar(UsuarioId usuarioId) {
        usuariosAutenticados.add(usuarioId);
    }

    public void iniciarPartida(PartidaId partidaId) {
        partidasIniciadas.add(partidaId);
    }

    public void encerrarPartida(PartidaId partidaId) {
        partidasEncerradas.add(partidaId);
    }

    public void iniciarTorneio(TorneioId torneioId) {
        torneiosIniciados.add(torneioId);
    }

    public void finalizarTorneio(TorneioId torneioId) {
        torneiosFinalizados.add(torneioId);
    }

    public void registrarOpcoesValidas(EventoAlvo eventoAlvo, long... opcoes) {
        Set<Long> valores = new HashSet<>();
        Arrays.stream(opcoes).forEach(valores::add);
        opcoesValidasPorEvento.put(eventoAlvo, valores);
    }

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return usuariosAutenticados.contains(usuarioId);
    }

    @Override
    public boolean partidaIniciada(PartidaId partidaId) {
        return partidasIniciadas.contains(partidaId);
    }

    @Override
    public boolean partidaEncerrada(PartidaId partidaId) {
        return partidasEncerradas.contains(partidaId);
    }

    @Override
    public boolean torneioIniciado(TorneioId torneioId) {
        return torneiosIniciados.contains(torneioId);
    }

    @Override
    public boolean torneioFinalizado(TorneioId torneioId) {
        return torneiosFinalizados.contains(torneioId);
    }

    @Override
    public boolean opcaoValidaParaEvento(EventoAlvo eventoAlvo, OpcaoPalpite opcao) {
        return opcoesValidasPorEvento.getOrDefault(eventoAlvo, Set.of()).contains(opcao.valor());
    }
}
