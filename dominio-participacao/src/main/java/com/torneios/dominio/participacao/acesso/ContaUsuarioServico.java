package com.torneios.dominio.participacao.acesso;

import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class ContaUsuarioServico {

    private final ContaUsuarioRepositorio contaUsuarioRepositorio;

    public ContaUsuarioServico(ContaUsuarioRepositorio contaUsuarioRepositorio) {
        this.contaUsuarioRepositorio = Objects.requireNonNull(contaUsuarioRepositorio,
                "O repositorio de contas de usuario e obrigatorio.");
    }

    public ContaUsuario cadastrarConta(UsuarioId usuarioId, String nome, String email, String senha) {
        Objects.requireNonNull(usuarioId, "O id do usuario e obrigatorio.");
        if (contaUsuarioRepositorio.buscarPorId(usuarioId).isPresent()) {
            throw new RegraDeNegocioException("Ja existe uma conta para o usuario informado.");
        }
        if (contaUsuarioRepositorio.buscarPorEmail(normalizarEmail(email)).isPresent()) {
            throw new RegraDeNegocioException("Ja existe uma conta cadastrada com este email.");
        }

        ContaUsuario contaUsuario = new ContaUsuario(usuarioId, nome, email, senha);
        contaUsuarioRepositorio.salvar(contaUsuario);
        return contaUsuario;
    }

    public ContaUsuario autenticar(String email, String senha) {
        ContaUsuario contaUsuario = contaUsuarioRepositorio.buscarPorEmail(normalizarEmail(email))
                .orElseThrow(() -> new OperacaoNaoPermitidaException("Email ou senha invalidos."));
        if (!contaUsuario.senhaConfere(senha)) {
            throw new OperacaoNaoPermitidaException("Email ou senha invalidos.");
        }
        return contaUsuario;
    }

    public ContaUsuario editarDados(UsuarioId usuarioId, String novoNome, String novoEmail) {
        ContaUsuario contaUsuario = obterConta(usuarioId);
        String emailNormalizado = normalizarEmail(novoEmail);
        contaUsuarioRepositorio.buscarPorEmail(emailNormalizado)
                .filter(contaEncontrada -> !contaEncontrada.getId().equals(usuarioId))
                .ifPresent(contaEncontrada -> {
                    throw new RegraDeNegocioException("Ja existe uma conta cadastrada com este email.");
                });

        contaUsuario.editarDados(novoNome, novoEmail);
        contaUsuarioRepositorio.salvar(contaUsuario);
        return contaUsuario;
    }

    public void alterarSenha(UsuarioId usuarioId, String novaSenha) {
        ContaUsuario contaUsuario = obterConta(usuarioId);
        contaUsuario.alterarSenha(novaSenha);
        contaUsuarioRepositorio.salvar(contaUsuario);
    }

    public void excluirConta(UsuarioId usuarioId) {
        obterConta(usuarioId);
        contaUsuarioRepositorio.remover(usuarioId);
    }

    public ContaUsuario obterConta(UsuarioId usuarioId) {
        return contaUsuarioRepositorio.buscarPorId(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta de usuario nao encontrada."));
    }

    private String normalizarEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("O email do usuario deve ser valido.");
        }
        return email.trim().toLowerCase();
    }
}
