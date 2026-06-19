package com.torneios.aplicacao.torneio.preparacao;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.geracao.PreparacaoCompeticao;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.partida.PartidaServico;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;
import com.torneios.dominio.competicao.rodada.Rodada;
import com.torneios.dominio.torneio.estrutura.EstruturaCompeticao;
import com.torneios.dominio.torneio.estrutura.Grupo;
import com.torneios.dominio.torneio.torneio.HistoricoEdicaoTorneio;
import com.torneios.dominio.torneio.torneio.Torneio;
import com.torneios.dominio.torneio.torneio.TorneioServico;

/**
 * Casos de uso de criacao, configuracao e preparacao do torneio ate rodadas e partidas.
 */
public class PreparacaoTorneioServicoAplicacao {

    private final TorneioServico torneioServico;
    private final PartidaServico partidaServico;

    public PreparacaoTorneioServicoAplicacao(TorneioServico torneioServico,
                                             PartidaServico partidaServico) {
        notNull(torneioServico, "O servico de torneio e obrigatorio.");
        notNull(partidaServico, "O servico de partida e obrigatorio.");
        this.torneioServico = torneioServico;
        this.partidaServico = partidaServico;
    }

    public TorneioResumoAplicacao criarTorneio(long torneioId,
                                               String nome,
                                               String formato,
                                               String formatoEquipe,
                                               long organizadorId,
                                               boolean aceitaSolicitacoes,
                                               String imagemUrl) {
        return converter(torneioServico.criarTorneio(
                new TorneioId(torneioId),
                nome,
                FormatoTorneio.valueOf(formato),
                FormatoEquipe.valueOf(formatoEquipe),
                new UsuarioId(organizadorId),
                aceitaSolicitacoes,
                imagemUrl));
    }

    public TorneioResumoAplicacao definirParticipantesIniciais(long torneioId,
                                                               long organizadorId,
                                                               List<Long> timesIds) {
        torneioServico.definirParticipantesIniciais(
                new TorneioId(torneioId),
                new UsuarioId(organizadorId),
                timesIds.stream().map(TimeId::new).toList());
        return obterTorneio(torneioId);
    }

    public TorneioResumoAplicacao aprovarParticipante(long torneioId, long organizadorId, long timeId) {
        torneioServico.aprovarParticipante(new TorneioId(torneioId), new UsuarioId(organizadorId), new TimeId(timeId));
        return obterTorneio(torneioId);
    }

    public TorneioResumoAplicacao removerParticipante(long torneioId, long organizadorId, long timeId) {
        torneioServico.removerParticipante(new TorneioId(torneioId), new UsuarioId(organizadorId), new TimeId(timeId));
        return obterTorneio(torneioId);
    }

    public TorneioResumoAplicacao abrirSolicitacoes(long torneioId, long organizadorId) {
        torneioServico.abrirSolicitacoes(new TorneioId(torneioId), new UsuarioId(organizadorId));
        return obterTorneio(torneioId);
    }

    public TorneioResumoAplicacao fecharSolicitacoes(long torneioId, long organizadorId) {
        torneioServico.fecharSolicitacoes(new TorneioId(torneioId), new UsuarioId(organizadorId));
        return obterTorneio(torneioId);
    }

    public TorneioResumoAplicacao renomearTorneio(long torneioId, long organizadorId, String novoNome) {
        torneioServico.renomearTorneio(
                new TorneioId(torneioId),
                new UsuarioId(organizadorId),
                novoNome);
        return obterTorneio(torneioId);
    }

    public TorneioResumoAplicacao atualizarConfiguracao(long torneioId,
                                                        long organizadorId,
                                                        String nome,
                                                        boolean aceitaSolicitacoes,
                                                        String imagemUrl) {
        torneioServico.atualizarConfiguracao(
                new TorneioId(torneioId),
                new UsuarioId(organizadorId),
                nome,
                aceitaSolicitacoes);
        if (imagemUrl != null && !imagemUrl.isBlank()) {
            torneioServico.alterarImagem(
                    new TorneioId(torneioId),
                    new UsuarioId(organizadorId),
                    imagemUrl);
        }
        return obterTorneio(torneioId);
    }

