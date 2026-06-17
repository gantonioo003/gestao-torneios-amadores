package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.torneios.aplicacao.participacao.profissional.ProfissionalRepositorioAplicacao;
import com.torneios.aplicacao.participacao.profissional.ProfissionalResumo;
import com.torneios.aplicacao.participacao.profissional.ProfissionalResumoExpandido;
import com.torneios.aplicacao.participacao.profissional.RegistroDeCarreiraResumo;
import com.torneios.aplicacao.participacao.time.TimeRepositorioAplicacao;
import com.torneios.aplicacao.participacao.time.TimeResumo;
import com.torneios.aplicacao.participacao.time.TimeResumoExpandido;
import com.torneios.aplicacao.participacao.time.VinculoProfissionalResumo;

@Repository
class ProfissionalRepositorioAplicacaoImpl implements ProfissionalRepositorioAplicacao {

    @Autowired
    ProfissionalJpaRepository repositorio;

    @Override
    public List<ProfissionalResumo> pesquisarResumosPorNome(String nome) {
        return repositorio.findByNomeContainingIgnoreCase(nome).stream()
                .map(jpa -> (ProfissionalResumo) new ProfissionalJpaResumo(jpa))
                .toList();
    }

    @Override
    public ProfissionalResumoExpandido pesquisarResumoExpandido(long profissionalId) {
        var jpa = repositorio.findById(profissionalId)
                .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException(
                        "Profissional nao encontrado."));
        return new ProfissionalJpaResumoExpandido(jpa);
    }
}

record ProfissionalJpaResumo(ProfissionalEsportivoJpa jpa) implements ProfissionalResumo {
    @Override
    public Long getId() {
        return jpa.id;
    }

    @Override
    public String getNome() {
        return jpa.nome;
    }

    @Override
    public String getTipo() {
        return jpa.tipo;
    }
}

record ProfissionalJpaResumoExpandido(ProfissionalEsportivoJpa jpa) implements ProfissionalResumoExpandido {
    @Override
    public ProfissionalResumo getProfissional() {
        return new ProfissionalJpaResumo(jpa);
    }

    @Override
    public List<RegistroDeCarreiraResumo> getHistorico() {
        return jpa.historico.stream()
                .map(r -> (RegistroDeCarreiraResumo) new RegistroDeCarreiraJpaResumo(r))
                .toList();
    }
}

record RegistroDeCarreiraJpaResumo(RegistroDeCarreiraJpa jpa) implements RegistroDeCarreiraResumo {
    @Override
    public Long getId() {
        return jpa.id;
    }

    @Override
    public String getNomeDoClube() {
        return jpa.nomeDoClube;
    }

    @Override
    public LocalDate getDataInicio() {
        return jpa.dataInicio;
    }

    @Override
    public LocalDate getDataFim() {
        return jpa.dataFim;
    }

    @Override
    public String getMotivoDeSaida() {
        return jpa.motivoDeSaida;
    }

    @Override
    public String getDescricao() {
        return jpa.descricao;
    }
}

@Repository
class TimeRepositorioAplicacaoImpl implements TimeRepositorioAplicacao {

    @Autowired
    TimeJpaRepository repositorio;

    @Override
    public List<TimeResumo> pesquisarResumosPorResponsavel(long responsavelId) {
        return repositorio.findByResponsavelId(responsavelId).stream()
                .map(jpa -> (TimeResumo) new TimeJpaResumo(jpa))
                .toList();
    }

    @Override
    public TimeResumoExpandido pesquisarResumoExpandido(long timeId) {
        var jpa = repositorio.findById(timeId)
                .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException(
                        "Time nao encontrado."));
        return new TimeJpaResumoExpandido(jpa);
    }
}

record TimeJpaResumo(TimeJpa jpa) implements TimeResumo {
    @Override
    public Long getId() {
        return jpa.id;
    }

    @Override
    public String getNome() {
        return jpa.nome;
    }

    @Override
    public Long getResponsavelId() {
        return jpa.responsavelId;
    }
}

record TimeJpaResumoExpandido(TimeJpa jpa) implements TimeResumoExpandido {
    @Override
    public TimeResumo getTime() {
        return new TimeJpaResumo(jpa);
    }

    @Override
    public List<VinculoProfissionalResumo> getElenco() {
        return jpa.elenco.stream()
                .map(v -> (VinculoProfissionalResumo) new VinculoProfissionalJpaResumo(v))
                .toList();
    }
}

record VinculoProfissionalJpaResumo(VinculoProfissionalJpa jpa) implements VinculoProfissionalResumo {
    @Override
    public Long getProfissionalId() {
        return jpa.profissionalId;
    }

    @Override
    public String getFuncao() {
        return jpa.funcao;
    }

    @Override
    public LocalDate getDataInicio() {
        return jpa.dataInicio;
    }

    @Override
    public LocalDate getDataLimiteContrato() {
        return jpa.dataLimiteContrato;
    }
}
