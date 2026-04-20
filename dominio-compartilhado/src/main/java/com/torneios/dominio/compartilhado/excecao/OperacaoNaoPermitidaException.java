package com.torneios.dominio.compartilhado.excecao;

public class OperacaoNaoPermitidaException extends DominioException {

    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
}
