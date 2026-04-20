package com.torneios.dominio.participacao.solicitacao;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.torneio.TorneioId;

public interface SolicitacaoParticipacaoRepositorio {

    void salvar(SolicitacaoParticipacao solicitacaoParticipacao);

    Optional<SolicitacaoParticipacao> buscarPorId(SolicitacaoParticipacaoId solicitacaoId);

    List<SolicitacaoParticipacao> listarPendentesPorTorneio(TorneioId torneioId);
}
