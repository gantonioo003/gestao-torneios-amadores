package com.torneios.dominio.estatisticas.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.torneios.dominio.estatisticas.EstatisticasFuncionalidade;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaJogador;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F14Steps extends EstatisticasFuncionalidade {

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

    @Quando("o sistema consolidar estatisticas dos jogadores")
    public void sistema_consolidar_estatisticas_jogadores() {
        try {
            estatisticasJogadores = estatisticaServico.listarEstatisticasJogadores(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
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

    @Entao("o sistema deve manter apenas o placar oficial sem scout detalhado")
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

    @Dado("que existem jogadores com assistencias e notas diferentes")
    public void que_existem_jogadores_com_assistencias_e_notas_diferentes() {
        configurarCenarioPadrao();
        eventoEstatisticoServico.registrarGol(40L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarAssistencia(41L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
        eventoEstatisticoServico.registrarAssistencia(42L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
    }

    @Quando("o sistema ordenar os rankings individuais do torneio")
    public void sistema_ordenar_rankings_individuais() {
        try {
            rankingAssistencias = estatisticaServico.listarLideresAssistencias(TORNEIO_ID);
            rankingNotas = estatisticaServico.listarMelhoresMediasDeNota(TORNEIO_ID, 1);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o lider de assistencias e o jogador de maior nota devem aparecer primeiro")
    public void lider_assistencias_e_maior_nota_devem_aparecer_primeiro() {
        assertNull(excecaoCapturada);
        assertEquals(JOGADOR_B_ID, rankingAssistencias.get(0).getJogadorId());
        assertEquals(2, rankingAssistencias.get(0).getAssistencias());
        assertEquals(JOGADOR_B_ID, rankingNotas.get(0).jogadorId());
        assertTrue(rankingNotas.get(0).media() > rankingNotas.get(1).media());
    }
}
