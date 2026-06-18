package com.torneios.dominio.participacao.acesso;

public enum TipoContaUsuario {
    JOGADOR,
    ORGANIZADOR,
    TREINADOR,
    AUXILIAR_TECNICO,
    PREPARADOR_FISICO,
    MEDICO;

    public boolean podeCriarTorneio() {
        return this == ORGANIZADOR;
    }

    public boolean podeGerenciarTimes() {
        return this == ORGANIZADOR || this == TREINADOR || this == AUXILIAR_TECNICO;
    }

    public boolean possuiPerfilProfissional() {
        return this != ORGANIZADOR;
    }
}
