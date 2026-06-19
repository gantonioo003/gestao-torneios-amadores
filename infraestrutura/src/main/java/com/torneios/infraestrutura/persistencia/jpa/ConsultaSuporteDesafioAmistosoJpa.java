package com.torneios.infraestrutura.persistencia.jpa;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.desafio.ConsultaSuporteDesafioAmistoso;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.acesso.ContaUsuarioRepositorio;
import com.torneios.dominio.participacao.time.TimeRepositorio;

@Component
class ConsultaSuporteDesafioAmistosoJpa implements ConsultaSuporteDesafioAmistoso {

    @Autowired
    AutenticacaoServico autenticacaoServico;

    @Autowired
    TimeRepositorio timeRepositorio;
    @Autowired
    ContaUsuarioRepositorio contaUsuarioRepositorio;

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return autenticacaoServico.estaAutenticado(usuarioId);
    }

    @Override
    public boolean usuarioPodeGerenciarTimes(UsuarioId usuarioId) {
        return contaUsuarioRepositorio.buscarPorId(usuarioId)
                .map(conta -> conta.podeGerenciarTimes())
                .orElse(false);
    }

    @Override
    public boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(time -> time.getResponsavel().equals(usuarioId))
                .orElse(false);
    }

    @Override
    public Optional<UsuarioId> buscarResponsavelDoTime(TimeId timeId) {
        return timeRepositorio.buscarPorId(timeId).map(time -> time.getResponsavel());
    }

    @Override
    public boolean timesPodemSeDesafiar(TimeId timeDesafianteId, TimeId timeDesafiadoId) {
        if (timeDesafianteId.equals(timeDesafiadoId)) {
            return false;
        }
        var desafiante = timeRepositorio.buscarPorId(timeDesafianteId);
        var desafiado = timeRepositorio.buscarPorId(timeDesafiadoId);
        return desafiante.isPresent()
                && desafiado.isPresent()
                && !desafiante.get().getResponsavel().equals(desafiado.get().getResponsavel());
    }
}
