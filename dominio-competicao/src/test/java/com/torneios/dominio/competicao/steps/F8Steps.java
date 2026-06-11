package com.torneios.dominio.competicao.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import com.torneios.dominio.competicao.CompeticaoFuncionalidade;
import com.torneios.dominio.competicao.contestacao.DecisaoContestacaoResultado;
import com.torneios.dominio.competicao.contestacao.StatusContestacaoResultado;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F8Steps extends CompeticaoFuncionalidade {

    @Dado("que existe uma partida finalizada com resultado oficial registrado")
    public void que_existe_partida_finalizada_com_resultado_oficial_registrado() {
        configurarCenarioContestacaoPadrao(true);
    }

    @Dado("que existe uma partida cadastrada sem resultado oficial registrado")
    public void que_existe_partida_cadastrada_sem_resultado_oficial_registrado() {
        configurarCenarioContestacaoPadrao(false);
    }

    @Dado("que o usuario autenticado e responsavel por um dos times da partida")
    public void que_usuario_autenticado_e_responsavel_por_time_da_partida() {
        assertTrue(consultaContestacao.usuarioEhResponsavelDoTime(TIME_A_ID, ORGANIZADOR_ID));
    }

    @Dado("que o usuario autenticado nao e responsavel pelos times da partida")
    public void que_usuario_nao_e_responsavel_pelos_times_da_partida() {
        usuarioSolicitanteId = OUTRO_USUARIO_ID;
    }

    @Dado("que existe uma partida finalizada com resultado oficial registrado ha mais tempo que o prazo do torneio")
    public void que_existe_partida_finalizada_ha_mais_tempo_que_prazo() {
        configurarTorneioPontosCorridos(true);
        consultaContestacao.registrarResponsavel(TIME_A_ID, ORGANIZADOR_ID);
        consultaContestacao.registrarPrazo(TORNEIO_ID, 24);
        Partida partida = new Partida(PARTIDA_ID, TORNEIO_ID, TIME_A_ID, TIME_B_ID, "Rodada 1", 5);
        partida.registrarResultado(new ResultadoPartida(2, 1), LocalDateTime.now().minusHours(30));
        partidaServico.salvar(partida);
    }

    @Dado("que ja existe uma contestacao pendente daquele time para a partida")
    public void que_ja_existe_contestacao_pendente_daquele_time_para_partida() {
        contestacaoResultado = contestacaoResultadoServico.abrirContestacao(
                CONTESTACAO_ID, PARTIDA_ID, TIME_A_ID, usuarioSolicitanteId,
                "Placar incorreto", "O segundo gol do mandante nao foi validado corretamente.",
                List.of("sumula.pdf"), LocalDateTime.now());
    }

    @Dado("que existe uma contestacao pendente de resultado")
    public void que_existe_uma_contestacao_pendente_de_resultado() {
        que_existe_partida_finalizada_com_resultado_oficial_registrado();
        que_ja_existe_contestacao_pendente_daquele_time_para_partida();
    }

    @Quando("ele abrir uma contestacao de resultado com motivo justificativa e evidencias")
    public void ele_abrir_contestacao_com_motivo_justificativa_e_evidencias() {
        try {
            contestacaoResultado = contestacaoResultadoServico.abrirContestacao(
                    CONTESTACAO_ID, PARTIDA_ID, TIME_A_ID, usuarioSolicitanteId,
                    "Placar incorreto", "O placar oficial nao considera um gol validado em sumula.",
                    List.of("sumula-oficial.pdf", "video-lance.mp4"), LocalDateTime.now());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar abrir uma contestacao de resultado")
    public void ele_tentar_abrir_uma_contestacao_de_resultado() {
        try {
            contestacaoResultado = contestacaoResultadoServico.abrirContestacao(
                    CONTESTACAO_ID, PARTIDA_ID, TIME_A_ID, usuarioSolicitanteId,
                    "Placar incorreto", "Ha divergencia entre o placar oficial e a sumula.",
                    List.of("sumula.pdf"), LocalDateTime.now());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar abrir outra contestacao para a mesma partida")
    public void ele_tentar_abrir_outra_contestacao_para_mesma_partida() {
        try {
            contestacaoResultado = contestacaoResultadoServico.abrirContestacao(
                    OUTRA_CONTESTACAO_ID, PARTIDA_ID, TIME_A_ID, usuarioSolicitanteId,
                    "Placar incorreto", "Nova tentativa de contestar o mesmo resultado.",
                    List.of(), LocalDateTime.now());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador aceitar a contestacao informando placar corrigido")
    public void organizador_aceitar_contestacao_informando_placar_corrigido() {
        try {
            contestacaoResultado = contestacaoResultadoServico.analisarContestacao(
                    CONTESTACAO_ID, ORGANIZADOR_ID, DecisaoContestacaoResultado.ACEITAR,
                    "Contestacao aceita apos conferencia da sumula.",
                    new ResultadoPartida(3, 1), LocalDateTime.now());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador rejeitar a contestacao")
    public void organizador_rejeitar_contestacao() {
        try {
            contestacaoResultado = contestacaoResultadoServico.analisarContestacao(
                    CONTESTACAO_ID, ORGANIZADOR_ID, DecisaoContestacaoResultado.REJEITAR,
                    "Resultado oficial confirmado.", null, LocalDateTime.now());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador solicitar correcao da contestacao")
    public void organizador_solicitar_correcao_da_contestacao() {
        try {
            contestacaoResultado = contestacaoResultadoServico.analisarContestacao(
                    CONTESTACAO_ID, ORGANIZADOR_ID, DecisaoContestacaoResultado.SOLICITAR_CORRECAO,
                    "Anexar evidencia mais legivel.", null, LocalDateTime.now());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que o usuario autenticado nao e o organizador do torneio")
    public void que_usuario_autenticado_nao_e_organizador_do_torneio() {
        assertFalse(consultaCompeticaoTorneio.usuarioEhOrganizador(TORNEIO_ID, OUTRO_USUARIO_ID));
    }

    @Quando("ele tentar analisar a contestacao")
    public void ele_tentar_analisar_a_contestacao() {
        try {
            contestacaoResultado = contestacaoResultadoServico.analisarContestacao(
                    CONTESTACAO_ID, OUTRO_USUARIO_ID, DecisaoContestacaoResultado.REJEITAR,
                    "Tentativa sem permissao.", null, LocalDateTime.now());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve registrar a contestacao como pendente")
    public void sistema_deve_registrar_contestacao_como_pendente() {
        assertNull(excecaoCapturada);
        assertNotNull(contestacaoResultado);
        assertEquals(StatusContestacaoResultado.PENDENTE, contestacaoResultado.getStatus());
        assertTrue(contestacaoRepositorio.buscarPorId(CONTESTACAO_ID).isPresent());
    }

    @Entao("a contestacao deve ficar associada a partida ao torneio ao time e ao usuario solicitante")
    public void contestacao_deve_ficar_associada_a_partida_torneio_time_usuario() {
        assertEquals(PARTIDA_ID, contestacaoResultado.getPartidaId());
        assertEquals(TORNEIO_ID, contestacaoResultado.getTorneioId());
        assertEquals(TIME_A_ID, contestacaoResultado.getTimeSolicitanteId());
        assertEquals(ORGANIZADOR_ID, contestacaoResultado.getUsuarioSolicitanteId());
    }

    @Entao("o sistema deve impedir a abertura da contestacao")
    public void sistema_deve_impedir_abertura_da_contestacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve marcar a contestacao como aceita")
    public void sistema_deve_marcar_contestacao_como_aceita() {
        assertNull(excecaoCapturada);
        assertEquals(StatusContestacaoResultado.ACEITA, contestacaoResultado.getStatus());
    }

    @Entao("o sistema deve marcar a contestacao como rejeitada")
    public void sistema_deve_marcar_contestacao_como_rejeitada() {
        assertNull(excecaoCapturada);
        assertEquals(StatusContestacaoResultado.REJEITADA, contestacaoResultado.getStatus());
    }

    @Entao("o sistema deve marcar a contestacao como aguardando correcao")
    public void sistema_deve_marcar_contestacao_aguardando_correcao() {
        assertNull(excecaoCapturada);
        assertEquals(StatusContestacaoResultado.CORRECAO_SOLICITADA, contestacaoResultado.getStatus());
    }

    @Entao("deve registrar a decisao no historico da contestacao")
    public void deve_registrar_decisao_no_historico_da_contestacao() {
        assertFalse(contestacaoResultado.getHistorico().isEmpty());
    }

    @Entao("deve atualizar o resultado oficial da partida")
    public void deve_atualizar_resultado_oficial_da_partida() {
        var partida = partidaRepositorio.buscarPorId(PARTIDA_ID).orElseThrow();
        assertEquals(3, partida.getResultado().golsMandante());
        assertEquals(1, partida.getResultado().golsVisitante());
    }

    @Entao("o sistema deve impedir a analise da contestacao")
    public void sistema_deve_impedir_analise_da_contestacao() {
        assertNotNull(excecaoCapturada);
        assertEquals(StatusContestacaoResultado.PENDENTE,
                contestacaoRepositorio.buscarPorId(CONTESTACAO_ID).orElseThrow().getStatus());
    }
}
