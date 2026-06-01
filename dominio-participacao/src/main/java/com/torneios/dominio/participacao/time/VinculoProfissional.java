package com.torneios.dominio.participacao.time;

import java.time.LocalDate;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;

public class VinculoProfissional {

    private final ProfissionalEsportivoId profissionalId;
    private String funcao;
    private LocalDate dataInicio;
    private LocalDate dataLimiteContrato;

    public VinculoProfissional(ProfissionalEsportivoId profissionalId, String funcao,
            LocalDate dataInicio, LocalDate dataLimiteContrato) {
        this.profissionalId = Objects.requireNonNull(profissionalId,
                "O id do profissional e obrigatorio.");
        this.funcao = validarFuncao(funcao);
        this.dataInicio = Objects.requireNonNull(dataInicio, "A data de inicio e obrigatoria.");
        if (dataLimiteContrato != null && !dataLimiteContrato.isAfter(dataInicio)) {
            throw new RegraDeNegocioException(
                "A data limite do contrato deve ser posterior a data de inicio.");
        }
        this.dataLimiteContrato = dataLimiteContrato;
    }

    public ProfissionalEsportivoId getProfissionalId() { return profissionalId; }
    public String getFuncao() { return funcao; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataLimiteContrato() { return dataLimiteContrato; }

    public void editar(String novaFuncao, LocalDate novaDataInicio, LocalDate novaDataLimite) {
        this.funcao = validarFuncao(novaFuncao);
        this.dataInicio = Objects.requireNonNull(novaDataInicio, "A data de inicio e obrigatoria.");
        if (novaDataLimite != null && !novaDataLimite.isAfter(novaDataInicio)) {
            throw new RegraDeNegocioException(
                "A data limite do contrato deve ser posterior a data de inicio.");
        }
        this.dataLimiteContrato = novaDataLimite;
    }

    private String validarFuncao(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("A funcao no time e obrigatoria.");
        }
        return valor.trim();
    }
}