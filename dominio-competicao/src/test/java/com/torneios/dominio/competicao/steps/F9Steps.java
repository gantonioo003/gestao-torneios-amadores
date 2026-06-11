package com.torneios.dominio.competicao.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.competicao.CompeticaoFuncionalidade;
import com.torneios.dominio.competicao.geracao.ModoPreparacaoCompeticao;
import com.torneios.dominio.competicao.partida.Partida;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F9Steps extends CompeticaoFuncionalidade {

    @Dado("que existe um torneio com formato pontos corridos")
    public void que_existe_torneio_pontos_corridos() {
        configurarTorneioPontosCorridos(true);
    }

    @Dado("que a estrutura da competicao ja foi gerada")
    public void que_estrutura_ja_foi_gerada() {
        assertTrue(consultaCompeticaoTorneio.estruturaGerada(TORNEIO_ID));
    }

    @Quando("o organizador preparar a competicao do torneio")
    public void o_organizador_preparar_competicao() {
        try {
            preparacaoCompeticao = partidaServico.prepararCompeticao(TORNEIO_ID, ORGANIZADOR_ID);
            partidasGeradas = preparacaoCompeticao.getPartidas();
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve registrar as partidas e rodadas da competicao")
    public void o_sistema_deve_registrar_partidas_e_rodadas_competicao() {
        assertNull(excecaoCapturada);
        assertNotNull(partidasGeradas);
        assertFalse(partidasGeradas.isEmpty());
        assertNotNull(preparacaoCompeticao);
        assertFalse(preparacaoCompeticao.getRodadas().isEmpty());
        assertEquals(3, partidasGeradas.size());
    }

    @Quando("o organizador preparar a competicao do torneio por sorteio")
    public void o_organizador_preparar_competicao_por_sorteio() {
        try {
            preparacaoCompeticao = partidaServico.prepararCompeticaoPorSorteio(TORNEIO_ID, ORGANIZADOR_ID);
            partidasGeradas = preparacaoCompeticao.getPartidas();
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador preparar a competicao escolhendo manualmente a ordem dos times")
    public void o_organizador_preparar_competicao_manual() {
        try {
            preparacaoCompeticao = partidaServico.prepararCompeticaoManual(
                    TORNEIO_ID, ORGANIZADOR_ID, List.of(TIME_A_ID, TIME_B_ID));
            partidasGeradas = preparacaoCompeticao.getPartidas();
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve registrar os confrontos conforme a ordem escolhida")
    public void sistema_deve_registrar_confrontos_conforme_ordem() {
        assertNull(excecaoCapturada);
        assertNotNull(preparacaoCompeticao);
        assertEquals(ModoPreparacaoCompeticao.MANUAL, preparacaoCompeticao.getModoPreparacao());
        assertEquals(TIME_A_ID, partidasGeradas.get(0).getMandante());
        assertEquals(TIME_B_ID, partidasGeradas.get(0).getVisitante());
    }

    @Dado("que existe um torneio com formato mata-mata")
    public void que_existe_torneio_mata_mata() {
        configurarTorneioMataMata(true);
        Partida p1 = new Partida(new PartidaId(1L), TORNEIO_ID, TIME_A_ID, TIME_B_ID, "Chaveamento", 5);
        partidaRepositorio.salvar(p1);
    }

    @Entao("o sistema deve registrar as partidas do chaveamento")
    public void o_sistema_deve_registrar_partidas_chaveamento() {
        assertNull(excecaoCapturada);
        assertNotNull(partidasGeradas);
        assertFalse(partidasGeradas.isEmpty());
        assertEquals(1, partidasGeradas.size());
    }

    @Dado("que existe um torneio com fase de grupos")
    public void que_existe_torneio_fase_grupos() {
        configurarTorneioFaseGrupos(true);
    }

    @Entao("o sistema deve registrar as partidas da fase de grupos")
    public void o_sistema_deve_registrar_partidas_fase_grupos() {
        assertNull(excecaoCapturada);
        assertNotNull(partidasGeradas);
        assertFalse(partidasGeradas.isEmpty());
        assertEquals(2, partidasGeradas.size());
    }

    @Dado("que existe um torneio configurado")
    public void que_existe_torneio_configurado() {
        configurarTorneioPontosCorridos(false);
    }

    @Dado("que a estrutura da competicao ainda nao foi gerada")
    public void que_estrutura_nao_foi_gerada() {
        assertFalse(consultaCompeticaoTorneio.estruturaGerada(TORNEIO_ID));
    }
}
