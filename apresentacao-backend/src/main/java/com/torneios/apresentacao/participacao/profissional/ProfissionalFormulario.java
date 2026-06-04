package com.torneios.apresentacao.participacao.profissional;

import java.time.LocalDate;
import java.util.List;

import com.torneios.aplicacao.participacao.profissional.RegistroDeCarreiraResumo;
import com.torneios.dominio.participacao.profissional.MotivoDeSaida;
import com.torneios.dominio.participacao.profissional.TipoProfissional;

public class ProfissionalFormulario {

    public ProfissionalDto profissional;
    public List<? extends RegistroDeCarreiraResumo> historico;

    public ProfissionalFormulario(ProfissionalDto profissional) {
        this.profissional = profissional;
    }

    public static class ProfissionalDto {
        public Long id;
        public String nome;
        public TipoProfissional tipo;
        public Long cadastranteId;
    }

    public static class RegistroDto {
        public Long id;
        public String nomeDoClube;
        public LocalDate dataInicio;
        public LocalDate dataFim;
        public MotivoDeSaida motivoDeSaida;
    }
}
