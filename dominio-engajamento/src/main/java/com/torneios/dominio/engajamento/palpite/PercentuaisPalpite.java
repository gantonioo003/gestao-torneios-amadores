package com.torneios.dominio.engajamento.palpite;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class PercentuaisPalpite {

    private final EventoAlvo eventoAlvo;
    private final long totalPalpites;
    private final Map<OpcaoPalpite, Double> percentuaisPorOpcao;

    private PercentuaisPalpite(EventoAlvo eventoAlvo,
                               long totalPalpites,
                               Map<OpcaoPalpite, Double> percentuaisPorOpcao) {
        this.eventoAlvo = eventoAlvo;
        this.totalPalpites = totalPalpites;
        this.percentuaisPorOpcao = Collections.unmodifiableMap(percentuaisPorOpcao);
    }

    public static PercentuaisPalpite calcular(EventoAlvo eventoAlvo, List<Palpite> palpites) {
        Objects.requireNonNull(eventoAlvo, "O evento alvo e obrigatorio.");
        Objects.requireNonNull(palpites, "Os palpites sao obrigatorios.");
        long total = palpites.size();
        Map<OpcaoPalpite, Long> contagem = new LinkedHashMap<>();
        for (Palpite palpite : palpites) {
            contagem.merge(palpite.getOpcao(), 1L, Long::sum);
        }
        Map<OpcaoPalpite, Double> percentuais = new LinkedHashMap<>();
        for (Map.Entry<OpcaoPalpite, Long> entry : contagem.entrySet()) {
            double percentual = total == 0 ? 0.0 : (entry.getValue() * 100.0) / total;
            percentuais.put(entry.getKey(), percentual);
        }
        return new PercentuaisPalpite(eventoAlvo, total, percentuais);
    }

    public EventoAlvo getEventoAlvo() {
        return eventoAlvo;
    }

    public long getTotalPalpites() {
        return totalPalpites;
    }

    public Map<OpcaoPalpite, Double> getPercentuaisPorOpcao() {
        return percentuaisPorOpcao;
    }
}
