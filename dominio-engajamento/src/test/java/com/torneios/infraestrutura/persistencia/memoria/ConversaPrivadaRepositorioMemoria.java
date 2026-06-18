package com.torneios.infraestrutura.persistencia.memoria;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.ConversaPrivada;
import com.torneios.dominio.engajamento.chat.ConversaPrivadaId;
import com.torneios.dominio.engajamento.chat.ConversaPrivadaRepositorio;

public class ConversaPrivadaRepositorioMemoria implements ConversaPrivadaRepositorio {

    private final Map<ConversaPrivadaId, ConversaPrivada> dados = new LinkedHashMap<>();

    @Override
    public void salvar(ConversaPrivada conversaPrivada) {
        dados.put(conversaPrivada.getId(), conversaPrivada);
    }

    @Override
    public Optional<ConversaPrivada> buscarPorId(ConversaPrivadaId conversaPrivadaId) {
        return Optional.ofNullable(dados.get(conversaPrivadaId));
    }

    @Override
    public List<ConversaPrivada> listarPorUsuario(UsuarioId usuarioId) {
        return dados.values().stream()
                .filter(conversa -> conversa.envolve(usuarioId))
                .toList();
    }

    @Override
    public List<ConversaPrivada> listarSolicitadasParaUsuario(UsuarioId usuarioId) {
        return dados.values().stream()
                .filter(conversa -> conversa.getDestinatarioId().equals(usuarioId))
                .filter(ConversaPrivada::estaSolicitada)
                .toList();
    }

    @Override
    public List<ConversaPrivada> listarSolicitadasPorUsuario(UsuarioId usuarioId) {
        return dados.values().stream()
                .filter(conversa -> conversa.getSolicitanteId().equals(usuarioId))
                .filter(ConversaPrivada::estaSolicitada)
                .toList();
    }

    @Override
    public List<ConversaPrivada> listarAprovadasPorUsuario(UsuarioId usuarioId) {
        return dados.values().stream()
                .filter(conversa -> conversa.envolve(usuarioId))
                .filter(ConversaPrivada::estaAprovada)
                .toList();
    }
}
