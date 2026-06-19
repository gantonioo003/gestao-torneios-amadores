package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.tecnico.Tecnico;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.time.Time;
import com.torneios.dominio.participacao.time.TimeRepositorio;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "TIME")
class TimeJpa {
    @Id
    Long id;
    String nome;
    @jakarta.persistence.Lob
    String imagemUrl;
    Long responsavelId;

    @ElementCollection
    @CollectionTable(name = "TIME_TORNEIO", joinColumns = @JoinColumn(name = "TIME_ID"))
    @Column(name = "TORNEIO_ID")
    List<Long> torneiosVinculados = new ArrayList<>();

    @OneToMany(mappedBy = "time", cascade = CascadeType.ALL, orphanRemoval = true)
    List<VinculoProfissionalJpa> elenco = new ArrayList<>();
}

@Entity
@Table(name = "VINCULO_PROFISSIONAL")
class VinculoProfissionalJpa {
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "TIME_ID")
    TimeJpa time;

    Long profissionalId;
    String funcao;
    LocalDate dataInicio;
    LocalDate dataLimiteContrato;
}

interface TimeJpaRepository extends JpaRepository<TimeJpa, Long> {
    List<TimeJpa> findByResponsavelId(Long responsavelId);
    List<TimeJpa> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
}

@Repository
class TimeRepositorioImpl implements TimeRepositorio {

    @Autowired
    TimeJpaRepository repositorio;

    @Override
    public void salvar(Time time) {
        var jpa = repositorio.findById(time.getId().valor()).orElse(new TimeJpa());
        jpa.id = time.getId().valor();
        jpa.nome = time.getNome();
        jpa.imagemUrl = time.getImagemUrl();
        jpa.responsavelId = time.getResponsavel().valor();

        jpa.torneiosVinculados.clear();
        for (var torneioId : time.getTorneiosVinculados()) {
            jpa.torneiosVinculados.add(torneioId.valor());
        }

        Map<Long, VinculoProfissionalJpa> vinculosExistentes = new HashMap<>();
        Set<Long> idsEmUso = new HashSet<>();
        for (var vinculo : jpa.elenco) {
            vinculosExistentes.put(vinculo.profissionalId, vinculo);
            idsEmUso.add(vinculo.id);
        }

        Set<Long> profissionaisMantidos = new HashSet<>();
        long seq = 1L;
        for (var v : time.getElenco()) {
            long profissionalId = v.getProfissionalId().valor();
            profissionaisMantidos.add(profissionalId);
            var vJpa = vinculosExistentes.get(profissionalId);
            if (vJpa == null) {
                vJpa = new VinculoProfissionalJpa();
                long novoId;
                do {
                    novoId = (jpa.id * 10000) + seq++;
                } while (idsEmUso.contains(novoId));
                vJpa.id = novoId;
                idsEmUso.add(novoId);
                jpa.elenco.add(vJpa);
            }
            vJpa.time = jpa;
            vJpa.profissionalId = profissionalId;
            vJpa.funcao = v.getFuncao();
            vJpa.dataInicio = v.getDataInicio();
            vJpa.dataLimiteContrato = v.getDataLimiteContrato();
        }
        jpa.elenco.removeIf(vinculo -> !profissionaisMantidos.contains(vinculo.profissionalId));

        repositorio.save(jpa);
    }

    @Override
    public Optional<Time> buscarPorId(TimeId timeId) {
        return repositorio.findById(timeId.valor()).map(this::toDomain);
    }

    @Override
    public List<Time> listarPorResponsavel(UsuarioId usuarioId) {
        return repositorio.findByResponsavelId(usuarioId.valor())
            .stream().map(this::toDomain).toList();
    }

    @Override
    public List<Time> listarTodos() {
        return repositorio.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void remover(TimeId timeId) {
        repositorio.deleteById(timeId.valor());
    }

    private Time toDomain(TimeJpa jpa) {
        var time = new Time(
            new TimeId(jpa.id),
            jpa.nome,
            new UsuarioId(jpa.responsavelId),
            jpa.imagemUrl
        );
        for (var torneioId : jpa.torneiosVinculados) {
            time.vincularAoTorneio(new TorneioId(torneioId));
        }
        for (var v : jpa.elenco) {
            time.vincularProfissional(
                new ProfissionalEsportivoId(v.profissionalId),
                v.funcao, v.dataInicio, v.dataLimiteContrato
            );
            if ("TREINADOR".equals(v.funcao) && time.getTecnico() == null) {
                time.associarTecnico(new Tecnico(
                        new TecnicoId(v.profissionalId),
                        "Treinador do " + jpa.nome,
                        new TimeId(jpa.id)));
            }
        }
        return time;
    }
}
