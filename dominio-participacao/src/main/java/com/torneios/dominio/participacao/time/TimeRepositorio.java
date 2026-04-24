package com.torneios.dominio.participacao.time;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface TimeRepositorio {

    void salvar(Time time);

    Optional<Time> buscarPorId(TimeId timeId);

    List<Time> listarPorResponsavel(UsuarioId usuarioId);

    void remover(TimeId timeId);
}
