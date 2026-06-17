package com.torneios.apresentacao.participacao.conta;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.participacao.conta.ContaServicoAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaUsuarioResumo;

@RestController
@RequestMapping("backend/conta-usuario")
class ContaUsuarioControlador {

    @Autowired
    ContaServicoAplicacao contaServicoConsulta;

    @RequestMapping(method = GET, path = "{id}")
    ContaUsuarioResumo buscarPorId(@PathVariable long id) {
        return contaServicoConsulta.pesquisarPorId(id)
                .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException(
                        "Conta de usuario nao encontrada."));
    }

    @RequestMapping(method = POST, path = "salvar")
    ContaUsuarioResumo salvar(@RequestBody ContaDto dto) {
        return contaServicoConsulta.cadastrarConta(
                gerarId(),
                dto.nome,
                dto.email,
                dto.senha,
                dto.tipo);
    }

    @RequestMapping(method = POST, path = "autenticar")
    ContaUsuarioResumo autenticar(@RequestBody AutenticacaoDto dto) {
        return contaServicoConsulta.autenticar(dto.email, dto.senha);
    }

    @RequestMapping(method = POST, path = "{id}/salvar")
    ContaUsuarioResumo atualizar(@PathVariable long id, @RequestBody AtualizacaoContaDto dto) {
        return contaServicoConsulta.editarDados(id, dto.nome, dto.email);
    }

    @RequestMapping(method = POST, path = "{id}/alterar-senha")
    void alterarSenha(@PathVariable long id, @RequestBody SenhaDto dto) {
        contaServicoConsulta.alterarSenha(id, dto.novaSenha);
    }

    @RequestMapping(method = POST, path = "{id}/excluir")
    void excluir(@PathVariable long id) {
        contaServicoConsulta.excluirConta(id);
    }

    private long gerarId() {
        return System.currentTimeMillis();
    }

    static class ContaDto {
        public String nome;
        public String email;
        public String senha;
        public String tipo;
    }

    static class AtualizacaoContaDto {
        public String nome;
        public String email;
    }

    static class AutenticacaoDto {
        public String email;
        public String senha;
    }

    static class SenhaDto {
        public String novaSenha;
    }
}
