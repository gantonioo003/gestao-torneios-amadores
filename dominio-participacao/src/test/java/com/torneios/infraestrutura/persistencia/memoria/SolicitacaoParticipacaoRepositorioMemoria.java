package com.torneios.infraestrutura.persistencia.memoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.enumeracao.StatusSolicitacao;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacao;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoRepositorio;

public class SolicitacaoParticipacaoRepositorioMemoria implements SolicitacaoParticipacaoRepositorio {

    private final List<SolicitacaoParticipacao> dados = new ArrayList<>();

    @Override
    public void salvar(SolicitacaoParticipacao solicitacao) {
        dados.removeIf(s -> s.getId().equals(solicitacao.getId()));
        dados.add(solicitacao);
    }

    @Override
    public Optional<SolicitacaoParticipacao> buscarPorId(SolicitacaoParticipacaoId id) {
        return dados.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<SolicitacaoParticipacao> listarPendentesPorTorneio(TorneioId torneioId) {
        return dados.stream()
                .filter(s -> s.getTorneioId().equals(torneioId) && s.getStatus() == StatusSolicitacao.PENDENTE)
                .toList();
    }

    @Override
    public boolean existePendentePorTimeETorneio(TimeId timeId, TorneioId torneioId) {
        return dados.stream()
                .anyMatch(s -> s.getTimeId().equals(timeId)
                        && s.getTorneioId().equals(torneioId)
                        && s.getStatus() == StatusSolicitacao.PENDENTE);
    }

    public void limpar() {
        dados.clear();
    }
}
