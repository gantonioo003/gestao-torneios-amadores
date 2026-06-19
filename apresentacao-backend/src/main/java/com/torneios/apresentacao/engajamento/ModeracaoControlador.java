package com.torneios.apresentacao.engajamento;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.engajamento.feed.ModeracaoServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/moderacao")
class ModeracaoControlador {
    @Autowired ModeracaoServicoAplicacao servico;

    @RequestMapping(method = POST, path = "denuncias")
    ModeracaoServicoAplicacao.DenunciaResumo denunciar(@RequestBody DenunciaDto dto, HttpSession sessao) {
        return servico.denunciar(
                System.currentTimeMillis(),
                SessaoUsuario.exigirUsuarioId(sessao),
                dto.tipoAlvo,
                dto.alvoId,
                dto.motivo);
    }

    @RequestMapping(method = GET, path = "denuncias")
    List<ModeracaoServicoAplicacao.DenunciaResumo> listar(HttpSession sessao) {
        return servico.listarPendentes(SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "denuncias/{id}/analisar")
    ModeracaoServicoAplicacao.DenunciaResumo analisar(@PathVariable long id, HttpSession sessao) {
        return servico.marcarAnalisada(id, SessaoUsuario.exigirUsuarioId(sessao));
    }

    static class DenunciaDto {
        public String tipoAlvo;
        public long alvoId;
        public String motivo;
    }
}
