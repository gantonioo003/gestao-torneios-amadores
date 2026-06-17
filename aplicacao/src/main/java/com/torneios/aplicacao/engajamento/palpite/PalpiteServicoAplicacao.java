package com.torneios.aplicacao.engajamento.palpite;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.palpite.EventoAlvo;
import com.torneios.dominio.engajamento.palpite.OpcaoPalpite;
import com.torneios.dominio.engajamento.palpite.Palpite;
import com.torneios.dominio.engajamento.palpite.PalpiteId;
import com.torneios.dominio.engajamento.palpite.PalpiteRepositorio;
import com.torneios.dominio.engajamento.palpite.PalpiteServico;
import com.torneios.dominio.engajamento.palpite.PercentuaisPalpite;
import com.torneios.dominio.engajamento.palpite.TipoPalpite;

/**
 * Casos de uso de palpites por usuario autenticado ou visitante.
 */
public class PalpiteServicoAplicacao {

    private final PalpiteServico palpiteServico;
    private final PalpiteRepositorio palpiteRepositorio;

    public PalpiteServicoAplicacao(PalpiteServico palpiteServico, PalpiteRepositorio palpiteRepositorio) {
        notNull(palpiteServico, "O servico de palpite e obrigatorio.");
        notNull(palpiteRepositorio, "O repositorio de palpite e obrigatorio.");
        this.palpiteServico = palpiteServico;
        this.palpiteRepositorio = palpiteRepositorio;
    }

    public PalpiteResumo registrarOuAtualizar(long palpiteId,
                                              long usuarioId,
                                              String tipo,
                                              long torneioId,
                                              Long partidaId,
                                              long opcao) {
        EventoAlvo eventoAlvo = criarEventoAlvo(tipo, torneioId, partidaId);
        return converter(palpiteServico.registrarOuAtualizar(
                new PalpiteId(palpiteId),
                new UsuarioId(usuarioId),
                eventoAlvo,
                new OpcaoPalpite(opcao)));
    }

    public PalpiteResumo registrarOuAtualizarComoVisitante(long palpiteId,
                                                           String visitanteId,
                                                           String tipo,
                                                           long torneioId,
                                                           Long partidaId,
                                                           long opcao) {
        EventoAlvo eventoAlvo = criarEventoAlvo(tipo, torneioId, partidaId);
        return converter(palpiteServico.registrarOuAtualizarComoVisitante(
                new PalpiteId(palpiteId),
                visitanteId,
                eventoAlvo,
                new OpcaoPalpite(opcao)));
    }

    public PercentuaisResumo obterPercentuais(String tipo, long torneioId, Long partidaId) {
        return converterPercentuais(palpiteServico.obterPercentuais(criarEventoAlvo(tipo, torneioId, partidaId)));
    }

    public List<PalpiteResumo> apurar(String tipo, long torneioId, Long partidaId, long resultadoReal) {
        return palpiteServico.apurar(criarEventoAlvo(tipo, torneioId, partidaId), resultadoReal).stream()
                .map(this::converter)
                .toList();
    }

    public List<PalpiteResumo> listarPorEvento(String tipo, long torneioId, Long partidaId) {
        return palpiteRepositorio.listarPorEvento(criarEventoAlvo(tipo, torneioId, partidaId)).stream()
                .map(this::converter)
                .toList();
    }

    private EventoAlvo criarEventoAlvo(String tipo, long torneioId, Long partidaId) {
        TipoPalpite tipoPalpite = TipoPalpite.valueOf(tipo);
        return switch (tipoPalpite) {
            case VENCEDOR_PARTIDA -> EventoAlvo.paraPartida(new TorneioId(torneioId), new PartidaId(partidaId));
            case CAMPEAO_TORNEIO -> EventoAlvo.paraCampeao(new TorneioId(torneioId));
            case ARTILHEIRO_TORNEIO -> EventoAlvo.paraArtilheiro(new TorneioId(torneioId));
            case LIDER_ASSISTENCIAS_TORNEIO -> EventoAlvo.paraLiderAssistencias(new TorneioId(torneioId));
        };
    }

    private PalpiteResumo converter(Palpite palpite) {
        return new PalpiteResumo(
                palpite.getId().valor(),
                palpite.getUsuarioId() == null ? null : palpite.getUsuarioId().valor(),
                palpite.getIdentificadorVotante(),
                palpite.getEventoAlvo().getTipo().name(),
                palpite.getEventoAlvo().getTorneioId().valor(),
                palpite.getEventoAlvo().getPartidaId() == null ? null : palpite.getEventoAlvo().getPartidaId().valor(),
                palpite.getOpcao().valor(),
                palpite.estaApurado(),
                palpite.acertou().orElse(null));
    }

    private PercentuaisResumo converterPercentuais(PercentuaisPalpite percentuaisPalpite) {
        Map<Long, Double> percentuais = new LinkedHashMap<>();
        percentuaisPalpite.getPercentuaisPorOpcao().forEach((opcaoPalpite, valorPercentual) ->
                percentuais.put(opcaoPalpite.valor(), valorPercentual));
        return new PercentuaisResumo(
                percentuaisPalpite.getEventoAlvo().getTipo().name(),
                percentuaisPalpite.getEventoAlvo().getTorneioId().valor(),
                percentuaisPalpite.getEventoAlvo().getPartidaId() == null
                        ? null
                        : percentuaisPalpite.getEventoAlvo().getPartidaId().valor(),
                percentuaisPalpite.getTotalPalpites(),
                percentuais);
    }

    public record PalpiteResumo(long id,
                                Long usuarioId,
                                String identificadorVotante,
                                String tipo,
                                long torneioId,
                                Long partidaId,
                                long opcao,
                                boolean apurado,
                                Boolean acertou) {
    }

    public record PercentuaisResumo(String tipo,
                                    long torneioId,
                                    Long partidaId,
                                    long totalPalpites,
                                    Map<Long, Double> percentuaisPorOpcao) {
    }
}
