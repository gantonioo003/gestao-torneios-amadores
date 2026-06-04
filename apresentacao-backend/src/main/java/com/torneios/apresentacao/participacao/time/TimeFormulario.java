package com.torneios.apresentacao.participacao.time;

import java.time.LocalDate;
import java.util.List;

import com.torneios.aplicacao.participacao.time.TimeResumo;
import com.torneios.aplicacao.participacao.time.VinculoProfissionalResumo;

public class TimeFormulario {

    public TimeDto time;
    public List<? extends TimeResumo> times;

    public TimeFormulario(TimeDto time) {
        this.time = time;
    }

    public static class TimeDto {
        public Long id;
        public String nome;
        public Long responsavelId;
        public List<VinculoProfissionalResumo> elenco;
    }

    public static class VinculoDto {
        public Long profissionalId;
        public String funcao;
        public LocalDate dataInicio;
        public LocalDate dataLimiteContrato;
    }
}
