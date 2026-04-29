package com.torneios.dominio.competicao.escalacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.torneios.dominio.compartilhado.enumeracao.EsquemaTatico;
import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.Posicao;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;

public class Escalacao {

    private final EscalacaoId id;
    private final PartidaId partidaId;
    private final TimeId timeId;
    private final FormatoEquipe formatoEquipe;
    private EsquemaTatico esquemaTatico;
    private final List<JogadorEscalado> titulares;
    private final List<JogadorId> reservas;
    private boolean congelada;

    public Escalacao(EscalacaoId id,
                     PartidaId partidaId,
                     TimeId timeId,
                     FormatoEquipe formatoEquipe,
                     EsquemaTatico esquemaTatico,
                     List<JogadorEscalado> titulares,
                     List<JogadorId> reservas) {
        this.id = Objects.requireNonNull(id, "O id da escalacao e obrigatorio.");
        this.partidaId = Objects.requireNonNull(partidaId, "A partida da escalacao e obrigatoria.");
        this.timeId = Objects.requireNonNull(timeId, "O time da escalacao e obrigatorio.");
        this.formatoEquipe = Objects.requireNonNull(formatoEquipe,
                "O formato de equipe da escalacao e obrigatorio.");
        this.titulares = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.congelada = false;
        definirEsquemaTatico(esquemaTatico, titulares, reservas);
    }

    public EscalacaoId getId() {
        return id;
    }

    public PartidaId getPartidaId() {
        return partidaId;
    }

    public TimeId getTimeId() {
        return timeId;
    }

    public FormatoEquipe getFormatoEquipe() {
        return formatoEquipe;
    }

    public EsquemaTatico getEsquemaTatico() {
        return esquemaTatico;
    }

    public List<JogadorEscalado> getTitulares() {
        return Collections.unmodifiableList(titulares);
    }

    public List<JogadorId> getReservas() {
        return Collections.unmodifiableList(reservas);
    }

    public boolean estaCongelada() {
        return congelada;
    }

    public boolean ehTitular(JogadorId jogadorId) {
        return titulares.stream().anyMatch(t -> t.jogadorId().equals(jogadorId));
    }

    public boolean ehReserva(JogadorId jogadorId) {
        return reservas.contains(jogadorId);
    }

    public void atualizar(EsquemaTatico novoEsquema,
                          List<JogadorEscalado> novosTitulares,
                          List<JogadorId> novosReservas) {
        garantirNaoCongelada();
        definirEsquemaTatico(novoEsquema, novosTitulares, novosReservas);
    }

    public void congelar() {
        this.congelada = true;
    }

    private void definirEsquemaTatico(EsquemaTatico novoEsquema,
                                      List<JogadorEscalado> novosTitulares,
                                      List<JogadorId> novosReservas) {
        Objects.requireNonNull(novoEsquema, "O esquema tatico da escalacao e obrigatorio.");
        Objects.requireNonNull(novosTitulares, "Os titulares da escalacao sao obrigatorios.");
        List<JogadorId> reservasParaValidar = novosReservas == null ? new ArrayList<>() : novosReservas;

        validarEsquemaCompativelComFormato(novoEsquema);
        validarQuantidadeTitulares(novosTitulares);
        validarDistribuicaoPorPosicao(novoEsquema, novosTitulares);
        validarSemDuplicacao(novosTitulares, reservasParaValidar);

        this.esquemaTatico = novoEsquema;
        this.titulares.clear();
        this.titulares.addAll(novosTitulares);
        this.reservas.clear();
        this.reservas.addAll(reservasParaValidar);
    }

    private void garantirNaoCongelada() {
        if (congelada) {
            throw new OperacaoNaoPermitidaException(
                    "Nao e permitido alterar a escalacao apos o inicio da partida.");
        }
    }

    private void validarEsquemaCompativelComFormato(EsquemaTatico novoEsquema) {
        if (!novoEsquema.ehCompativelCom(formatoEquipe)) {
            throw new RegraDeNegocioException(
                    "O esquema tatico " + novoEsquema.getDescricao()
                            + " nao e compativel com o formato de equipe do torneio.");
        }
    }

    private void validarQuantidadeTitulares(List<JogadorEscalado> novosTitulares) {
        int quantidadeEsperada = formatoEquipe.getQuantidadeJogadores();
        if (novosTitulares.size() != quantidadeEsperada) {
            throw new RegraDeNegocioException(
                    "A escalacao deve possuir exatamente " + quantidadeEsperada
                            + " titulares, mas foram informados " + novosTitulares.size() + ".");
        }
    }

    private void validarDistribuicaoPorPosicao(EsquemaTatico novoEsquema,
                                               List<JogadorEscalado> novosTitulares) {
        Map<Posicao, Long> contagemPorPosicao = novosTitulares.stream()
                .collect(Collectors.groupingBy(JogadorEscalado::posicao, Collectors.counting()));
        for (Map.Entry<Posicao, Integer> entry : novoEsquema.getDistribuicaoPosicoes().entrySet()) {
            long contagem = contagemPorPosicao.getOrDefault(entry.getKey(), 0L);
            if (contagem != entry.getValue().longValue()) {
                throw new RegraDeNegocioException(
                        "A distribuicao de titulares na posicao " + entry.getKey()
                                + " nao bate com o esquema tatico " + novoEsquema.getDescricao() + ".");
            }
        }
    }

    private void validarSemDuplicacao(List<JogadorEscalado> novosTitulares,
                                      List<JogadorId> novosReservas) {
        Set<JogadorId> idsTitulares = novosTitulares.stream()
                .map(JogadorEscalado::jogadorId)
                .collect(Collectors.toSet());
        if (idsTitulares.size() != novosTitulares.size()) {
            throw new RegraDeNegocioException(
                    "Um mesmo jogador nao pode aparecer mais de uma vez como titular.");
        }
        Set<JogadorId> idsReservas = new HashSet<>(novosReservas);
        if (idsReservas.size() != novosReservas.size()) {
            throw new RegraDeNegocioException(
                    "Um mesmo jogador nao pode aparecer mais de uma vez como reserva.");
        }
        for (JogadorId reserva : novosReservas) {
            if (idsTitulares.contains(reserva)) {
                throw new RegraDeNegocioException(
                        "Um mesmo jogador nao pode ser titular e reserva da mesma escalacao.");
            }
        }
    }
}
