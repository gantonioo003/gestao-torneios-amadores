package com.torneios.dominio.participacao.solicitacao;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface PoliticaParticipacaoTorneio {

    boolean aceitaSolicitacoes(TorneioId torneioId);

    boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId);
}
