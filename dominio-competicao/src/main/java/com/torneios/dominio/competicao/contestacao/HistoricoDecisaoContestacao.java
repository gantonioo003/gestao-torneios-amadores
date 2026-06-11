package com.torneios.dominio.competicao.contestacao;

import java.time.LocalDateTime;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

public record HistoricoDecisaoContestacao(
        UsuarioId organizadorId,
        DecisaoContestacaoResultado decisao,
        String observacao,
        ResultadoPartida resultadoAnterior,
        ResultadoPartida resultadoCorrigido,
        LocalDateTime dataHoraDecisao) {

    public HistoricoDecisaoContestacao {
        if (organizadorId == null) {
            throw new IllegalArgumentException("O organizador da decisao e obrigatorio.");
        }
        if (decisao == null) {
            throw new IllegalArgumentException("A decisao da contestacao e obrigatoria.");
        }
        if (dataHoraDecisao == null) {
            throw new IllegalArgumentException("A data da decisao e obrigatoria.");
        }
        observacao = observacao == null ? "" : observacao.trim();
    }
}
