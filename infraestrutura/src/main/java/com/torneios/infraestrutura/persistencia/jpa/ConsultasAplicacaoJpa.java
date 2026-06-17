package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.torneios.aplicacao.competicao.andamento.PartidaRepositorioAplicacao;
import com.torneios.aplicacao.competicao.andamento.PartidaResumo;
import com.torneios.aplicacao.participacao.candidatura.SolicitacaoRepositorioAplicacao;
import com.torneios.aplicacao.participacao.candidatura.SolicitacaoResumo;
import com.torneios.aplicacao.participacao.conta.ContaRepositorioAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaUsuarioResumo;
import com.torneios.aplicacao.participacao.profissional.ProfissionalRepositorioAplicacao;
import com.torneios.aplicacao.participacao.profissional.ProfissionalResumo;
import com.torneios.aplicacao.participacao.profissional.ProfissionalResumoExpandido;
import com.torneios.aplicacao.participacao.profissional.RegistroDeCarreiraResumo;
import com.torneios.aplicacao.participacao.time.TimeRepositorioAplicacao;
import com.torneios.aplicacao.participacao.time.TimeResumo;
import com.torneios.aplicacao.participacao.time.TimeResumoExpandido;
import com.torneios.aplicacao.participacao.time.VinculoProfissionalResumo;
import com.torneios.aplicacao.torneio.criacao.TorneioRepositorioAplicacao;
import com.torneios.aplicacao.torneio.criacao.TorneioResumo;
import com.torneios.dominio.compartilhado.enumeracao.StatusSolicitacao;

// ─────────────────────────────────────────────────────────────────────────────
// Torneio
// ─────────────────────────────────────────────────────────────────────────────

@Repository
class TorneioRepositorioAplicacaoImpl implements TorneioRepositorioAplicacao {

    @Autowired TorneioJpaRepository repositorio;

    @Override
    public List<TorneioResumo> pesquisarResumos() {
        return repositorio.findAll().stream().map(jpa -> (TorneioResumo) new TorneioJpaResumo(jpa)).toList();
    }

    @Override
    public List<TorneioResumo> pesquisarResumosPorOrganizador(long organizadorId) {
        return repositorio.findByOrganizadorId(organizadorId)
                          .stream().map(jpa -> (TorneioResumo) new TorneioJpaResumo(jpa)).toList();
    }
}

record TorneioJpaResumo(TorneioJpa jpa) implements TorneioResumo {
    @Override public Long getId()              { return jpa.id; }
    @Override public String getNome()          { return jpa.nome; }
    @Override public String getFormato()       { return jpa.formato != null ? jpa.formato.name() : null; }
    @Override public String getFormatoEquipe() { return jpa.formatoEquipe != null ? jpa.formatoEquipe.name() : null; }
    @Override public Long getOrganizadorId()   { return jpa.organizadorId; }
    @Override public String getStatus()        { return jpa.status != null ? jpa.status.name() : null; }
    @Override public boolean aceitaSolicitacoes() { return jpa.aceitaSolicitacoes; }
}

// ─────────────────────────────────────────────────────────────────────────────
// Partida
// ─────────────────────────────────────────────────────────────────────────────

@Repository
class PartidaRepositorioAplicacaoImpl implements PartidaRepositorioAplicacao {

    @Autowired PartidaJpaRepository repositorio;

    @Override
    public List<PartidaResumo> pesquisarResumosPorTorneio(long torneioId) {
        return repositorio.findByTorneioId(torneioId)
                          .stream().map(jpa -> (PartidaResumo) new PartidaJpaResumo(jpa)).toList();
    }
}

record PartidaJpaResumo(PartidaJpa jpa) implements PartidaResumo {
    @Override public Long getId()          { return jpa.id; }
    @Override public Long getTorneioId()   { return jpa.torneioId; }
    @Override public Long getMandanteId()  { return jpa.mandanteId; }
    @Override public Long getVisitanteId() { return jpa.visitanteId; }
    @Override public String getEtapa()     { return jpa.etapa; }
    @Override public boolean isEncerrada() { return jpa.encerrada; }
}

// ─────────────────────────────────────────────────────────────────────────────
// Solicitação
// ─────────────────────────────────────────────────────────────────────────────

@Repository
class SolicitacaoRepositorioAplicacaoImpl implements SolicitacaoRepositorioAplicacao {

    @Autowired SolicitacaoJpaRepository repositorio;

    @Override
    public List<SolicitacaoResumo> pesquisarPorSolicitante(long solicitanteId) {
        return repositorio.findBySolicitanteId(solicitanteId)
                          .stream().map(jpa -> (SolicitacaoResumo) new SolicitacaoJpaResumo(jpa)).toList();
    }

    @Override
    public List<SolicitacaoResumo> pesquisarPendentesPorTorneio(long torneioId) {
        return repositorio.findByTorneioIdAndStatus(torneioId, StatusSolicitacao.PENDENTE.name())
                          .stream().map(jpa -> (SolicitacaoResumo) new SolicitacaoJpaResumo(jpa)).toList();
    }
}

record SolicitacaoJpaResumo(SolicitacaoParticipacaoJpa jpa) implements SolicitacaoResumo {
    @Override public Long getId()            { return jpa.id; }
    @Override public Long getTimeId()        { return jpa.timeId; }
    @Override public Long getTorneioId()     { return jpa.torneioId; }
    @Override public Long getSolicitanteId() { return jpa.solicitanteId; }
    @Override public String getStatus()      { return jpa.status; }
}

