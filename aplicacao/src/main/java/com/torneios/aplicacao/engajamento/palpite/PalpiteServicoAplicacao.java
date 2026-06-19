package com.torneios.aplicacao.engajamento.palpite;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.torneios.dominio.compartilhado.enumeracao.StatusTorneio;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.engajamento.palpite.EventoAlvoPalpite;
import com.torneios.dominio.engajamento.palpite.OpcaoPalpite;
import com.torneios.dominio.engajamento.palpite.Palpite;
import com.torneios.dominio.engajamento.palpite.PalpiteId;
import com.torneios.dominio.engajamento.palpite.PalpiteRepositorio;
import com.torneios.dominio.engajamento.palpite.PalpiteServico;
import com.torneios.dominio.engajamento.palpite.PercentuaisPalpite;
import com.torneios.dominio.engajamento.palpite.TipoPalpite;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;
import com.torneios.dominio.participacao.profissional.TipoProfissional;
import com.torneios.dominio.participacao.time.TimeRepositorio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;

public class PalpiteServicoAplicacao {

    private final PalpiteServico palpiteServico;
    private final PalpiteRepositorio palpiteRepositorio;
    private final TorneioRepositorio torneioRepositorio;
    private final PartidaRepositorio partidaRepositorio;
    private final TimeRepositorio timeRepositorio;
    private final ProfissionalEsportivoRepositorio profissionalRepositorio;

    public PalpiteServicoAplicacao(PalpiteServico palpiteServico, PalpiteRepositorio palpiteRepositorio) {
        this(palpiteServico, palpiteRepositorio, null, null, null, null);
    }

    public PalpiteServicoAplicacao(PalpiteServico palpiteServico,
                                   PalpiteRepositorio palpiteRepositorio,
                                   TorneioRepositorio torneioRepositorio,
                                   PartidaRepositorio partidaRepositorio,
                                   TimeRepositorio timeRepositorio,
                                   ProfissionalEsportivoRepositorio profissionalRepositorio) {
        notNull(palpiteServico, "O servico de palpite e obrigatorio.");
        notNull(palpiteRepositorio, "O repositorio de palpite e obrigatorio.");
        this.palpiteServico = palpiteServico;
        this.palpiteRepositorio = palpiteRepositorio;
        this.torneioRepositorio = torneioRepositorio;
        this.partidaRepositorio = partidaRepositorio;
        this.timeRepositorio = timeRepositorio;
        this.profissionalRepositorio = profissionalRepositorio;
    }

    public PalpiteResumo registrarOuAtualizar(long palpiteId,
                                              long usuarioId,
                                              String tipo,
                                              long torneioId,
                                              Long partidaId,
                                              long opcao) {
        EventoAlvoPalpite eventoAlvo = criarEventoAlvo(tipo, torneioId, partidaId);
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
        EventoAlvoPalpite eventoAlvo = criarEventoAlvo(tipo, torneioId, partidaId);
        return converter(palpiteServico.registrarOuAtualizarComoVisitante(
                new PalpiteId(palpiteId),
                visitanteId,
                eventoAlvo,
                new OpcaoPalpite(opcao)));
    }

