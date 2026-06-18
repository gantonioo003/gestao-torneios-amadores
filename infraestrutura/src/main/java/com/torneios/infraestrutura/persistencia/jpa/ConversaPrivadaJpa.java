package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.ConversaPrivada;
import com.torneios.dominio.engajamento.chat.ConversaPrivadaId;
import com.torneios.dominio.engajamento.chat.ConversaPrivadaRepositorio;
import com.torneios.dominio.engajamento.chat.MensagemChat;
import com.torneios.dominio.engajamento.chat.MensagemChatId;
import com.torneios.dominio.engajamento.chat.StatusConversa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONVERSA_PRIVADA")
class ConversaPrivadaJpa {

    @Id
    Long id;

    Long solicitanteId;
    Long destinatarioId;
    String status;
    LocalDateTime solicitadaEm;

    @Lob
    String mensagensData;
}

interface ConversaPrivadaJpaRepository extends JpaRepository<ConversaPrivadaJpa, Long> {
    List<ConversaPrivadaJpa> findBySolicitanteIdOrDestinatarioId(Long solicitanteId, Long destinatarioId);
    List<ConversaPrivadaJpa> findByDestinatarioIdAndStatus(Long destinatarioId, String status);
    List<ConversaPrivadaJpa> findBySolicitanteIdAndStatus(Long solicitanteId, String status);
    List<ConversaPrivadaJpa> findByStatusAndSolicitanteIdOrStatusAndDestinatarioId(
            String primeiroStatus,
            Long solicitanteId,
            String segundoStatus,
            Long destinatarioId);
}

@Repository
class ConversaPrivadaRepositorioImpl implements ConversaPrivadaRepositorio {

    @Autowired
    ConversaPrivadaJpaRepository repositorio;

    @Override
    public void salvar(ConversaPrivada conversaPrivada) {
        var jpa = repositorio.findById(conversaPrivada.getId().valor()).orElse(new ConversaPrivadaJpa());
        jpa.id = conversaPrivada.getId().valor();
        jpa.solicitanteId = conversaPrivada.getSolicitanteId().valor();
        jpa.destinatarioId = conversaPrivada.getDestinatarioId().valor();
        jpa.status = conversaPrivada.getStatus().name();
        jpa.solicitadaEm = conversaPrivada.getSolicitadaEm();
        jpa.mensagensData = PersistenciaTextoUtil.serializarLinhas(
                conversaPrivada.getMensagens().stream()
                        .map(this::serializarMensagem)
                        .toList());
        repositorio.save(jpa);
    }

    @Override
    public Optional<ConversaPrivada> buscarPorId(ConversaPrivadaId conversaPrivadaId) {
        return repositorio.findById(conversaPrivadaId.valor()).map(this::paraDominio);
    }

    @Override
    public List<ConversaPrivada> listarPorUsuario(UsuarioId usuarioId) {
        return repositorio.findBySolicitanteIdOrDestinatarioId(usuarioId.valor(), usuarioId.valor()).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<ConversaPrivada> listarSolicitadasParaUsuario(UsuarioId usuarioId) {
        return repositorio.findByDestinatarioIdAndStatus(usuarioId.valor(), StatusConversa.SOLICITADA.name()).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<ConversaPrivada> listarSolicitadasPorUsuario(UsuarioId usuarioId) {
        return repositorio.findBySolicitanteIdAndStatus(usuarioId.valor(), StatusConversa.SOLICITADA.name()).stream()
                .map(this::paraDominio)
                .toList();
    }

    @Override
    public List<ConversaPrivada> listarAprovadasPorUsuario(UsuarioId usuarioId) {
        return repositorio.findByStatusAndSolicitanteIdOrStatusAndDestinatarioId(
                        StatusConversa.APROVADA.name(),
                        usuarioId.valor(),
                        StatusConversa.APROVADA.name(),
                        usuarioId.valor()).stream()
                .map(this::paraDominio)
                .toList();
    }

    private ConversaPrivada paraDominio(ConversaPrivadaJpa jpa) {
        ConversaPrivada conversa = new ConversaPrivada(
                new ConversaPrivadaId(jpa.id),
                new UsuarioId(jpa.solicitanteId),
                new UsuarioId(jpa.destinatarioId),
                jpa.solicitadaEm == null ? LocalDateTime.now() : jpa.solicitadaEm);
        StatusConversa status = StatusConversa.valueOf(jpa.status);
        if (status == StatusConversa.APROVADA) {
            conversa.aprovar(new UsuarioId(jpa.destinatarioId));
        } else if (status == StatusConversa.RECUSADA) {
            conversa.recusar(new UsuarioId(jpa.destinatarioId));
        }
        if (status == StatusConversa.APROVADA) {
            for (List<String> linha : PersistenciaTextoUtil.desserializarLinhas(jpa.mensagensData)) {
                MensagemChat mensagem = conversa.enviarMensagem(
                        new MensagemChatId(Long.parseLong(linha.get(0))),
                        new UsuarioId(Long.parseLong(linha.get(1))),
                        linha.get(2));
                ReflexaoDominioJpa.definirCampo(mensagem, "enviadaEm", PersistenciaTextoUtil.paraLocalDateTime(linha.get(3)));
            }
        }
        return conversa;
    }

    private List<String> serializarMensagem(MensagemChat mensagemChat) {
        List<String> linha = new ArrayList<>();
        linha.add(String.valueOf(mensagemChat.getId().valor()));
        linha.add(String.valueOf(mensagemChat.getAutorId().valor()));
        linha.add(mensagemChat.getConteudo());
        linha.add(PersistenciaTextoUtil.deLocalDateTime(mensagemChat.getEnviadaEm()));
        return linha;
    }
}
