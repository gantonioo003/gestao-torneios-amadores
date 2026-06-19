package com.torneios.dominio.engajamento.chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class GrupoChat {

    private final GrupoChatId id;
    private final String nome;
    private final UsuarioId criadorId;
    private final Set<UsuarioId> participantes;
    private final Set<UsuarioId> convitesPendentes;
    private final List<MensagemChat> mensagens = new ArrayList<>();
    private final LocalDateTime criadoEm;

    public GrupoChat(GrupoChatId id, String nome, UsuarioId criadorId, Collection<UsuarioId> convidados) {
        this(id, nome, criadorId, convidados, List.of(), LocalDateTime.now());
    }

    public GrupoChat(GrupoChatId id, String nome, UsuarioId criadorId,
                     Collection<UsuarioId> participantesDiretos,
                     Collection<UsuarioId> convitesPendentes,
                     LocalDateTime criadoEm) {
        if (id == null || criadorId == null) throw new IllegalArgumentException("Id e criador do grupo sao obrigatorios.");
        if (nome == null || nome.isBlank() || nome.trim().length() > 80) {
            throw new IllegalArgumentException("O nome do grupo deve possuir entre 1 e 80 caracteres.");
        }
        this.id = id;
        this.nome = nome.trim();
        this.criadorId = criadorId;
        this.criadoEm = criadoEm == null ? LocalDateTime.now() : criadoEm;
        this.participantes = new LinkedHashSet<>();
        this.participantes.add(criadorId);
        if (participantesDiretos != null) this.participantes.addAll(participantesDiretos);
        this.convitesPendentes = new LinkedHashSet<>();
        if (convitesPendentes != null) this.convitesPendentes.addAll(convitesPendentes);
        this.convitesPendentes.removeAll(this.participantes);
        if (this.participantes.size() + this.convitesPendentes.size() < 2) {
            throw new IllegalArgumentException("Um grupo precisa possuir pelo menos duas pessoas.");
        }
    }

    public MensagemChat enviarMensagem(MensagemChatId mensagemId, UsuarioId autorId, String conteudo) {
        if (!participantes.contains(autorId)) {
            throw new OperacaoNaoPermitidaException("Apenas participantes podem enviar mensagens no grupo.");
        }
        MensagemChat mensagem = new MensagemChat(mensagemId, autorId, conteudo);
        mensagens.add(mensagem);
        return mensagem;
    }

    public void adicionarParticipante(UsuarioId solicitanteId, UsuarioId participanteId) {
        exigirCriador(solicitanteId);
        participantes.add(participanteId);
    }

    public void convidar(UsuarioId participanteId) {
        if (!participantes.contains(participanteId)) convitesPendentes.add(participanteId);
    }

    public void aceitarConvite(UsuarioId usuarioId) {
        if (!convitesPendentes.remove(usuarioId)) {
            throw new OperacaoNaoPermitidaException("Nao existe convite pendente para este usuario.");
        }
        participantes.add(usuarioId);
    }

    public void recusarConvite(UsuarioId usuarioId) {
        if (!convitesPendentes.remove(usuarioId)) {
            throw new OperacaoNaoPermitidaException("Nao existe convite pendente para este usuario.");
        }
    }

    public void removerParticipante(UsuarioId solicitanteId, UsuarioId participanteId) {
        exigirCriador(solicitanteId);
        if (criadorId.equals(participanteId)) {
            throw new OperacaoNaoPermitidaException("O criador nao pode ser removido do proprio grupo.");
        }
        participantes.remove(participanteId);
    }

    public boolean possuiParticipante(UsuarioId usuarioId) { return participantes.contains(usuarioId); }
    public boolean possuiConvitePendente(UsuarioId usuarioId) { return convitesPendentes.contains(usuarioId); }
    public LocalDateTime getUltimaAtividadeEm() {
        return mensagens.isEmpty() ? criadoEm : mensagens.get(mensagens.size() - 1).getEnviadaEm();
    }
    private void exigirCriador(UsuarioId usuarioId) {
        if (!criadorId.equals(usuarioId)) {
            throw new OperacaoNaoPermitidaException("Apenas o criador pode alterar os participantes do grupo.");
        }
    }
    public GrupoChatId getId() { return id; }
    public String getNome() { return nome; }
    public UsuarioId getCriadorId() { return criadorId; }
    public Set<UsuarioId> getParticipantes() { return Set.copyOf(participantes); }
    public Set<UsuarioId> getConvitesPendentes() { return Set.copyOf(convitesPendentes); }
    public List<MensagemChat> getMensagens() { return List.copyOf(mensagens); }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
