package com.torneios.aplicacao.participacao.conta;

public interface IdentidadeExternaVerificador {

    boolean configurado();

    String clientIdPublico();

    IdentidadeExterna verificar(String credencial);
}
