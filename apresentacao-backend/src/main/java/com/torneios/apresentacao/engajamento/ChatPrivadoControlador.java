package com.torneios.apresentacao.engajamento;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.engajamento.chat.ChatPrivadoServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/chat-privado")
class ChatPrivadoControlador {

    private final AtomicLong geradorId = new AtomicLong(System.currentTimeMillis());

    @Autowired
    ChatPrivadoServicoAplicacao chatPrivadoServicoAplicacao;

    @RequestMapping(method = POST, path = "solicitar")
    ChatPrivadoServicoAplicacao.ConversaResumo solicitar(@RequestParam long destinatarioId,
                                                         HttpSession sessao) {
        return chatPrivadoServicoAplicacao.solicitarConversa(
                proximoId(),
                SessaoUsuario.exigirUsuarioId(sessao),
                destinatarioId);
    }

    @RequestMapping(method = POST, path = "{id}/aprovar")
    ChatPrivadoServicoAplicacao.ConversaResumo aprovar(@PathVariable long id, HttpSession sessao) {
        return chatPrivadoServicoAplicacao.aprovarSolicitacao(
                id,
                SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/recusar")
    ChatPrivadoServicoAplicacao.ConversaResumo recusar(@PathVariable long id, HttpSession sessao) {
        return chatPrivadoServicoAplicacao.recusarSolicitacao(
                id,
                SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/mensagem")
    ChatPrivadoServicoAplicacao.MensagemResumo enviarMensagem(@PathVariable long id,
                                                              @RequestBody MensagemDto dto,
                                                              HttpSession sessao) {
        return chatPrivadoServicoAplicacao.enviarMensagem(
                id,
                proximoId(),
                SessaoUsuario.exigirUsuarioId(sessao),
                dto.conteudo);
    }

    @RequestMapping(method = GET, path = "solicitacoes/recebidas")
    List<ChatPrivadoServicoAplicacao.ConversaResumo> solicitacoesRecebidas(HttpSession sessao) {
        return chatPrivadoServicoAplicacao.listarSolicitadas(SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "solicitacoes/enviadas")
    List<ChatPrivadoServicoAplicacao.ConversaResumo> solicitacoesEnviadas(HttpSession sessao) {
        return chatPrivadoServicoAplicacao.listarSolicitacoesEnviadas(SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "inbox")
    List<ChatPrivadoServicoAplicacao.ConversaResumo> inbox(HttpSession sessao) {
        return chatPrivadoServicoAplicacao.listarConversasAprovadas(SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "{id}")
    ChatPrivadoServicoAplicacao.ConversaResumo consultar(@PathVariable long id,
                                                         HttpSession sessao) {
        return chatPrivadoServicoAplicacao.consultarConversa(
                id,
                SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "usuarios")
    List<ChatPrivadoServicoAplicacao.UsuarioChatResumo> pesquisarUsuarios(
            @RequestParam(defaultValue = "") String termo,
            HttpSession sessao) {
        return chatPrivadoServicoAplicacao.pesquisarUsuarios(
                termo,
                SessaoUsuario.exigirUsuarioId(sessao));
    }

    private long proximoId() {
        return geradorId.incrementAndGet();
    }

    static class MensagemDto {
        public String conteudo;
    }
}
