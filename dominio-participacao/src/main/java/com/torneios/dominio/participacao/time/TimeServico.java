package com.torneios.dominio.participacao.time;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.jogador.Jogador;
import com.torneios.dominio.participacao.tecnico.Tecnico;

public class TimeServico {

    private final TimeRepositorio timeRepositorio;

    public TimeServico(TimeRepositorio timeRepositorio) {
        this.timeRepositorio = Objects.requireNonNull(timeRepositorio, "O repositorio de times e obrigatorio.");
    }

    public Time criarTime(TimeId timeId, String nome, UsuarioId responsavel) {
        Time time = new Time(timeId, nome, responsavel);
        timeRepositorio.salvar(time);
        return time;
    }

    public void editarTime(TimeId timeId, String novoNome) {
        Time time = obterTime(timeId);
        time.renomear(novoNome);
        timeRepositorio.salvar(time);
    }

    public void excluirTime(TimeId timeId) {
        Time time = obterTime(timeId);
        if (time.estaVinculadoATorneio()) {
            throw new IllegalStateException("Nao e permitido excluir um time vinculado a torneio.");
        }
    }

    public void adicionarJogador(TimeId timeId, Jogador jogador) {
        Time time = obterTime(timeId);
        time.adicionarJogador(jogador);
        timeRepositorio.salvar(time);
    }

    public void removerJogador(TimeId timeId, long jogadorId) {
        Time time = obterTime(timeId);
        time.removerJogador(jogadorId);
        timeRepositorio.salvar(time);
    }

    public void associarTecnico(TimeId timeId, Tecnico tecnico) {
        Time time = obterTime(timeId);
        time.associarTecnico(tecnico);
        timeRepositorio.salvar(time);
    }

    public void removerTecnico(TimeId timeId) {
        Time time = obterTime(timeId);
        time.removerTecnico();
        timeRepositorio.salvar(time);
    }

    public List<Time> listarTimesDoUsuario(UsuarioId usuarioId) {
        return timeRepositorio.listarPorResponsavel(usuarioId);
    }

    public Time obterTime(TimeId timeId) {
        return timeRepositorio.buscarPorId(timeId)
                .orElseThrow(() -> new IllegalArgumentException("Time nao encontrado."));
    }
}
