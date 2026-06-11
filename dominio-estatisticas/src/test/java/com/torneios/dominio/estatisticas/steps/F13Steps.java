package com.torneios.dominio.estatisticas.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.estatisticas.EstatisticasFuncionalidade;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F13Steps extends EstatisticasFuncionalidade {

    @Dado("que existe uma partida cadastrada")
    public void que_existe_partida_cadastrada() {
        configurarCenarioPadrao();
    }

    @Dado("que o usuario autenticado e o organizador")
    public void que_usuario_e_organizador() {
        assertTrue(consultaEstatisticaCompeticao.usuarioEhOrganizador(TORNEIO_ID, ORGANIZADOR_ID));
    }

    @Quando("ele optar por nao registrar eventos individuais da partida")
    public void ele_optar_por_nao_registrar_eventos_individuais_da_partida() {
    }

    @Entao("o sistema deve manter o scout opcional vazio")
    public void o_sistema_deve_manter_o_scout_opcional_vazio() {
        assertNull(excecaoCapturada);
        assertTrue(eventoRepositorio.listarPorPartida(PARTIDA_ID).isEmpty());
    }

    @Quando("ele registrar um gol e uma assistencia para jogadores")
    public void ele_registrar_gol_e_assistencia() {
        try {
            eventoEstatisticoServico.registrarGol(1L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
            eventoEstatisticoServico.registrarAssistencia(2L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve armazenar os eventos corretamente")
    public void o_sistema_deve_armazenar_eventos() {
        assertNull(excecaoCapturada);
        var eventos = eventoRepositorio.listarPorPartida(PARTIDA_ID);
        assertEquals(2, eventos.size());
    }

    @Quando("ele registrar cartao amarelo ou vermelho para jogadores")
    public void ele_registrar_cartoes() {
        try {
            eventoEstatisticoServico.registrarCartaoAmarelo(3L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
            eventoEstatisticoServico.registrarCartaoVermelho(4L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que existe uma partida cadastrada sem mesa tatica informada")
    public void que_existe_partida_cadastrada_sem_escalacao_informada() {
        configurarCenarioPadrao();
    }

    @Quando("ele registrar uma substituicao trocando um jogador por outro")
    public void ele_registrar_substituicao_trocando_jogador_por_outro() {
        try {
            eventoRegistrado = eventoEstatisticoServico.registrarSubstituicao(
                    50L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID, JOGADOR_B_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve armazenar a substituicao no scout da partida")
    public void sistema_deve_armazenar_substituicao_no_scout() {
        assertNull(excecaoCapturada);
        var evento = eventoRepositorio.buscarPorId(50L).orElseThrow();
        assertEquals(TipoEventoEstatistico.SUBSTITUICAO, evento.getTipo());
    }

    @Dado("que existe um evento individual registrado no scout da partida")
    public void que_existe_evento_individual_registrado_no_scout() {
        configurarCenarioPadrao();
        eventoRegistrado = eventoEstatisticoServico.registrarGol(
                100L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
    }

    @Quando("ele corrigir o evento individual do scout")
    public void ele_corrigir_evento_individual_do_scout() {
        try {
            eventoRegistrado = eventoEstatisticoServico.corrigirEvento(
                    100L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID,
                    TipoEventoEstatistico.ASSISTENCIA);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve atualizar o evento estatistico da partida")
    public void sistema_deve_atualizar_evento_estatistico_partida() {
        assertNull(excecaoCapturada);
        var eventoAtualizado = eventoRepositorio.buscarPorId(100L);
        assertTrue(eventoAtualizado.isPresent());
        assertEquals(TipoEventoEstatistico.ASSISTENCIA, eventoAtualizado.get().getTipo());
        assertEquals(JOGADOR_B_ID, eventoAtualizado.get().getJogadorId());
        assertEquals(1, eventoRepositorio.listarPorPartida(PARTIDA_ID).size());
    }

    @Quando("ele remover o evento individual do scout")
    public void ele_remover_evento_individual_do_scout() {
        try {
            eventoEstatisticoServico.removerEvento(100L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve retirar o evento do scout da partida")
    public void sistema_deve_retirar_evento_do_scout() {
        assertNull(excecaoCapturada);
        assertTrue(eventoRepositorio.buscarPorId(100L).isEmpty());
        assertTrue(eventoRepositorio.listarPorPartida(PARTIDA_ID).isEmpty());
    }

    @Dado("que o usuario autenticado nao e o organizador")
    public void que_usuario_nao_e_organizador() {
        assertFalse(consultaEstatisticaCompeticao.usuarioEhOrganizador(TORNEIO_ID, OUTRO_USUARIO_ID));
    }

    @Quando("ele tentar registrar eventos da partida")
    public void ele_tentar_registrar_eventos() {
        try {
            eventoEstatisticoServico.registrarGol(5L, TORNEIO_ID, PARTIDA_ID, OUTRO_USUARIO_ID, JOGADOR_A_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve impedir a operacao")
    public void o_sistema_deve_impedir_operacao() {
        assertNotNull(excecaoCapturada);
    }

    @Dado("que o jogador nao pertence aos times da partida")
    public void que_jogador_nao_pertence_aos_times() {
        assertFalse(consultaEstatisticaCompeticao.jogadorPertenceAosTimesDaPartida(PARTIDA_ID, JOGADOR_INVALIDO_ID));
    }

    @Quando("o organizador tentar registrar um evento para esse jogador")
    public void o_organizador_tentar_registrar_evento_jogador_invalido() {
        try {
            eventoEstatisticoServico.registrarGol(6L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_INVALIDO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve impedir o registro")
    public void o_sistema_deve_impedir_registro() {
        assertNotNull(excecaoCapturada);
    }
}
