package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.CategoriaNotificacao;
import com.torneios.dominio.participacao.acesso.Notificacao;
import com.torneios.dominio.participacao.acesso.NotificacaoId;
import com.torneios.dominio.participacao.acesso.NotificacaoRepositorio;
import com.torneios.dominio.participacao.acesso.PreferenciasNotificacao;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "NOTIFICACAO_PARTICIPACAO")
class NotificacaoParticipacaoJpa {
    @Id
    Long id;
    Long usuarioId;
    @Column(nullable = false, length = 30)
    String categoria;
    @Column(nullable = false, length = 160)
    String titulo;
    @Column(nullable = false, length = 500)
    String mensagem;
    @Column(nullable = false, length = 500)
    String link;
    boolean lida;
    boolean arquivada;
    LocalDateTime criadaEm;
}

@Entity
@Table(name = "PREFERENCIA_NOTIFICACAO")
class PreferenciaNotificacaoJpa {
    @Id
    Long usuarioId;
    boolean torneio;
    boolean time;
    boolean amistoso;
    boolean social;
    boolean sistema;
}

interface NotificacaoParticipacaoJpaRepository extends JpaRepository<NotificacaoParticipacaoJpa, Long> {
    List<NotificacaoParticipacaoJpa> findByUsuarioIdOrderByCriadaEmDesc(Long usuarioId);
}

interface PreferenciaNotificacaoJpaRepository extends JpaRepository<PreferenciaNotificacaoJpa, Long> {
}

@Repository
class NotificacaoParticipacaoRepositorioJpa implements NotificacaoRepositorio {

    @Autowired
    NotificacaoParticipacaoJpaRepository repositorio;

    @Autowired
    PreferenciaNotificacaoJpaRepository preferenciaRepositorio;

    @Override
    public void salvar(Notificacao notificacao) {
        NotificacaoParticipacaoJpa jpa = repositorio.findById(notificacao.getId().valor())
                .orElse(new NotificacaoParticipacaoJpa());
        jpa.id = notificacao.getId().valor();
        jpa.usuarioId = notificacao.getUsuarioId().valor();
        jpa.categoria = notificacao.getCategoria().name();
        jpa.titulo = notificacao.getTitulo();
        jpa.mensagem = notificacao.getMensagem();
        jpa.link = notificacao.getLink();
        jpa.lida = notificacao.isLida();
        jpa.arquivada = notificacao.isArquivada();
        jpa.criadaEm = notificacao.getCriadaEm();
        repositorio.save(jpa);
    }

    @Override
    public Optional<Notificacao> buscarPorId(NotificacaoId id) {
        return repositorio.findById(id.valor()).map(this::toDomain);
    }

    @Override
    public List<Notificacao> listarPorUsuario(UsuarioId usuarioId) {
        return repositorio.findByUsuarioIdOrderByCriadaEmDesc(usuarioId.valor()).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void salvarPreferencias(PreferenciasNotificacao preferencias) {
        PreferenciaNotificacaoJpa jpa = preferenciaRepositorio
                .findById(preferencias.getUsuarioId().valor())
                .orElse(new PreferenciaNotificacaoJpa());
        jpa.usuarioId = preferencias.getUsuarioId().valor();
        jpa.torneio = preferencias.permite(CategoriaNotificacao.TORNEIO);
        jpa.time = preferencias.permite(CategoriaNotificacao.TIME);
        jpa.amistoso = preferencias.permite(CategoriaNotificacao.AMISTOSO);
        jpa.social = preferencias.permite(CategoriaNotificacao.SOCIAL);
        jpa.sistema = preferencias.permite(CategoriaNotificacao.SISTEMA);
        preferenciaRepositorio.save(jpa);
    }

    @Override
    public Optional<PreferenciasNotificacao> buscarPreferencias(UsuarioId usuarioId) {
        return preferenciaRepositorio.findById(usuarioId.valor()).map(jpa -> {
            EnumSet<CategoriaNotificacao> categorias = EnumSet.noneOf(CategoriaNotificacao.class);
            if (jpa.torneio) categorias.add(CategoriaNotificacao.TORNEIO);
            if (jpa.time) categorias.add(CategoriaNotificacao.TIME);
            if (jpa.amistoso) categorias.add(CategoriaNotificacao.AMISTOSO);
            if (jpa.social) categorias.add(CategoriaNotificacao.SOCIAL);
            if (jpa.sistema) categorias.add(CategoriaNotificacao.SISTEMA);
            return new PreferenciasNotificacao(usuarioId, categorias);
        });
    }

    private Notificacao toDomain(NotificacaoParticipacaoJpa jpa) {
        return new Notificacao(
                new NotificacaoId(jpa.id),
                new UsuarioId(jpa.usuarioId),
                CategoriaNotificacao.valueOf(
                        jpa.categoria == null ? CategoriaNotificacao.SISTEMA.name() : jpa.categoria),
                jpa.titulo,
                jpa.mensagem,
                jpa.link,
                jpa.lida,
                jpa.arquivada,
                jpa.criadaEm);
    }
}
