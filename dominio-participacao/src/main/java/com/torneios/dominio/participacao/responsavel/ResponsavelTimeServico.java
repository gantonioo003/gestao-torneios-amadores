package com.torneios.dominio.participacao.responsavel;

import java.util.Objects;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.time.Time;
import com.torneios.dominio.participacao.time.TimeRepositorio;

public class ResponsavelTimeServico {

    private final TimeRepositorio timeRepositorio;

    public ResponsavelTimeServico(TimeRepositorio timeRepositorio) {
        this.timeRepositorio = Objects.requireNonNull(timeRepositorio, "O repositorio de times e obrigatorio.");
    }

    public void definirResponsavel(TimeId timeId, UsuarioId usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("O usuario responsavel deve existir.");
        }

        Time time = timeRepositorio.buscarPorId(timeId)
                .orElseThrow(() -> new IllegalArgumentException("Time nao encontrado."));
        time.alterarResponsavel(usuarioId);
        timeRepositorio.salvar(time);
    }

    public void validarResponsavel(Time time, UsuarioId usuarioId) {
        if (time == null) {
            throw new IllegalArgumentException("O time e obrigatorio.");
        }
        if (usuarioId == null || !time.getResponsavel().equals(usuarioId)) {
            throw new IllegalStateException("Apenas o responsavel do time pode executar esta operacao.");
        }
    }
}
