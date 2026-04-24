package com.torneios.infraestrutura.persistencia.memoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.time.Time;
import com.torneios.dominio.participacao.time.TimeRepositorio;

public class TimeRepositorioMemoria implements TimeRepositorio {

    private final List<Time> dados = new ArrayList<>();

    @Override
    public void salvar(Time time) {
        dados.removeIf(t -> t.getId().equals(time.getId()));
        dados.add(time);
    }

    @Override
    public Optional<Time> buscarPorId(TimeId id) {
        return dados.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Time> listarPorResponsavel(UsuarioId usuarioId) {
        return dados.stream()
                .filter(t -> t.getResponsavel().equals(usuarioId))
                .toList();
    }

    @Override
    public void remover(TimeId timeId) {
        dados.removeIf(t -> t.getId().equals(timeId));
    }

    public void limpar() {
        dados.clear();
    }
}
