package com.torneios.dominio.compartilhado.enumeracao;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public enum EsquemaTatico {

    UM_UM(FormatoEquipe.TRES_POR_TRES, "1-1",
            mapa(1, 1, 0, 1)),
    DOIS_UM(FormatoEquipe.TRES_POR_TRES, "2-1",
            mapa(0, 2, 0, 1)),

    UM_DOIS_UM(FormatoEquipe.CINCO_POR_CINCO, "1-2-1",
            mapa(1, 2, 1, 1)),
    DOIS_UM_UM(FormatoEquipe.CINCO_POR_CINCO, "2-1-1",
            mapa(1, 2, 1, 1)),
    DOIS_DOIS(FormatoEquipe.CINCO_POR_CINCO, "2-2",
            mapa(1, 2, 0, 2)),

    DOIS_TRES_UM(FormatoEquipe.SETE_POR_SETE, "2-3-1",
            mapa(1, 2, 3, 1)),
    TRES_DOIS_UM(FormatoEquipe.SETE_POR_SETE, "3-2-1",
            mapa(1, 3, 2, 1)),
    TRES_UM_DOIS(FormatoEquipe.SETE_POR_SETE, "3-1-2",
            mapa(1, 3, 1, 2)),

    QUATRO_QUATRO_DOIS(FormatoEquipe.ONZE_POR_ONZE, "4-4-2",
            mapa(1, 4, 4, 2)),
    QUATRO_TRES_TRES(FormatoEquipe.ONZE_POR_ONZE, "4-3-3",
            mapa(1, 4, 3, 3)),
    TRES_CINCO_DOIS(FormatoEquipe.ONZE_POR_ONZE, "3-5-2",
            mapa(1, 3, 5, 2)),
    QUATRO_DOIS_TRES_UM(FormatoEquipe.ONZE_POR_ONZE, "4-2-3-1",
            mapa(1, 4, 5, 1));

    private final FormatoEquipe formatoCompativel;
    private final String descricao;
    private final Map<Posicao, Integer> distribuicaoPosicoes;

    EsquemaTatico(FormatoEquipe formatoCompativel,
                  String descricao,
                  Map<Posicao, Integer> distribuicaoPosicoes) {
        this.formatoCompativel = formatoCompativel;
        this.descricao = descricao;
        this.distribuicaoPosicoes = Collections.unmodifiableMap(distribuicaoPosicoes);
    }

    public FormatoEquipe getFormatoCompativel() {
        return formatoCompativel;
    }

    public String getDescricao() {
        return descricao;
    }

    public Map<Posicao, Integer> getDistribuicaoPosicoes() {
        return distribuicaoPosicoes;
    }

    public int getQuantidadeTitulares() {
        return distribuicaoPosicoes.values().stream().mapToInt(Integer::intValue).sum();
    }

    public boolean ehCompativelCom(FormatoEquipe formato) {
        return this.formatoCompativel == formato;
    }

    public int quantidadeNaPosicao(Posicao posicao) {
        return distribuicaoPosicoes.getOrDefault(posicao, 0);
    }

    private static Map<Posicao, Integer> mapa(int goleiros,
                                              int defensores,
                                              int meioCampistas,
                                              int atacantes) {
        Map<Posicao, Integer> distribuicao = new EnumMap<>(Posicao.class);
        if (goleiros > 0) {
            distribuicao.put(Posicao.GOLEIRO, goleiros);
        }
        if (defensores > 0) {
            distribuicao.put(Posicao.DEFENSOR, defensores);
        }
        if (meioCampistas > 0) {
            distribuicao.put(Posicao.MEIO_CAMPISTA, meioCampistas);
        }
        if (atacantes > 0) {
            distribuicao.put(Posicao.ATACANTE, atacantes);
        }
        return distribuicao;
    }
}
