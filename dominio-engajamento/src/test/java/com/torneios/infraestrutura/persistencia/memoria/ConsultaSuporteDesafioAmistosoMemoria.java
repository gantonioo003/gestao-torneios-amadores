package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.desafio.ConsultaSuporteDesafioAmistoso;

public class ConsultaSuporteDesafioAmistosoMemoria implements ConsultaSuporteDesafioAmistoso {

    private final Set<UsuarioId> usuariosAutenticados = new HashSet<>();
    private final Map<TimeId, UsuarioId> responsaveisPorTime = new HashMap<>();

    public void autenticar(UsuarioId usuarioId) {
        usuariosAutenticados.add(usuarioId);
    }

    public void definirResponsavel(TimeId timeId, UsuarioId usuarioId) {
        responsaveisPorTime.put(timeId, usuarioId);
    }

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return usuarioId != null && usuariosAutenticados.contains(usuarioId);
    }

    @Override
    public boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId) {
        return usuarioId != null && usuarioId.equals(responsaveisPorTime.get(timeId));
    }

    @Override
    public boolean timesPodemSeDesafiar(TimeId timeDesafianteId, TimeId timeDesafiadoId) {
        return responsaveisPorTime.containsKey(timeDesafianteId)
                && responsaveisPorTime.containsKey(timeDesafiadoId)
                && !timeDesafianteId.equals(timeDesafiadoId);
    }
}
