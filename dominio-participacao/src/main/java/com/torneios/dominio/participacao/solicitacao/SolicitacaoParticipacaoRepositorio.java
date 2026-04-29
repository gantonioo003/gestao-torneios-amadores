package com.torneios.dominio.participacao.solicitacao;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface SolicitacaoParticipacaoRepositorio {

    void salvar(SolicitacaoParticipacao solicitacaoParticipacao);

    Optional<SolicitacaoParticipacao> buscarPorId(SolicitacaoParticipacaoId solicitacaoId);

    List<SolicitacaoParticipacao> listarPendentesPorTorneio(TorneioId torneioId);

    List<SolicitacaoParticipacao> listarPorSolicitante(UsuarioId usuarioId);

    boolean existePendentePorTimeETorneio(TimeId timeId, TorneioId torneioId);
}
