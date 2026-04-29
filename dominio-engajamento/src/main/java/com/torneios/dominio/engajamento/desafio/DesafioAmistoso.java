package com.torneios.dominio.engajamento.desafio;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class DesafioAmistoso {

    private final DesafioAmistosoId id;
    private final UsuarioId proponenteId;
    private final TimeId timeDesafianteId;
    private final TimeId timeDesafiadoId;
    private LocalDateTime dataHora;
    private String local;
    private StatusDesafioAmistoso status;
    private ResultadoAmistoso resultado;

    public DesafioAmistoso(DesafioAmistosoId id,
                           UsuarioId proponenteId,
                           TimeId timeDesafianteId,
                           TimeId timeDesafiadoId,
                           LocalDateTime dataHora,
                           String local) {
        this.id = Objects.requireNonNull(id, "O id do desafio e obrigatorio.");
        this.proponenteId = Objects.requireNonNull(proponenteId, "O proponente do desafio e obrigatorio.");
        this.timeDesafianteId = Objects.requireNonNull(timeDesafianteId, "O time desafiante e obrigatorio.");
        this.timeDesafiadoId = Objects.requireNonNull(timeDesafiadoId, "O time desafiado e obrigatorio.");
        if (timeDesafianteId.equals(timeDesafiadoId)) {
            throw new RegraDeNegocioException("Um time nao pode desafiar ele mesmo.");
        }
        this.dataHora = Objects.requireNonNull(dataHora, "A data e horario do amistoso sao obrigatorios.");
        this.local = validarLocal(local);
        this.status = StatusDesafioAmistoso.PROPOSTO;
    }

    public DesafioAmistosoId getId() {
        return id;
    }

    public UsuarioId getProponenteId() {
        return proponenteId;
    }

    public TimeId getTimeDesafianteId() {
        return timeDesafianteId;
    }

    public TimeId getTimeDesafiadoId() {
        return timeDesafiadoId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getLocal() {
        return local;
    }

    public StatusDesafioAmistoso getStatus() {
        return status;
    }

    public Optional<ResultadoAmistoso> getResultado() {
        return Optional.ofNullable(resultado);
    }

    public void aceitar() {
        exigirStatus(StatusDesafioAmistoso.PROPOSTO);
        status = StatusDesafioAmistoso.ACEITO;
    }

    public void recusar() {
        exigirStatus(StatusDesafioAmistoso.PROPOSTO);
        status = StatusDesafioAmistoso.RECUSADO;
    }

    public void reagendar(LocalDateTime novaDataHora, String novoLocal) {
        if (status != StatusDesafioAmistoso.PROPOSTO && status != StatusDesafioAmistoso.ACEITO) {
            throw new OperacaoNaoPermitidaException("Nao e permitido reagendar desafio ja encerrado.");
        }
        dataHora = Objects.requireNonNull(novaDataHora, "A nova data e horario sao obrigatorios.");
        local = validarLocal(novoLocal);
    }

    public void cancelar() {
        if (status == StatusDesafioAmistoso.RESULTADO_REGISTRADO) {
            throw new OperacaoNaoPermitidaException("Nao e permitido cancelar amistoso com resultado registrado.");
        }
        status = StatusDesafioAmistoso.CANCELADO;
    }

    public void registrarResultado(ResultadoAmistoso resultadoAmistoso) {
        exigirStatus(StatusDesafioAmistoso.ACEITO);
        resultado = Objects.requireNonNull(resultadoAmistoso, "O resultado do amistoso e obrigatorio.");
        status = StatusDesafioAmistoso.RESULTADO_REGISTRADO;
    }

    public boolean envolveTime(TimeId timeId) {
        return timeDesafianteId.equals(timeId) || timeDesafiadoId.equals(timeId);
    }

    private void exigirStatus(StatusDesafioAmistoso statusEsperado) {
        if (status != statusEsperado) {
            throw new OperacaoNaoPermitidaException("O desafio nao esta no status esperado para esta operacao.");
        }
    }

    private String validarLocal(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O local do amistoso e obrigatorio.");
        }
        return valor.trim();
    }
}
