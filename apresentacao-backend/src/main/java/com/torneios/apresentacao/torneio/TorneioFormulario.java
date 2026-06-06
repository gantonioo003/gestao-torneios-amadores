package com.torneios.apresentacao.torneio;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;

public class TorneioFormulario {

    public static class TorneioDto {
        public Long id;
        public String nome;
        public FormatoTorneio formato;
        public FormatoEquipe formatoEquipe;
        public Long organizadorId;
        public boolean aceitaSolicitacoes;
    }
}
