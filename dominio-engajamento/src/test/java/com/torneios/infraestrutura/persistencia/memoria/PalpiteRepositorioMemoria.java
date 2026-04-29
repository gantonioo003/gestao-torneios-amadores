package com.torneios.infraestrutura.persistencia.memoria;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.palpite.EventoAlvo;
import com.torneios.dominio.engajamento.palpite.Palpite;
import com.torneios.dominio.engajamento.palpite.PalpiteRepositorio;

public class PalpiteRepositorioMemoria implements PalpiteRepositorio {

    private final Map<String, Palpite> palpites = new LinkedHashMap<>();

    @Override
    public void salvar(Palpite palpite) {
        palpites.put(chave(palpite.getUsuarioId(), palpite.getEventoAlvo()), palpite);
    }

    @Override
    public Optional<Palpite> buscarPorUsuarioEEvento(UsuarioId usuarioId, EventoAlvo eventoAlvo) {
        return Optional.ofNullable(palpites.get(chave(usuarioId, eventoAlvo)));
    }

    @Override
    public List<Palpite> listarPorEvento(EventoAlvo eventoAlvo) {
        return palpites.values().stream()
                .filter(palpite -> palpite.getEventoAlvo().equals(eventoAlvo))
                .toList();
    }

    public List<Palpite> listarTodos() {
        return new ArrayList<>(palpites.values());
    }

    private String chave(UsuarioId usuarioId, EventoAlvo eventoAlvo) {
        return usuarioId.valor() + ":" + eventoAlvo.hashCode();
    }
}
