package com.torneios.infraestrutura.persistencia.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.engajamento.feed.ConsultaSuporteFeedTorneio;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.participacao.time.TimeRepositorio;

@Component
class ConsultaSuporteFeedTorneioJpa implements ConsultaSuporteFeedTorneio {

    @Autowired
    AutenticacaoServico autenticacaoServico;

    @Autowired
    TorneioRepositorio torneioRepositorio;

    @Autowired
    PartidaRepositorio partidaRepositorio;

    @Autowired
    TimeRepositorio timeRepositorio;

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return autenticacaoServico.estaAutenticado(usuarioId);
    }

    @Override
    public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        return torneioRepositorio.buscarPorId(torneioId)
                .map(torneio -> torneio.getOrganizadorId().equals(usuarioId))
                .orElse(false);
    }

    @Override
    public boolean partidaPertenceAoTorneio(TorneioId torneioId, PartidaId partidaId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .map(partida -> partida.getTorneioId().equals(torneioId))
                .orElse(false);
    }

    @Override
    public boolean usuarioEhResponsavelTime(TimeId timeId, UsuarioId usuarioId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(time -> time.getResponsavel().equals(usuarioId))
                .orElse(false);
    }
}
