package com.torneios.dominio.engajamento.palpite;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface PalpiteRepositorio {

    void salvar(Palpite palpite);

    Optional<Palpite> buscarPorUsuarioEEvento(UsuarioId usuarioId, EventoAlvo eventoAlvo);

    List<Palpite> listarPorEvento(EventoAlvo eventoAlvo);
}
