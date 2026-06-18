package com.torneios.aplicacao.participacao.conta;

import java.util.List;

public interface ContaAtividadeRepositorioAplicacao {

    ContaAtividadeResumo pesquisar(long usuarioId);

    record ContaAtividadeResumo(List<TorneioAtividadeResumo> torneiosOrganizados,
                                List<TorneioAtividadeResumo> torneiosParticipando,
                                List<TorneioAtividadeResumo> torneiosSalvos,
                                List<PalpiteAtividadeResumo> palpites) {
    }

    record TorneioAtividadeResumo(long id,
                                  String nome,
                                  String formato,
                                  String status,
                                  boolean aceitaSolicitacoes) {
    }

    record PalpiteAtividadeResumo(long id,
                                  String tipo,
                                  long torneioId,
                                  String torneioNome,
                                  Long partidaId,
                                  long opcao,
                                  boolean apurado,
                                  Boolean acertou) {
    }
}
