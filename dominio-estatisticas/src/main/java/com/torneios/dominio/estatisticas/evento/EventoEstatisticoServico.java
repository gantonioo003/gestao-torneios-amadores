package com.torneios.dominio.estatisticas.evento;

import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class EventoEstatisticoServico {

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
        return registrarEvento(eventoId, torneioId, partidaId, organizadorId, jogadorId,
                TipoEventoEstatistico.CARTAO_AMARELO);
    }

    public EventoEstatistico registrarCartaoVermelho(long eventoId,
                                                     TorneioId torneioId,
                                                     PartidaId partidaId,
                                                     UsuarioId organizadorId,
                                                     JogadorId jogadorId) {
        return registrarEvento(eventoId, torneioId, partidaId, organizadorId, jogadorId,
                TipoEventoEstatistico.CARTAO_VERMELHO);
    }

    public EventoEstatistico registrarSubstituicao(long eventoId,
                                                   TorneioId torneioId,
                                                   PartidaId partidaId,
                                                   UsuarioId organizadorId,
                                                   JogadorId jogadorSaiuId,
                                                   JogadorId jogadorEntrouId) {
        validarRegistroBase(torneioId, partidaId, organizadorId);
        validarJogadorDaPartida(partidaId, jogadorSaiuId);
        validarJogadorDaPartida(partidaId, jogadorEntrouId);

        EventoEstatistico eventoEstatistico = new Substituicao(
                eventoId, torneioId, partidaId, jogadorSaiuId, jogadorEntrouId);
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
        return registrarEvento(eventoExistente.getId(), torneioId, partidaId, organizadorId, jogadorId, novoTipo);
    }

    public void removerEvento(long eventoId,
                              TorneioId torneioId,
                              PartidaId partidaId,
                              UsuarioId organizadorId) {
        validarRegistroBase(torneioId, partidaId, organizadorId);
        obterEventoDoScout(eventoId, torneioId, partidaId);
        eventoEstatisticoRepositorio.remover(eventoId);
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
}
