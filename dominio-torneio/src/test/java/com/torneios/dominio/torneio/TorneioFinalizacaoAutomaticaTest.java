package com.torneios.dominio.torneio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.enumeracao.StatusTorneio;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.torneio.Torneio;

class TorneioFinalizacaoAutomaticaTest {

    @Test
    void finalizaTorneioComEstruturaQuandoCompeticaoFoiConcluida() {
        Torneio torneio = torneioComEstruturaGerada();

        torneio.finalizarAutomaticamente(true);

        assertEquals(StatusTorneio.FINALIZADO, torneio.getStatus());
        assertDoesNotThrow(() -> torneio.finalizarAutomaticamente(true));
    }

    @Test
    void impedeFinalizacaoAutomaticaComPartidasPendentes() {
        Torneio torneio = torneioComEstruturaGerada();

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> torneio.finalizarAutomaticamente(false));
        assertEquals(StatusTorneio.ESTRUTURA_GERADA, torneio.getStatus());
    }

    private Torneio torneioComEstruturaGerada() {
        Torneio torneio = new Torneio(
                new TorneioId(1L),
                "Final de teste",
                FormatoTorneio.FINAL_UNICA,
                FormatoEquipe.CINCO_POR_CINCO,
                new UsuarioId(10L),
                false);
        torneio.adicionarParticipante(new TimeId(100L));
        torneio.adicionarParticipante(new TimeId(200L));
        torneio.marcarEstruturaGerada();
        return torneio;
    }
}
