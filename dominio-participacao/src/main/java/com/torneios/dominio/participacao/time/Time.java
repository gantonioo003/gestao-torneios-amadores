package com.torneios.dominio.participacao.time;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.jogador.Jogador;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.tecnico.Tecnico;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class Time {

    private final TimeId id;
    private String nome;
    private UsuarioId responsavel;
    private final Map<JogadorId, Jogador> jogadores;
    private final Set<TorneioId> torneiosVinculados;
    private Tecnico tecnico;

    public Time(TimeId id, String nome, UsuarioId responsavel) {
        this.id = Objects.requireNonNull(id, "O id do time e obrigatorio.");
        this.nome = validarNome(nome);
        this.responsavel = Objects.requireNonNull(responsavel, "O responsavel do time e obrigatorio.");
        this.jogadores = new LinkedHashMap<>();
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

    public Collection<Jogador> getJogadores() {
        return java.util.List.copyOf(jogadores.values());
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
        Objects.requireNonNull(jogador, "O jogador do time e obrigatorio.");
        if (!jogador.getTimeId().equals(id)) {
            throw new RegraDeNegocioException("O jogador deve estar vinculado ao proprio time.");
        }
        if (jogadores.containsKey(jogador.getId())) {
            throw new RegraDeNegocioException("Ja existe um jogador com esse id no elenco do time.");
        }
        jogadores.put(jogador.getId(), jogador);
    }

    public void editarJogador(JogadorId jogadorId, String novoNome) {
        obterJogador(jogadorId).renomear(novoNome);
    }

    public void removerJogador(JogadorId jogadorId) {
        if (jogadores.remove(jogadorId) == null) {
            throw new EntidadeNaoEncontradaException("Jogador nao encontrado no elenco do time.");
        }
    }

    public void associarTecnico(Tecnico tecnico) {
        Objects.requireNonNull(tecnico, "O tecnico do time e obrigatorio.");
        if (!tecnico.getTimeId().equals(id)) {
            throw new RegraDeNegocioException("O tecnico deve estar vinculado ao proprio time.");
        }
        this.tecnico = tecnico;
    }

    public void editarTecnico(TecnicoId tecnicoId, String novoNome) {
        if (tecnico == null || !tecnico.getId().equals(tecnicoId)) {
            throw new EntidadeNaoEncontradaException("Tecnico nao encontrado no time.");
        }
        tecnico.renomear(novoNome);
    }

    public void removerTecnico() {
        if (tecnico == null) {
            throw new EntidadeNaoEncontradaException("Nao existe tecnico associado ao time.");
        }
        this.tecnico = null;
    }

    public void vincularAoTorneio(TorneioId torneioId) {
        Objects.requireNonNull(torneioId, "O torneio vinculado e obrigatorio.");
        torneiosVinculados.add(torneioId);
    }

    public boolean estaVinculadoATorneio() {
        return !torneiosVinculados.isEmpty();
    }

    public boolean possuiJogador(JogadorId jogadorId) {
        return jogadores.containsKey(jogadorId);
    }

    public boolean estaVinculadoAoTorneio(TorneioId torneioId) {
        return torneiosVinculados.contains(torneioId);
    }

    public void removerVinculoTorneio(TorneioId torneioId) {
        torneiosVinculados.remove(torneioId);
    }

    private String validarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do time e obrigatorio.");
        }
        return valor.trim();
    }

    private Jogador obterJogador(JogadorId jogadorId) {
        Jogador jogador = jogadores.get(jogadorId);
        if (jogador == null) {
            throw new EntidadeNaoEncontradaException("Jogador nao encontrado no elenco do time.");
        }
        return jogador;
    }
}
