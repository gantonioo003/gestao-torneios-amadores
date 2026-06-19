package com.torneios.apresentacao.participacao.notificacao;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoServicoAplicacao;
import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoServicoAplicacao.NotificacaoResumo;
import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoServicoAplicacao.PreferenciasResumo;
import com.torneios.apresentacao.SessaoUsuario;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/notificacoes")
class NotificacaoParticipacaoControlador {

    @Autowired
    NotificacaoParticipacaoServicoAplicacao servico;

    @RequestMapping(method = GET)
    List<NotificacaoResumo> listar(
            @org.springframework.web.bind.annotation.RequestParam(
                    required = false, defaultValue = "false") boolean incluirArquivadas,
            HttpSession sessao) {
        return servico.listar(SessaoUsuario.exigirUsuarioId(sessao), incluirArquivadas);
    }

    @RequestMapping(method = POST, path = "{id}/ler")
    void marcarComoLida(@PathVariable long id, HttpSession sessao) {
        servico.marcarComoLida(id, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "ler-todas")
    void marcarTodasComoLidas(HttpSession sessao) {
        servico.marcarTodasComoLidas(SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/arquivar")
    void arquivar(@PathVariable long id, HttpSession sessao) {
        servico.arquivar(id, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "preferencias")
    PreferenciasResumo obterPreferencias(HttpSession sessao) {
        return servico.obterPreferencias(SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "preferencias")
    PreferenciasResumo atualizarPreferencias(
            @org.springframework.web.bind.annotation.RequestBody PreferenciasDto dto,
            HttpSession sessao) {
        return servico.atualizarPreferencias(
                SessaoUsuario.exigirUsuarioId(sessao),
                dto.categoriasAtivas);
    }

    @RequestMapping(method = GET, path = "categorias")
    List<String> listarCategorias() {
        return servico.listarCategorias();
    }

    static class PreferenciasDto {
        public List<String> categoriasAtivas;
    }
}
