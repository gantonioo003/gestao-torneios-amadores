package com.torneios.dominio.participacao.acesso;

import java.time.LocalDate;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class ContaUsuarioServico {

    private final ContaUsuarioRepositorio contaUsuarioRepositorio;
    private final CodificadorSenha codificadorSenha;

    public ContaUsuarioServico(ContaUsuarioRepositorio contaUsuarioRepositorio, CodificadorSenha codificadorSenha) {
        this.contaUsuarioRepositorio = Objects.requireNonNull(contaUsuarioRepositorio,
                "O repositorio de contas de usuario e obrigatorio.");
        this.codificadorSenha = Objects.requireNonNull(codificadorSenha,
                "O codificador de senha e obrigatorio.");
    }

    public ContaUsuario cadastrarConta(UsuarioId usuarioId, String nome, String email, String senha) {
        return cadastrarConta(usuarioId, nome, email, senha, TipoContaUsuario.ORGANIZADOR);
    }

    public ContaUsuario cadastrarConta(UsuarioId usuarioId,
                                       String nome,
                                       String email,
                                       String senha,
                                       TipoContaUsuario tipo) {
        return cadastrarContaCompleta(
                usuarioId,
                nome,
                gerarNomeUsuarioPadrao(email, usuarioId),
                email,
                null,
                null,
                null,
                null,
                null,
                null,
                senha,
                tipo,
                ProvedorAutenticacao.LOCAL);
    }

    public ContaUsuario cadastrarContaCompleta(UsuarioId usuarioId,
                                               String nome,
                                               String nomeUsuario,
                                               String email,
                                               String telefone,
                                               LocalDate dataNascimento,
                                               String cidade,
                                               String estado,
                                               String biografia,
                                               String fotoPerfilUrl,
                                               String senha,
                                               TipoContaUsuario tipo,
                                               ProvedorAutenticacao provedor) {
        Objects.requireNonNull(usuarioId, "O id do usuario e obrigatorio.");
        Objects.requireNonNull(tipo, "O tipo da conta e obrigatorio.");
        Objects.requireNonNull(provedor, "O provedor de autenticacao e obrigatorio.");
        if (contaUsuarioRepositorio.buscarPorId(usuarioId).isPresent()) {
            throw new RegraDeNegocioException("Ja existe uma conta para o usuario informado.");
        }
        if (contaUsuarioRepositorio.buscarPorEmail(normalizarEmail(email)).isPresent()) {
            throw new RegraDeNegocioException("Ja existe uma conta cadastrada com este email.");
        }
        String nomeUsuarioNormalizado = normalizarNomeUsuario(nomeUsuario);
        if (contaUsuarioRepositorio.buscarPorNomeUsuario(nomeUsuarioNormalizado).isPresent()) {
            throw new RegraDeNegocioException("Este nome de usuario ja esta em uso.");
        }

        validarSenhaInformada(senha);
        ContaUsuario contaUsuario = new ContaUsuario(
                usuarioId,
                nome,
                nomeUsuarioNormalizado,
                email,
                telefone,
                dataNascimento,
                cidade,
                estado,
                biografia,
                fotoPerfilUrl,
                codificadorSenha.codificar(senha),
                tipo,
                provedor);
        contaUsuarioRepositorio.salvar(contaUsuario);
        return contaUsuario;
    }

    public ContaUsuario autenticar(String email, String senha) {
        ContaUsuario contaUsuario = contaUsuarioRepositorio.buscarPorEmail(normalizarEmail(email))
                .orElseThrow(() -> new OperacaoNaoPermitidaException("Email ou senha invalidos."));
        if (!codificadorSenha.confere(senha, contaUsuario.getSenhaArmazenada())) {
            throw new OperacaoNaoPermitidaException("Email ou senha invalidos.");
        }
        if (codificadorSenha.precisaRehash(contaUsuario.getSenhaArmazenada())) {
            contaUsuario.alterarSenha(codificadorSenha.codificar(senha));
            contaUsuarioRepositorio.salvar(contaUsuario);
        }
        return contaUsuario;
    }

    public ContaUsuario autenticarExterno(String email) {
        return contaUsuarioRepositorio.buscarPorEmail(normalizarEmail(email))
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Ainda nao existe uma conta vinculada a este e-mail. Crie a conta primeiro."));
    }

    public ContaUsuario editarDados(UsuarioId usuarioId, String novoNome, String novoEmail) {
        ContaUsuario contaUsuario = obterConta(usuarioId);
        return editarDados(
                usuarioId,
                novoNome,
                contaUsuario.getNomeUsuario(),
                novoEmail,
                contaUsuario.getTelefone(),
                contaUsuario.getDataNascimento(),
                contaUsuario.getCidade(),
                contaUsuario.getEstado(),
                contaUsuario.getBiografia(),
                contaUsuario.getFotoPerfilUrl());
    }

    public ContaUsuario editarDados(UsuarioId usuarioId,
                                    String novoNome,
                                    String novoNomeUsuario,
                                    String novoEmail,
                                    String novoTelefone,
                                    LocalDate novaDataNascimento,
                                    String novaCidade,
                                    String novoEstado,
                                    String novaBiografia,
                                    String novaFotoPerfilUrl) {
        ContaUsuario contaUsuario = obterConta(usuarioId);
        String emailNormalizado = normalizarEmail(novoEmail);
        contaUsuarioRepositorio.buscarPorEmail(emailNormalizado)
                .filter(contaEncontrada -> !contaEncontrada.getId().equals(usuarioId))
                .ifPresent(contaEncontrada -> {
                    throw new RegraDeNegocioException("Ja existe uma conta cadastrada com este email.");
                });

        String nomeUsuarioNormalizado = normalizarNomeUsuario(novoNomeUsuario);
        contaUsuarioRepositorio.buscarPorNomeUsuario(nomeUsuarioNormalizado)
                .filter(contaEncontrada -> !contaEncontrada.getId().equals(usuarioId))
                .ifPresent(contaEncontrada -> {
                    throw new RegraDeNegocioException("Este nome de usuario ja esta em uso.");
                });

        contaUsuario.editarDados(
                novoNome,
                nomeUsuarioNormalizado,
                emailNormalizado,
                novoTelefone,
                novaDataNascimento,
                novaCidade,
                novoEstado,
                novaBiografia,
                novaFotoPerfilUrl);
        contaUsuarioRepositorio.salvar(contaUsuario);
        return contaUsuario;
    }

    public void alterarSenha(UsuarioId usuarioId, String senhaAtual, String novaSenha) {
        ContaUsuario contaUsuario = obterConta(usuarioId);
        if (!codificadorSenha.confere(senhaAtual, contaUsuario.getSenhaArmazenada())) {
            throw new OperacaoNaoPermitidaException("A senha atual informada esta incorreta.");
        }
        validarSenhaInformada(novaSenha);
        contaUsuario.alterarSenha(codificadorSenha.codificar(novaSenha));
        contaUsuarioRepositorio.salvar(contaUsuario);
    }

    public ContaUsuario salvarTorneio(UsuarioId usuarioId, TorneioId torneioId) {
        ContaUsuario contaUsuario = obterConta(usuarioId);
        contaUsuario.salvarTorneio(torneioId);
        contaUsuarioRepositorio.salvar(contaUsuario);
        return contaUsuario;
    }

    public ContaUsuario removerTorneioSalvo(UsuarioId usuarioId, TorneioId torneioId) {
        ContaUsuario contaUsuario = obterConta(usuarioId);
        contaUsuario.removerTorneioSalvo(torneioId);
        contaUsuarioRepositorio.salvar(contaUsuario);
        return contaUsuario;
    }

    public void excluirConta(UsuarioId usuarioId) {
        obterConta(usuarioId);
        contaUsuarioRepositorio.remover(usuarioId);
    }

    public ContaUsuario obterConta(UsuarioId usuarioId) {
        return contaUsuarioRepositorio.buscarPorId(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta de usuario nao encontrada."));
    }

    public ContaUsuario exigirPodeCriarTorneio(UsuarioId usuarioId) {
        ContaUsuario conta = obterConta(usuarioId);
        if (!conta.podeCriarTorneio()) {
            throw new OperacaoNaoPermitidaException("Apenas contas de organizador podem criar e gerenciar torneios.");
        }
        return conta;
    }

    public ContaUsuario exigirPodeGerenciarTimes(UsuarioId usuarioId) {
        ContaUsuario conta = obterConta(usuarioId);
        if (!conta.podeGerenciarTimes()) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas contas de treinador podem criar ou alterar times e elencos.");
        }
        return conta;
    }

    private String normalizarEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("O email do usuario deve ser valido.");
        }
        return email.trim().toLowerCase();
    }

    private void validarSenhaInformada(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("A senha deve possuir pelo menos 6 caracteres.");
        }
    }

    private String normalizarNomeUsuario(String nomeUsuario) {
        if (nomeUsuario == null || nomeUsuario.isBlank()) {
            throw new IllegalArgumentException("O nome de usuario e obrigatorio.");
        }
        return nomeUsuario.trim().toLowerCase();
    }

    private String gerarNomeUsuarioPadrao(String email, UsuarioId usuarioId) {
        String base = normalizarEmail(email).split("@", 2)[0].replaceAll("[^a-z0-9._]", "");
        if (base.length() < 3) {
            base = "usuario";
        }
        String sufixo = String.valueOf(usuarioId.valor());
        sufixo = sufixo.substring(Math.max(0, sufixo.length() - 12));
        int limite = Math.min(base.length(), 30 - sufixo.length() - 1);
        return base.substring(0, limite) + "_" + sufixo;
    }
}
