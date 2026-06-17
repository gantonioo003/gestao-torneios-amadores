package com.torneios.aplicacao.estatisticas.sumula;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.estatisticas.evento.ControleDisciplinarJogador;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoRepositorio;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoServico;
import com.torneios.dominio.estatisticas.evento.Substituicao;

/**
 * Casos de uso do scout estatistico opcional da partida.
 */
public class SumulaEstatisticaServicoAplicacao {

    private final EventoEstatisticoServico eventoEstatisticoServico;
    private final EventoEstatisticoRepositorio eventoEstatisticoRepositorio;

    public SumulaEstatisticaServicoAplicacao(EventoEstatisticoServico eventoEstatisticoServico,
                                             EventoEstatisticoRepositorio eventoEstatisticoRepositorio) {
        notNull(eventoEstatisticoServico, "O servico de eventos e obrigatorio.");
        notNull(eventoEstatisticoRepositorio, "O repositorio de eventos e obrigatorio.");
        this.eventoEstatisticoServico = eventoEstatisticoServico;
        this.eventoEstatisticoRepositorio = eventoEstatisticoRepositorio;
    }

    public EventoResumo registrarGol(long eventoId, long torneioId, long partidaId, long organizadorId, long jogadorId) {
        return converter(eventoEstatisticoServico.registrarGol(
                eventoId, new TorneioId(torneioId), new PartidaId(partidaId), new UsuarioId(organizadorId), new JogadorId(jogadorId)));
    }

    public EventoResumo registrarAssistencia(long eventoId, long torneioId, long partidaId, long organizadorId, long jogadorId) {
        return converter(eventoEstatisticoServico.registrarAssistencia(
                eventoId, new TorneioId(torneioId), new PartidaId(partidaId), new UsuarioId(organizadorId), new JogadorId(jogadorId)));
    }

    public EventoResumo registrarCartaoAmarelo(long eventoId, long torneioId, long partidaId, long organizadorId, long jogadorId) {
        return converter(eventoEstatisticoServico.registrarCartaoAmarelo(
                eventoId, new TorneioId(torneioId), new PartidaId(partidaId), new UsuarioId(organizadorId), new JogadorId(jogadorId)));
    }

    public EventoResumo registrarCartaoVermelho(long eventoId, long torneioId, long partidaId, long organizadorId, long jogadorId) {
        return converter(eventoEstatisticoServico.registrarCartaoVermelho(
                eventoId, new TorneioId(torneioId), new PartidaId(partidaId), new UsuarioId(organizadorId), new JogadorId(jogadorId)));
    }

    public EventoResumo registrarSubstituicao(long eventoId,
                                              long torneioId,
                                              long partidaId,
                                              long organizadorId,
                                              long jogadorSaiuId,
                                              long jogadorEntrouId) {
        return registrarSubstituicaoComParadaJogo(eventoId, torneioId, partidaId, organizadorId,
                jogadorSaiuId, jogadorEntrouId, null, null);
    }

    public EventoResumo registrarSubstituicaoComParadaJogo(long eventoId,
                                                           long torneioId,
                                                           long partidaId,
                                                           long organizadorId,
                                                           long jogadorSaiuId,
                                                           long jogadorEntrouId,
                                                           Integer ordemParadaJogo,
                                                           String descricaoParadaJogo) {
        return converter(eventoEstatisticoServico.registrarSubstituicao(
                eventoId,
                new TorneioId(torneioId),
                new PartidaId(partidaId),
                new UsuarioId(organizadorId),
                new JogadorId(jogadorSaiuId),
                new JogadorId(jogadorEntrouId),
                ordemParadaJogo,
                descricaoParadaJogo));
    }

    public EventoResumo corrigirEvento(long eventoId,
                                       long torneioId,
                                       long partidaId,
                                       long organizadorId,
                                       long jogadorId,
                                       String novoTipo) {
        return converter(eventoEstatisticoServico.corrigirEvento(
                eventoId,
                new TorneioId(torneioId),
                new PartidaId(partidaId),
                new UsuarioId(organizadorId),
                new JogadorId(jogadorId),
                TipoEventoEstatistico.valueOf(novoTipo)));
    }

