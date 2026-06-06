package com.torneios.dominio.competicao.classificacao;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Padrão Iterator.
 *
 * Percorre a lista de classificações em ordem de ranking (do 1.º colocado em
 * diante) sem expor a estrutura de dados interna ao cliente.
 */
public class ClassificacaoIterador implements Iterator<Classificacao> {

    private final List<Classificacao> classificacoes;
    private int posicaoAtual = 0;

    public ClassificacaoIterador(List<Classificacao> classificacoes) {
        this.classificacoes = List.copyOf(Objects.requireNonNull(classificacoes,
                "A lista de classificacoes nao pode ser nula."));
    }

    @Override
    public boolean hasNext() {
        return posicaoAtual < classificacoes.size();
    }

    @Override
    public Classificacao next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Nao ha mais classificacoes.");
        }
        return classificacoes.get(posicaoAtual++);
    }

    /** Retorna a posição (1-based) do próximo elemento a ser retornado. */
    public int posicaoAtual() {
        return posicaoAtual + 1;
    }
}
