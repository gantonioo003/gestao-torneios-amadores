package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.desafio.ConsultaSuporteDesafioAmistoso;

public class ConsultaSuporteDesafioAmistosoMemoria implements ConsultaSuporteDesafioAmistoso {

    private final Set<UsuarioId> usuariosAutenticados = new HashSet<>();
    private final Set<UsuarioId> usuariosQueGerenciamTimes = new HashSet<>();
    private final Map<TimeId, UsuarioId> responsaveisPorTime = new HashMap<>();

    public void autenticar(UsuarioId usuarioId) {
        usuariosAutenticados.add(usuarioId);
    }

    public void definirResponsavel(TimeId timeId, UsuarioId usuarioId) {
        responsaveisPorTime.put(timeId, usuarioId);
        usuariosQueGerenciamTimes.add(usuarioId);
    }

    public void bloquearGerenciamentoDeTimes(UsuarioId usuarioId) {
        usuariosQueGerenciamTimes.remove(usuarioId);
    }

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return usuarioId != null && usuariosAutenticados.contains(usuarioId);
    }

    @Override
    public boolean usuarioPodeGerenciarTimes(UsuarioId usuarioId) {
        return usuariosQueGerenciamTimes.contains(usuarioId);
    }

    @Override
    public boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId) {
        return usuarioId != null && usuarioId.equals(responsaveisPorTime.get(timeId));
    }

    @Override
    public Optional<UsuarioId> buscarResponsavelDoTime(TimeId timeId) {
        return Optional.ofNullable(responsaveisPorTime.get(timeId));
    }

    @Override
    public boolean timesPodemSeDesafiar(TimeId timeDesafianteId, TimeId timeDesafiadoId) {
        return responsaveisPorTime.containsKey(timeDesafianteId)
                && responsaveisPorTime.containsKey(timeDesafiadoId)
                && !timeDesafianteId.equals(timeDesafiadoId)
                && !responsaveisPorTime.get(timeDesafianteId)
                    .equals(responsaveisPorTime.get(timeDesafiadoId));
    }
}
