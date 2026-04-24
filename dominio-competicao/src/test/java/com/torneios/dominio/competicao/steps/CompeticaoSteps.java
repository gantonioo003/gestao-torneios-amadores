package com.torneios.dominio.competicao.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.competicao.CompeticaoFuncionalidade;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class CompeticaoSteps extends CompeticaoFuncionalidade {

    // =====================================================================
    // F12: Gerar partidas do torneio
    // =====================================================================

    @Dado("que existe um torneio com formato pontos corridos")
    public void que_existe_torneio_pontos_corridos() {
        configurarTorneioPontosCorridos(true);
    }

    @Dado("que a estrutura da competição já foi gerada")
    public void que_estrutura_ja_foi_gerada() {
        assertTrue(consultaCompeticaoTorneio.estruturaGerada(TORNEIO_ID));
    }

    @Quando("o organizador gerar as partidas do torneio")
    public void o_organizador_gerar_partidas() {
        try {
            partidasGeradas = partidaServico.gerarPartidas(TORNEIO_ID, ORGANIZADOR_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve registrar as partidas da competição")
    public void o_sistema_deve_registrar_partidas_competicao() {
        assertNull(excecaoCapturada);
        assertNotNull(partidasGeradas);
        assertFalse(partidasGeradas.isEmpty());
        assertEquals(3, partidasGeradas.size());
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

    @Dado("que a estrutura da competição ainda não foi gerada")
    public void que_estrutura_nao_foi_gerada() {
        assertFalse(consultaCompeticaoTorneio.estruturaGerada(TORNEIO_ID));
    }

    // =====================================================================
    // F13: Registrar resultado da partida
    // =====================================================================

    @Dado("que existe uma partida cadastrada no torneio")
    public void que_existe_partida_cadastrada() {
        configurarTorneioPontosCorridos(true);
        Partida partida = new Partida(PARTIDA_ID, TORNEIO_ID, TIME_A_ID, TIME_B_ID, "Pontos corridos", 5);
        partidaRepositorio.salvar(partida);
    }

    @Dado("que o usuário autenticado é o organizador do torneio")
    public void que_usuario_e_organizador() {
        assertTrue(consultaCompeticaoTorneio.usuarioEhOrganizador(TORNEIO_ID, ORGANIZADOR_ID));
    }

    @Quando("ele registrar o placar da partida")
    public void ele_registrar_placar() {
        try {
            atualizacaoCompeticao = partidaServico.registrarResultado(
                    TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, new ResultadoPartida(2, 1));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve armazenar o resultado da partida")
    public void o_sistema_deve_armazenar_resultado() {
        assertNull(excecaoCapturada);
        Partida partida = partidaServico.obterPartida(PARTIDA_ID);
        assertTrue(partida.estaEncerrada());
        assertNotNull(partida.getResultado());
        assertEquals(2, partida.getResultado().golsMandante());
        assertEquals(1, partida.getResultado().golsVisitante());
    }

    @Quando("ele registrar o resultado da partida")
    public void ele_registrar_resultado() {
        try {
            atualizacaoCompeticao = partidaServico.registrarResultado(
                    TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, new ResultadoPartida(1, 0));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve atualizar automaticamente a classificação ou chaveamento")
    public void o_sistema_deve_atualizar_classificacao_ou_chaveamento() {
        assertNull(excecaoCapturada);
        assertNotNull(atualizacaoCompeticao);
        assertNotNull(atualizacaoCompeticao.classificacaoAtualizada());
        assertFalse(atualizacaoCompeticao.classificacaoAtualizada().isEmpty());
    }

    @Dado("que o usuário autenticado não é o organizador")
    public void que_usuario_nao_e_organizador() {
        assertFalse(consultaCompeticaoTorneio.usuarioEhOrganizador(TORNEIO_ID, OUTRO_USUARIO_ID));
    }

    @Quando("ele tentar registrar o resultado da partida")
    public void ele_tentar_registrar_resultado() {
        try {
            atualizacaoCompeticao = partidaServico.registrarResultado(
                    TORNEIO_ID, PARTIDA_ID, OUTRO_USUARIO_ID, new ResultadoPartida(1, 0));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que não existe a partida informada")
    public void que_nao_existe_partida() {
        configurarTorneioPontosCorridos(true);
    }

    @Quando("o usuário tentar registrar um resultado")
    public void o_usuario_tentar_registrar_resultado() {
        try {
            PartidaId partidaInexistente = new PartidaId(999L);
            atualizacaoCompeticao = partidaServico.registrarResultado(
                    TORNEIO_ID, partidaInexistente, ORGANIZADOR_ID, new ResultadoPartida(1, 0));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // F14: Visualizar classificação ou chaveamento
    // =====================================================================

    @Quando("o usuário acessar a classificação")
    public void o_usuario_acessar_classificacao() {
        try {
            classificacao = partidaServico.visualizarClassificacao(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve exibir a tabela com a pontuação dos times")
    public void o_sistema_deve_exibir_tabela_pontuacao() {
        assertNull(excecaoCapturada);
        assertNotNull(classificacao);
        assertFalse(classificacao.isEmpty());
    }

    @Quando("o usuário acessar o chaveamento")
    public void o_usuario_acessar_chaveamento() {
        try {
            chaveamento = partidaServico.visualizarChaveamento(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve exibir a estrutura eliminatória do torneio")
    public void o_sistema_deve_exibir_estrutura_eliminatoria() {
        assertNull(excecaoCapturada);
        assertNotNull(chaveamento);
        assertFalse(chaveamento.getFases().isEmpty());
        assertFalse(chaveamento.getPartidas().isEmpty());
    }

    @Dado("que um resultado de partida foi registrado")
    public void que_resultado_de_partida_foi_registrado() {
        configurarTorneioPontosCorridos(true);
        Partida p1 = new Partida(new PartidaId(1L), TORNEIO_ID, TIME_A_ID, TIME_B_ID, "Pontos corridos", 5);
        p1.registrarResultado(new ResultadoPartida(1, 0));
        partidaRepositorio.salvar(p1);
    }

    @Entao("o sistema deve exibir a classificação atualizada")
    public void o_sistema_deve_exibir_classificacao_atualizada() {
        assertNull(excecaoCapturada);
        assertNotNull(classificacao);
        assertFalse(classificacao.isEmpty());
        assertEquals(TIME_A_ID, classificacao.get(0).getTimeId());
        assertEquals(3, classificacao.get(0).getPontos());
    }

    @Dado("que o torneio ainda não possui estrutura definida")
    public void que_torneio_nao_possui_estrutura() {
        configurarTorneioPontosCorridos(false);
    }

    @Quando("o usuário acessar classificação ou chaveamento")
    public void o_usuario_acessar_classificacao_ou_chaveamento() {
        try {
            classificacao = partidaServico.visualizarClassificacao(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve informar que a competição ainda não foi iniciada")
    public void o_sistema_deve_informar_competicao_nao_iniciada() {
        assertNotNull(excecaoCapturada);
    }

    // =====================================================================
    // Steps compartilhados
    // =====================================================================

    @Entao("o sistema deve impedir a operação")
    public void o_sistema_deve_impedir_operacao() {
        assertNotNull(excecaoCapturada);
    }
}
