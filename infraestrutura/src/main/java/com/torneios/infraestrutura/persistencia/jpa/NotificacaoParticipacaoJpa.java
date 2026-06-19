package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoRepositorioAplicacao;
import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoRepositorioAplicacao.NotificacaoParticipacao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "NOTIFICACAO_PARTICIPACAO")
class NotificacaoParticipacaoJpa {
    @Id
    Long id;
    Long usuarioId;
    String titulo;
    String mensagem;
    String link;
    boolean lida;
    LocalDateTime criadaEm;
}

interface NotificacaoParticipacaoJpaRepository extends JpaRepository<NotificacaoParticipacaoJpa, Long> {
    List<NotificacaoParticipacaoJpa> findByUsuarioIdOrderByCriadaEmDesc(Long usuarioId);
}

@Repository
class NotificacaoParticipacaoRepositorioAplicacaoJpa
        implements NotificacaoParticipacaoRepositorioAplicacao {

    @Autowired
    NotificacaoParticipacaoJpaRepository repositorio;

    @Override
    public void salvar(NotificacaoParticipacao notificacao) {
        NotificacaoParticipacaoJpa jpa = new NotificacaoParticipacaoJpa();
        jpa.id = notificacao.id();
        jpa.usuarioId = notificacao.usuarioId();
        jpa.titulo = notificacao.titulo();
        jpa.mensagem = notificacao.mensagem();
        jpa.link = notificacao.link();
        jpa.lida = notificacao.lida();
        jpa.criadaEm = notificacao.criadaEm();
        repositorio.save(jpa);
    }

    @Override
    public List<NotificacaoParticipacao> listarPorUsuario(long usuarioId) {
        return repositorio.findByUsuarioIdOrderByCriadaEmDesc(usuarioId).stream()
                .map(jpa -> new NotificacaoParticipacao(
                        jpa.id, jpa.usuarioId, jpa.titulo, jpa.mensagem,
                        jpa.link, jpa.lida, jpa.criadaEm))
                .toList();
    }

    @Override
    public void marcarComoLida(long notificacaoId, long usuarioId) {
        repositorio.findById(notificacaoId)
                .filter(item -> item.usuarioId == usuarioId)
                .ifPresent(item -> {
                    item.lida = true;
                    repositorio.save(item);
                });
    }
}
