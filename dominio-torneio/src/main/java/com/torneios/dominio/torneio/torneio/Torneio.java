package com.torneios.dominio.torneio.torneio;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.torneios.dominio.torneio.participante.ParticipanteTorneio;

public class Torneio {

    private final TorneioId id;
    private String nome;
    private final FormatoTorneio formato;
    private final FormatoEquipe formatoEquipe;
    private final long organizadorId;
    private final Set<ParticipanteTorneio> participantesAprovados;
    private boolean aceitaSolicitacoes;
    private StatusTorneio status;

    public Torneio(TorneioId id,
                   String nome,
                   FormatoTorneio formato,
                   FormatoEquipe formatoEquipe,
                   long organizadorId,
                   boolean aceitaSolicitacoes) {
        this.id = Objects.requireNonNull(id, "O id do torneio e obrigatorio.");
        this.nome = validarNome(nome);
        this.formato = Objects.requireNonNull(formato, "O formato do torneio e obrigatorio.");
        this.formatoEquipe = Objects.requireNonNull(formatoEquipe, "O formato de equipe e obrigatorio.");
        if (organizadorId <= 0) {
            throw new IllegalArgumentException("O id do organizador deve ser maior que zero.");
        }
        this.organizadorId = organizadorId;
        this.aceitaSolicitacoes = aceitaSolicitacoes;
        this.status = StatusTorneio.CRIADO;
        this.participantesAprovados = new LinkedHashSet<>();
    }

    public TorneioId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public FormatoTorneio getFormato() {
        return formato;
    }

    public FormatoEquipe getFormatoEquipe() {
        return formatoEquipe;
    }

    public long getOrganizadorId() {
        return organizadorId;
    }

    public boolean aceitaSolicitacoes() {
        return aceitaSolicitacoes;
    }

    public StatusTorneio getStatus() {
        return status;
    }

    public Set<ParticipanteTorneio> getParticipantesAprovados() {
        return Set.copyOf(participantesAprovados);
    }

    public void renomear(String nome) {
        this.nome = validarNome(nome);
    }

    public void abrirParaSolicitacoes() {
        validarNaoIniciado();
        this.aceitaSolicitacoes = true;
        this.status = StatusTorneio.CONFIGURADO;
    }

    public void fecharSolicitacoes() {
        validarNaoIniciado();
        this.aceitaSolicitacoes = false;
        this.status = StatusTorneio.CONFIGURADO;
    }

    public void adicionarParticipante(long timeId) {
        validarNaoIniciado();
        participantesAprovados.add(new ParticipanteTorneio(timeId, id));
    }

    public void adicionarParticipantes(Collection<Long> timesIds) {
        Objects.requireNonNull(timesIds, "A lista de participantes nao pode ser nula.");
        timesIds.forEach(this::adicionarParticipante);
    }

    public void removerParticipante(long timeId) {
        validarNaoIniciado();
        participantesAprovados.remove(new ParticipanteTorneio(timeId, id));
    }

    public boolean possuiParticipante(long timeId) {
        return participantesAprovados.stream().anyMatch(participante -> participante.getTimeId() == timeId);
    }

    public boolean possuiParticipantesSuficientes() {
        return participantesAprovados.size() >= formato.quantidadeMinimaParticipantes();
    }

    public void marcarEstruturaGerada() {
        if (!possuiParticipantesSuficientes()) {
            throw new IllegalStateException("Nao ha participantes suficientes para gerar a estrutura da competicao.");
        }
        this.status = StatusTorneio.ESTRUTURA_GERADA;
    }

    public void iniciar() {
        if (!possuiParticipantesSuficientes()) {
            throw new IllegalStateException("O torneio nao pode ser iniciado sem participantes suficientes.");
        }
        if (status != StatusTorneio.ESTRUTURA_GERADA) {
            throw new IllegalStateException("O torneio so pode ser iniciado apos a geracao da estrutura.");
        }
        this.status = StatusTorneio.INICIADO;
    }

    public void finalizar() {
        if (status != StatusTorneio.INICIADO) {
            throw new IllegalStateException("O torneio so pode ser finalizado apos ser iniciado.");
        }
        this.status = StatusTorneio.FINALIZADO;
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do torneio e obrigatorio.");
        }
        return nome.trim();
    }

    private void validarNaoIniciado() {
        if (status == StatusTorneio.INICIADO || status == StatusTorneio.FINALIZADO) {
            throw new IllegalStateException("Nao e permitido alterar participantes ou configuracoes apos o inicio do torneio.");
        }
    }
}
