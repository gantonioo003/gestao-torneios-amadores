package com.torneios.aplicacao.engajamento.chat;

import static org.apache.commons.lang3.Validate.notNull;

import java.time.LocalDateTime;
import java.util.List;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.ChatPrivadoServico;
import com.torneios.dominio.engajamento.chat.ConversaPrivada;
import com.torneios.dominio.engajamento.chat.ConversaPrivadaId;
import com.torneios.dominio.engajamento.chat.MensagemChat;
import com.torneios.dominio.engajamento.chat.MensagemChatId;

/**
 * Casos de uso de conversa privada entre usuarios.
 */
public class ChatPrivadoServicoAplicacao {

    private final ChatPrivadoServico chatPrivadoServico;

    public ChatPrivadoServicoAplicacao(ChatPrivadoServico chatPrivadoServico) {
        notNull(chatPrivadoServico, "O servico de chat privado e obrigatorio.");
        this.chatPrivadoServico = chatPrivadoServico;
    }

    public ConversaResumo solicitarConversa(long conversaId, long solicitanteId, long destinatarioId) {
        return converter(chatPrivadoServico.solicitarConversa(
                new ConversaPrivadaId(conversaId),
                new UsuarioId(solicitanteId),
                new UsuarioId(destinatarioId)));
    }

    public ConversaResumo aprovarSolicitacao(long conversaId, long destinatarioId) {
        return converter(chatPrivadoServico.aprovarSolicitacao(
                new ConversaPrivadaId(conversaId),
                new UsuarioId(destinatarioId)));
    }

    public ConversaResumo recusarSolicitacao(long conversaId, long destinatarioId) {
        return converter(chatPrivadoServico.recusarSolicitacao(
                new ConversaPrivadaId(conversaId),
                new UsuarioId(destinatarioId)));
    }

    public MensagemResumo enviarMensagem(long conversaId, long mensagemId, long autorId, String conteudo) {
        return converter(chatPrivadoServico.enviarMensagem(
                new ConversaPrivadaId(conversaId),
                new MensagemChatId(mensagemId),
                new UsuarioId(autorId),
                conteudo));
    }

    public List<ConversaResumo> listarSolicitadas(long usuarioId) {
        return chatPrivadoServico.listarSolicitadas(new UsuarioId(usuarioId)).stream()
                .map(this::converter)
                .toList();
    }

    public List<ConversaResumo> listarConversasAprovadas(long usuarioId) {
        return chatPrivadoServico.listarConversasAprovadas(new UsuarioId(usuarioId)).stream()
                .map(this::converter)
                .toList();
    }

    private ConversaResumo converter(ConversaPrivada conversaPrivada) {
        return new ConversaResumo(
                conversaPrivada.getId().valor(),
                conversaPrivada.getSolicitanteId().valor(),
                conversaPrivada.getDestinatarioId().valor(),
                conversaPrivada.getStatus().name(),
                conversaPrivada.getMensagens().stream().map(this::converter).toList());
    }

    private MensagemResumo converter(MensagemChat mensagemChat) {
        return new MensagemResumo(
                mensagemChat.getId().valor(),
                mensagemChat.getAutorId().valor(),
                mensagemChat.getConteudo(),
                mensagemChat.getEnviadaEm());
    }

    public record ConversaResumo(long id,
                                 long solicitanteId,
                                 long destinatarioId,
                                 String status,
                                 List<MensagemResumo> mensagens) {
    }

    public record MensagemResumo(long id,
                                 long autorId,
                                 String conteudo,
                                 LocalDateTime enviadaEm) {
    }
}
