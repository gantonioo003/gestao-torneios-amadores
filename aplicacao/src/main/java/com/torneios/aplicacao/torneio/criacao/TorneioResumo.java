package com.torneios.aplicacao.torneio.criacao;

public interface TorneioResumo {
    Long getId();
    String getNome();
    String getImagemUrl();
    String getFormato();
    String getFormatoEquipe();
    Long getOrganizadorId();
    String getStatus();
    boolean aceitaSolicitacoes();
}
