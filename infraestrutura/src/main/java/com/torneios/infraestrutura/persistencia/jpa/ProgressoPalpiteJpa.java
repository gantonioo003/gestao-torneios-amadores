package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.palpite.ProgressoPalpite;
import com.torneios.dominio.engajamento.palpite.ProgressoPalpiteRepositorio;
import com.torneios.dominio.engajamento.palpite.SeloPalpite;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROGRESSO_PALPITE")
class ProgressoPalpiteJpa {
    @Id Long usuarioId;
    int pontos;
    int sequenciaAtual;
    int maiorSequencia;
    int totalPalpites;
    int totalAcertos;
    LocalDate ultimaParticipacao;
    @Lob String selosData;
}

interface ProgressoPalpiteJpaRepository extends JpaRepository<ProgressoPalpiteJpa, Long> {
    List<ProgressoPalpiteJpa> findAllByOrderByPontosDescTotalAcertosDesc();
}

@Repository
class ProgressoPalpiteRepositorioImpl implements ProgressoPalpiteRepositorio {
    @Autowired ProgressoPalpiteJpaRepository repositorio;

    @Override
    public void salvar(ProgressoPalpite progresso) {
        ProgressoPalpiteJpa jpa = repositorio.findById(progresso.getUsuarioId().valor())
                .orElse(new ProgressoPalpiteJpa());
        jpa.usuarioId = progresso.getUsuarioId().valor();
        jpa.pontos = progresso.getPontos();
        jpa.sequenciaAtual = progresso.getSequenciaAtual();
        jpa.maiorSequencia = progresso.getMaiorSequencia();
        jpa.totalPalpites = progresso.getTotalPalpites();
        jpa.totalAcertos = progresso.getTotalAcertos();
        jpa.ultimaParticipacao = progresso.getUltimaParticipacao();
        jpa.selosData = PersistenciaTextoUtil.serializarLista(
                progresso.getSelos().stream().map(Enum::name).toList());
        repositorio.save(jpa);
    }

    @Override
    public Optional<ProgressoPalpite> buscarPorUsuario(UsuarioId usuarioId) {
        return repositorio.findById(usuarioId.valor()).map(this::paraDominio);
    }

    @Override
    public List<ProgressoPalpite> listarRanking() {
        return repositorio.findAllByOrderByPontosDescTotalAcertosDesc().stream()
                .map(this::paraDominio)
                .toList();
    }

    private ProgressoPalpite paraDominio(ProgressoPalpiteJpa jpa) {
        ProgressoPalpite progresso = new ProgressoPalpite(new UsuarioId(jpa.usuarioId));
        ReflexaoDominioJpa.definirCampo(progresso, "pontos", jpa.pontos);
        ReflexaoDominioJpa.definirCampo(progresso, "sequenciaAtual", jpa.sequenciaAtual);
        ReflexaoDominioJpa.definirCampo(progresso, "maiorSequencia", jpa.maiorSequencia);
        ReflexaoDominioJpa.definirCampo(progresso, "totalPalpites", jpa.totalPalpites);
        ReflexaoDominioJpa.definirCampo(progresso, "totalAcertos", jpa.totalAcertos);
        ReflexaoDominioJpa.definirCampo(progresso, "ultimaParticipacao", jpa.ultimaParticipacao);
        var selos = ReflexaoDominioJpa.conjuntoCampo(progresso, "selos");
        PersistenciaTextoUtil.desserializarLista(jpa.selosData).stream()
                .map(SeloPalpite::valueOf)
                .forEach(selos::add);
        return progresso;
    }
}
