package com.torneios.infraestrutura.persistencia.jpa;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

final class PersistenciaTextoUtil {

    private static final String CAMPO_NULO = "~";

    private PersistenciaTextoUtil() {
    }

    static String serializarLinhas(List<List<String>> linhas) {
        if (linhas == null || linhas.isEmpty()) {
            return "";
        }
        List<String> linhasCodificadas = new ArrayList<>();
        for (List<String> linha : linhas) {
            List<String> campos = new ArrayList<>();
            for (String valor : linha) {
                campos.add(codificarCampo(valor));
            }
            linhasCodificadas.add(String.join(",", campos));
        }
        return String.join(";", linhasCodificadas);
    }

    static List<List<String>> desserializarLinhas(String dados) {
        if (dados == null || dados.isBlank()) {
            return List.of();
        }
        List<List<String>> linhas = new ArrayList<>();
        for (String linhaCodificada : dados.split(";", -1)) {
            if (linhaCodificada.isBlank()) {
                continue;
            }
            List<String> linha = new ArrayList<>();
            for (String campoCodificado : linhaCodificada.split(",", -1)) {
                linha.add(decodificarCampo(campoCodificado));
            }
            linhas.add(linha);
        }
        return linhas;
    }

    static String serializarLista(List<String> valores) {
        List<List<String>> linhas = new ArrayList<>();
        for (String valor : valores == null ? List.<String>of() : valores) {
            linhas.add(List.of(valor));
        }
        return serializarLinhas(linhas);
    }

    static List<String> desserializarLista(String dados) {
        return desserializarLinhas(dados).stream()
                .filter(linha -> !linha.isEmpty())
                .map(linha -> linha.get(0))
                .toList();
    }

    static String deLong(Long valor) {
        return valor == null ? null : String.valueOf(valor);
    }

    static Long paraLong(String valor) {
        return valor == null || valor.isBlank() ? null : Long.valueOf(valor);
    }

    static String deInteger(Integer valor) {
        return valor == null ? null : String.valueOf(valor);
    }

    static Integer paraInteger(String valor) {
        return valor == null || valor.isBlank() ? null : Integer.valueOf(valor);
    }

    static String deBoolean(Boolean valor) {
        return valor == null ? null : String.valueOf(valor);
    }

    static Boolean paraBoolean(String valor) {
        return valor == null || valor.isBlank() ? null : Boolean.valueOf(valor);
    }

    static String deLocalDate(LocalDate valor) {
        return valor == null ? null : valor.toString();
    }

    static LocalDate paraLocalDate(String valor) {
        return valor == null || valor.isBlank() ? null : LocalDate.parse(valor);
    }

    static String deLocalDateTime(LocalDateTime valor) {
        return valor == null ? null : valor.toString();
    }

    static LocalDateTime paraLocalDateTime(String valor) {
        return valor == null || valor.isBlank() ? null : LocalDateTime.parse(valor);
    }

    private static String codificarCampo(String valor) {
        if (valor == null) {
            return CAMPO_NULO;
        }
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(valor.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodificarCampo(String valorCodificado) {
        if (CAMPO_NULO.equals(valorCodificado)) {
            return null;
        }
        return new String(Base64.getUrlDecoder().decode(valorCodificado), StandardCharsets.UTF_8);
    }
}
