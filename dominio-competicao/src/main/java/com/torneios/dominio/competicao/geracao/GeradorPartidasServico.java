package com.torneios.dominio.competicao.geracao;

import java.util.ArrayList;
import java.util.Collections;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.rodada.Rodada;

public class GeradorPartidasServico {

    public List<Partida> gerar(TorneioId torneioId,
                               FormatoTorneio formatoTorneio,
                               int quantidadeJogadoresPorEquipe,
                               List<TimeId> participantes,
                               List<List<TimeId>> grupos) {
        return gerar(torneioId, formatoTorneio, quantidadeJogadoresPorEquipe, participantes, grupos,
                ModoPreparacaoCompeticao.SORTEIO);
    }

    public List<Partida> gerar(TorneioId torneioId,
                               FormatoTorneio formatoTorneio,
                               int quantidadeJogadoresPorEquipe,
                               List<TimeId> participantes,
                               List<List<TimeId>> grupos,
                               ModoPreparacaoCompeticao modoPreparacao) {
        List<TimeId> participantesOrdenados = ordenarParticipantes(participantes, modoPreparacao);
        List<Partida> partidas = switch (formatoTorneio) {
            case PONTOS_CORRIDOS -> gerarPontosCorridos(torneioId, quantidadeJogadoresPorEquipe, participantesOrdenados);
            case MATA_MATA, FINAL_UNICA -> gerarMataMata(torneioId, quantidadeJogadoresPorEquipe, participantesOrdenados);
            case FASE_DE_GRUPOS_COM_MATA_MATA -> gerarFaseDeGrupos(torneioId, quantidadeJogadoresPorEquipe, grupos);
        };
        LocalDateTime inicioCalendario = LocalDateTime.now()
                .plusDays(1).withHour(19).withMinute(0).withSecond(0).withNano(0);
        for (int indice = 0; indice < partidas.size(); indice++) {
            partidas.get(indice).agendar(inicioCalendario.plusDays(indice), null);
        }
        return partidas;
    }

    public List<Partida> gerarPontosCorridos(TorneioId torneioId,
                                             int quantidadeJogadoresPorEquipe,
                                             List<TimeId> participantes) {
        return new GeradorPontosCorridos().gerar(torneioId, quantidadeJogadoresPorEquipe, participantes);
    }

    public List<Partida> gerarMataMata(TorneioId torneioId,
                                       int quantidadeJogadoresPorEquipe,
                                       List<TimeId> participantes) {
        return new GeradorMataMata().gerar(torneioId, quantidadeJogadoresPorEquipe, participantes);
    }

    public List<Partida> gerarFaseDeGrupos(TorneioId torneioId,
                                           int quantidadeJogadoresPorEquipe,
                                           List<List<TimeId>> grupos) {
        if (grupos == null || grupos.isEmpty()) {
            throw new RegraDeNegocioException("A fase de grupos exige grupos previamente definidos na estrutura.");
        }

        List<Partida> partidas = new ArrayList<>();
        long sequencia = 1L;
        char identificador = 'A';

        for (List<TimeId> grupo : grupos) {
            validarParticipantes(grupo);
            String nomeGrupo = "Grupo " + identificador++;

            for (int i = 0; i < grupo.size(); i++) {
                for (int j = i + 1; j < grupo.size(); j++) {
                    partidas.add(new Partida(
                            new PartidaId(sequencia++),
                            torneioId,
                            grupo.get(i),
                            grupo.get(j),
                            nomeGrupo,
                            quantidadeJogadoresPorEquipe));
                }
            }
        }

        return partidas;
    }

    public List<Rodada> distribuirEmRodadas(TorneioId torneioId, List<Partida> partidas) {
        List<Rodada> rodadas = new ArrayList<>();
        int numeroRodada = 1;

        for (Partida partida : partidas) {
            Rodada rodada = new Rodada(torneioId, numeroRodada++);
            rodada.adicionarPartida(partida.getId());
            rodadas.add(rodada);
        }

        return rodadas;
    }

    private void validarParticipantes(List<TimeId> participantes) {
        if (participantes == null || participantes.size() < 2) {
            throw new IllegalArgumentException("E necessario ter ao menos dois participantes para gerar partidas.");
        }
    }

    private List<TimeId> ordenarParticipantes(List<TimeId> participantes, ModoPreparacaoCompeticao modoPreparacao) {
        validarParticipantes(participantes);
        List<TimeId> ordenados = new ArrayList<>(participantes);
        if (modoPreparacao == ModoPreparacaoCompeticao.SORTEIO) {
            Collections.shuffle(ordenados, new Random(1L));
        }
        return ordenados;
    }
}
