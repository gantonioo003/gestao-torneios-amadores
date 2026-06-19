package com.torneios.aplicacao.engajamento.chat;

import static org.apache.commons.lang3.Validate.notNull;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.torneios.aplicacao.participacao.conta.ContaRepositorioAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaUsuarioResumo;
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
    private final ContaRepositorioAplicacao contaRepositorio;

    public ChatPrivadoServicoAplicacao(ChatPrivadoServico chatPrivadoServico,
                                       ContaRepositorioAplicacao contaRepositorio) {
        notNull(chatPrivadoServico, "O servico de chat privado e obrigatorio.");
        notNull(contaRepositorio, "O repositorio de contas e obrigatorio.");
        this.chatPrivadoServico = chatPrivadoServico;
        this.contaRepositorio = contaRepositorio;
    }

    public ConversaResumo solicitarConversa(long conversaId, long solicitanteId, long destinatarioId) {
        return converter(chatPrivadoServico.solicitarConversa(
                new ConversaPrivadaId(conversaId),
                new UsuarioId(solicitanteId),
                new UsuarioId(destinatarioId)), solicitanteId);
    }

    public ConversaResumo aprovarSolicitacao(long conversaId, long destinatarioId) {
        return converter(chatPrivadoServico.aprovarSolicitacao(
                new ConversaPrivadaId(conversaId),
                new UsuarioId(destinatarioId)), destinatarioId);
    }

    public ConversaResumo recusarSolicitacao(long conversaId, long destinatarioId) {
        return converter(chatPrivadoServico.recusarSolicitacao(
                new ConversaPrivadaId(conversaId),
                new UsuarioId(destinatarioId)), destinatarioId);
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
                .map(conversa -> converter(conversa, usuarioId))
                .toList();
    }

    public List<ConversaResumo> listarSolicitacoesEnviadas(long usuarioId) {
        return chatPrivadoServico.listarSolicitacoesEnviadas(new UsuarioId(usuarioId)).stream()
                .map(conversa -> converter(conversa, usuarioId))
                .toList();
    }

    public List<ConversaResumo> listarConversasAprovadas(long usuarioId) {
        return chatPrivadoServico.listarConversasAprovadas(new UsuarioId(usuarioId)).stream()
                .sorted(Comparator.comparing(ConversaPrivada::getUltimaAtividadeEm).reversed())
                .map(conversa -> converter(conversa, usuarioId))
                .toList();
    }

    public ConversaResumo consultarConversa(long conversaId, long usuarioId) {
        return converter(chatPrivadoServico.consultarConversa(
                new ConversaPrivadaId(conversaId),
                new UsuarioId(usuarioId)), usuarioId);
    }

    public List<UsuarioChatResumo> pesquisarUsuarios(String termo, long usuarioIdAtual) {
        return contaRepositorio.pesquisarUsuarios(termo, usuarioIdAtual).stream()
                .map(conta -> new UsuarioChatResumo(
                        String.valueOf(conta.getId()),
                        conta.getNome(),
                        conta.getEmail(),
                        conta.getTipo(),
                        conta.getFotoPerfilUrl()))
                .toList();
    }

    private ConversaResumo converter(ConversaPrivada conversaPrivada, long usuarioAtualId) {
        long outroUsuarioId = conversaPrivada.getSolicitanteId().valor() == usuarioAtualId
                ? conversaPrivada.getDestinatarioId().valor()
                : conversaPrivada.getSolicitanteId().valor();
        ContaUsuarioResumo outroUsuario = contaRepositorio.pesquisarPorId(outroUsuarioId)
                .orElseThrow(() -> new IllegalStateException("A conta vinculada a conversa nao foi encontrada."));
        return new ConversaResumo(
                conversaPrivada.getId().valor(),
                conversaPrivada.getSolicitanteId().valor(),
                conversaPrivada.getDestinatarioId().valor(),
                outroUsuarioId,
                outroUsuario.getNome(),
                outroUsuario.getEmail(),
                outroUsuario.getTipo(),
                outroUsuario.getFotoPerfilUrl(),
                conversaPrivada.getStatus().name(),
                conversaPrivada.getSolicitadaEm(),
                conversaPrivada.getUltimaAtividadeEm(),
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
                                 long outroUsuarioId,
                                 String outroUsuarioNome,
                                 String outroUsuarioEmail,
                                 String outroUsuarioTipo,
                                 String outroUsuarioFotoPerfilUrl,
                                 String status,
                                 LocalDateTime solicitadaEm,
                                 LocalDateTime ultimaAtividadeEm,
                                 List<MensagemResumo> mensagens) {
    }

    public record UsuarioChatResumo(String id, String nome, String email, String tipo,
                                    String fotoPerfilUrl) {
    }

    public record MensagemResumo(long id,
                                 long autorId,
                                 String conteudo,
                                 LocalDateTime enviadaEm) {
    }
}
