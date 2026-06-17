package com.torneios.dominio.participacao.time;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreira;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreiraId;
import com.torneios.dominio.compartilhado.jogador.Jogador;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.tecnico.Tecnico;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.responsavel.ResponsavelTimeServico;

public class TimeServico {

    private final TimeRepositorio timeRepositorio;
    private final AutenticacaoServico autenticacaoServico;
    private final ResponsavelTimeServico responsavelTimeServico;
    private final ProfissionalEsportivoRepositorio profissionalRepositorio;

    public TimeServico(TimeRepositorio timeRepositorio, AutenticacaoServico autenticacaoServico,
            ResponsavelTimeServico responsavelTimeServico) {
        this(timeRepositorio, autenticacaoServico, responsavelTimeServico, null);
    }

    public TimeServico(TimeRepositorio timeRepositorio, AutenticacaoServico autenticacaoServico,
            ResponsavelTimeServico responsavelTimeServico,
            ProfissionalEsportivoRepositorio profissionalRepositorio) {
        this.timeRepositorio = Objects.requireNonNull(timeRepositorio);
        this.autenticacaoServico = Objects.requireNonNull(autenticacaoServico);
        this.responsavelTimeServico = Objects.requireNonNull(responsavelTimeServico);
        this.profissionalRepositorio = profissionalRepositorio;
    }

    public Time criarTime(TimeId timeId, String nome, UsuarioId responsavel) {
        autenticacaoServico.exigirAutenticacao(responsavel);
        Time time = new Time(timeId, nome, responsavel);
        timeRepositorio.salvar(time);
        return time;
    }

    public void editarTime(TimeId timeId, UsuarioId usuarioId, String novoNome) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        time.renomear(novoNome);
        timeRepositorio.salvar(time);
    }

    public void excluirTime(TimeId timeId, UsuarioId usuarioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        if (time.estaVinculadoATorneio())
            throw new OperacaoNaoPermitidaException("Nao e permitido excluir um time vinculado a torneio.");
        timeRepositorio.remover(timeId);
    }

    public void adicionarJogador(TimeId timeId, UsuarioId usuarioId, JogadorId jogadorId, String nomeJogador) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        time.adicionarJogador(new Jogador(jogadorId, nomeJogador, timeId));
        timeRepositorio.salvar(time);
    }

    public void editarJogador(TimeId timeId, UsuarioId usuarioId, JogadorId jogadorId, String novoNome) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        time.editarJogador(jogadorId, novoNome);
        timeRepositorio.salvar(time);
    }

    public void removerJogador(TimeId timeId, UsuarioId usuarioId, JogadorId jogadorId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        time.removerJogador(jogadorId);
        timeRepositorio.salvar(time);
    }

    public void associarTecnico(TimeId timeId, UsuarioId usuarioId, TecnicoId tecnicoId, String nomeTecnico) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        time.associarTecnico(new Tecnico(tecnicoId, nomeTecnico, timeId));
        timeRepositorio.salvar(time);
    }

    public void editarTecnico(TimeId timeId, UsuarioId usuarioId, TecnicoId tecnicoId, String novoNome) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        time.editarTecnico(tecnicoId, novoNome);
        timeRepositorio.salvar(time);
    }

    public void removerTecnico(TimeId timeId, UsuarioId usuarioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        time.removerTecnico();
        timeRepositorio.salvar(time);
    }

    public List<Time> listarTimesDoUsuario(UsuarioId usuarioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        return timeRepositorio.listarPorResponsavel(usuarioId);
    }

    public List<Time> buscarTimesDisponiveisParaJogador(UsuarioId jogadorUsuarioId) {
        autenticacaoServico.exigirAutenticacao(jogadorUsuarioId);
        return timeRepositorio.listarTodos();
    }

    public void vincularTimeAoTorneio(TimeId timeId, TorneioId torneioId) {
        Time time = obterTime(timeId);
        time.vincularAoTorneio(torneioId);
        timeRepositorio.salvar(time);
    }

    public void removerVinculoComTorneio(TimeId timeId, TorneioId torneioId) {
        Time time = obterTime(timeId);
        time.removerVinculoTorneio(torneioId);
        timeRepositorio.salvar(time);
    }

    // ── F5: VinculoProfissional ────────────────────────────────────
    public void vincularProfissional(TimeId timeId, UsuarioId usuarioId,
            ProfissionalEsportivoId profissionalId, String funcao,
            LocalDate dataInicio, LocalDate dataLimiteContrato) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        time.vincularProfissional(profissionalId, funcao, dataInicio, dataLimiteContrato);
        timeRepositorio.salvar(time);
        adicionarCarreiraAutomatica(profissionalId, time.getNome(), dataInicio);
    }

    public void editarVinculoProfissional(TimeId timeId, UsuarioId usuarioId,
            ProfissionalEsportivoId profissionalId, String funcao,
            LocalDate dataInicio, LocalDate dataLimiteContrato) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        time.editarVinculoProfissional(profissionalId, funcao, dataInicio, dataLimiteContrato);
        timeRepositorio.salvar(time);
    }

    public void removerVinculoProfissional(TimeId timeId, UsuarioId usuarioId,
            ProfissionalEsportivoId profissionalId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = responsavelTimeServico.obterTimeSobResponsabilidade(timeId, usuarioId);
        time.removerVinculoProfissional(profissionalId);
        timeRepositorio.salvar(time);
        fecharCarreiraAutomatica(profissionalId, time.getNome());
    }

    public Time obterTime(TimeId timeId) {
        return timeRepositorio.buscarPorId(timeId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Time nao encontrado."));
    }

    private void adicionarCarreiraAutomatica(ProfissionalEsportivoId profissionalId,
            String nomeDoTime, LocalDate dataInicio) {
        if (profissionalRepositorio == null) return;
        profissionalRepositorio.buscarPorId(profissionalId).ifPresent(profissional -> {
            var registroId = new RegistroDeCarreiraId(System.currentTimeMillis());
            profissional.adicionarRegistroDeCarreira(
                new RegistroDeCarreira(registroId, nomeDoTime, dataInicio, null, null));
            profissionalRepositorio.salvar(profissional);
        });
    }

    private void fecharCarreiraAutomatica(ProfissionalEsportivoId profissionalId, String nomeDoTime) {
        if (profissionalRepositorio == null) return;
        profissionalRepositorio.buscarPorId(profissionalId).ifPresent(profissional -> {
            profissional.getHistorico().stream()
                .filter(r -> r.getDataFim() == null && nomeDoTime.equals(r.getNomeDoClube()))
                .findFirst()
                .ifPresent(r -> {
                    r.setDataFim(LocalDate.now());
                    profissionalRepositorio.salvar(profissional);
                });
        });
    }
}
