package com.torneios.infraestrutura.persistencia.memoria;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.GrupoChat;
import com.torneios.dominio.engajamento.chat.GrupoChatId;
import com.torneios.dominio.engajamento.chat.GrupoChatRepositorio;

public class GrupoChatRepositorioMemoria implements GrupoChatRepositorio {
    private final Map<GrupoChatId, GrupoChat> dados = new LinkedHashMap<>();

    @Override public void salvar(GrupoChat grupo) { dados.put(grupo.getId(), grupo); }
    @Override public Optional<GrupoChat> buscarPorId(GrupoChatId id) { return Optional.ofNullable(dados.get(id)); }
    @Override public List<GrupoChat> listarPorUsuario(UsuarioId usuarioId) {
        return dados.values().stream()
                .filter(grupo -> grupo.possuiParticipante(usuarioId) || grupo.possuiConvitePendente(usuarioId))
                .toList();
    }
}
