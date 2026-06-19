package com.torneios.apresentacao.participacao.notificacao;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoRepositorioAplicacao.NotificacaoParticipacao;
import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/notificacoes")
class NotificacaoParticipacaoControlador {

    @Autowired
    NotificacaoParticipacaoServicoAplicacao servico;

    @RequestMapping(method = GET)
    List<NotificacaoParticipacao> listar(HttpSession sessao) {
        return servico.listar(SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/ler")
    void marcarComoLida(@PathVariable long id, HttpSession sessao) {
        servico.marcarComoLida(id, SessaoUsuario.exigirUsuarioId(sessao));
    }
}