    public EstruturaCompeticaoResumo gerarEstruturaPorSorteio(long torneioId, long organizadorId) {
        return converter(torneioServico.gerarEstruturaCompeticao(
                new TorneioId(torneioId),
                new UsuarioId(organizadorId)));
    }

    public EstruturaCompeticaoResumo gerarEstruturaManual(long torneioId,
                                                          long organizadorId,
                                                          List<Long> ordemManualParticipantes) {
        return converter(torneioServico.gerarEstruturaManual(
                new TorneioId(torneioId),
                new UsuarioId(organizadorId),
                ordemManualParticipantes.stream().map(TimeId::new).toList()));
    }

    public PreparacaoCompeticaoResumo prepararCompeticaoPorSorteio(long torneioId, long organizadorId) {
        Torneio torneio = torneioServico.obterTorneio(new TorneioId(torneioId));
        if (torneio.getStatus() == com.torneios.dominio.compartilhado.enumeracao.StatusTorneio.CONFIGURADO) {
            torneioServico.gerarEstruturaCompeticao(
                    new TorneioId(torneioId), new UsuarioId(organizadorId));
        }
        return converter(partidaServico.prepararCompeticaoPorSorteio(
                new TorneioId(torneioId),
                new UsuarioId(organizadorId)));
    }

    public PreparacaoCompeticaoResumo prepararCompeticaoManual(long torneioId,
                                                               long organizadorId,
                                                               List<Long> ordemManualParticipantes) {
        Torneio torneio = torneioServico.obterTorneio(new TorneioId(torneioId));
        List<TimeId> ordem = ordemManualParticipantes.stream().map(TimeId::new).toList();
        if (torneio.getStatus() == com.torneios.dominio.compartilhado.enumeracao.StatusTorneio.CONFIGURADO) {
            torneioServico.gerarEstruturaManual(
                    new TorneioId(torneioId), new UsuarioId(organizadorId), ordem);
        }
        return converter(partidaServico.prepararCompeticaoManual(
                new TorneioId(torneioId),
                new UsuarioId(organizadorId),
                ordem));
    }

    public TorneioResumoAplicacao iniciarTorneio(long torneioId, long organizadorId) {
        torneioServico.iniciarTorneio(new TorneioId(torneioId), new UsuarioId(organizadorId));
        return obterTorneio(torneioId);
    }

    public TorneioResumoAplicacao finalizarTorneio(long torneioId, long organizadorId) {
        torneioServico.finalizarTorneio(new TorneioId(torneioId), new UsuarioId(organizadorId));
        return obterTorneio(torneioId);
    }

    public HistoricoEdicaoResumo repetirTorneio(long torneioId, long organizadorId, boolean abrirSolicitacoes) {
        return converter(torneioServico.repetirTorneio(
                new TorneioId(torneioId),
                new UsuarioId(organizadorId),
                abrirSolicitacoes));
    }

    public TorneioResumoAplicacao obterTorneio(long torneioId) {
        return converter(torneioServico.obterTorneio(new TorneioId(torneioId)));
    }

    private TorneioResumoAplicacao converter(Torneio torneio) {
        return new TorneioResumoAplicacao(
                torneio.getId().valor(),
                torneio.getNome(),
                torneio.getImagemUrl(),
                torneio.getFormato().name(),
                torneio.getFormatoEquipe().name(),
                String.valueOf(torneio.getOrganizadorId().valor()),
                torneio.aceitaSolicitacoes(),
                torneio.getStatus().name(),
                torneio.getEdicaoAtual(),
                torneio.getParticipantesAprovados().stream()
                        .map(participante -> participante.getTimeId().valor())
                        .toList());
    }

