package com.torneios.aplicacao.estatisticas.comparacao;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.estatisticas.comparacao.ComparacaoDesempenhoServico;
import com.torneios.dominio.estatisticas.comparacao.ComparativoDesempenho;
import com.torneios.dominio.estatisticas.comparacao.DesempenhoComparado;

/**
 * Casos de uso para comparativos salvos e gerados entre jogadores e times.
 */
public class ComparativoDesempenhoServicoAplicacao {

    private final ComparacaoDesempenhoServico comparacaoDesempenhoServico;

    public ComparativoDesempenhoServicoAplicacao(ComparacaoDesempenhoServico comparacaoDesempenhoServico) {
        notNull(comparacaoDesempenhoServico, "O servico de comparacao e obrigatorio.");
        this.comparacaoDesempenhoServico = comparacaoDesempenhoServico;
    }

    public ComparativoResumo gerarComparativoJogadores(long comparativoId,
                                                       long torneioId,
                                                       long primeiroJogadorId,
                                                       long segundoJogadorId) {
        return converter(comparacaoDesempenhoServico.gerarComparativoJogadores(
                comparativoId,
                new TorneioId(torneioId),
                new JogadorId(primeiroJogadorId),
                new JogadorId(segundoJogadorId)));
    }

    public ComparativoResumo gerarComparativoTimes(long comparativoId,
                                                   long torneioId,
                                                   long primeiroTimeId,
                                                   long segundoTimeId) {
        return converter(comparacaoDesempenhoServico.gerarComparativoTimes(
                comparativoId,
                new TorneioId(torneioId),
                new TimeId(primeiroTimeId),
                new TimeId(segundoTimeId)));
    }

    public void salvarComparativo(long comparativoId,
                                  long torneioId,
                                  long primeiroTimeId,
                                  long segundoTimeId) {
        comparacaoDesempenhoServico.salvarComparativo(comparacaoDesempenhoServico.gerarComparativoTimes(
                comparativoId,
                new TorneioId(torneioId),
                new TimeId(primeiroTimeId),
                new TimeId(segundoTimeId)));
    }

    public List<ComparativoResumo> consultarComparativosSalvos(long torneioId) {
        return comparacaoDesempenhoServico.consultarComparativosSalvos(new TorneioId(torneioId)).stream()
                .map(this::converter)
                .toList();
    }

    public ComparativoResumo atualizarComparativoSalvo(ComparativoResumo comparativoResumo) {
        notNull(comparativoResumo, "O comparativo atualizado e obrigatorio.");
        ComparativoDesempenho comparativo = new ComparativoDesempenho(
                comparativoResumo.id(),
                new TorneioId(comparativoResumo.torneioId()),
                com.torneios.dominio.estatisticas.comparacao.TipoComparativoDesempenho.valueOf(comparativoResumo.tipo()),
                new DesempenhoComparado(
                        comparativoResumo.primeiro().rotulo(),
                        comparativoResumo.primeiro().gols(),
                        comparativoResumo.primeiro().assistencias(),
                        comparativoResumo.primeiro().cartoesAmarelos(),
                        comparativoResumo.primeiro().cartoesVermelhos(),
                        comparativoResumo.primeiro().partidasComEventos(),
                        comparativoResumo.primeiro().posicaoRanking(),
                        comparativoResumo.primeiro().pontuacaoComparativa()),
                new DesempenhoComparado(
                        comparativoResumo.segundo().rotulo(),
                        comparativoResumo.segundo().gols(),
                        comparativoResumo.segundo().assistencias(),
                        comparativoResumo.segundo().cartoesAmarelos(),
                        comparativoResumo.segundo().cartoesVermelhos(),
                        comparativoResumo.segundo().partidasComEventos(),
                        comparativoResumo.segundo().posicaoRanking(),
                        comparativoResumo.segundo().pontuacaoComparativa()));
        return converter(comparacaoDesempenhoServico.atualizarComparativoSalvo(comparativo));
    }

    public void excluirComparativoSalvo(long comparativoId) {
        comparacaoDesempenhoServico.excluirComparativoSalvo(comparativoId);
    }

    private ComparativoResumo converter(ComparativoDesempenho comparativoDesempenho) {
        Long melhorId = comparativoDesempenho.getMelhorDesempenho().isPresent()
                && comparativoDesempenho.getMelhorDesempenho().get().equals(comparativoDesempenho.getPrimeiro())
                ? 1L
                : comparativoDesempenho.getMelhorDesempenho().isPresent() ? 2L : null;
        return new ComparativoResumo(
                comparativoDesempenho.getId(),
                comparativoDesempenho.getTorneioId().valor(),
                comparativoDesempenho.getTipo().name(),
                converterDesempenho(comparativoDesempenho.getPrimeiro()),
                converterDesempenho(comparativoDesempenho.getSegundo()),
                melhorId);
    }

    private DesempenhoResumo converterDesempenho(DesempenhoComparado desempenhoComparado) {
        return new DesempenhoResumo(
                desempenhoComparado.getRotulo(),
                desempenhoComparado.getGols(),
                desempenhoComparado.getAssistencias(),
                desempenhoComparado.getCartoesAmarelos(),
                desempenhoComparado.getCartoesVermelhos(),
                desempenhoComparado.getPartidasComEventos(),
                desempenhoComparado.getPosicaoRanking(),
                desempenhoComparado.getPontuacaoComparativa());
    }

    public record ComparativoResumo(long id,
                                    long torneioId,
                                    String tipo,
                                    DesempenhoResumo primeiro,
                                    DesempenhoResumo segundo,
                                    Long melhorLado) {
    }

    public record DesempenhoResumo(String rotulo,
                                   int gols,
                                   int assistencias,
                                   int cartoesAmarelos,
                                   int cartoesVermelhos,
                                   int partidasComEventos,
                                   int posicaoRanking,
                                   double pontuacaoComparativa) {
    }
}
