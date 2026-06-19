package com.torneios.dominio.engajamento.feed;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface DenunciaRepositorio {
    void salvar(Denuncia denuncia);
    Optional<Denuncia> buscarPorId(DenunciaId id);
    boolean existePendente(UsuarioId denuncianteId, TipoAlvoDenuncia tipoAlvo, long alvoId);
    List<Denuncia> listarPendentes();
}
