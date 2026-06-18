package com.torneios.infraestrutura.autenticacao;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.torneios.aplicacao.participacao.conta.IdentidadeExterna;
import com.torneios.aplicacao.participacao.conta.IdentidadeExternaVerificador;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;

@Component
public class GoogleIdentidadeExternaVerificador implements IdentidadeExternaVerificador {

    private final String clientId;

    public GoogleIdentidadeExternaVerificador(@Value("${app.google.client-id:}") String clientId) {
        this.clientId = clientId == null ? "" : clientId.trim();
    }

    @Override
    public boolean configurado() {
        return !clientId.isBlank();
    }

    @Override
    public String clientIdPublico() {
        return clientId;
    }

    @Override
    public IdentidadeExterna verificar(String credencial) {
        if (!configurado()) {
            throw new IllegalStateException("A entrada com Google ainda nao foi configurada.");
        }
        if (credencial == null || credencial.isBlank()) {
            throw new OperacaoNaoPermitidaException("A credencial do Google e obrigatoria.");
        }

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(List.of(clientId))
                    .build();

            GoogleIdToken token = verifier.verify(credencial);
            if (token == null) {
                throw new OperacaoNaoPermitidaException("A credencial do Google e invalida ou expirou.");
            }

            GoogleIdToken.Payload payload = token.getPayload();
            return new IdentidadeExterna(
                    payload.getSubject(),
                    texto(payload.get("name"), payload.getEmail()),
                    payload.getEmail(),
                    texto(payload.get("picture"), null),
                    Boolean.TRUE.equals(payload.getEmailVerified()));
        } catch (GeneralSecurityException | IOException e) {
            throw new OperacaoNaoPermitidaException("Nao foi possivel validar a conta Google.");
        }
    }

    private String texto(Object valor, String padrao) {
        return valor == null || valor.toString().isBlank() ? padrao : valor.toString();
    }
}
