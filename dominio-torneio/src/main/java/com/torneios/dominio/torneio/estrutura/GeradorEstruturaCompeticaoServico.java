package com.torneios.dominio.torneio.estrutura;

import com.torneios.dominio.torneio.torneio.FormatoTorneio;
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
}
