package com.torneios.apresentacao.participacao.conta;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.participacao.conta.ContaServicoAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaUsuarioResumo;

@RestController
@RequestMapping("backend/conta-usuario")
class ContaUsuarioControlador {

    @Autowired ContaServicoAplicacao contaServicoConsulta;

    @RequestMapping(method = GET, path = "{id}")
    ContaUsuarioResumo buscarPorId(@PathVariable long id) {
        return contaServicoConsulta.pesquisarPorId(id)
                .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException(
                        "Conta de usuario nao encontrada."));
    }
}