    public PercentuaisPalpiteResumo obterPercentuais(String tipo, long torneioId, Long partidaId) {
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

    public List<PalpiteResumo> listarPorUsuario(long usuarioId) {
        return palpiteRepositorio.listarPorUsuario(new UsuarioId(usuarioId)).stream()
                .map(this::converter)
                .toList();
    }

    public CentralPalpitesResumo listarOportunidades() {
        notNull(torneioRepositorio, "O repositorio de torneios e obrigatorio para listar oportunidades.");
        notNull(partidaRepositorio, "O repositorio de partidas e obrigatorio para listar oportunidades.");
        notNull(timeRepositorio, "O repositorio de times e obrigatorio para listar oportunidades.");
        notNull(profissionalRepositorio, "O repositorio de profissionais e obrigatorio para listar oportunidades.");

        var torneios = torneioRepositorio.listarTodos();
        var torneiosDisponiveis = torneios.stream()
                .filter(torneio -> torneio.getStatus() == StatusTorneio.CONFIGURADO
                        || torneio.getStatus() == StatusTorneio.ESTRUTURA_GERADA)
                .filter(torneio -> !torneio.getParticipantesAprovados().isEmpty())
                .map(torneio -> {
                    long torneioId = torneio.getId().valor();
                    List<OpcaoResumo> times = torneio.getParticipantesAprovados().stream()
                            .map(participante -> opcaoTime(participante.getTimeId().valor()))
                            .toList();
                    List<OpcaoResumo> jogadores = torneio.getParticipantesAprovados().stream()
                            .flatMap(participante -> timeRepositorio.buscarPorId(participante.getTimeId()).stream())
                            .flatMap(time -> time.getElenco().stream())
                            .filter(vinculo -> TipoProfissional.JOGADOR.name().equalsIgnoreCase(vinculo.getFuncao()))
                            .map(vinculo -> opcaoJogador(vinculo.getProfissionalId().valor()))
                            .distinct()
                            .toList();
                    var campeao = eventoTorneio(
                            TipoPalpite.CAMPEAO_TORNEIO, "Quem sera o campeao?", times, torneioId);
                    List<EventoTorneioPalpiteResumo> eventos = jogadores.isEmpty()
                            ? List.of(campeao)
                            : List.of(
                                    campeao,
                                    eventoTorneio(TipoPalpite.ARTILHEIRO_TORNEIO,
                                            "Quem sera o artilheiro?", jogadores, torneioId),
                                    eventoTorneio(TipoPalpite.LIDER_ASSISTENCIAS_TORNEIO,
                                            "Quem liderara em assistencias?", jogadores, torneioId));
                    return new TorneioPalpiteResumo(
                            torneioId,
                            torneio.getNome(),
                            torneio.getStatus().name(),
                            times,
                            campeao.percentuais(),
                            eventos);
                })
                .toList();

        var partidasDisponiveis = torneios.stream()
                .flatMap(torneio -> partidaRepositorio.listarPorTorneio(torneio.getId()).stream()
                        .filter(partida -> !partida.estaIniciada() && !partida.estaEncerrada())
                        .map(partida -> new PartidaPalpiteResumo(
                                partida.getId().valor(),
                                torneio.getId().valor(),
                                torneio.getNome(),
                                partida.getEtapa(),
                                opcaoTime(partida.getMandante().valor()),
                                new OpcaoResumo(OpcaoPalpite.EMPATE, "Empate"),
                                opcaoTime(partida.getVisitante().valor()),
                                obterPercentuais(
                                        TipoPalpite.VENCEDOR_PARTIDA.name(),
                                        torneio.getId().valor(),
                                        partida.getId().valor()))))
                .limit(12)
                .toList();

        return new CentralPalpitesResumo(torneiosDisponiveis, partidasDisponiveis);
    }

    private EventoAlvoPalpite criarEventoAlvo(String tipo, long torneioId, Long partidaId) {
        TipoPalpite tipoPalpite = TipoPalpite.valueOf(tipo);
        return switch (tipoPalpite) {
            case VENCEDOR_PARTIDA -> EventoAlvoPalpite.paraPartida(new TorneioId(torneioId), new PartidaId(partidaId));
            case CAMPEAO_TORNEIO -> EventoAlvoPalpite.paraCampeao(new TorneioId(torneioId));
            case ARTILHEIRO_TORNEIO -> EventoAlvoPalpite.paraArtilheiro(new TorneioId(torneioId));
            case LIDER_ASSISTENCIAS_TORNEIO -> EventoAlvoPalpite.paraLiderAssistencias(new TorneioId(torneioId));
        };
    }

    private OpcaoResumo opcaoTime(long timeId) {
        String nome = timeRepositorio.buscarPorId(new TimeId(timeId))
                .map(time -> time.getNome())
                .orElse("Time #" + timeId);
        return new OpcaoResumo(timeId, nome);
    }

    private OpcaoResumo opcaoJogador(long jogadorId) {
        String nome = profissionalRepositorio.buscarPorId(new ProfissionalEsportivoId(jogadorId))
                .map(profissional -> profissional.getNome())
                .orElse("Jogador #" + jogadorId);
        return new OpcaoResumo(jogadorId, nome);
    }

    private EventoTorneioPalpiteResumo eventoTorneio(TipoPalpite tipo,
                                                      String titulo,
                                                      List<OpcaoResumo> opcoes,
                                                      long torneioId) {
        return new EventoTorneioPalpiteResumo(
                tipo.name(),
                titulo,
                opcoes,
                obterPercentuais(tipo.name(), torneioId, null));
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

    private PercentuaisPalpiteResumo converterPercentuais(PercentuaisPalpite percentuaisPalpite) {
        Map<Long, Double> percentuais = new LinkedHashMap<>();
        percentuaisPalpite.getPercentuaisPorOpcao().forEach((opcaoPalpite, valorPercentual) ->
                percentuais.put(opcaoPalpite.valor(), valorPercentual));
        return new PercentuaisPalpiteResumo(
                percentuaisPalpite.getEventoAlvo().getTipo().name(),
                percentuaisPalpite.getEventoAlvo().getTorneioId().valor(),
                percentuaisPalpite.getEventoAlvo().getPartidaId() == null
                        ? null
                        : percentuaisPalpite.getEventoAlvo().getPartidaId().valor(),
                percentuaisPalpite.getTotalPalpites(),
                percentuais);
    }

    public record PalpiteResumo(Long id,
                                Long usuarioId,
                                String identificadorVotante,
                                String tipo,
                                Long torneioId,
                                Long partidaId,
                                Long opcao,
                                boolean apurado,
                                Boolean acertou) {
    }

    public record PercentuaisPalpiteResumo(String tipo,
                                           Long torneioId,
                                           Long partidaId,
                                           long totalPalpites,
                                           Map<Long, Double> percentuaisPorOpcao) {
    }

    public record CentralPalpitesResumo(List<TorneioPalpiteResumo> torneios,
                                        List<PartidaPalpiteResumo> partidas) {
    }

    public record TorneioPalpiteResumo(Long id,
                                       String nome,
                                       String status,
                                       List<OpcaoResumo> opcoes,
                                       PercentuaisPalpiteResumo percentuais,
                                       List<EventoTorneioPalpiteResumo> eventos) {
    }

    public record EventoTorneioPalpiteResumo(String tipo,
                                             String titulo,
                                             List<OpcaoResumo> opcoes,
                                             PercentuaisPalpiteResumo percentuais) {
    }

    public record PartidaPalpiteResumo(Long id,
                                       Long torneioId,
                                       String torneioNome,
                                       String etapa,
                                       OpcaoResumo mandante,
                                       OpcaoResumo empate,
                                       OpcaoResumo visitante,
                                       PercentuaisPalpiteResumo percentuais) {
    }

    public record OpcaoResumo(Long id, String nome) {
    }
}
