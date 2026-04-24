package com.torneios.dominio.torneio.torneio;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.enumeracao.StatusTorneio;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.participante.ParticipanteTorneio;

public class Torneio {

    private final TorneioId id;
    private String nome;
    private final FormatoTorneio formato;
    private final FormatoEquipe formatoEquipe;
    private final UsuarioId organizadorId;
    private final Set<ParticipanteTorneio> participantesAprovados;
    private boolean aceitaSolicitacoes;
    private StatusTorneio status;

    public Torneio(TorneioId id,
                   String nome,
                   FormatoTorneio formato,
                   FormatoEquipe formatoEquipe,
                   UsuarioId organizadorId,
                   boolean aceitaSolicitacoes) {
        this.id = Objects.requireNonNull(id, "O id do torneio e obrigatorio.");
        this.nome = validarNome(nome);
        this.formato = Objects.requireNonNull(formato, "O formato do torneio e obrigatorio.");
        this.formatoEquipe = Objects.requireNonNull(formatoEquipe, "O formato de equipe e obrigatorio.");
        this.organizadorId = Objects.requireNonNull(organizadorId, "O organizador do torneio e obrigatorio.");
        this.aceitaSolicitacoes = aceitaSolicitacoes;
        this.status = StatusTorneio.CONFIGURADO;
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

    public UsuarioId getOrganizadorId() {
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

    public void adicionarParticipante(TimeId timeId) {
        validarNaoIniciado();
        if (timeId == null) {
            throw new IllegalArgumentException("O time participante e obrigatorio.");
        }
        participantesAprovados.add(new ParticipanteTorneio(timeId, id));
    }

    public void adicionarParticipantes(Collection<TimeId> timesIds) {
        Objects.requireNonNull(timesIds, "A lista de participantes nao pode ser nula.");
        timesIds.forEach(this::adicionarParticipante);
    }

    public void removerParticipante(TimeId timeId) {
        validarNaoIniciado();
        if (!participantesAprovados.remove(new ParticipanteTorneio(timeId, id))) {
            throw new RegraDeNegocioException("O time informado nao esta entre os participantes aprovados.");
        }
    }

    public boolean possuiParticipante(TimeId timeId) {
        return participantesAprovados.stream().anyMatch(participante -> participante.getTimeId().equals(timeId));
    }

    public boolean possuiParticipantesSuficientes() {
        return participantesAprovados.size() >= formato.quantidadeMinimaParticipantes();
    }

    public void marcarEstruturaGerada() {
        if (!possuiParticipantesSuficientes()) {
            throw new RegraDeNegocioException("Nao ha participantes suficientes para gerar a estrutura da competicao.");
        }
        this.status = StatusTorneio.ESTRUTURA_GERADA;
    }

    public void iniciar() {
        if (!possuiParticipantesSuficientes()) {
            throw new RegraDeNegocioException("O torneio nao pode ser iniciado sem participantes suficientes.");
        }
        if (status != StatusTorneio.ESTRUTURA_GERADA) {
            throw new OperacaoNaoPermitidaException("O torneio so pode ser iniciado apos a geracao da estrutura.");
        }
        this.status = StatusTorneio.INICIADO;
    }

    public void finalizar() {
        if (status != StatusTorneio.INICIADO) {
            throw new OperacaoNaoPermitidaException("O torneio so pode ser finalizado apos ser iniciado.");
        }
        this.status = StatusTorneio.FINALIZADO;
    }

    public boolean estaDisponivelParaVisualizacao() {
        return status != StatusTorneio.FINALIZADO;
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do torneio e obrigatorio.");
        }
        return nome.trim();
    }

    private void validarNaoIniciado() {
        if (status == StatusTorneio.INICIADO || status == StatusTorneio.FINALIZADO) {
            throw new OperacaoNaoPermitidaException(
                    "Nao e permitido alterar participantes ou configuracoes apos o inicio do torneio.");
        }
    }
}
