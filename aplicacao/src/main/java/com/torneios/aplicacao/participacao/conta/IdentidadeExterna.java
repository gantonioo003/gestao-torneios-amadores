package com.torneios.aplicacao.participacao.conta;

public record IdentidadeExterna(
        String identificador,
        String nome,
        String email,
        String fotoPerfilUrl,
        boolean emailVerificado) {
}
