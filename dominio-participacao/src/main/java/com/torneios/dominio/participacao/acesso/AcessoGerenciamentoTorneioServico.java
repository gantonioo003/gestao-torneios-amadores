package com.torneios.dominio.participacao.acesso;

import java.util.Objects;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class AcessoGerenciamentoTorneioServico {

    private final AutenticacaoServico autenticacaoServico;

    public AcessoGerenciamentoTorneioServico(AutenticacaoServico autenticacaoServico) {
        this.autenticacaoServico = Objects.requireNonNull(autenticacaoServico,
                "O servico de autenticacao e obrigatorio.");
    }

    public boolean podeAcessarCriacaoTorneio(UsuarioId usuarioId) {
        return autenticacaoServico.estaAutenticado(usuarioId);
    }

    public boolean podeAcessarGerenciamentoTorneios(UsuarioId usuarioId) {
        return autenticacaoServico.estaAutenticado(usuarioId);
    }

    public void exigirAcessoCriacaoTorneio(UsuarioId usuarioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
    }

    public void exigirAcessoGerenciamentoTorneios(UsuarioId usuarioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
    }
}
