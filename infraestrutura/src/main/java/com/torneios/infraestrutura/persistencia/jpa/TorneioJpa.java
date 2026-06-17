package com.torneios.infraestrutura.persistencia.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.enumeracao.StatusTorneio;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.torneio.HistoricoEdicaoTorneio;
import com.torneios.dominio.torneio.torneio.Torneio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "TORNEIO")
class TorneioJpa {

    @Id
    Long id;

    String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "FORMATO")
    FormatoTorneio formato;

    @Enumerated(EnumType.STRING)
    @Column(name = "FORMATO_EQUIPE")
    FormatoEquipe formatoEquipe;

    Long organizadorId;

    boolean aceitaSolicitacoes;

    Integer edicaoAtual;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    StatusTorneio status;

    @ElementCollection
    @CollectionTable(name = "TORNEIO_PARTICIPANTE", joinColumns = @JoinColumn(name = "TORNEIO_ID"))
    @Column(name = "TIME_ID")
    List<Long> participantes = new ArrayList<>();

    @Lob
    String historicoEdicoesData;
}

interface TorneioJpaRepository extends JpaRepository<TorneioJpa, Long> {
    List<TorneioJpa> findByOrganizadorId(Long organizadorId);
}

@Repository
class TorneioRepositorioImpl implements TorneioRepositorio {

    @Autowired
    TorneioJpaRepository repositorio;

    @Override
    public void salvar(Torneio torneio) {
        var jpa = repositorio.findById(torneio.getId().valor()).orElse(new TorneioJpa());
        jpa.id = torneio.getId().valor();
        jpa.nome = torneio.getNome();
        jpa.formato = torneio.getFormato();
        jpa.formatoEquipe = torneio.getFormatoEquipe();
        jpa.organizadorId = torneio.getOrganizadorId().valor();
        jpa.aceitaSolicitacoes = torneio.aceitaSolicitacoes();
        jpa.edicaoAtual = torneio.getEdicaoAtual();
        jpa.status = torneio.getStatus();

        jpa.participantes.clear();
        torneio.getParticipantesAprovados()
               .forEach(p -> jpa.participantes.add(p.getTimeId().valor()));
        jpa.historicoEdicoesData = PersistenciaTextoUtil.serializarLinhas(
                torneio.getHistoricoEdicoes().stream()
                        .map(this::serializarHistorico)
                        .toList());

        repositorio.save(jpa);
    }

    @Override
    public Optional<Torneio> buscarPorId(TorneioId id) {
        return repositorio.findById(id.valor()).map(this::toDomain);
    }

    @Override
    public List<Torneio> listarTodos() {
        return repositorio.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Torneio> listarPorOrganizador(UsuarioId organizadorId) {
        return repositorio.findByOrganizadorId(organizadorId.valor())
                          .stream().map(this::toDomain).toList();
    }

    private Torneio toDomain(TorneioJpa jpa) {
        var torneio = new Torneio(
                new TorneioId(jpa.id),
                jpa.nome,
                jpa.formato,
                jpa.formatoEquipe,
                new UsuarioId(jpa.organizadorId),
                jpa.aceitaSolicitacoes);

        if (jpa.status == StatusTorneio.ESTRUTURA_GERADA || jpa.status == StatusTorneio.INICIADO
                || jpa.status == StatusTorneio.FINALIZADO) {
            jpa.participantes.forEach(id -> torneio.adicionarParticipante(new TimeId(id)));
            torneio.marcarEstruturaGerada();
        }
        if (jpa.status == StatusTorneio.INICIADO || jpa.status == StatusTorneio.FINALIZADO) {
            torneio.iniciar();
        }
        if (jpa.status == StatusTorneio.FINALIZADO) {
            torneio.finalizar();
        }

        if (jpa.status == StatusTorneio.CONFIGURADO) {
            jpa.participantes.forEach(id -> torneio.adicionarParticipante(new TimeId(id)));
        }
        if (jpa.edicaoAtual != null) {
            ReflexaoDominioJpa.definirCampo(torneio, "edicaoAtual", jpa.edicaoAtual);
        }
        ReflexaoDominioJpa.listarCampo(torneio, "historicoEdicoes").addAll(
                PersistenciaTextoUtil.desserializarLinhas(jpa.historicoEdicoesData).stream()
                        .map(linha -> desserializarHistorico(torneio.getId(), linha))
                        .toList());

        return torneio;
    }

    private List<String> serializarHistorico(HistoricoEdicaoTorneio historico) {
        List<String> linha = new ArrayList<>();
        linha.add(String.valueOf(historico.getNumeroEdicao()));
        linha.add(historico.getNomeTorneio());
        linha.add(PersistenciaTextoUtil.serializarLista(
                historico.getParticipantes().stream()
                        .map(TimeId::valor)
                        .map(String::valueOf)
                        .toList()));
        return linha;
    }

    private HistoricoEdicaoTorneio desserializarHistorico(TorneioId torneioId, List<String> linha) {
        return new HistoricoEdicaoTorneio(
                torneioId,
                Integer.parseInt(linha.get(0)),
                linha.get(1),
                PersistenciaTextoUtil.desserializarLista(linha.get(2)).stream()
                        .map(Long::parseLong)
                        .map(TimeId::new)
                        .toList());
    }
}
