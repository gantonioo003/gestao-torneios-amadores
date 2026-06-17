package com.torneios.aplicacao.engajamento.desafio;

import static org.apache.commons.lang3.Validate.notNull;

import java.time.LocalDateTime;
import java.util.List;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistoso;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoServico;
import com.torneios.dominio.engajamento.desafio.ResultadoAmistoso;

/**
 * Casos de uso de desafios amistosos fora do fluxo principal do torneio.
 */
public class DesafioServicoAplicacao {

    private final DesafioAmistosoServico desafioAmistosoServico;

    public DesafioServicoAplicacao(DesafioAmistosoServico desafioAmistosoServico) {
        notNull(desafioAmistosoServico, "O servico de desafio amistoso e obrigatorio.");
        this.desafioAmistosoServico = desafioAmistosoServico;
    }

    public DesafioResumo proporConfronto(long desafioId,
                                         long usuarioId,
                                         long timeDesafianteId,
                                         long timeDesafiadoId,
                                         LocalDateTime dataHora,
                                         String local) {
        return converter(desafioAmistosoServico.proporConfronto(
                new DesafioAmistosoId(desafioId),
                new UsuarioId(usuarioId),
                new TimeId(timeDesafianteId),
                new TimeId(timeDesafiadoId),
                dataHora,
                local));
    }

    public DesafioResumo aceitarConvite(long desafioId, long usuarioId) {
        return converter(desafioAmistosoServico.aceitarConvite(new DesafioAmistosoId(desafioId), new UsuarioId(usuarioId)));
    }

    public DesafioResumo recusarConvite(long desafioId, long usuarioId) {
        return converter(desafioAmistosoServico.recusarConvite(new DesafioAmistosoId(desafioId), new UsuarioId(usuarioId)));
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

    private DesafioResumo converter(DesafioAmistoso desafioAmistoso) {
        return new DesafioResumo(
                desafioAmistoso.getId().valor(),
                desafioAmistoso.getProponenteId().valor(),
                desafioAmistoso.getTimeDesafianteId().valor(),
                desafioAmistoso.getTimeDesafiadoId().valor(),
                desafioAmistoso.getDataHora(),
                desafioAmistoso.getLocal(),
                desafioAmistoso.getStatus().name(),
                desafioAmistoso.getResultado().map(ResultadoAmistoso::golsDesafiante).orElse(null),
                desafioAmistoso.getResultado().map(ResultadoAmistoso::golsDesafiado).orElse(null));
    }

    public record DesafioResumo(long id,
                                long proponenteId,
                                long timeDesafianteId,
                                long timeDesafiadoId,
                                LocalDateTime dataHora,
                                String local,
                                String status,
                                Integer golsDesafiante,
                                Integer golsDesafiado) {
    }
}
