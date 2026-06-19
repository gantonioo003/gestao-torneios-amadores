package com.torneios.dominio.engajamento.feed;

public record DenunciaId(long valor) {
    public DenunciaId {
        if (valor <= 0) throw new IllegalArgumentException("O id da denuncia deve ser positivo.");
    }
}
