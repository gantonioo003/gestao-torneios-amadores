package com.torneios.apresentacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;

public final class SessaoUsuario {

    private static final String USUARIO_ID = "usuarioId";

    private SessaoUsuario() {
    }

    public static void autenticar(HttpSession sessao, long usuarioId) {
        sessao.setAttribute(USUARIO_ID, usuarioId);
    }

    public static long exigirUsuarioId(HttpSession sessao) {
        Long usuarioId = usuarioIdOuNulo(sessao);
        if (usuarioId != null) {
            return usuarioId;
        }
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Entre ou crie uma conta para acessar o chat privado.");
    }

    public static Long usuarioIdOuNulo(HttpSession sessao) {
        Object usuarioId = sessao.getAttribute(USUARIO_ID);
        return usuarioId instanceof Number numero ? numero.longValue() : null;
    }

    public static void exigirMesmoUsuario(HttpSession sessao, long usuarioId) {
        if (exigirUsuarioId(sessao) != usuarioId) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Voce nao tem permissao para alterar os dados desta conta.");
        }
    }
}
