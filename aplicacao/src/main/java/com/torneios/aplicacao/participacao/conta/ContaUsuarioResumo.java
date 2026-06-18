package com.torneios.aplicacao.participacao.conta;

import java.time.LocalDate;
import java.util.List;

public interface ContaUsuarioResumo {
    Long getId();
    String getNome();
    String getNomeUsuario();
    String getEmail();
    String getTelefone();
    LocalDate getDataNascimento();
    String getCidade();
    String getEstado();
    String getBiografia();
    String getFotoPerfilUrl();
    String getTipo();
    String getProvedor();
    boolean isPodeCriarTorneio();
    boolean isPodeGerenciarTimes();
    boolean isPossuiPerfilProfissional();
    List<Long> getTorneiosSalvos();
}
