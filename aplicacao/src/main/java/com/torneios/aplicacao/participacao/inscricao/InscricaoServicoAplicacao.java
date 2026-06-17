package com.torneios.aplicacao.participacao.inscricao;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacao;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;

/**
 * Casos de uso de inscricao e acompanhamento de candidaturas.
 */
public class InscricaoServicoAplicacao {

    private final SolicitacaoParticipacaoServico solicitacaoParticipacaoServico;

    public InscricaoServicoAplicacao(SolicitacaoParticipacaoServico solicitacaoParticipacaoServico) {
        notNull(solicitacaoParticipacaoServico, "O servico de solicitacao e obrigatorio.");
        this.solicitacaoParticipacaoServico = solicitacaoParticipacaoServico;
    }

    public InscricaoResumo solicitarParticipacao(long solicitacaoId,
                                                 long usuarioId,
                                                 long timeId,
                                                 long torneioId) {
        return converter(solicitacaoParticipacaoServico.solicitarParticipacao(
                new SolicitacaoParticipacaoId(solicitacaoId),
                new UsuarioId(usuarioId),
                new TimeId(timeId),
                new TorneioId(torneioId)));
    }

    public InscricaoResumo aprovarSolicitacao(long solicitacaoId, long organizadorId) {
        solicitacaoParticipacaoServico.aprovarSolicitacao(
                new SolicitacaoParticipacaoId(solicitacaoId),
                new UsuarioId(organizadorId));
        return obterSolicitacao(solicitacaoId);
    }

    public InscricaoResumo rejeitarSolicitacao(long solicitacaoId, long organizadorId) {
        solicitacaoParticipacaoServico.rejeitarSolicitacao(
                new SolicitacaoParticipacaoId(solicitacaoId),
                new UsuarioId(organizadorId));
        return obterSolicitacao(solicitacaoId);
    }

    public void removerParticipanteAprovado(long torneioId, long timeId, long organizadorId) {
        solicitacaoParticipacaoServico.removerParticipanteAprovado(
                new TorneioId(torneioId),
                new TimeId(timeId),
                new UsuarioId(organizadorId));
    }

    public List<InscricaoResumo> acompanharCandidaturas(long usuarioId) {
        return solicitacaoParticipacaoServico.acompanharCandidaturas(new UsuarioId(usuarioId)).stream()
                .map(this::converter)
                .toList();
    }

    public void cancelarCandidatura(long solicitacaoId, long usuarioId) {
        solicitacaoParticipacaoServico.cancelarCandidatura(
                new SolicitacaoParticipacaoId(solicitacaoId),
                new UsuarioId(usuarioId));
    }

    public List<InscricaoResumo> listarPendentesParaAvaliacao(long torneioId, long organizadorId) {
        return solicitacaoParticipacaoServico.listarPendentesParaAvaliacao(
                new TorneioId(torneioId),
                new UsuarioId(organizadorId)).stream()
                .map(this::converter)
                .toList();
    }

    public InscricaoResumo obterSolicitacao(long solicitacaoId) {
        return converter(solicitacaoParticipacaoServico.obterSolicitacao(new SolicitacaoParticipacaoId(solicitacaoId)));
    }

    private InscricaoResumo converter(SolicitacaoParticipacao solicitacaoParticipacao) {
        return new InscricaoResumo(
                solicitacaoParticipacao.getId().valor(),
                solicitacaoParticipacao.getSolicitante().valor(),
                solicitacaoParticipacao.getTimeId().valor(),
                solicitacaoParticipacao.getTorneioId().valor(),
                solicitacaoParticipacao.getStatus().name());
    }

    public record InscricaoResumo(long id,
                                  long solicitanteId,
                                  long timeId,
                                  long torneioId,
                                  String status) {
    }
}
