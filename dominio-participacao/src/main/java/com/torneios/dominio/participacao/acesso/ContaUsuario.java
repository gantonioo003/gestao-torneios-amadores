package com.torneios.dominio.participacao.acesso;

import java.util.Objects;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class ContaUsuario {

    private final UsuarioId id;
    private String nome;
    private String email;
    private String senha;

    public ContaUsuario(UsuarioId id, String nome, String email, String senha) {
        this.id = Objects.requireNonNull(id, "O id da conta e obrigatorio.");
        this.nome = validarNome(nome);
        this.email = validarEmail(email);
        this.senha = validarSenha(senha);
    }

    public UsuarioId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public boolean senhaConfere(String senhaInformada) {
        return senha.equals(senhaInformada);
    }

    public void editarDados(String novoNome, String novoEmail) {
        this.nome = validarNome(novoNome);
        this.email = validarEmail(novoEmail);
    }

    public void alterarSenha(String novaSenha) {
        this.senha = validarSenha(novaSenha);
    }

    private String validarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do usuario e obrigatorio.");
        }
        return valor.trim();
    }

    private String validarEmail(String valor) {
        if (valor == null || valor.isBlank() || !valor.contains("@")) {
            throw new IllegalArgumentException("O email do usuario deve ser valido.");
        }
        return valor.trim().toLowerCase();
    }

    private String validarSenha(String valor) {
        if (valor == null || valor.length() < 6) {
            throw new IllegalArgumentException("A senha deve possuir pelo menos 6 caracteres.");
        }
        return valor;
    }
}
