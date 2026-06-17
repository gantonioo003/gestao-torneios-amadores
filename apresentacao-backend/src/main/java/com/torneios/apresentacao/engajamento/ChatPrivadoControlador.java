package com.torneios.apresentacao.engajamento;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.engajamento.chat.ChatPrivadoServicoAplicacao;

@RestController
@RequestMapping("backend/chat-privado")
class ChatPrivadoControlador {

    @Autowired
    ChatPrivadoServicoAplicacao chatPrivadoServicoAplicacao;

    @RequestMapping(method = POST, path = "solicitar")
    ChatPrivadoServicoAplicacao.ConversaResumo solicitar(@RequestParam long solicitanteId,
                                                         @RequestParam long destinatarioId) {
        return chatPrivadoServicoAplicacao.solicitarConversa(
                System.currentTimeMillis(),
                solicitanteId,
                destinatarioId);
    }

    @RequestMapping(method = POST, path = "{id}/aprovar")
    ChatPrivadoServicoAplicacao.ConversaResumo aprovar(@PathVariable long id, @RequestParam long destinatarioId) {
        return chatPrivadoServicoAplicacao.aprovarSolicitacao(id, destinatarioId);
    }

    @RequestMapping(method = POST, path = "{id}/recusar")
    ChatPrivadoServicoAplicacao.ConversaResumo recusar(@PathVariable long id, @RequestParam long destinatarioId) {
        return chatPrivadoServicoAplicacao.recusarSolicitacao(id, destinatarioId);
    }

    @RequestMapping(method = POST, path = "{id}/mensagem")
    ChatPrivadoServicoAplicacao.MensagemResumo enviarMensagem(@PathVariable long id,
                                                              @RequestBody MensagemDto dto) {
        return chatPrivadoServicoAplicacao.enviarMensagem(
                id,
                System.currentTimeMillis(),
                dto.autorId,
                dto.conteudo);
    }

    @RequestMapping(method = GET, path = "solicitadas")
    List<ChatPrivadoServicoAplicacao.ConversaResumo> solicitadas(@RequestParam long usuarioId) {
        return chatPrivadoServicoAplicacao.listarSolicitadas(usuarioId);
    }

    @RequestMapping(method = GET, path = "aprovadas")
    List<ChatPrivadoServicoAplicacao.ConversaResumo> aprovadas(@RequestParam long usuarioId) {
        return chatPrivadoServicoAplicacao.listarConversasAprovadas(usuarioId);
    }

    static class MensagemDto {
        public long autorId;
        public String conteudo;
    }
}
