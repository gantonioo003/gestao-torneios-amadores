package com.torneios.dominio.participacao.profissional;

import java.time.LocalDate;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;

public class RegistroDeCarreira {

    private final RegistroDeCarreiraId id;
    private String nomeDoClube;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private MotivoDeSaida motivoDeSaida;
    private String descricao;

    public RegistroDeCarreira(RegistroDeCarreiraId id, String nomeDoClube, LocalDate dataInicio,
            LocalDate dataFim, MotivoDeSaida motivoDeSaida) {
        this(id, nomeDoClube, dataInicio, dataFim, motivoDeSaida, null);
    }

    public RegistroDeCarreira(RegistroDeCarreiraId id, String nomeDoClube, LocalDate dataInicio,
            LocalDate dataFim, MotivoDeSaida motivoDeSaida, String descricao) {
        this.id = Objects.requireNonNull(id, "O id do registro e obrigatorio.");
        this.nomeDoClube = validarNomeDoClube(nomeDoClube);
        this.dataInicio = Objects.requireNonNull(dataInicio, "A data de inicio e obrigatoria.");
        setDataFim(dataFim);
        this.motivoDeSaida = motivoDeSaida;
        this.descricao = descricao;
    }

    public RegistroDeCarreiraId getId() { return id; }
    public String getNomeDoClube() { return nomeDoClube; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public MotivoDeSaida getMotivoDeSaida() { return motivoDeSaida; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setMotivoDeSaida(MotivoDeSaida motivoDeSaida) { this.motivoDeSaida = motivoDeSaida; }

    public void setDataFim(LocalDate dataFim) {
        if (dataFim != null && dataFim.isBefore(dataInicio)) {
            throw new RegraDeNegocioException("A data de fim nao pode ser anterior a data de inicio.");
        }
        this.dataFim = dataFim;
    }

    public boolean sobrepoeComPeriodo(LocalDate inicio, LocalDate fim) {
        LocalDate thisFim = this.dataFim != null ? this.dataFim : LocalDate.MAX;
        LocalDate outroFim = fim != null ? fim : LocalDate.MAX;
        return !this.dataInicio.isAfter(outroFim) && !inicio.isAfter(thisFim);
    }

    private String validarNomeDoClube(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do clube e obrigatorio.");
        }
        return valor.trim();
    }
}
