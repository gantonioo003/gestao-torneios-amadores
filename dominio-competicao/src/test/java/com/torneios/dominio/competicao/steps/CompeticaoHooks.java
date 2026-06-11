package com.torneios.dominio.competicao.steps;

import com.torneios.dominio.competicao.CompeticaoFuncionalidade;

import io.cucumber.java.Before;

public class CompeticaoHooks extends CompeticaoFuncionalidade {

    @Before
    public void resetar() {
        super.resetar();
    }
}
