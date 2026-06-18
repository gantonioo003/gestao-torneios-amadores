package com.torneios.dominio.participacao.acesso;

public interface CodificadorSenha {

    String codificar(String senhaEmTextoPlano);

    boolean confere(String senhaEmTextoPlano, String senhaArmazenada);

    boolean precisaRehash(String senhaArmazenada);
}
