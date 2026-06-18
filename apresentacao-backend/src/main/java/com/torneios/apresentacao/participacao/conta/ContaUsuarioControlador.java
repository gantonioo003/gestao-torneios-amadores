package com.torneios.apresentacao.participacao.conta;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;

import com.torneios.apresentacao.SessaoUsuario;
import com.torneios.aplicacao.participacao.conta.ContaAtividadeRepositorioAplicacao.ContaAtividadeResumo;
import com.torneios.aplicacao.participacao.conta.ContaAtividadeServicoAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaServicoAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaUsuarioResumo;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/conta-usuario")
class ContaUsuarioControlador {

    @Autowired
    ContaServicoAplicacao contaServicoConsulta;

    @Autowired
    ContaAtividadeServicoAplicacao contaAtividadeServicoAplicacao;

    @RequestMapping(method = GET, path = "{id}")
    ContaUsuarioResumo buscarPorId(@PathVariable long id) {
        return contaServicoConsulta.pesquisarPorId(id)
                .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException(
                        "Conta de usuario nao encontrada."));
    }

    @RequestMapping(method = GET, path = "perfil/{nomeUsuario}")
    PerfilPublicoDto buscarPorNomeUsuario(@PathVariable String nomeUsuario) {
        ContaUsuarioResumo conta = contaServicoConsulta.pesquisarPorNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException(
                        "Perfil de usuario nao encontrado."));
        return new PerfilPublicoDto(
                conta.getId(),
                conta.getNome(),
                conta.getNomeUsuario(),
                conta.getCidade(),
                conta.getEstado(),
                conta.getBiografia(),
                conta.getFotoPerfilUrl(),
                conta.getTipo(),
                conta.isPodeCriarTorneio(),
                conta.isPodeGerenciarTimes(),
                conta.isPossuiPerfilProfissional());
    }

    @RequestMapping(method = GET, path = "perfil/{nomeUsuario}/atividade")
    ContaAtividadeResumo buscarAtividade(@PathVariable String nomeUsuario, HttpSession sessao) {
        ContaUsuarioResumo conta = contaServicoConsulta.pesquisarPorNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException(
                        "Perfil de usuario nao encontrado."));
        Long usuarioSessao = SessaoUsuario.usuarioIdOuNulo(sessao);
        return contaAtividadeServicoAplicacao.pesquisar(
                conta.getId(),
                usuarioSessao != null && usuarioSessao.equals(conta.getId()));
    }

    @RequestMapping(method = GET, path = "configuracao-autenticacao")
    ConfiguracaoAutenticacaoDto configuracaoAutenticacao() {
        return new ConfiguracaoAutenticacaoDto(
                contaServicoConsulta.autenticacaoExternaConfigurada(),
                contaServicoConsulta.clientIdExternoPublico());
    }

    @RequestMapping(method = POST, path = "salvar")
    ContaUsuarioResumo salvar(@RequestBody ContaDto dto, HttpSession sessao) {
        ContaUsuarioResumo conta = contaServicoConsulta.cadastrarContaCompleta(
                gerarId(),
                dto.nome,
                dto.nomeUsuario,
                dto.email,
                dto.telefone,
                dto.dataNascimento,
                dto.cidade,
                dto.estado,
                dto.biografia,
                dto.senha,
                dto.tipo);
        SessaoUsuario.autenticar(sessao, conta.getId());
        return conta;
    }

    @RequestMapping(method = POST, path = "autenticar/google")
    ContaUsuarioResumo autenticarGoogle(@RequestBody GoogleAutenticacaoDto dto, HttpSession sessao) {
        ContaUsuarioResumo conta;
        try {
            conta = contaServicoConsulta.autenticarGoogle(
                    gerarId(),
                    dto.credencial,
                    dto.tipo,
                    dto.nomeUsuario);
        } catch (OperacaoNaoPermitidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
        SessaoUsuario.autenticar(sessao, conta.getId());
        return conta;
    }

    @RequestMapping(method = POST, path = "autenticar")
    ContaUsuarioResumo autenticar(@RequestBody AutenticacaoDto dto, HttpSession sessao) {
        ContaUsuarioResumo conta;
        try {
            conta = contaServicoConsulta.autenticar(dto.email, dto.senha);
        } catch (OperacaoNaoPermitidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
        SessaoUsuario.autenticar(sessao, conta.getId());
        return conta;
    }

    @RequestMapping(method = GET, path = "sessao")
    ContaUsuarioResumo sessao(HttpSession sessao) {
        return contaServicoConsulta.pesquisarPorId(SessaoUsuario.exigirUsuarioId(sessao))
                .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException(
                        "Conta da sessao nao encontrada."));
    }

    @RequestMapping(method = POST, path = "sair")
    void sair(HttpSession sessao) {
        sessao.invalidate();
    }

    @RequestMapping(method = POST, path = "{id}/salvar")
    ContaUsuarioResumo atualizar(@PathVariable long id, @RequestBody AtualizacaoContaDto dto, HttpSession sessao) {
        SessaoUsuario.exigirMesmoUsuario(sessao, id);
        return contaServicoConsulta.editarDados(
                id,
                dto.nome,
                dto.nomeUsuario,
                dto.email,
                dto.telefone,
                dto.dataNascimento,
                dto.cidade,
                dto.estado,
                dto.biografia,
                dto.fotoPerfilUrl);
    }

    @RequestMapping(method = POST, path = "{id}/alterar-senha")
    void alterarSenha(@PathVariable long id, @RequestBody SenhaDto dto, HttpSession sessao) {
        SessaoUsuario.exigirMesmoUsuario(sessao, id);
        contaServicoConsulta.alterarSenha(id, dto.novaSenha);
    }

    @RequestMapping(method = POST, path = "torneios-salvos/{torneioId}/salvar")
    ContaUsuarioResumo salvarTorneio(@PathVariable long torneioId, HttpSession sessao) {
        return contaServicoConsulta.salvarTorneio(
                SessaoUsuario.exigirUsuarioId(sessao),
                torneioId);
    }

    @RequestMapping(method = POST, path = "torneios-salvos/{torneioId}/remover")
    ContaUsuarioResumo removerTorneioSalvo(@PathVariable long torneioId, HttpSession sessao) {
        return contaServicoConsulta.removerTorneioSalvo(
                SessaoUsuario.exigirUsuarioId(sessao),
                torneioId);
    }

    @RequestMapping(method = POST, path = "{id}/excluir")
    void excluir(@PathVariable long id, HttpSession sessao) {
        SessaoUsuario.exigirMesmoUsuario(sessao, id);
        contaServicoConsulta.excluirConta(id);
        sessao.invalidate();
    }

    private long gerarId() {
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return id == 0 ? 1L : id;
    }

    static class ContaDto {
        public String nome;
        public String nomeUsuario;
        public String email;
        public String telefone;
        public java.time.LocalDate dataNascimento;
        public String cidade;
        public String estado;
        public String biografia;
        public String senha;
        public String tipo;
    }

    static class AtualizacaoContaDto {
        public String nome;
        public String nomeUsuario;
        public String email;
        public String telefone;
        public java.time.LocalDate dataNascimento;
        public String cidade;
        public String estado;
        public String biografia;
        public String fotoPerfilUrl;
    }

    static class AutenticacaoDto {
        public String email;
        public String senha;
    }

    static class SenhaDto {
        public String novaSenha;
    }

    static class GoogleAutenticacaoDto {
        public String credencial;
        public String tipo;
        public String nomeUsuario;
    }

    record ConfiguracaoAutenticacaoDto(boolean googleHabilitado, String googleClientId) {
    }

    record PerfilPublicoDto(Long id,
                            String nome,
                            String nomeUsuario,
                            String cidade,
                            String estado,
                            String biografia,
                            String fotoPerfilUrl,
                            String tipo,
                            boolean podeCriarTorneio,
                            boolean podeGerenciarTimes,
                            boolean possuiPerfilProfissional) {
    }
}
