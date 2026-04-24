package com.torneios.dominio.competicao.geracao;

import java.util.ArrayList;
import java.util.List;

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
        return switch (formatoTorneio) {
            case PONTOS_CORRIDOS -> gerarPontosCorridos(torneioId, quantidadeJogadoresPorEquipe, participantes);
            case MATA_MATA, FINAL_UNICA -> gerarMataMata(torneioId, quantidadeJogadoresPorEquipe, participantes);
            case FASE_DE_GRUPOS_COM_MATA_MATA -> gerarFaseDeGrupos(torneioId, quantidadeJogadoresPorEquipe, grupos);
        };
    }

    public List<Partida> gerarPontosCorridos(TorneioId torneioId,
                                             int quantidadeJogadoresPorEquipe,
                                             List<TimeId> participantes) {
        validarParticipantes(participantes);

        List<Partida> partidas = new ArrayList<>();
        long sequencia = 1L;

        for (int i = 0; i < participantes.size(); i++) {
            for (int j = i + 1; j < participantes.size(); j++) {
                partidas.add(new Partida(
                        new PartidaId(sequencia++),
                        torneioId,
                        participantes.get(i),
                        participantes.get(j),
                        "Pontos corridos",
                        quantidadeJogadoresPorEquipe));
            }
        }

        return partidas;
    }

    public List<Partida> gerarMataMata(TorneioId torneioId,
                                       int quantidadeJogadoresPorEquipe,
                                       List<TimeId> participantes) {
        validarParticipantes(participantes);

        List<Partida> partidas = new ArrayList<>();
        long sequencia = 1L;

        for (int i = 0; i < participantes.size(); i += 2) {
            if (i + 1 >= participantes.size()) {
                break;
            }
            partidas.add(new Partida(
                    new PartidaId(sequencia++),
                    torneioId,
                    participantes.get(i),
                    participantes.get(i + 1),
                    "Chaveamento",
                    quantidadeJogadoresPorEquipe));
        }

        return partidas;
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
}
