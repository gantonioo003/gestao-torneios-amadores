package com.torneios.dominio.engajamento.palpite;

import java.util.Objects;
import java.util.Optional;

import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class Palpite {

    private final PalpiteId id;
    private final UsuarioId usuarioId;
    private final EventoAlvo eventoAlvo;
    private OpcaoPalpite opcao;
    private boolean apurado;
    private Boolean acertou;

    public Palpite(PalpiteId id, UsuarioId usuarioId, EventoAlvo eventoAlvo, OpcaoPalpite opcao) {
        this.id = Objects.requireNonNull(id, "O id do palpite e obrigatorio.");
        this.usuarioId = Objects.requireNonNull(usuarioId, "O usuario do palpite e obrigatorio.");
        this.eventoAlvo = Objects.requireNonNull(eventoAlvo, "O evento alvo do palpite e obrigatorio.");
        this.opcao = Objects.requireNonNull(opcao, "A opcao do palpite e obrigatoria.");
        this.apurado = false;
        this.acertou = null;
    }

    public PalpiteId getId() {
        return id;
    }

    public UsuarioId getUsuarioId() {
        return usuarioId;
    }

    public EventoAlvo getEventoAlvo() {
        return eventoAlvo;
    }

    public OpcaoPalpite getOpcao() {
        return opcao;
    }

    public boolean estaApurado() {
        return apurado;
    }

    public Optional<Boolean> acertou() {
        return Optional.ofNullable(acertou);
    }

    public void alterarOpcao(OpcaoPalpite novaOpcao) {
        if (apurado) {
            throw new OperacaoNaoPermitidaException(
                    "Nao e permitido alterar um palpite ja apurado.");
        }
        this.opcao = Objects.requireNonNull(novaOpcao, "A opcao do palpite e obrigatoria.");
    }

    public void apurar(long resultadoReal) {
        if (apurado) {
            throw new OperacaoNaoPermitidaException("O palpite ja foi apurado.");
        }
        this.apurado = true;
        this.acertou = opcao.correspondeA(resultadoReal);
    }
}