// ─────────────────────────────────────────────────────────────────────────────
// Conta de Usuário
// ─────────────────────────────────────────────────────────────────────────────

@Repository
class ContaRepositorioAplicacaoImpl implements ContaRepositorioAplicacao {

    @Autowired ContaUsuarioJpaRepository repositorio;

    @Override
    public Optional<ContaUsuarioResumo> pesquisarPorId(long usuarioId) {
        return repositorio.findById(usuarioId).map(ContaUsuarioJpaResumo::new);
    }
}

record ContaUsuarioJpaResumo(ContaUsuarioJpa jpa) implements ContaUsuarioResumo {
    @Override public Long getId()      { return jpa.id; }
    @Override public String getNome()  { return jpa.nome; }
    @Override public String getEmail() { return jpa.email; }
    @Override public String getTipo()  { return jpa.tipo; }
}

// ─────────────────────────────────────────────────────────────────────────────
// Profissional Esportivo
// ─────────────────────────────────────────────────────────────────────────────

@Repository
class ProfissionalRepositorioAplicacaoImpl implements ProfissionalRepositorioAplicacao {

    @Autowired ProfissionalJpaRepository repositorio;

    @Override
    public List<ProfissionalResumo> pesquisarResumosPorNome(String nome) {
        return repositorio.findByNomeContainingIgnoreCase(nome)
                          .stream().map(jpa -> (ProfissionalResumo) new ProfissionalJpaResumo(jpa)).toList();
    }

    @Override
    public ProfissionalResumoExpandido pesquisarResumoExpandido(long profissionalId) {
        var jpa = repositorio.findById(profissionalId)
                             .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao
                                     .EntidadeNaoEncontradaException("Profissional nao encontrado."));
        return new ProfissionalJpaResumoExpandido(jpa);
    }
}

record ProfissionalJpaResumo(ProfissionalEsportivoJpa jpa) implements ProfissionalResumo {
    @Override public Long getId()     { return jpa.id; }
    @Override public String getNome() { return jpa.nome; }
    @Override public String getTipo() { return jpa.tipo; }
}

record ProfissionalJpaResumoExpandido(ProfissionalEsportivoJpa jpa) implements ProfissionalResumoExpandido {
    @Override
    public ProfissionalResumo getProfissional() { return new ProfissionalJpaResumo(jpa); }

    @Override
    public List<RegistroDeCarreiraResumo> getHistorico() {
        return jpa.historico.stream().map(r -> (RegistroDeCarreiraResumo) new RegistroDeCarreiraJpaResumo(r)).toList();
    }
}

record RegistroDeCarreiraJpaResumo(RegistroDeCarreiraJpa jpa) implements RegistroDeCarreiraResumo {
    @Override public Long getId()              { return jpa.id; }
    @Override public String getNomeDoClube()   { return jpa.nomeDoClube; }
    @Override public LocalDate getDataInicio() { return jpa.dataInicio; }
    @Override public LocalDate getDataFim()    { return jpa.dataFim; }
    @Override public String getMotivoDeSaida() { return jpa.motivoDeSaida; }
    @Override public String getDescricao()      { return jpa.descricao; }
}

// ─────────────────────────────────────────────────────────────────────────────
// Time
// ─────────────────────────────────────────────────────────────────────────────

@Repository
class TimeRepositorioAplicacaoImpl implements TimeRepositorioAplicacao {

    @Autowired com.torneios.infraestrutura.persistencia.jpa.TimeJpaRepository repositorio;

    @Override
    public List<TimeResumo> pesquisarResumosPorResponsavel(long responsavelId) {
        return repositorio.findByResponsavelId(responsavelId)
                          .stream().map(jpa -> (TimeResumo) new TimeJpaResumo(jpa)).toList();
    }

    @Override
    public TimeResumoExpandido pesquisarResumoExpandido(long timeId) {
        var jpa = repositorio.findById(timeId)
                             .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao
                                     .EntidadeNaoEncontradaException("Time nao encontrado."));
        return new TimeJpaResumoExpandido(jpa);
    }
}

record TimeJpaResumo(TimeJpa jpa) implements TimeResumo {
    @Override public Long getId()             { return jpa.id; }
    @Override public String getNome()         { return jpa.nome; }
    @Override public Long getResponsavelId()  { return jpa.responsavelId; }
}

record TimeJpaResumoExpandido(TimeJpa jpa) implements TimeResumoExpandido {
    @Override
    public TimeResumo getTime() { return new TimeJpaResumo(jpa); }

    @Override
    public List<VinculoProfissionalResumo> getElenco() {
        return jpa.elenco.stream().map(v -> (VinculoProfissionalResumo) new VinculoProfissionalJpaResumo(v)).toList();
    }
}

record VinculoProfissionalJpaResumo(VinculoProfissionalJpa jpa) implements VinculoProfissionalResumo {
    @Override public Long getProfissionalId()           { return jpa.profissionalId; }
    @Override public String getFuncao()                 { return jpa.funcao; }
    @Override public LocalDate getDataInicio()          { return jpa.dataInicio; }
    @Override public LocalDate getDataLimiteContrato()  { return jpa.dataLimiteContrato; }
}
