package com.torneios.dominio.participacao.solicitacao;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.compartilhado.time.TimeId;

public interface PoliticaParticipacaoTorneio {

    boolean aceitaSolicitacoes(TorneioId torneioId);

    boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId);

    UsuarioId organizadorDoTorneio(TorneioId torneioId);

    void adicionarParticipante(TorneioId torneioId, TimeId timeId);

    void removerParticipante(TorneioId torneioId, TimeId timeId);

    boolean possuiParticipante(TorneioId torneioId, TimeId timeId);

    default boolean torneioIniciado(TorneioId torneioId) {
        return false;
    }
}
