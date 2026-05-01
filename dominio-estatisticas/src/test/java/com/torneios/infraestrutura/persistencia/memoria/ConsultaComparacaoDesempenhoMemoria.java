package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.estatisticas.comparacao.ConsultaComparacaoDesempenho;

public class ConsultaComparacaoDesempenhoMemoria implements ConsultaComparacaoDesempenho {

    private final Map<JogadorId, String> nomesJogadores = new HashMap<>();
    private final Map<TimeId, String> nomesTimes = new HashMap<>();
    private final Map<JogadorId, TimeId> timesDosJogadores = new HashMap<>();

    public void registrarJogador(JogadorId jogadorId, String nome, TimeId timeId) {
        nomesJogadores.put(jogadorId, nome);
        timesDosJogadores.put(jogadorId, timeId);
    }

    public void registrarTime(TimeId timeId, String nome) {
        nomesTimes.put(timeId, nome);
    }

    @Override
    public String nomeJogador(JogadorId jogadorId) {
        return nomesJogadores.getOrDefault(jogadorId, "Jogador " + jogadorId.valor());
    }

    @Override
    public String nomeTime(TimeId timeId) {
        return nomesTimes.getOrDefault(timeId, "Time " + timeId.valor());
    }

    @Override
    public Optional<TimeId> timeDoJogador(JogadorId jogadorId) {
        return Optional.ofNullable(timesDosJogadores.get(jogadorId));
    }

    public void limpar() {
        nomesJogadores.clear();
        nomesTimes.clear();
        timesDosJogadores.clear();
    }
}
