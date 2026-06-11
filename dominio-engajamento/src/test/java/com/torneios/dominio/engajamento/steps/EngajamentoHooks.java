package com.torneios.dominio.engajamento.steps;

import com.torneios.dominio.engajamento.EngajamentoFuncionalidade;

import io.cucumber.java.Before;

public class EngajamentoHooks extends EngajamentoFuncionalidade {

    @Before
    public void resetar() {
        super.resetar();
    }
}
