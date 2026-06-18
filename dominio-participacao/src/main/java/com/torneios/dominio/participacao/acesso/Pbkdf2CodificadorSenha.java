package com.torneios.dominio.participacao.acesso;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Pbkdf2CodificadorSenha implements CodificadorSenha {

    private static final String PREFIXO = "pbkdf2";
    private static final int ITERACOES = 310000;
    private static final int TAMANHO_SALT = 16;
    private static final int TAMANHO_HASH_BITS = 256;
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String codificar(String senhaEmTextoPlano) {
        byte[] salt = new byte[TAMANHO_SALT];
        secureRandom.nextBytes(salt);
        return formatarSenha(ITERACOES, salt, gerarHash(senhaEmTextoPlano, salt, ITERACOES));
    }

    @Override
    public boolean confere(String senhaEmTextoPlano, String senhaArmazenada) {
        if (senhaArmazenada == null || senhaEmTextoPlano == null) {
            return false;
        }
        if (precisaRehash(senhaArmazenada)) {
            return senhaArmazenada.equals(senhaEmTextoPlano);
        }

        String[] partes = senhaArmazenada.split("\\$");
        if (partes.length != 4 || !PREFIXO.equals(partes[0])) {
            return false;
        }

        int iteracoes = Integer.parseInt(partes[1]);
        byte[] salt = Base64.getDecoder().decode(partes[2]);
        byte[] hashEsperado = Base64.getDecoder().decode(partes[3]);
        byte[] hashCalculado = gerarHash(senhaEmTextoPlano, salt, iteracoes);
        return java.security.MessageDigest.isEqual(hashEsperado, hashCalculado);
    }

    @Override
    public boolean precisaRehash(String senhaArmazenada) {
        return senhaArmazenada == null || !senhaArmazenada.startsWith(PREFIXO + "$");
    }

    private String formatarSenha(int iteracoes, byte[] salt, byte[] hash) {
        return PREFIXO + "$"
                + iteracoes + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    private byte[] gerarHash(String senhaEmTextoPlano, byte[] salt, int iteracoes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(senhaEmTextoPlano.toCharArray(), salt, iteracoes, TAMANHO_HASH_BITS);
            return SecretKeyFactory.getInstance(ALGORITMO)
                    .generateSecret(spec)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Nao foi possivel proteger a senha informada.", e);
        }
    }
}
