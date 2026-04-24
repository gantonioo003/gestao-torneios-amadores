package com.torneios.dominio.competicao.chaveamento;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;

public class ChaveamentoServico {

    public Chaveamento gerar(TorneioId torneioId, FormatoTorneio formatoTorneio, List<Partida> partidas) {
        if (torneioId == null) {
            throw new IllegalArgumentException("O torneio do chaveamento e obrigatorio.");
        }
        if (formatoTorneio == null) {
            throw new IllegalArgumentException("O formato do torneio e obrigatorio.");
        }
        if (!formatoTorneio.exigeChaveamento()) {
            throw new IllegalArgumentException("O formato informado nao utiliza chaveamento.");
        }

        Chaveamento chaveamento = new Chaveamento(torneioId);
        Map<String, List<Partida>> partidasPorEtapa = new LinkedHashMap<>();

        for (Partida partida : partidas) {
            partidasPorEtapa.computeIfAbsent(partida.getEtapa(), chave -> new java.util.ArrayList<>()).add(partida);
        }

        partidasPorEtapa.forEach((etapa, partidasEtapa) -> {
            chaveamento.adicionarFase(etapa);
            partidasEtapa.forEach(partida -> chaveamento.vincularPartida(partida.getId()));
        });

        return chaveamento;
    }
}
