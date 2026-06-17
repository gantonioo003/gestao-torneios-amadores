package com.torneios.dominio.estatisticas.evento;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class EventoEstatisticoServico {

    private static final int LIMITE_EXPULSAO_POR_AMARELOS = 2;
    private static final int LIMITE_SUSPENSAO_POR_ACUMULO = 3;

    private final EventoEstatisticoRepositorio eventoEstatisticoRepositorio;
    private final ConsultaEstatisticaCompeticao consultaEstatisticaCompeticao;

    public EventoEstatisticoServico(EventoEstatisticoRepositorio eventoEstatisticoRepositorio,
                                    ConsultaEstatisticaCompeticao consultaEstatisticaCompeticao) {
        this.eventoEstatisticoRepositorio = Objects.requireNonNull(eventoEstatisticoRepositorio,
                "O repositorio de eventos estatisticos e obrigatorio.");
        this.consultaEstatisticaCompeticao = Objects.requireNonNull(consultaEstatisticaCompeticao,
                "A consulta de estatisticas da competicao e obrigatoria.");
    }

    public EventoEstatistico registrarGol(long eventoId,
                                          TorneioId torneioId,
                                          PartidaId partidaId,
                                          UsuarioId organizadorId,
                                          JogadorId jogadorId) {
        return registrarEvento(eventoId, torneioId, partidaId, organizadorId, jogadorId, TipoEventoEstatistico.GOL);
    }

    public EventoEstatistico registrarAssistencia(long eventoId,
                                                  TorneioId torneioId,
                                                  PartidaId partidaId,
                                                  UsuarioId organizadorId,
                                                  JogadorId jogadorId) {
        return registrarEvento(eventoId, torneioId, partidaId, organizadorId, jogadorId,
                TipoEventoEstatistico.ASSISTENCIA);
    }

    public EventoEstatistico registrarCartaoAmarelo(long eventoId,
                                                    TorneioId torneioId,
                                                    PartidaId partidaId,
                                                    UsuarioId organizadorId,
                                                    JogadorId jogadorId) {
        EventoEstatistico eventoEstatistico = registrarEvento(eventoId, torneioId, partidaId, organizadorId, jogadorId,
                TipoEventoEstatistico.CARTAO_AMARELO);
        reprocessarDisciplinaDoJogador(torneioId, partidaId, jogadorId);
        return eventoEstatistico;
    }

    public EventoEstatistico registrarCartaoVermelho(long eventoId,
                                                     TorneioId torneioId,
                                                     PartidaId partidaId,
                                                     UsuarioId organizadorId,
                                                     JogadorId jogadorId) {
        EventoEstatistico eventoEstatistico = registrarEvento(eventoId, torneioId, partidaId, organizadorId, jogadorId,
                TipoEventoEstatistico.CARTAO_VERMELHO);
        reprocessarDisciplinaDoJogador(torneioId, partidaId, jogadorId);
        return eventoEstatistico;
    }

    public EventoEstatistico registrarSubstituicao(long eventoId,
                                                   TorneioId torneioId,
                                                   PartidaId partidaId,
                                                   UsuarioId organizadorId,
                                                   JogadorId jogadorSaiuId,
                                                   JogadorId jogadorEntrouId) {
        return registrarSubstituicao(eventoId, torneioId, partidaId, organizadorId, jogadorSaiuId,
                jogadorEntrouId, null, null);
    }

    public EventoEstatistico registrarSubstituicao(long eventoId,
                                                   TorneioId torneioId,
                                                   PartidaId partidaId,
                                                   UsuarioId organizadorId,
                                                   JogadorId jogadorSaiuId,
                                                   JogadorId jogadorEntrouId,
                                                   Integer ordemParadaJogo,
                                                   String descricaoParadaJogo) {
        validarRegistroBase(torneioId, partidaId, organizadorId);
        validarJogadorDaPartida(partidaId, jogadorSaiuId);
        validarJogadorDaPartida(partidaId, jogadorEntrouId);

        EventoEstatistico eventoEstatistico = new Substituicao(
                eventoId, torneioId, partidaId, jogadorSaiuId, jogadorEntrouId,
                ordemParadaJogo, descricaoParadaJogo);
        eventoEstatisticoRepositorio.salvar(eventoEstatistico);
        return eventoEstatistico;
    }

    public EventoEstatistico registrarEvento(long eventoId,
                                             TorneioId torneioId,
                                             PartidaId partidaId,
                                             UsuarioId organizadorId,
                                             JogadorId jogadorId,
                                             TipoEventoEstatistico tipoEventoEstatistico) {
        Objects.requireNonNull(tipoEventoEstatistico, "O tipo do evento estatistico e obrigatorio.");
        validarRegistro(torneioId, partidaId, organizadorId, jogadorId);

        EventoEstatistico eventoEstatistico = switch (tipoEventoEstatistico) {
            case GOL -> new Gol(eventoId, torneioId, partidaId, jogadorId);
            case ASSISTENCIA -> new Assistencia(eventoId, torneioId, partidaId, jogadorId);
            case CARTAO_AMARELO -> new CartaoAmarelo(eventoId, torneioId, partidaId, jogadorId);
            case CARTAO_VERMELHO -> new CartaoVermelho(eventoId, torneioId, partidaId, jogadorId);
            case SUBSTITUICAO -> throw new RegraDeNegocioException(
                    "Substituicao exige informar jogador que saiu e jogador que entrou.");
        };

        eventoEstatisticoRepositorio.salvar(eventoEstatistico);
        return eventoEstatistico;
    }

    public EventoEstatistico corrigirEvento(long eventoId,
                                             TorneioId torneioId,
                                             PartidaId partidaId,
                                             UsuarioId organizadorId,
                                             JogadorId jogadorId,
                                             TipoEventoEstatistico novoTipo) {
        EventoEstatistico eventoExistente = obterEventoDoScout(eventoId, torneioId, partidaId);
        validarRegistro(torneioId, partidaId, organizadorId, jogadorId);
        eventoEstatisticoRepositorio.remover(eventoExistente.getId());
        EventoEstatistico corrigido = registrarEvento(eventoExistente.getId(), torneioId, partidaId, organizadorId,
                jogadorId, novoTipo);
        reprocessarEmCascata(torneioId, partidaId, eventoExistente, corrigido);
        return corrigido;
    }

    public EventoEstatistico corrigirSubstituicao(long eventoId,
                                                  TorneioId torneioId,
                                                  PartidaId partidaId,
                                                  UsuarioId organizadorId,
                                                  JogadorId jogadorSaiuId,
                                                  JogadorId jogadorEntrouId,
                                                  Integer ordemParadaJogo,
                                                  String descricaoParadaJogo) {
        EventoEstatistico eventoExistente = obterEventoDoScout(eventoId, torneioId, partidaId);
        eventoEstatisticoRepositorio.remover(eventoExistente.getId());
        EventoEstatistico corrigido = registrarSubstituicao(
                eventoId,
                torneioId,
                partidaId,
                organizadorId,
                jogadorSaiuId,
                jogadorEntrouId,
                ordemParadaJogo,
                descricaoParadaJogo);
        reprocessarEmCascata(torneioId, partidaId, eventoExistente, corrigido);
        return corrigido;
    }

    public void removerEvento(long eventoId,
                              TorneioId torneioId,
                              PartidaId partidaId,
                              UsuarioId organizadorId) {
        validarRegistroBase(torneioId, partidaId, organizadorId);
        EventoEstatistico evento = obterEventoDoScout(eventoId, torneioId, partidaId);
        eventoEstatisticoRepositorio.remover(eventoId);
        reprocessarEmCascata(torneioId, partidaId, evento, null);
    }

    public ControleDisciplinarJogador consultarControleDisciplinar(TorneioId torneioId,
                                                                   PartidaId partidaId,
                                                                   JogadorId jogadorId) {
        Objects.requireNonNull(torneioId, "O torneio do controle disciplinar e obrigatorio.");
        Objects.requireNonNull(partidaId, "A partida do controle disciplinar e obrigatoria.");
        Objects.requireNonNull(jogadorId, "O jogador do controle disciplinar e obrigatorio.");

        List<EventoEstatistico> eventosDoJogadorNoTorneio = eventoEstatisticoRepositorio
                .listarPorJogadorNoTorneio(jogadorId, torneioId);
        List<EventoEstatistico> eventosDaPartida = eventosDoJogadorNoTorneio.stream()
                .filter(evento -> evento.getPartidaId().equals(partidaId))
                .toList();
        int amarelosNoTorneio = contarPorTipo(eventosDoJogadorNoTorneio, TipoEventoEstatistico.CARTAO_AMARELO);
        int amarelosNaPartida = contarPorTipo(eventosDaPartida, TipoEventoEstatistico.CARTAO_AMARELO);
        int vermelhosNoTorneio = contarPorTipo(eventosDoJogadorNoTorneio, TipoEventoEstatistico.CARTAO_VERMELHO);
        boolean expulsoAutomaticamenteNaPartida = eventosDaPartida.stream()
                .anyMatch(evento -> evento.getTipo() == TipoEventoEstatistico.CARTAO_VERMELHO && evento.isAutomatico());
        boolean suspensaoAutomaticaPendente = amarelosNoTorneio > 0
                && amarelosNoTorneio % LIMITE_SUSPENSAO_POR_ACUMULO == 0;
        List<Long> eventosAutomaticos = eventosDoJogadorNoTorneio.stream()
                .filter(EventoEstatistico::isAutomatico)
                .map(EventoEstatistico::getId)
                .toList();

        return new ControleDisciplinarJogador(
                torneioId,
                partidaId,
                jogadorId,
                amarelosNoTorneio,
                amarelosNaPartida,
                vermelhosNoTorneio,
                expulsoAutomaticamenteNaPartida,
                suspensaoAutomaticaPendente,
                eventosAutomaticos);
    }

    private void validarRegistro(TorneioId torneioId,
                                 PartidaId partidaId,
                                 UsuarioId organizadorId,
                                 JogadorId jogadorId) {
        validarRegistroBase(torneioId, partidaId, organizadorId);
        validarJogadorDaPartida(partidaId, jogadorId);
    }

    private void validarJogadorDaPartida(PartidaId partidaId, JogadorId jogadorId) {
        if (!consultaEstatisticaCompeticao.jogadorPertenceAosTimesDaPartida(partidaId, jogadorId)) {
            throw new RegraDeNegocioException(
                    "Nao e permitido registrar evento para jogador que nao pertence aos times da partida.");
        }
    }

    private void validarRegistroBase(TorneioId torneioId,
                                     PartidaId partidaId,
                                     UsuarioId organizadorId) {
        if (!consultaEstatisticaCompeticao.usuarioEhOrganizador(torneioId, organizadorId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o organizador do torneio pode registrar eventos estatisticos.");
        }
        if (!consultaEstatisticaCompeticao.partidaPertenceAoTorneio(partidaId, torneioId)) {
            throw new RegraDeNegocioException("A partida informada nao pertence ao torneio.");
        }
    }

    private EventoEstatistico obterEventoDoScout(long eventoId, TorneioId torneioId, PartidaId partidaId) {
        EventoEstatistico evento = eventoEstatisticoRepositorio.buscarPorId(eventoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Evento estatistico nao encontrado."));
        if (!evento.getTorneioId().equals(torneioId) || !evento.getPartidaId().equals(partidaId)) {
            throw new RegraDeNegocioException("O evento informado nao pertence ao scout estatistico da partida.");
        }
        return evento;
    }

    private void reprocessarEmCascata(TorneioId torneioId,
                                      PartidaId partidaId,
                                      EventoEstatistico eventoAnterior,
                                      EventoEstatistico eventoNovo) {
        Set<JogadorId> jogadoresAfetados = new LinkedHashSet<>();
        jogadoresAfetados.addAll(jogadoresAfetados(eventoAnterior));
        jogadoresAfetados.addAll(jogadoresAfetados(eventoNovo));
        for (JogadorId jogadorAfetado : jogadoresAfetados) {
            reprocessarDisciplinaDoJogador(torneioId, partidaId, jogadorAfetado);
        }
    }

    private Set<JogadorId> jogadoresAfetados(EventoEstatistico eventoEstatistico) {
        if (eventoEstatistico == null) {
            return Set.of();
        }
        Set<JogadorId> jogadores = new LinkedHashSet<>();
        jogadores.add(eventoEstatistico.getJogadorId());
        if (eventoEstatistico instanceof Substituicao substituicao) {
            jogadores.add(substituicao.getJogadorSaiuId());
            jogadores.add(substituicao.getJogadorEntrouId());
        }
        return jogadores;
    }

    private void reprocessarDisciplinaDoJogador(TorneioId torneioId, PartidaId partidaId, JogadorId jogadorId) {
        List<EventoEstatistico> eventosDaPartida = eventoEstatisticoRepositorio.listarPorPartida(partidaId).stream()
                .filter(evento -> evento.getJogadorId().equals(jogadorId))
                .sorted(java.util.Comparator.comparingLong(EventoEstatistico::getId))
                .toList();

        List<EventoEstatistico> automaticos = eventosDaPartida.stream()
                .filter(EventoEstatistico::isAutomatico)
                .toList();
        for (EventoEstatistico automatico : automaticos) {
            eventoEstatisticoRepositorio.remover(automatico.getId());
        }

        List<EventoEstatistico> amarelos = eventosDaPartida.stream()
                .filter(evento -> evento.getTipo() == TipoEventoEstatistico.CARTAO_AMARELO)
                .toList();
        boolean possuiVermelhoManual = eventosDaPartida.stream()
                .anyMatch(evento -> evento.getTipo() == TipoEventoEstatistico.CARTAO_VERMELHO && !evento.isAutomatico());

        if (amarelos.size() >= LIMITE_EXPULSAO_POR_AMARELOS && !possuiVermelhoManual) {
            EventoEstatistico segundoAmarelo = amarelos.get(LIMITE_EXPULSAO_POR_AMARELOS - 1);
            long eventoAutomaticoId = gerarIdEventoAutomatico(segundoAmarelo.getId());
            eventoEstatisticoRepositorio.salvar(CartaoVermelho.automaticoPorSegundoAmarelo(
                    eventoAutomaticoId,
                    torneioId,
                    partidaId,
                    jogadorId,
                    segundoAmarelo.getId()));
        }
    }

    private int contarPorTipo(List<EventoEstatistico> eventos, TipoEventoEstatistico tipoEventoEstatistico) {
        return (int) eventos.stream()
                .filter(evento -> evento.getTipo() == tipoEventoEstatistico)
                .count();
    }

    private long gerarIdEventoAutomatico(long eventoOrigemId) {
        return eventoOrigemId * 1_000L + 901L;
    }
}
