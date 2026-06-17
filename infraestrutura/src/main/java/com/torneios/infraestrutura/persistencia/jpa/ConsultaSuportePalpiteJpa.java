package com.torneios.infraestrutura.persistencia.jpa;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.enumeracao.StatusTorneio;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.engajamento.palpite.ConsultaSuportePalpite;
import com.torneios.dominio.engajamento.palpite.EventoAlvo;
import com.torneios.dominio.engajamento.palpite.OpcaoPalpite;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.profissional.TipoProfissional;
import com.torneios.dominio.participacao.time.TimeRepositorio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;

@Component
class ConsultaSuportePalpiteJpa implements ConsultaSuportePalpite {

    @Autowired
    AutenticacaoServico autenticacaoServico;

    @Autowired
    PartidaRepositorio partidaRepositorio;

    @Autowired
    TorneioRepositorio torneioRepositorio;

    @Autowired
    TimeRepositorio timeRepositorio;

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return autenticacaoServico.estaAutenticado(usuarioId);
    }

    @Override
    public boolean partidaIniciada(PartidaId partidaId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .map(Partida::estaEncerrada)
                .orElse(false);
    }

    @Override
    public boolean partidaEncerrada(PartidaId partidaId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .map(Partida::estaEncerrada)
                .orElse(false);
    }

    @Override
    public boolean torneioIniciado(TorneioId torneioId) {
        return torneioRepositorio.buscarPorId(torneioId)
                .map(torneio -> torneio.getStatus() == StatusTorneio.INICIADO
                        || torneio.getStatus() == StatusTorneio.FINALIZADO)
                .orElse(false);
    }

    @Override
    public boolean torneioFinalizado(TorneioId torneioId) {
        return torneioRepositorio.buscarPorId(torneioId)
                .map(torneio -> torneio.getStatus() == StatusTorneio.FINALIZADO)
                .orElse(false);
    }

    @Override
    public boolean opcaoValidaParaEvento(EventoAlvo eventoAlvo, OpcaoPalpite opcao) {
        Objects.requireNonNull(eventoAlvo, "O evento do palpite e obrigatorio.");
        Objects.requireNonNull(opcao, "A opcao do palpite e obrigatoria.");
        return switch (eventoAlvo.getTipo()) {
            case VENCEDOR_PARTIDA -> partidaRepositorio.buscarPorId(eventoAlvo.getPartidaId())
                    .map(partida -> partida.getMandante().valor() == opcao.valor()
                            || partida.getVisitante().valor() == opcao.valor())
                    .orElse(false);
            case CAMPEAO_TORNEIO -> torneioRepositorio.buscarPorId(eventoAlvo.getTorneioId())
                    .map(torneio -> torneio.getParticipantesAprovados().stream()
                            .anyMatch(participante -> participante.getTimeId().valor() == opcao.valor()))
                    .orElse(false);
            case ARTILHEIRO_TORNEIO, LIDER_ASSISTENCIAS_TORNEIO -> jogadoresValidosDoTorneio(eventoAlvo.getTorneioId())
                    .stream()
                    .anyMatch(jogadorId -> jogadorId.valor() == opcao.valor());
        };
    }

    private List<JogadorId> jogadoresValidosDoTorneio(TorneioId torneioId) {
        return torneioRepositorio.buscarPorId(torneioId)
                .map(torneio -> torneio.getParticipantesAprovados().stream()
                        .flatMap(participante -> timeRepositorio.buscarPorId(participante.getTimeId()).stream())
                        .flatMap(time -> time.getElenco().stream())
                        .filter(vinculo -> TipoProfissional.JOGADOR.name().equalsIgnoreCase(vinculo.getFuncao()))
                        .map(vinculo -> new JogadorId(vinculo.getProfissionalId().valor()))
                        .distinct()
                        .toList())
                .orElse(List.of());
    }
}
