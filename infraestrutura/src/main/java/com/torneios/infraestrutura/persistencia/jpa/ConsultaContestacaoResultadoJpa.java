package com.torneios.infraestrutura.persistencia.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.contestacao.ConsultaContestacaoResultado;
import com.torneios.dominio.participacao.time.TimeRepositorio;

@Component
class ConsultaContestacaoResultadoJpa implements ConsultaContestacaoResultado {

    private static final int PRAZO_PADRAO_HORAS = 48;

    @Autowired
    TimeRepositorio timeRepositorio;

    @Override
    public boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(time -> time.getResponsavel().equals(usuarioId))
                .orElse(false);
    }

    @Override
    public int prazoContestacaoEmHoras(TorneioId torneioId) {
        return PRAZO_PADRAO_HORAS;
    }
}
