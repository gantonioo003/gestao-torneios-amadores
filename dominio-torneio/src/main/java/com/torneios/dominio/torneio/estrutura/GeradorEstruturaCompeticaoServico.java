package com.torneios.dominio.torneio.estrutura;

import java.util.ArrayList;
import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.torneio.torneio.Torneio;

public class GeradorEstruturaCompeticaoServico {

    public EstruturaCompeticao gerar(Torneio torneio) {
        if (torneio == null) {
            throw new IllegalArgumentException("O torneio e obrigatorio para gerar a estrutura.");
        }
        if (!torneio.possuiParticipantesSuficientes()) {
            throw new IllegalStateException("O torneio nao possui participantes suficientes para gerar a estrutura.");
        }

        TipoEstruturaCompeticao tipo = mapearTipo(torneio.getFormato());
        EstruturaCompeticao estrutura = new EstruturaCompeticao(torneio.getId(), tipo);

        switch (tipo) {
            case CHAVEAMENTO -> {
                estrutura.adicionarEtapa("Chaveamento inicial");
                estrutura.adicionarEtapa("Fases eliminatorias");
            }
            case TABELA -> estrutura.adicionarEtapa("Tabela de pontos corridos");
            case GRUPOS -> {
                estrutura.adicionarEtapa("Fase de grupos");
                estrutura.adicionarEtapa("Transicao para mata-mata");
                gerarGrupos(torneio, estrutura);
            }
        }

        torneio.marcarEstruturaGerada();
        return estrutura;
    }

    private TipoEstruturaCompeticao mapearTipo(FormatoTorneio formato) {
        return switch (formato) {
            case MATA_MATA, FINAL_UNICA -> TipoEstruturaCompeticao.CHAVEAMENTO;
            case PONTOS_CORRIDOS -> TipoEstruturaCompeticao.TABELA;
            case FASE_DE_GRUPOS_COM_MATA_MATA -> TipoEstruturaCompeticao.GRUPOS;
        };
    }

    private void gerarGrupos(Torneio torneio, EstruturaCompeticao estrutura) {
        List<TimeId> participantes = torneio.getParticipantesAprovados()
                .stream()
                .map(participante -> participante.getTimeId())
                .toList();

        List<Grupo> grupos = new ArrayList<>();
        grupos.add(new Grupo("Grupo A"));
        grupos.add(new Grupo("Grupo B"));

        for (int i = 0; i < participantes.size(); i++) {
            Grupo grupo = grupos.get(i % grupos.size());
            grupo.adicionarParticipante(participantes.get(i));
        }

        grupos.forEach(estrutura::adicionarGrupo);
    }
}
