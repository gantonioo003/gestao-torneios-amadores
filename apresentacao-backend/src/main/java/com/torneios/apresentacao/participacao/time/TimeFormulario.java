package com.torneios.apresentacao.participacao.time;

import java.time.LocalDate;
import java.util.List;

import com.torneios.aplicacao.participacao.time.TimeResumo;
import com.torneios.aplicacao.participacao.time.VinculoProfissionalResumo;

public class TimeFormulario {

    public TimeDto time;
    public List<? extends TimeResumo> times;
    public boolean podeEditarTime;
    public boolean podeGerenciarElenco;

    public TimeFormulario(TimeDto time) {
        this.time = time;
    }

    public static class TimeDto {
        public Long id;
        public String nome;
        public String imagemUrl;
        public Long responsavelId;
        public List<VinculoEnriquecidoDto> elenco;
    }

    public static class VinculoEnriquecidoDto {
        public Long profissionalId;
        public String nomeProfissional;
        public String tipoProfissional;
        public String funcao;
        public java.time.LocalDate dataInicio;
        public java.time.LocalDate dataLimiteContrato;

        public VinculoEnriquecidoDto(VinculoProfissionalResumo v, String nomeProfissional,
                String tipoProfissional) {
            this.profissionalId = v.getProfissionalId();
            this.nomeProfissional = nomeProfissional;
            this.tipoProfissional = tipoProfissional;
            this.funcao = v.getFuncao();
            this.dataInicio = v.getDataInicio();
            this.dataLimiteContrato = v.getDataLimiteContrato();
        }
    }

    public static class VinculoDto {
        public Long profissionalId;
        public String funcao;
        public LocalDate dataInicio;
        public LocalDate dataLimiteContrato;
    }

    public static class NovoIntegranteDto extends VinculoDto {
        public String nome;
        public String fotoUrl;
        public com.torneios.dominio.participacao.profissional.TipoProfissional tipo;
    }

    public static class RemocaoDto {
        public com.torneios.dominio.participacao.profissional.MotivoDeSaida motivoDeSaida;
        public String descricao;
    }
}
