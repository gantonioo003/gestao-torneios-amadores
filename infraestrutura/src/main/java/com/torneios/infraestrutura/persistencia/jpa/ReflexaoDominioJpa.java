package com.torneios.infraestrutura.persistencia.jpa;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ReflexaoDominioJpa {

    private ReflexaoDominioJpa() {
    }

    static void definirCampo(Object alvo, String nomeCampo, Object valor) {
        try {
            Field campo = localizarCampo(alvo.getClass(), nomeCampo);
            campo.setAccessible(true);
            campo.set(alvo, valor);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Nao foi possivel definir o campo '" + nomeCampo + "'.", e);
        }
    }

    @SuppressWarnings("unchecked")
    static <T> List<T> listarCampo(Object alvo, String nomeCampo) {
        return (List<T>) lerCampo(alvo, nomeCampo);
    }

    @SuppressWarnings("unchecked")
    static <T> T valorCampo(Object alvo, String nomeCampo, Class<T> tipo) {
        return (T) lerCampo(alvo, nomeCampo);
    }

    @SuppressWarnings("unchecked")
    static <T> Set<T> conjuntoCampo(Object alvo, String nomeCampo) {
        return (Set<T>) lerCampo(alvo, nomeCampo);
    }

    @SuppressWarnings("unchecked")
    static <K, V> Map<K, V> mapaCampo(Object alvo, String nomeCampo) {
        return (Map<K, V>) lerCampo(alvo, nomeCampo);
    }

    private static Object lerCampo(Object alvo, String nomeCampo) {
        try {
            Field campo = localizarCampo(alvo.getClass(), nomeCampo);
            campo.setAccessible(true);
            return campo.get(alvo);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Nao foi possivel ler o campo '" + nomeCampo + "'.", e);
        }
    }

    private static Field localizarCampo(Class<?> tipo, String nomeCampo) throws NoSuchFieldException {
        Class<?> atual = tipo;
        while (atual != null) {
            try {
                return atual.getDeclaredField(nomeCampo);
            } catch (NoSuchFieldException ignored) {
                atual = atual.getSuperclass();
            }
        }
        throw new NoSuchFieldException(nomeCampo);
    }
}
