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
        validarRegistro(torneioId, partidaId, organizadorId, jogadorId);

        EventoEstatistico eventoEstatistico = switch (tipoEventoEstatistico) {
            case GOL -> new Gol(eventoId, torneioId, partidaId, jogadorId);
            case ASSISTENCIA -> new Assistencia(eventoId, torneioId, partidaId, jogadorId);
            case CARTAO_AMARELO -> new CartaoAmarelo(eventoId, torneioId, partidaId, jogadorId);
            case CARTAO_VERMELHO -> new CartaoVermelho(eventoId, torneioId, partidaId, jogadorId);
        };

        eventoEstatisticoRepositorio.salvar(eventoEstatistico);
        return eventoEstatistico;
    }

    private void validarRegistro(TorneioId torneioId,
                                 PartidaId partidaId,
                                 UsuarioId organizadorId,
                                 JogadorId jogadorId) {
        if (!consultaEstatisticaCompeticao.usuarioEhOrganizador(torneioId, organizadorId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o organizador do torneio pode registrar eventos estatisticos.");
        }
        if (!consultaEstatisticaCompeticao.partidaPertenceAoTorneio(partidaId, torneioId)) {
            throw new RegraDeNegocioException("A partida informada nao pertence ao torneio.");
        }
        if (!consultaEstatisticaCompeticao.jogadorPertenceAosTimesDaPartida(partidaId, jogadorId)) {
            throw new RegraDeNegocioException(
                    "Nao e permitido registrar evento para jogador que nao pertence aos times da partida.");
        }
    }
}
