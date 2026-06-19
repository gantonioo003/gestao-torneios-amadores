package com.torneios.aplicacao.engajamento.desafio;

import static org.apache.commons.lang3.Validate.notNull;

import java.time.LocalDateTime;
import java.util.List;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistoso;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoServico;
import com.torneios.dominio.engajamento.desafio.ConsultaSuporteDesafioAmistoso;
import com.torneios.dominio.engajamento.desafio.ResultadoAmistoso;
import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoServicoAplicacao;

/**
 * Casos de uso de desafios amistosos fora do fluxo principal do torneio.
 */
public class DesafioServicoAplicacao {

    private final DesafioAmistosoServico desafioAmistosoServico;
    private final ConsultaSuporteDesafioAmistoso consultaSuporte;
    private final NotificacaoParticipacaoServicoAplicacao notificacaoServico;

    public DesafioServicoAplicacao(DesafioAmistosoServico desafioAmistosoServico,
                                   ConsultaSuporteDesafioAmistoso consultaSuporte,
                                   NotificacaoParticipacaoServicoAplicacao notificacaoServico) {
        notNull(desafioAmistosoServico, "O servico de desafio amistoso e obrigatorio.");
        notNull(consultaSuporte, "A consulta de suporte de desafio amistoso e obrigatoria.");
        notNull(notificacaoServico, "O servico de notificacoes e obrigatorio.");
        this.desafioAmistosoServico = desafioAmistosoServico;
        this.consultaSuporte = consultaSuporte;
        this.notificacaoServico = notificacaoServico;
    }

    public DesafioResumo proporConfronto(long desafioId,
                                         long usuarioId,
                                         long timeDesafianteId,
                                         long timeDesafiadoId,
                                         LocalDateTime dataHora,
                                         String local) {
        DesafioAmistoso desafio = desafioAmistosoServico.proporConfronto(
                new DesafioAmistosoId(desafioId),
                new UsuarioId(usuarioId),
                new TimeId(timeDesafianteId),
                new TimeId(timeDesafiadoId),
                dataHora,
                local);
        consultaSuporte.buscarResponsavelDoTime(new TimeId(timeDesafiadoId))
                .ifPresent(responsavel -> notificacaoServico.notificarDesafioRecebido(
                        responsavel.valor(), timeDesafiadoId));
        return converter(desafio);
    }

    public DesafioResumo aceitarConvite(long desafioId, long usuarioId) {
        DesafioAmistoso desafio = desafioAmistosoServico.aceitarConvite(
                new DesafioAmistosoId(desafioId), new UsuarioId(usuarioId));
        notificacaoServico.notificarDesafioAceito(
                desafio.getProponenteId().valor(), desafio.getTimeDesafianteId().valor());
        consultaSuporte.buscarResponsavelDoTime(desafio.getTimeDesafiadoId())
                .filter(responsavel -> !responsavel.equals(desafio.getProponenteId()))
                .ifPresent(responsavel -> notificacaoServico.notificarDesafioAceito(
                        responsavel.valor(), desafio.getTimeDesafiadoId().valor()));
        return converter(desafio);
    }

    public DesafioResumo recusarConvite(long desafioId, long usuarioId) {
        return converter(desafioAmistosoServico.recusarConvite(new DesafioAmistosoId(desafioId), new UsuarioId(usuarioId)));
    }

    public DesafioResumo cancelarDesafio(long desafioId, long usuarioId) {
        return converter(desafioAmistosoServico.cancelarDesafio(
                new DesafioAmistosoId(desafioId), new UsuarioId(usuarioId)));
    }

    public DesafioResumo reagendarAmistoso(long desafioId, long usuarioId, LocalDateTime novaDataHora, String novoLocal) {
        return converter(desafioAmistosoServico.reagendarAmistoso(
                new DesafioAmistosoId(desafioId),
                new UsuarioId(usuarioId),
                novaDataHora,
                novoLocal));
    }

    public DesafioResumo registrarResultado(long desafioId,
                                            long usuarioId,
                                            int golsDesafiante,
                                            int golsDesafiado) {
        return converter(desafioAmistosoServico.registrarResultado(
                new DesafioAmistosoId(desafioId),
                new UsuarioId(usuarioId),
                new ResultadoAmistoso(golsDesafiante, golsDesafiado)));
    }

    public List<DesafioResumo> listarHistoricoDoTime(long timeId) {
        return desafioAmistosoServico.listarHistoricoDoTime(new TimeId(timeId)).stream()
                .map(this::converter)
                .toList();
    }

    public List<DesafioResumo> acompanharConfrontosDoTime(long timeId, long usuarioId) {
        return desafioAmistosoServico.acompanharConfrontosDoTime(
                        new TimeId(timeId), new UsuarioId(usuarioId)).stream()
                .map(this::converter)
                .toList();
    }

    private DesafioResumo converter(DesafioAmistoso desafioAmistoso) {
        return new DesafioResumo(
                String.valueOf(desafioAmistoso.getId().valor()),
                String.valueOf(desafioAmistoso.getProponenteId().valor()),
                String.valueOf(desafioAmistoso.getTimeDesafianteId().valor()),
                String.valueOf(desafioAmistoso.getTimeDesafiadoId().valor()),
                desafioAmistoso.getDataHora(),
                desafioAmistoso.getLocal(),
                desafioAmistoso.getStatus().name(),
                desafioAmistoso.getResultado().map(ResultadoAmistoso::golsDesafiante).orElse(null),
                desafioAmistoso.getResultado().map(ResultadoAmistoso::golsDesafiado).orElse(null));
    }

    public record DesafioResumo(String id,
                                String proponenteId,
                                String timeDesafianteId,
                                String timeDesafiadoId,
                                LocalDateTime dataHora,
                                String local,
                                String status,
                                Integer golsDesafiante,
                                Integer golsDesafiado) {
    }
}
