package com.torneios.apresentacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;

@RestControllerAdvice
public class TratadorDeExcecoes {

    public record ErroResposta(String mensagem) {}

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta entidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
        return new ErroResposta(ex.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErroResposta operacaoNaoPermitida(OperacaoNaoPermitidaException ex) {
        return new ErroResposta(ex.getMessage());
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta regraDeNegocio(RegraDeNegocioException ex) {
        return new ErroResposta(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta argumentoInvalido(IllegalArgumentException ex) {
        return new ErroResposta(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta estadoInvalido(IllegalStateException ex) {
        return new ErroResposta(ex.getMessage());
    }
}
