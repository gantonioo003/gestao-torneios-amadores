package com.torneios.aplicacao.estatisticas.ranking;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.artilharia.ArtilhariaServico;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaCarreiraJogador;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaJogador;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaServico;
import com.torneios.dominio.estatisticas.desempenho.HistoricoEstatisticoTorneio;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;
import com.torneios.dominio.estatisticas.nota.NotaMediaJogador;
import com.torneios.dominio.estatisticas.nota.NotaEstatistica;

/**
 * Casos de uso de ranking, nota, historico e consolidacao estatistica.
 */
public class RankingServicoAplicacao {

    private final EstatisticaServico estatisticaServico;
    private final ArtilhariaServico artilhariaServico;

    public RankingServicoAplicacao(EstatisticaServico estatisticaServico,
                                   ArtilhariaServico artilhariaServico) {
        notNull(estatisticaServico, "O servico de estatistica e obrigatorio.");
        notNull(artilhariaServico, "O servico de artilharia e obrigatorio.");
        this.estatisticaServico = estatisticaServico;
        this.artilhariaServico = artilhariaServico;
    }

    public List<EstatisticaJogadorResumo> gerarRankingArtilharia(long torneioId) {
        return artilhariaServico.gerarRanking(new TorneioId(torneioId)).stream()
                .map(this::converterEstatistica)
                .toList();
    }

    public List<EstatisticaJogadorResumo> listarEstatisticasJogadores(long torneioId) {
        return estatisticaServico.listarEstatisticasJogadores(new TorneioId(torneioId)).stream()
                .map(this::converterEstatistica)
                .toList();
    }

    public List<EstatisticaJogadorResumo> listarLideresAssistencias(long torneioId) {
        return estatisticaServico.listarLideresAssistencias(new TorneioId(torneioId)).stream()
                .map(this::converterEstatistica)
                .toList();
    }

    public NotaEstatisticaResumo calcularNotaJogador(long torneioId, long partidaId, long jogadorId) {
        return estatisticaServico.calcularNotaJogador(
                        new TorneioId(torneioId),
                        new PartidaId(partidaId),
                        new JogadorId(jogadorId))
                .map(this::converterNota)
                .orElse(null);
    }

    public EstatisticaJogadorResumo obterEstatisticaJogador(long torneioId, long jogadorId) {
        return estatisticaServico.obterEstatisticaJogador(new TorneioId(torneioId), new JogadorId(jogadorId))
                .map(this::converterEstatistica)
                .orElse(null);
    }

    public List<EventoScoutResumo> obterHistoricoJogador(long torneioId, long jogadorId) {
        return estatisticaServico.obterHistoricoJogador(new TorneioId(torneioId), new JogadorId(jogadorId)).stream()
                .map(this::converterEvento)
                .toList();
    }

    public HistoricoEdicaoEstatisticaResumo arquivarEstatisticasDaEdicao(long torneioId, int numeroEdicao) {
        return converterHistorico(estatisticaServico.arquivarEstatisticasDaEdicao(new TorneioId(torneioId), numeroEdicao));
    }

    public HistoricoEdicaoEstatisticaResumo recalcularEdicaoFechada(long torneioId, int numeroEdicao) {
        return converterHistorico(estatisticaServico.recalcularEdicaoFechada(new TorneioId(torneioId), numeroEdicao));
    }

    public List<NotaMediaResumo> listarMelhoresMediasDeNota(long torneioId, int minimoPartidas) {
        return estatisticaServico.listarMelhoresMediasDeNota(new TorneioId(torneioId), minimoPartidas).stream()
                .map(this::converterMedia)
                .toList();
    }

    public NotaMediaResumo obterMelhorNotaComElegibilidade(long torneioId, int minimoPartidas) {
        return estatisticaServico.obterMelhorNotaComElegibilidade(new TorneioId(torneioId), minimoPartidas)
                .map(this::converterMedia)
                .orElse(null);
    }

    public EstatisticaCarreiraResumo obterEstatisticaCarreiraJogador(long jogadorId) {
        return converterCarreira(estatisticaServico.obterEstatisticaCarreiraJogador(new JogadorId(jogadorId)));
    }

    private EstatisticaJogadorResumo converterEstatistica(EstatisticaJogador estatisticaJogador) {
        return new EstatisticaJogadorResumo(
                estatisticaJogador.getTorneioId().valor(),
                estatisticaJogador.getJogadorId().valor(),
                estatisticaJogador.getGols(),
                estatisticaJogador.getAssistencias(),
                estatisticaJogador.getCartoesAmarelos(),
                estatisticaJogador.getCartoesVermelhos());
    }

    private NotaEstatisticaResumo converterNota(NotaEstatistica notaEstatistica) {
        return new NotaEstatisticaResumo(
                notaEstatistica.torneioId().valor(),
                notaEstatistica.partidaId().valor(),
                notaEstatistica.jogadorId().valor(),
                notaEstatistica.valor());
    }

    private EventoScoutResumo converterEvento(EventoEstatistico eventoEstatistico) {
        return new EventoScoutResumo(
                eventoEstatistico.getId(),
                eventoEstatistico.getPartidaId().valor(),
                eventoEstatistico.getTipo().name());
    }

    private HistoricoEdicaoEstatisticaResumo converterHistorico(HistoricoEstatisticoTorneio historicoEstatisticoTorneio) {
        return new HistoricoEdicaoEstatisticaResumo(
                historicoEstatisticoTorneio.getTorneioId().valor(),
                historicoEstatisticoTorneio.getNumeroEdicao(),
                historicoEstatisticoTorneio.getEstatisticasArquivadas().stream()
                        .map(this::converterEstatistica)
                        .toList());
    }

    private NotaMediaResumo converterMedia(NotaMediaJogador notaMediaJogador) {
        return new NotaMediaResumo(
                notaMediaJogador.torneioId().valor(),
                notaMediaJogador.jogadorId().valor(),
                notaMediaJogador.media(),
                notaMediaJogador.partidasConsideradas());
    }

    private EstatisticaCarreiraResumo converterCarreira(EstatisticaCarreiraJogador estatisticaCarreiraJogador) {
        return new EstatisticaCarreiraResumo(
                estatisticaCarreiraJogador.jogadorId().valor(),
                estatisticaCarreiraJogador.gols(),
                estatisticaCarreiraJogador.assistencias(),
                estatisticaCarreiraJogador.cartoesAmarelos(),
                estatisticaCarreiraJogador.cartoesVermelhos(),
                estatisticaCarreiraJogador.torneiosComEventos());
    }

    public record EstatisticaJogadorResumo(long torneioId,
                                           long jogadorId,
                                           int gols,
                                           int assistencias,
                                           int cartoesAmarelos,
                                           int cartoesVermelhos) {
    }

    public record NotaEstatisticaResumo(long torneioId, long partidaId, long jogadorId, double valor) {
    }

    public record EventoScoutResumo(long id, long partidaId, String tipo) {
    }

    public record HistoricoEdicaoEstatisticaResumo(long torneioId,
                                                   int numeroEdicao,
                                                   List<EstatisticaJogadorResumo> estatisticasArquivadas) {
    }

    public record NotaMediaResumo(long torneioId, long jogadorId, double media, int partidasConsideradas) {
    }

    public record EstatisticaCarreiraResumo(long jogadorId,
                                            int gols,
                                            int assistencias,
                                            int cartoesAmarelos,
                                            int cartoesVermelhos,
                                            int torneiosComEventos) {
    }
}