    private EstruturaCompeticaoResumo converter(EstruturaCompeticao estruturaCompeticao) {
        return new EstruturaCompeticaoResumo(
                estruturaCompeticao.getTorneioId().valor(),
                estruturaCompeticao.getTipo().name(),
                estruturaCompeticao.getModoGeracao().name(),
                estruturaCompeticao.getEtapas(),
                estruturaCompeticao.getGrupos().stream()
                        .map(this::converterGrupo)
                        .toList());
    }

    private GrupoResumo converterGrupo(Grupo grupo) {
        return new GrupoResumo(grupo.getNome(), grupo.getParticipantes().stream().map(TimeId::valor).toList());
    }

    private PreparacaoCompeticaoResumo converter(PreparacaoCompeticao preparacaoCompeticao) {
        return new PreparacaoCompeticaoResumo(
                preparacaoCompeticao.getTorneioId().valor(),
                preparacaoCompeticao.getModoPreparacao().name(),
                preparacaoCompeticao.getPartidas().stream().map(this::converterPartida).toList(),
                preparacaoCompeticao.getRodadas().stream().map(this::converterRodada).toList());
    }

    private PartidaPreparadaResumo converterPartida(Partida partida) {
        ResultadoPartida resultado = partida.getResultado();
        return new PartidaPreparadaResumo(
                partida.getId().valor(),
                partida.getTorneioId().valor(),
                partida.getMandante().valor(),
                partida.getVisitante().valor(),
                partida.getEtapa(),
                partida.getQuantidadeJogadoresPorEquipe(),
                partida.estaEncerrada(),
                resultado == null ? null : resultado.golsMandante(),
                resultado == null ? null : resultado.golsVisitante());
    }

    private RodadaResumo converterRodada(Rodada rodada) {
        return new RodadaResumo(
                rodada.getTorneioId().valor(),
                rodada.getNumero(),
                rodada.getPartidas().stream().map(PartidaId::valor).toList());
    }

    private HistoricoEdicaoResumo converter(HistoricoEdicaoTorneio historicoEdicaoTorneio) {
        return new HistoricoEdicaoResumo(
                historicoEdicaoTorneio.getTorneioId().valor(),
                historicoEdicaoTorneio.getNumeroEdicao(),
                historicoEdicaoTorneio.getNomeTorneio(),
                historicoEdicaoTorneio.getParticipantes().stream().map(TimeId::valor).toList());
    }

    public record TorneioResumoAplicacao(long id,
                                         String nome,
                                         String imagemUrl,
                                         String formato,
                                         String formatoEquipe,
                                         String organizadorId,
                                         boolean aceitaSolicitacoes,
                                         String status,
                                         int edicaoAtual,
                                         List<Long> participantesAprovados) {
    }

    public record EstruturaCompeticaoResumo(long torneioId,
                                            String tipo,
                                            String modoGeracao,
                                            List<String> etapas,
                                            List<GrupoResumo> grupos) {
    }

    public record GrupoResumo(String nome, List<Long> participantes) {
    }

    public record PreparacaoCompeticaoResumo(long torneioId,
                                             String modoPreparacao,
                                             List<PartidaPreparadaResumo> partidas,
                                             List<RodadaResumo> rodadas) {
    }

    public record PartidaPreparadaResumo(long id,
                                         long torneioId,
                                         long mandanteId,
                                         long visitanteId,
                                         String etapa,
                                         int quantidadeJogadoresPorEquipe,
                                         boolean encerrada,
                                         Integer golsMandante,
                                         Integer golsVisitante) {
    }

    public record RodadaResumo(long torneioId, int numero, List<Long> partidas) {
    }

    public record HistoricoEdicaoResumo(long torneioId,
                                        int numeroEdicao,
                                        String nomeTorneio,
                                        List<Long> participantes) {
    }
}
