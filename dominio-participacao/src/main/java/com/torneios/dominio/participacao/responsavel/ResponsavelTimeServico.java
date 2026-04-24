package com.torneios.dominio.participacao.responsavel;

import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.time.Time;
import com.torneios.dominio.participacao.time.TimeRepositorio;

public class ResponsavelTimeServico {

    private final TimeRepositorio timeRepositorio;
    private final ConsultaUsuario consultaUsuario;

    public ResponsavelTimeServico(TimeRepositorio timeRepositorio, ConsultaUsuario consultaUsuario) {
        this.timeRepositorio = Objects.requireNonNull(timeRepositorio, "O repositorio de times e obrigatorio.");
        this.consultaUsuario = Objects.requireNonNull(consultaUsuario, "A consulta de usuarios e obrigatoria.");
    }

    public void definirResponsavel(TimeId timeId, UsuarioId usuarioId) {
        if (usuarioId == null || !consultaUsuario.existe(usuarioId)) {
            throw new EntidadeNaoEncontradaException("O usuario responsavel informado nao existe.");
        }

        Time time = timeRepositorio.buscarPorId(timeId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Time nao encontrado."));
        time.alterarResponsavel(usuarioId);
        timeRepositorio.salvar(time);
    }

    public void validarResponsavel(Time time, UsuarioId usuarioId) {
        if (time == null) {
            throw new IllegalArgumentException("O time e obrigatorio.");
        }
        if (usuarioId == null || !time.getResponsavel().equals(usuarioId)) {
            throw new OperacaoNaoPermitidaException("Apenas o responsavel do time pode executar esta operacao.");
        }
    }

    public Time obterTimeSobResponsabilidade(TimeId timeId, UsuarioId usuarioId) {
        Time time = timeRepositorio.buscarPorId(timeId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Time nao encontrado."));
        validarResponsavel(time, usuarioId);
        return time;
    }
}
