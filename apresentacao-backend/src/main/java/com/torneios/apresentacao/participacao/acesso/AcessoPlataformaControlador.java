package com.torneios.apresentacao.participacao.acesso;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.participacao.acesso.AcessoPlataformaServicoAplicacao;

@RestController
@RequestMapping("backend/acesso-plataforma")
class AcessoPlataformaControlador {

    @Autowired
    AcessoPlataformaServicoAplicacao acessoPlataformaServicoAplicacao;

    @RequestMapping(method = GET, path = "torneios-disponiveis")
    List<AcessoPlataformaServicoAplicacao.TorneioDisponivelResumo> visualizarTorneiosDisponiveis() {
        return acessoPlataformaServicoAplicacao.visualizarTorneiosDisponiveis();
    }

    @RequestMapping(method = GET, path = "existem-torneios")
    boolean existemTorneiosDisponiveis() {
        return acessoPlataformaServicoAplicacao.existemTorneiosDisponiveis();
    }

    @RequestMapping(method = GET, path = "pode-criar-torneio")
    boolean podeCriarTorneio(@RequestParam(required = false) Long usuarioId) {
        return acessoPlataformaServicoAplicacao.podeAcessarCriacaoTorneio(usuarioId);
    }

    @RequestMapping(method = GET, path = "pode-gerenciar-torneios")
    boolean podeGerenciarTorneios(@RequestParam(required = false) Long usuarioId) {
        return acessoPlataformaServicoAplicacao.podeAcessarGerenciamentoTorneios(usuarioId);
    }
}
