package com.torneios.dominio.estatisticas.comparacao;

import java.util.Objects;
import java.util.Optional;

public class ComparativoDesempenho {

    private final long id;
    private final TipoComparativoDesempenho tipo;
    private final DesempenhoComparado primeiro;
    private final DesempenhoComparado segundo;

    public ComparativoDesempenho(long id,
                                 TipoComparativoDesempenho tipo,
                                 DesempenhoComparado primeiro,
                                 DesempenhoComparado segundo) {
        if (id <= 0) {
            throw new IllegalArgumentException("O id do comparativo deve ser maior que zero.");
        }
        this.id = id;
        this.tipo = Objects.requireNonNull(tipo, "O tipo do comparativo e obrigatorio.");
        this.primeiro = Objects.requireNonNull(primeiro, "O primeiro desempenho comparado e obrigatorio.");
        this.segundo = Objects.requireNonNull(segundo, "O segundo desempenho comparado e obrigatorio.");
    }

    public long getId() {
        return id;
    }

    public TipoComparativoDesempenho getTipo() {
        return tipo;
    }

    public DesempenhoComparado getPrimeiro() {
        return primeiro;
    }

    public DesempenhoComparado getSegundo() {
        return segundo;
    }

    public Optional<DesempenhoComparado> getMelhorDesempenho() {
        int comparacao = Double.compare(primeiro.getPontuacaoComparativa(), segundo.getPontuacaoComparativa());
        if (comparacao == 0) {
            return Optional.empty();
        }
        return Optional.of(comparacao > 0 ? primeiro : segundo);
    }
}
