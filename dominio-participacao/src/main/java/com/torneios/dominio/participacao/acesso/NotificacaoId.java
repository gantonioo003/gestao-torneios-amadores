package com.torneios.dominio.participacao.acesso;

public record NotificacaoId(long valor) {
    public NotificacaoId {
        if (valor <= 0) {
            throw new IllegalArgumentException("O id da notificacao deve ser positivo.");
        }
    }
}
