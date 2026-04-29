package com.torneios.dominio.estatisticas.evento;

import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
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

    public EventoEstatistico registrarEvento(long eventoId,
                                             TorneioId torneioId,
                                             PartidaId partidaId,
                                             UsuarioId organizadorId,
                                             JogadorId jogadorId,
                                             TipoEventoEstatistico tipoEventoEstatistico) {
        if (tipoEventoEstatistico == TipoEventoEstatistico.SUBSTITUICAO) {
            throw new OperacaoNaoPermitidaException(
                    "Substituicoes devem ser registradas pelo metodo registrarSubstituicao.");
        }
        validarRegistro(torneioId, partidaId, organizadorId, jogadorId);

        EventoEstatistico eventoEstatistico = switch (tipoEventoEstatistico) {
            case GOL -> new Gol(eventoId, torneioId, partidaId, jogadorId);
            case ASSISTENCIA -> new Assistencia(eventoId, torneioId, partidaId, jogadorId);
            case CARTAO_AMARELO -> new CartaoAmarelo(eventoId, torneioId, partidaId, jogadorId);
            case CARTAO_VERMELHO -> new CartaoVermelho(eventoId, torneioId, partidaId, jogadorId);
            case SUBSTITUICAO -> throw new OperacaoNaoPermitidaException(
                    "Substituicoes devem ser registradas pelo metodo registrarSubstituicao.");
        };

        eventoEstatisticoRepositorio.salvar(eventoEstatistico);
        return eventoEstatistico;
    }

    public Substituicao registrarSubstituicao(long eventoId,
                                              TorneioId torneioId,
                                              PartidaId partidaId,
                                              UsuarioId organizadorId,
                                              JogadorId jogadorQueSaiu,
                                              JogadorId jogadorQueEntrou) {
        validarRegistroBase(torneioId, partidaId, organizadorId);
        if (!consultaEstatisticaCompeticao.partidaEncerrada(partidaId)) {
            throw new OperacaoNaoPermitidaException(
                    "Substituicoes so podem ser registradas apos o termino da partida.");
        }
        if (!consultaEstatisticaCompeticao.jogadorEhTitularNaEscalacao(partidaId, jogadorQueSaiu)) {
            throw new RegraDeNegocioException(
                    "O jogador que saiu deve ser titular escalado da partida.");
        }
        if (!consultaEstatisticaCompeticao.jogadorEhReservaNaEscalacao(partidaId, jogadorQueEntrou)) {
            throw new RegraDeNegocioException(
                    "O jogador que entrou deve ser reserva escalado da partida.");
        }
        if (!consultaEstatisticaCompeticao.jogadoresPertencemAoMesmoTimeNaPartida(
                partidaId, jogadorQueSaiu, jogadorQueEntrou)) {
            throw new RegraDeNegocioException(
                    "A substituicao deve envolver dois jogadores do mesmo time.");
        }

        Substituicao substituicao = new Substituicao(eventoId, torneioId, partidaId,
                jogadorQueSaiu, jogadorQueEntrou);
        eventoEstatisticoRepositorio.salvar(substituicao);
        return substituicao;
    }

    private void validarRegistro(TorneioId torneioId,
                                 PartidaId partidaId,
                                 UsuarioId organizadorId,
                                 JogadorId jogadorId) {
        validarRegistroBase(torneioId, partidaId, organizadorId);
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
}
