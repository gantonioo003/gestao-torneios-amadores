package com.torneios.dominio.torneio.estrutura;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.torneio.torneio.Torneio;

public class GeradorEstruturaCompeticaoServico {

    public EstruturaCompeticao gerar(Torneio torneio) {
        return gerarPorSorteio(torneio);
    }

    public EstruturaCompeticao gerarPorSorteio(Torneio torneio) {
        List<TimeId> participantesSorteados = participantes(torneio);
        Collections.shuffle(participantesSorteados, new Random(1L));
        return gerarComParticipantes(torneio, participantesSorteados, ModoGeracaoEstrutura.SORTEIO);
    }

    public EstruturaCompeticao gerarManual(Torneio torneio, List<TimeId> ordemManualParticipantes) {
        validarOrdemManual(torneio, ordemManualParticipantes);
        return gerarComParticipantes(torneio, ordemManualParticipantes, ModoGeracaoEstrutura.MANUAL);
    }

    private EstruturaCompeticao gerarComParticipantes(Torneio torneio,
                                                      List<TimeId> participantes,
                                                      ModoGeracaoEstrutura modoGeracao) {
        if (torneio == null) {
            throw new IllegalArgumentException("O torneio e obrigatorio para gerar a estrutura.");
        }
        if (!torneio.possuiParticipantesSuficientes()) {
            throw new IllegalStateException("O torneio nao possui participantes suficientes para gerar a estrutura.");
        }

        TipoEstruturaCompeticao tipo = mapearTipo(torneio.getFormato());
        EstruturaCompeticao estrutura = new EstruturaCompeticao(torneio.getId(), tipo, modoGeracao);

        switch (tipo) {
            case CHAVEAMENTO -> {
                estrutura.adicionarEtapa("Chaveamento inicial");
                estrutura.adicionarEtapa("Fases eliminatorias");
            }
            case TABELA -> estrutura.adicionarEtapa("Tabela de pontos corridos");
            case GRUPOS -> {
                estrutura.adicionarEtapa("Fase de grupos");
                estrutura.adicionarEtapa("Transicao para mata-mata");
                gerarGrupos(participantes, estrutura);
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

    private List<TimeId> participantes(Torneio torneio) {
        Objects.requireNonNull(torneio, "O torneio e obrigatorio para obter participantes.");
        return new ArrayList<>(torneio.getParticipantesAprovados()
                .stream()
                .map(participante -> participante.getTimeId())
                .toList());
    }

    private void gerarGrupos(List<TimeId> participantes, EstruturaCompeticao estrutura) {
        List<Grupo> grupos = new ArrayList<>();
        grupos.add(new Grupo("Grupo A"));
        grupos.add(new Grupo("Grupo B"));

        for (int i = 0; i < participantes.size(); i++) {
            Grupo grupo = grupos.get(i % grupos.size());
            grupo.adicionarParticipante(participantes.get(i));
        }

        grupos.forEach(estrutura::adicionarGrupo);
    }

    private void validarOrdemManual(Torneio torneio, List<TimeId> ordemManualParticipantes) {
        List<TimeId> participantes = participantes(torneio);
        if (ordemManualParticipantes == null || ordemManualParticipantes.size() != participantes.size()) {
            throw new RegraDeNegocioException(
                    "A montagem manual deve informar todos os participantes aprovados do torneio.");
        }
        if (!participantes.containsAll(ordemManualParticipantes)
                || !ordemManualParticipantes.containsAll(participantes)) {
            throw new RegraDeNegocioException(
                    "A montagem manual so pode usar times aprovados no torneio.");
        }
    }
}
