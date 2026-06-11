package com.torneios.dominio.participacao.steps;

import com.torneios.dominio.participacao.ParticipacaoFuncionalidade;

import io.cucumber.java.Before;

public class ParticipacaoHooks extends ParticipacaoFuncionalidade {

    @Before
    public void resetar() {
        super.resetar();
    }
}
