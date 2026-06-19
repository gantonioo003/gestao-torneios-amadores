package com.torneios.aplicacao.participacao.inscricao;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacao;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;
import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoServicoAplicacao;
import com.torneios.dominio.participacao.solicitacao.TipoSolicitacaoParticipacao;

/**
 * Casos de uso de inscricao e acompanhamento de candidaturas.
 */
public class InscricaoServicoAplicacao {

    private final SolicitacaoParticipacaoServico solicitacaoParticipacaoServico;
    private final NotificacaoParticipacaoServicoAplicacao notificacaoServicoAplicacao;

    public InscricaoServicoAplicacao(SolicitacaoParticipacaoServico solicitacaoParticipacaoServico,
                                     NotificacaoParticipacaoServicoAplicacao notificacaoServicoAplicacao) {
        notNull(solicitacaoParticipacaoServico, "O servico de solicitacao e obrigatorio.");
        this.solicitacaoParticipacaoServico = solicitacaoParticipacaoServico;
        this.notificacaoServicoAplicacao = notificacaoServicoAplicacao;
    }

    public InscricaoResumo convidarTime(long solicitacaoId, long organizadorId, long timeId, long torneioId) {
        return converter(solicitacaoParticipacaoServico.convidarTime(
                new SolicitacaoParticipacaoId(solicitacaoId),
                new UsuarioId(organizadorId),
                new TimeId(timeId),
                new TorneioId(torneioId)));
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

    public InscricaoResumo aprovarSolicitacao(long solicitacaoId, long usuarioId) {
        SolicitacaoParticipacao solicitacao = solicitacaoParticipacaoServico.aprovarSolicitacao(
                new SolicitacaoParticipacaoId(solicitacaoId),
                new UsuarioId(usuarioId));
        long organizador = solicitacaoParticipacaoServico.organizadorDoTorneio(solicitacao.getTorneioId()).valor();
        long treinador = solicitacaoParticipacaoServico.responsavelDoTime(solicitacao.getTimeId()).valor();
        if (solicitacao.getTipo() == TipoSolicitacaoParticipacao.CANDIDATURA) {
            notificacaoServicoAplicacao.notificarTimeAceito(
                    System.currentTimeMillis(), treinador,
                    solicitacao.getTimeId().valor(), solicitacao.getTorneioId().valor());
        } else {
            notificacaoServicoAplicacao.notificarConviteAceito(
                    System.currentTimeMillis(), organizador,
                    solicitacao.getTimeId().valor(), solicitacao.getTorneioId().valor());
        }
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

    public List<InscricaoResumo> acompanharTorneio(long torneioId, long organizadorId) {
        return solicitacaoParticipacaoServico.acompanharTorneio(
                new TorneioId(torneioId), new UsuarioId(organizadorId)).stream().map(this::converter).toList();
    }

    public List<InscricaoResumo> acompanharTime(long timeId, long usuarioId) {
        return solicitacaoParticipacaoServico.acompanharTime(
                new TimeId(timeId), new UsuarioId(usuarioId)).stream().map(this::converter).toList();
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
                solicitacaoParticipacao.getStatus().name(),
                solicitacaoParticipacao.getTipo().name());
    }

    public record InscricaoResumo(long id,
                                  long solicitanteId,
                                  long timeId,
                                  long torneioId,
                                  String status,
                                  String tipo) {
    }
}
