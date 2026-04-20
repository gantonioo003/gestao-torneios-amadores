package com.torneios.dominio.participacao.time;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.jogador.Jogador;
import com.torneios.dominio.participacao.tecnico.Tecnico;

public class Time {

    private final TimeId id;
    private String nome;
    private UsuarioId responsavel;
    private final Set<Jogador> jogadores;
    private final Set<Long> torneiosVinculados;
    private Tecnico tecnico;

    public Time(TimeId id, String nome, UsuarioId responsavel) {
        this.id = Objects.requireNonNull(id, "O id do time e obrigatorio.");
        this.nome = validarNome(nome);
        this.responsavel = Objects.requireNonNull(responsavel, "O responsavel do time e obrigatorio.");
        this.jogadores = new LinkedHashSet<>();
        this.torneiosVinculados = new LinkedHashSet<>();
    }

    public TimeId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public UsuarioId getResponsavel() {
        return responsavel;
    }

    public Set<Jogador> getJogadores() {
        return Set.copyOf(jogadores);
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void renomear(String novoNome) {
        this.nome = validarNome(novoNome);
    }

    public void alterarResponsavel(UsuarioId novoResponsavel) {
        this.responsavel = Objects.requireNonNull(novoResponsavel, "O novo responsavel e obrigatorio.");
    }

    public void adicionarJogador(Jogador jogador) {
        jogadores.add(Objects.requireNonNull(jogador, "O jogador do time e obrigatorio."));
    }

    public void removerJogador(long jogadorId) {
        jogadores.removeIf(jogador -> jogador.getId() == jogadorId);
    }

    public void associarTecnico(Tecnico tecnico) {
        this.tecnico = Objects.requireNonNull(tecnico, "O tecnico do time e obrigatorio.");
    }

    public void removerTecnico() {
        this.tecnico = null;
    }

    public void vincularAoTorneio(long torneioId) {
        if (torneioId <= 0) {
            throw new IllegalArgumentException("O id do torneio vinculado deve ser maior que zero.");
        }
        torneiosVinculados.add(torneioId);
    }

    public boolean estaVinculadoATorneio() {
        return !torneiosVinculados.isEmpty();
    }

    public boolean possuiJogador(long jogadorId) {
        return jogadores.stream().anyMatch(jogador -> jogador.getId() == jogadorId);
    }

    private String validarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do time e obrigatorio.");
        }
        return valor.trim();
    }
}
