package com.torneios.dominio.competicao.partida;

import java.util.List;

import com.torneios.dominio.competicao.chaveamento.Chaveamento;
import com.torneios.dominio.competicao.classificacao.Classificacao;

public record AtualizacaoCompeticao(List<Classificacao> classificacaoAtualizada, Chaveamento chaveamentoAtualizado) {

    public static AtualizacaoCompeticao comClassificacao(List<Classificacao> classificacao) {
        return new AtualizacaoCompeticao(classificacao, null);
    }

    public static AtualizacaoCompeticao comChaveamento(Chaveamento chaveamento) {
        return new AtualizacaoCompeticao(null, chaveamento);
    }
}