    public EventoResumo corrigirSubstituicao(long eventoId,
                                             long torneioId,
                                             long partidaId,
                                             long organizadorId,
                                             long jogadorSaiuId,
                                             long jogadorEntrouId,
                                             Integer ordemParadaJogo,
                                             String descricaoParadaJogo) {
        return converter(eventoEstatisticoServico.corrigirSubstituicao(
                eventoId,
                new TorneioId(torneioId),
                new PartidaId(partidaId),
                new UsuarioId(organizadorId),
                new JogadorId(jogadorSaiuId),
                new JogadorId(jogadorEntrouId),
                ordemParadaJogo,
                descricaoParadaJogo));
    }

    public void removerEvento(long eventoId, long torneioId, long partidaId, long organizadorId) {
        eventoEstatisticoServico.removerEvento(
                eventoId,
                new TorneioId(torneioId),
                new PartidaId(partidaId),
                new UsuarioId(organizadorId));
    }

    public List<EventoResumo> listarPorPartida(long partidaId) {
        return eventoEstatisticoRepositorio.listarPorPartida(new PartidaId(partidaId)).stream()
                .map(this::converter)
                .toList();
    }

    public List<EventoResumo> listarPorTorneio(long torneioId) {
        return eventoEstatisticoRepositorio.listarPorTorneio(new TorneioId(torneioId)).stream()
                .map(this::converter)
                .toList();
    }

    public ControleDisciplinarResumo consultarControleDisciplinar(long torneioId, long partidaId, long jogadorId) {
        return converterControle(eventoEstatisticoServico.consultarControleDisciplinar(
                new TorneioId(torneioId),
                new PartidaId(partidaId),
                new JogadorId(jogadorId)));
    }

    private EventoResumo converter(EventoEstatistico eventoEstatistico) {
        Long jogadorSaiuId = null;
        Long jogadorEntrouId = null;
        Integer ordemParadaJogo = null;
        String descricaoParadaJogo = null;
        if (eventoEstatistico instanceof Substituicao substituicao) {
            jogadorSaiuId = substituicao.getJogadorSaiuId().valor();
            jogadorEntrouId = substituicao.getJogadorEntrouId().valor();
            ordemParadaJogo = substituicao.getOrdemParadaJogo();
            descricaoParadaJogo = substituicao.getDescricaoParadaJogo();
        }
        return new EventoResumo(
                eventoEstatistico.getId(),
                eventoEstatistico.getTorneioId().valor(),
                eventoEstatistico.getPartidaId().valor(),
                eventoEstatistico.getJogadorId().valor(),
                eventoEstatistico.getTipo().name(),
                jogadorSaiuId,
                jogadorEntrouId,
                eventoEstatistico.isAutomatico(),
                eventoEstatistico.getEventoOrigemId().orElse(null),
                ordemParadaJogo,
                descricaoParadaJogo);
    }

    private ControleDisciplinarResumo converterControle(ControleDisciplinarJogador controleDisciplinarJogador) {
        return new ControleDisciplinarResumo(
                controleDisciplinarJogador.torneioId().valor(),
                controleDisciplinarJogador.partidaId().valor(),
                controleDisciplinarJogador.jogadorId().valor(),
                controleDisciplinarJogador.cartoesAmarelosNoTorneio(),
                controleDisciplinarJogador.cartoesAmarelosNaPartida(),
                controleDisciplinarJogador.cartoesVermelhosNoTorneio(),
                controleDisciplinarJogador.expulsoAutomaticamenteNaPartida(),
                controleDisciplinarJogador.suspensaoAutomaticaPendente(),
                controleDisciplinarJogador.eventosAutomaticosGerados());
    }

    public record EventoResumo(long id,
                               long torneioId,
                               long partidaId,
                               long jogadorId,
                               String tipo,
                               Long jogadorSaiuId,
                               Long jogadorEntrouId,
                               boolean automatico,
                               Long eventoOrigemId,
                               Integer ordemParadaJogo,
                               String descricaoParadaJogo) {
    }

    public record ControleDisciplinarResumo(long torneioId,
                                            long partidaId,
                                            long jogadorId,
                                            int cartoesAmarelosNoTorneio,
                                            int cartoesAmarelosNaPartida,
                                            int cartoesVermelhosNoTorneio,
                                            boolean expulsoAutomaticamenteNaPartida,
                                            boolean suspensaoAutomaticaPendente,
                                            List<Long> eventosAutomaticosGerados) {
    }
}
