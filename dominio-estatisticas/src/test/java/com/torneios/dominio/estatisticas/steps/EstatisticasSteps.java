package com.torneios.dominio.estatisticas.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.estatisticas.EstatisticasFuncionalidade;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaJogador;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class EstatisticasSteps extends EstatisticasFuncionalidade {

    // =====================================================================
    // F15: Gerenciar sumula estatistica da partida
    // =====================================================================

    @Dado("que existe uma partida cadastrada")
    public void que_existe_partida_cadastrada() {
        configurarCenarioPadrao();
    }

    @Dado("que o usuario autenticado e o organizador")
    public void que_usuario_e_organizador() {
        assertTrue(consultaEstatisticaCompeticao.usuarioEhOrganizador(TORNEIO_ID, ORGANIZADOR_ID));
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

    @Dado("que existe um evento estatistico registrado na sumula da partida")
    public void que_existe_evento_estatistico_registrado_na_sumula() {
        configurarCenarioPadrao();
        eventoRegistrado = eventoEstatisticoServico.registrarGol(
                100L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
    }

    @Quando("ele corrigir o evento estatistico da sumula")
    public void ele_corrigir_evento_estatistico_da_sumula() {
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

    @Quando("ele remover o evento estatistico da sumula")
    public void ele_remover_evento_estatistico_da_sumula() {
        try {
            eventoEstatisticoServico.removerEvento(100L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve retirar o evento da sumula da partida")
    public void sistema_deve_retirar_evento_da_sumula() {
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

    // =====================================================================
    // F16: Consolidar estatisticas e rankings do torneio
    // =====================================================================

    @Dado("que existem eventos registrados para um jogador")
    public void que_existem_eventos_registrados() {
        configurarCenarioPadrao();
        eventoEstatisticoServico.registrarGol(1L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarGol(2L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarAssistencia(3L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarCartaoAmarelo(4L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
    }

    @Quando("o sistema consolidar estatisticas e rankings do torneio")
    public void sistema_consolidar_estatisticas_e_rankings() {
        try {
            estatisticasJogadores = estatisticaServico.listarEstatisticasJogadores(TORNEIO_ID);
            rankingArtilharia = artilhariaServico.gerarRanking(TORNEIO_ID);
            rankingAssistencias = estatisticaServico.listarLideresAssistencias(TORNEIO_ID);
            historicoJogador = estatisticaServico.obterHistoricoJogador(TORNEIO_ID, JOGADOR_A_ID);
            notaEstatistica = estatisticaServico.calcularNotaJogador(TORNEIO_ID, PARTIDA_ID, JOGADOR_A_ID)
                    .orElse(null);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve atualizar nota, artilharia, lideres de assistencias e historico do jogador")
    public void sistema_deve_atualizar_nota_rankings_e_historico() {
        assertNull(excecaoCapturada);
        assertNotNull(notaEstatistica);
        assertEquals(10.0, notaEstatistica.valor(), 0.01);

        assertNotNull(rankingArtilharia);
        assertFalse(rankingArtilharia.isEmpty());
        assertEquals(JOGADOR_A_ID, rankingArtilharia.get(0).getJogadorId());
        assertEquals(2, rankingArtilharia.get(0).getGols());

        assertNotNull(rankingAssistencias);
        assertFalse(rankingAssistencias.isEmpty());
        assertEquals(JOGADOR_A_ID, rankingAssistencias.get(0).getJogadorId());
        assertEquals(1, rankingAssistencias.get(0).getAssistencias());

        assertNotNull(historicoJogador);
        assertEquals(4, historicoJogador.size());
    }

    @Dado("que existem gols registrados no torneio")
    public void que_existem_gols_registrados() {
        configurarCenarioPadrao();
        eventoEstatisticoServico.registrarGol(10L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarGol(11L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarGol(12L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
    }

    @Quando("o sistema consolidar a artilharia do torneio")
    public void sistema_consolidar_artilharia_torneio() {
        try {
            rankingArtilharia = artilhariaServico.gerarRanking(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve exibir os jogadores ordenados por numero de gols")
    public void o_sistema_deve_exibir_jogadores_ordenados() {
        assertNull(excecaoCapturada);
        assertNotNull(rankingArtilharia);
        assertFalse(rankingArtilharia.isEmpty());
        assertEquals(JOGADOR_A_ID, rankingArtilharia.get(0).getJogadorId());
        assertEquals(2, rankingArtilharia.get(0).getGols());
        assertEquals(JOGADOR_B_ID, rankingArtilharia.get(1).getJogadorId());
        assertEquals(1, rankingArtilharia.get(1).getGols());
    }

    @Dado("que novos eventos foram registrados em uma partida")
    public void que_novos_eventos_foram_registrados() {
        configurarCenarioPadrao();
        eventoEstatisticoServico.registrarGol(30L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarCartaoVermelho(31L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
    }

    @Entao("o sistema deve exibir os dados atualizados")
    public void o_sistema_deve_exibir_dados_atualizados() {
        assertNull(excecaoCapturada);
        assertNotNull(estatisticasJogadores);
        EstatisticaJogador estatA = estatisticasJogadores.stream()
                .filter(e -> e.getJogadorId().equals(JOGADOR_A_ID))
                .findFirst()
                .orElse(null);
        EstatisticaJogador estatB = estatisticasJogadores.stream()
                .filter(e -> e.getJogadorId().equals(JOGADOR_B_ID))
                .findFirst()
                .orElse(null);
        assertNotNull(estatA);
        assertNotNull(estatB);
        assertEquals(1, estatA.getGols());
        assertEquals(1, estatB.getCartoesVermelhos());
    }

    @Dado("que existe uma partida sem eventos estatisticos registrados")
    public void que_existe_uma_partida_sem_eventos_estatisticos_registrados() {
        configurarCenarioPadrao();
    }

    @Quando("o sistema tentar consolidar estatisticas sem eventos")
    public void sistema_tentar_consolidar_estatisticas_sem_eventos() {
        try {
            estatisticasJogadores = estatisticaServico.listarEstatisticasJogadores(TORNEIO_ID);
            rankingArtilharia = artilhariaServico.gerarRanking(TORNEIO_ID);
            rankingAssistencias = estatisticaServico.listarLideresAssistencias(TORNEIO_ID);
            historicoJogador = estatisticaServico.obterHistoricoJogador(TORNEIO_ID, JOGADOR_A_ID);
            notaEstatistica = estatisticaServico.calcularNotaJogador(TORNEIO_ID, PARTIDA_ID, JOGADOR_A_ID)
                    .orElse(null);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve manter apenas o placar oficial sem dados estatisticos")
    public void sistema_deve_manter_apenas_placar_oficial() {
        assertNull(excecaoCapturada);
        assertNotNull(estatisticasJogadores);
        assertTrue(estatisticasJogadores.isEmpty());
        assertNotNull(rankingArtilharia);
        assertTrue(rankingArtilharia.isEmpty());
        assertNotNull(rankingAssistencias);
        assertTrue(rankingAssistencias.isEmpty());
        assertNotNull(historicoJogador);
        assertTrue(historicoJogador.isEmpty());
        assertNull(notaEstatistica);
    }
}
