package com.torneios.dominio.competicao.geracao;

import java.util.List;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;

/**
 * Padrão Template Method.
 *
 * Define o esqueleto do algoritmo de geração de partidas: validar participantes,
 * depois criar as partidas no formato específico. As subclasses implementam
 * apenas a etapa de criação ({@link #criarPartidas}).
 */
public abstract class GeradorPartidas {

    /**
     * Método template: valida participantes e delega a criação de partidas
     * à subclasse concreta.
     */
    public final List<Partida> gerar(TorneioId torneioId,
                                     int quantidadeJogadoresPorEquipe,
                                     List<TimeId> participantes) {
        validarParticipantes(participantes);
        return criarPartidas(torneioId, quantidadeJogadoresPorEquipe, participantes);
    }

    /**
     * Hook concreto: valida que há pelo menos dois participantes.
     * Subclasses podem sobrescrever para validações adicionais.
     */
    protected void validarParticipantes(List<TimeId> participantes) {
        if (participantes == null || participantes.size() < 2) {
            throw new IllegalArgumentException(
                    "E necessario ter ao menos dois participantes para gerar partidas.");
        }
    }

    /**
     * Passo abstrato: cria as partidas no formato específico do torneio.
     */
    protected abstract List<Partida> criarPartidas(TorneioId torneioId,
                                                    int quantidadeJogadoresPorEquipe,
                                                    List<TimeId> participantes);

    /** Gera um PartidaId sequencial a partir da posição base. */
    protected static PartidaId proximoId(long[] sequencia) {
        return new PartidaId(sequencia[0]++);
    }
}
