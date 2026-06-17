package com.torneios.infraestrutura.persistencia.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.desafio.ConsultaSuporteDesafioAmistoso;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.time.TimeRepositorio;

@Component
class ConsultaSuporteDesafioAmistosoJpa implements ConsultaSuporteDesafioAmistoso {

    @Autowired
    AutenticacaoServico autenticacaoServico;

    @Autowired
    TimeRepositorio timeRepositorio;

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return autenticacaoServico.estaAutenticado(usuarioId);
    }

    @Override
    public boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(time -> time.getResponsavel().equals(usuarioId))
                .orElse(false);
    }

    @Override
    public boolean timesPodemSeDesafiar(TimeId timeDesafianteId, TimeId timeDesafiadoId) {
        if (timeDesafianteId.equals(timeDesafiadoId)) {
            return false;
        }
        return timeRepositorio.buscarPorId(timeDesafianteId).isPresent()
                && timeRepositorio.buscarPorId(timeDesafiadoId).isPresent();
    }
}
