package com.torneios.dominio.engajamento.chat;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class ChatPrivadoServico {

    private final ConversaPrivadaRepositorio conversaPrivadaRepositorio;
    private final ConsultaSuporteChat consultaSuporteChat;

    public ChatPrivadoServico(ConversaPrivadaRepositorio conversaPrivadaRepositorio,
                              ConsultaSuporteChat consultaSuporteChat) {
        this.conversaPrivadaRepositorio = Objects.requireNonNull(conversaPrivadaRepositorio,
                "O repositorio de conversas e obrigatorio.");
        this.consultaSuporteChat = Objects.requireNonNull(consultaSuporteChat,
                "A consulta de suporte do chat e obrigatoria.");
    }

    public ConversaPrivada solicitarConversa(ConversaPrivadaId conversaPrivadaId,
                                             UsuarioId solicitanteId,
                                             UsuarioId destinatarioId) {
        validarUsuarioAutenticado(solicitanteId);
        validarUsuarioExistente(destinatarioId);
        impedirConversaDuplicada(solicitanteId, destinatarioId);

        ConversaPrivada conversa = new ConversaPrivada(conversaPrivadaId, solicitanteId, destinatarioId);
        conversaPrivadaRepositorio.salvar(conversa);
        return conversa;
    }

    public ConversaPrivada aprovarSolicitacao(ConversaPrivadaId conversaPrivadaId, UsuarioId destinatarioId) {
        validarUsuarioAutenticado(destinatarioId);
        ConversaPrivada conversa = obterConversa(conversaPrivadaId);
        conversa.aprovar(destinatarioId);
        conversaPrivadaRepositorio.salvar(conversa);
        return conversa;
    }

    public ConversaPrivada recusarSolicitacao(ConversaPrivadaId conversaPrivadaId, UsuarioId destinatarioId) {
        validarUsuarioAutenticado(destinatarioId);
        ConversaPrivada conversa = obterConversa(conversaPrivadaId);
        conversa.recusar(destinatarioId);
        conversaPrivadaRepositorio.salvar(conversa);
        return conversa;
    }

    public MensagemChat enviarMensagem(ConversaPrivadaId conversaPrivadaId,
                                       MensagemChatId mensagemChatId,
                                       UsuarioId autorId,
                                       String conteudo) {
        validarUsuarioAutenticado(autorId);
        ConversaPrivada conversa = obterConversa(conversaPrivadaId);
        MensagemChat mensagem = conversa.enviarMensagem(mensagemChatId, autorId, conteudo);
        conversaPrivadaRepositorio.salvar(conversa);
        return mensagem;
    }

    public List<ConversaPrivada> listarSolicitadas(UsuarioId usuarioId) {
        validarUsuarioAutenticado(usuarioId);
        return conversaPrivadaRepositorio.listarSolicitadasParaUsuario(usuarioId);
    }

    public List<ConversaPrivada> listarSolicitacoesEnviadas(UsuarioId usuarioId) {
        validarUsuarioAutenticado(usuarioId);
        return conversaPrivadaRepositorio.listarSolicitadasPorUsuario(usuarioId);
    }

    public List<ConversaPrivada> listarConversasAprovadas(UsuarioId usuarioId) {
        validarUsuarioAutenticado(usuarioId);
        return conversaPrivadaRepositorio.listarAprovadasPorUsuario(usuarioId);
    }

    public ConversaPrivada consultarConversa(ConversaPrivadaId conversaPrivadaId, UsuarioId usuarioId) {
        validarUsuarioAutenticado(usuarioId);
        ConversaPrivada conversa = obterConversa(conversaPrivadaId);
        if (!conversa.envolve(usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas participantes podem consultar a conversa privada.");
        }
        return conversa;
    }

    private ConversaPrivada obterConversa(ConversaPrivadaId conversaPrivadaId) {
        return conversaPrivadaRepositorio.buscarPorId(conversaPrivadaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conversa privada nao encontrada."));
    }

    private void impedirConversaDuplicada(UsuarioId solicitanteId, UsuarioId destinatarioId) {
        boolean jaExisteConversaAberta = conversaPrivadaRepositorio.listarPorUsuario(solicitanteId).stream()
                .anyMatch(conversa -> conversa.envolveAmbos(solicitanteId, destinatarioId)
                        && conversa.getStatus() != StatusConversa.RECUSADA);
        if (jaExisteConversaAberta) {
            throw new RegraDeNegocioException("Ja existe uma conversa solicitada ou aprovada entre esses usuarios.");
        }
    }

    private void validarUsuarioAutenticado(UsuarioId usuarioId) {
        Objects.requireNonNull(usuarioId, "O usuario do chat e obrigatorio.");
        if (!consultaSuporteChat.usuarioEstaAutenticado(usuarioId)) {
            throw new OperacaoNaoPermitidaException("Apenas usuarios autenticados podem usar o chat.");
        }
    }

    private void validarUsuarioExistente(UsuarioId usuarioId) {
        Objects.requireNonNull(usuarioId, "O destinatario da conversa e obrigatorio.");
        if (!consultaSuporteChat.usuarioExiste(usuarioId)) {
            throw new EntidadeNaoEncontradaException("Usuario destinatario nao encontrado.");
        }
    }
}
