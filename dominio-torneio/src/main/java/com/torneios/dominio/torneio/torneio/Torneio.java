package com.torneios.dominio.torneio.torneio;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
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
    private String imagemUrl;
    private final FormatoTorneio formato;
    private final FormatoEquipe formatoEquipe;
    private final UsuarioId organizadorId;
    private final Set<ParticipanteTorneio> participantesAprovados;
    private final List<HistoricoEdicaoTorneio> historicoEdicoes;
    private int edicaoAtual;
    private boolean aceitaSolicitacoes;
    private StatusTorneio status;

    public Torneio(TorneioId id,
                   String nome,
                   FormatoTorneio formato,
                   FormatoEquipe formatoEquipe,
                   UsuarioId organizadorId,
                   boolean aceitaSolicitacoes) {
        this(id, nome, formato, formatoEquipe, organizadorId, aceitaSolicitacoes, imagemPadrao());
    }

    public Torneio(TorneioId id,
                   String nome,
                   FormatoTorneio formato,
                   FormatoEquipe formatoEquipe,
                   UsuarioId organizadorId,
                   boolean aceitaSolicitacoes,
                   String imagemUrl) {
        this.id = Objects.requireNonNull(id, "O id do torneio e obrigatorio.");
        this.nome = validarNome(nome);
        this.imagemUrl = validarImagem(imagemUrl);
        this.formato = Objects.requireNonNull(formato, "O formato do torneio e obrigatorio.");
        this.formatoEquipe = Objects.requireNonNull(formatoEquipe, "O formato de equipe e obrigatorio.");
        this.organizadorId = Objects.requireNonNull(organizadorId, "O organizador do torneio e obrigatorio.");
        this.aceitaSolicitacoes = aceitaSolicitacoes;
        this.status = StatusTorneio.CONFIGURADO;
        this.participantesAprovados = new LinkedHashSet<>();
        this.historicoEdicoes = new ArrayList<>();
        this.edicaoAtual = 1;
    }

    public TorneioId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getImagemUrl() {
        return imagemUrl;
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

    public int getEdicaoAtual() {
        return edicaoAtual;
    }

    public List<HistoricoEdicaoTorneio> getHistoricoEdicoes() {
        return List.copyOf(historicoEdicoes);
    }

    public void renomear(String nome) {
        validarNaoIniciado();
        this.nome = validarNome(nome);
    }

    public void alterarImagem(String novaImagemUrl) {
        validarNaoIniciado();
        this.imagemUrl = validarImagem(novaImagemUrl);
    }

    public void atualizarConfiguracao(String nome, boolean aceitaSolicitacoes) {
        validarNaoIniciado();
        this.nome = validarNome(nome);
        this.aceitaSolicitacoes = aceitaSolicitacoes;
    }

    public void abrirParaSolicitacoes() {
        validarNaoIniciado();
        this.aceitaSolicitacoes = true;
    }

    public void fecharSolicitacoes() {
        validarNaoIniciado();
        this.aceitaSolicitacoes = false;
    }

    public void adicionarParticipante(TimeId timeId) {
        validarNaoIniciado();
        if (timeId == null) {
            throw new IllegalArgumentException("O time participante e obrigatorio.");
        }
        if (participantesAprovados.add(new ParticipanteTorneio(timeId, id))) {
            invalidarEstruturaGerada();
        }
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
        invalidarEstruturaGerada();
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

    public HistoricoEdicaoTorneio repetirComoNovaEdicao(boolean abrirSolicitacoes) {
        if (status != StatusTorneio.FINALIZADO) {
            throw new OperacaoNaoPermitidaException(
                    "O torneio so pode ser repetido depois de finalizar a edicao atual.");
        }

        HistoricoEdicaoTorneio historico = new HistoricoEdicaoTorneio(
                id,
                edicaoAtual,
                nome,
                participantesAprovados.stream()
                        .map(ParticipanteTorneio::getTimeId)
                        .toList());
        historicoEdicoes.add(historico);
        participantesAprovados.clear();
        edicaoAtual++;
        aceitaSolicitacoes = abrirSolicitacoes;
        status = StatusTorneio.CONFIGURADO;
        return historico;
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

    private static String validarImagem(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("A identidade visual do torneio e obrigatoria.");
        }
        return valor.trim();
    }

    private static String imagemPadrao() {
        return "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 140 140'%3E"
                + "%3Crect width='140' height='140' rx='34' fill='%2318211f'/%3E"
                + "%3Cpath fill='%2312b85f' d='M42 28h56v18c0 19-10 34-28 42-18-8-28-23-28-42Z'/%3E"
                + "%3Cpath fill='%23fff' d='M55 96h30v10H55zM48 108h44v8H48z'/%3E%3C/svg%3E";
    }

    private void validarNaoIniciado() {
        if (status == StatusTorneio.INICIADO || status == StatusTorneio.FINALIZADO) {
            throw new OperacaoNaoPermitidaException(
                    "Nao e permitido alterar participantes ou configuracoes apos o inicio do torneio.");
        }
    }

    private void invalidarEstruturaGerada() {
        if (status == StatusTorneio.ESTRUTURA_GERADA) {
            status = StatusTorneio.CONFIGURADO;
        }
    }
}
