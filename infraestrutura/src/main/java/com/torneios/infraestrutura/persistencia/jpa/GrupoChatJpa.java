package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.GrupoChat;
import com.torneios.dominio.engajamento.chat.GrupoChatId;
import com.torneios.dominio.engajamento.chat.GrupoChatRepositorio;
import com.torneios.dominio.engajamento.chat.MensagemChat;
import com.torneios.dominio.engajamento.chat.MensagemChatId;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "GRUPO_CHAT")
class GrupoChatJpa {
    @Id Long id;
    String nome;
    Long criadorId;
    LocalDateTime criadoEm;
    @Lob String participantesData;
    @Lob String convitesPendentesData;
    @Lob String mensagensData;
}

interface GrupoChatJpaRepository extends JpaRepository<GrupoChatJpa, Long> {}

@Repository
class GrupoChatRepositorioImpl implements GrupoChatRepositorio {
    @Autowired GrupoChatJpaRepository repositorio;

    @Override
    public void salvar(GrupoChat grupo) {
        GrupoChatJpa jpa = repositorio.findById(grupo.getId().valor()).orElse(new GrupoChatJpa());
        jpa.id = grupo.getId().valor();
        jpa.nome = grupo.getNome();
        jpa.criadorId = grupo.getCriadorId().valor();
        jpa.criadoEm = grupo.getCriadoEm();
        jpa.participantesData = PersistenciaTextoUtil.serializarLista(
                grupo.getParticipantes().stream().map(UsuarioId::valor).map(String::valueOf).toList());
        jpa.convitesPendentesData = PersistenciaTextoUtil.serializarLista(
                grupo.getConvitesPendentes().stream().map(UsuarioId::valor).map(String::valueOf).toList());
        jpa.mensagensData = PersistenciaTextoUtil.serializarLinhas(
                grupo.getMensagens().stream().map(this::serializarMensagem).toList());
        repositorio.save(jpa);
    }

    @Override
    public Optional<GrupoChat> buscarPorId(GrupoChatId grupoId) {
        return repositorio.findById(grupoId.valor()).map(this::paraDominio);
    }

    @Override
    public List<GrupoChat> listarPorUsuario(UsuarioId usuarioId) {
        return repositorio.findAll().stream()
                .map(this::paraDominio)
                .filter(grupo -> grupo.possuiParticipante(usuarioId) || grupo.possuiConvitePendente(usuarioId))
                .toList();
    }

    private GrupoChat paraDominio(GrupoChatJpa jpa) {
        List<UsuarioId> participantes = PersistenciaTextoUtil.desserializarLista(jpa.participantesData).stream()
                .map(Long::parseLong)
                .map(UsuarioId::new)
                .toList();
        List<UsuarioId> pendentes = PersistenciaTextoUtil.desserializarLista(jpa.convitesPendentesData).stream()
                .map(Long::parseLong)
                .map(UsuarioId::new)
                .toList();
        GrupoChat grupo = new GrupoChat(
                new GrupoChatId(jpa.id),
                jpa.nome,
                new UsuarioId(jpa.criadorId),
                participantes,
                pendentes,
                jpa.criadoEm);
        for (List<String> linha : PersistenciaTextoUtil.desserializarLinhas(jpa.mensagensData)) {
            MensagemChat mensagem = grupo.enviarMensagem(
                    new MensagemChatId(Long.parseLong(linha.get(0))),
                    new UsuarioId(Long.parseLong(linha.get(1))),
                    linha.get(2));
            ReflexaoDominioJpa.definirCampo(
                    mensagem, "enviadaEm", PersistenciaTextoUtil.paraLocalDateTime(linha.get(3)));
        }
        return grupo;
    }

    private List<String> serializarMensagem(MensagemChat mensagem) {
        List<String> linha = new ArrayList<>();
        linha.add(String.valueOf(mensagem.getId().valor()));
        linha.add(String.valueOf(mensagem.getAutorId().valor()));
        linha.add(mensagem.getConteudo());
        linha.add(PersistenciaTextoUtil.deLocalDateTime(mensagem.getEnviadaEm()));
        return linha;
    }
}
