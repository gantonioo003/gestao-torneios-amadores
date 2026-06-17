package com.torneios.infraestrutura.persistencia.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.estatisticas.evento.ConsultaEstatisticaCompeticao;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoRepositorio;
import com.torneios.dominio.participacao.profissional.TipoProfissional;
import com.torneios.dominio.participacao.time.TimeRepositorio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;

@Component
class ConsultaEstatisticaCompeticaoJpa implements ConsultaEstatisticaCompeticao {

    @Autowired
    TorneioRepositorio torneioRepositorio;

    @Autowired
    PartidaRepositorio partidaRepositorio;

    @Autowired
    TimeRepositorio timeRepositorio;

    @Autowired
    EventoEstatisticoRepositorio eventoEstatisticoRepositorio;

    @Override
    public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        return torneioRepositorio.buscarPorId(torneioId)
                .map(torneio -> torneio.getOrganizadorId().equals(usuarioId))
                .orElse(false);
    }

    @Override
    public boolean partidaPertenceAoTorneio(PartidaId partidaId, TorneioId torneioId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .map(partida -> partida.getTorneioId().equals(torneioId))
                .orElse(false);
    }

    @Override
    public boolean jogadorPertenceAosTimesDaPartida(PartidaId partidaId, JogadorId jogadorId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .map(partida -> jogadorPertenceAoTime(partida.getMandante(), jogadorId)
                        || jogadorPertenceAoTime(partida.getVisitante(), jogadorId))
                .orElse(false);
    }

    @Override
    public List<PartidaId> listarPartidasDoJogadorNoTorneio(TorneioId torneioId, JogadorId jogadorId) {
        return partidaRepositorio.listarPorTorneio(torneioId).stream()
                .filter(partida -> jogadorPertenceAosTimesDaPartida(partida.getId(), jogadorId))
                .map(com.torneios.dominio.competicao.partida.Partida::getId)
                .toList();
    }

    private boolean jogadorPertenceAoTime(TimeId timeId, JogadorId jogadorId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(time -> time.getElenco().stream()
                        .filter(vinculo -> TipoProfissional.JOGADOR.name().equalsIgnoreCase(vinculo.getFuncao()))
                        .anyMatch(vinculo -> vinculo.getProfissionalId().valor() == jogadorId.valor()))
                .orElse(false);
    }
}
