package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.profissional.MotivoDeSaida;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivo;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreira;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreiraId;
import com.torneios.dominio.participacao.profissional.TipoProfissional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROFISSIONAL_ESPORTIVO")
class ProfissionalEsportivoJpa {
    @Id
    Long id;
    String nome;
    String tipo;
    Long cadastranteId;

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, orphanRemoval = true)
    List<RegistroDeCarreiraJpa> historico = new ArrayList<>();
}

@Entity
@Table(name = "REGISTRO_DE_CARREIRA")
class RegistroDeCarreiraJpa {
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "PROFISSIONAL_ID")
    ProfissionalEsportivoJpa profissional;

    String nomeDoClube;
    LocalDate dataInicio;
    LocalDate dataFim;
    String motivoDeSaida;
    String descricao;
}

interface ProfissionalJpaRepository extends JpaRepository<ProfissionalEsportivoJpa, Long> {
    List<ProfissionalEsportivoJpa> findByNomeContainingIgnoreCase(String nome);
}

@Repository
class ProfissionalEsportivoRepositorioImpl implements ProfissionalEsportivoRepositorio {

    @Autowired
    ProfissionalJpaRepository repositorio;

    @Override
    public void salvar(ProfissionalEsportivo p) {
        var jpa = repositorio.findById(p.getId().valor()).orElse(new ProfissionalEsportivoJpa());
        jpa.id = p.getId().valor();
        jpa.nome = p.getNome();
        jpa.tipo = p.getTipo().name();
        jpa.cadastranteId = p.getCadastranteId().valor();

        jpa.historico.clear();
        for (var r : p.getHistorico()) {
            var rJpa = new RegistroDeCarreiraJpa();
            rJpa.id = r.getId().valor();
            rJpa.profissional = jpa;
            rJpa.nomeDoClube = r.getNomeDoClube();
            rJpa.dataInicio = r.getDataInicio();
            rJpa.dataFim = r.getDataFim();
            rJpa.motivoDeSaida = r.getMotivoDeSaida() != null ? r.getMotivoDeSaida().name() : null;
            rJpa.descricao = r.getDescricao();
            jpa.historico.add(rJpa);
        }
        repositorio.save(jpa);
    }

    @Override
    public Optional<ProfissionalEsportivo> buscarPorId(ProfissionalEsportivoId id) {
        return repositorio.findById(id.valor()).map(this::toDomain);
    }

    @Override
    public List<ProfissionalEsportivo> pesquisarPorNome(String nome) {
        return repositorio.findByNomeContainingIgnoreCase(nome)
            .stream().map(this::toDomain).toList();
    }

    @Override
    public void remover(ProfissionalEsportivoId id) {
        repositorio.deleteById(id.valor());
    }

    private ProfissionalEsportivo toDomain(ProfissionalEsportivoJpa jpa) {
        var p = new ProfissionalEsportivo(
            new ProfissionalEsportivoId(jpa.id),
            jpa.nome,
            TipoProfissional.valueOf(jpa.tipo),
            new UsuarioId(jpa.cadastranteId)
        );
        for (var r : jpa.historico) {
            var motivo = r.motivoDeSaida != null ? MotivoDeSaida.valueOf(r.motivoDeSaida) : null;
            p.adicionarRegistroDeCarreira(new RegistroDeCarreira(
                new RegistroDeCarreiraId(r.id),
                r.nomeDoClube, r.dataInicio, r.dataFim, motivo, r.descricao
            ));
        }
        return p;
    }
}
