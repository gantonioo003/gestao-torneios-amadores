package com.torneios.dominio.estatisticas.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.estatisticas.EstatisticasFuncionalidade;
import com.torneios.dominio.estatisticas.comparacao.TipoComparativoDesempenho;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F7Steps extends EstatisticasFuncionalidade {

    @Dado("que existem estatisticas registradas para dois jogadores")
    public void que_existem_estatisticas_registradas_para_dois_jogadores() {
        configurarCenarioPadrao();
        eventoEstatisticoServico.registrarGol(201L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarAssistencia(202L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarGol(203L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
        eventoEstatisticoServico.registrarGol(204L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
        eventoEstatisticoServico.registrarCartaoAmarelo(205L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
    }

    @Dado("que existem estatisticas registradas para dois times")
    public void que_existem_estatisticas_registradas_para_dois_times() {
        que_existem_estatisticas_registradas_para_dois_jogadores();
    }

    @Dado("que nao existem estatisticas registradas para comparacao")
    public void que_nao_existem_estatisticas_registradas_para_comparacao() {
        configurarCenarioPadrao();
    }

    @Quando("o usuario gerar um comparativo entre os jogadores")
    public void usuario_gerar_comparativo_entre_jogadores() {
        try {
            comparativoDesempenho = comparacaoDesempenhoServico.gerarComparativoJogadores(
                    1L, JOGADOR_A_ID, JOGADOR_B_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario gerar um comparativo entre os times")
    public void usuario_gerar_comparativo_entre_times() {
        try {
            comparativoDesempenho = comparacaoDesempenhoServico.gerarComparativoTimes(
                    2L, TIME_A_ID, TIME_B_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario tentar gerar um comparativo entre os jogadores")
    public void usuario_tentar_gerar_comparativo_entre_jogadores() {
        usuario_gerar_comparativo_entre_jogadores();
    }

    @Quando("o usuario tentar comparar um jogador com um perfil de treinador")
    public void usuario_tentar_comparar_jogador_com_treinador() {
        try {
            comparacaoDesempenhoServico.gerarComparativoJogadores(
                    3L, JOGADOR_A_ID, new JogadorId(999L));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que existe um comparativo temporario gerado")
    public void que_existe_um_comparativo_temporario_gerado() {
        que_existem_estatisticas_registradas_para_dois_jogadores();
        comparativoDesempenho = comparacaoDesempenhoServico.gerarComparativoJogadores(
                10L, JOGADOR_A_ID, JOGADOR_B_ID);
    }

    @Dado("que existe um comparativo geral salvo")
    public void que_existe_um_comparativo_geral_salvo() {
        que_existe_um_comparativo_temporario_gerado();
        comparacaoDesempenhoServico.salvarComparativo(comparativoDesempenho);
    }

    @Dado("que novos eventos alteraram o desempenho dos jogadores")
    public void que_novos_eventos_alteraram_o_desempenho_dos_jogadores() {
        eventoEstatisticoServico.registrarGol(206L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarGol(207L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
    }

    @Quando("o usuario salvar o comparativo escolhido")
    public void usuario_salvar_comparativo_escolhido() {
        try {
            comparacaoDesempenhoServico.salvarComparativo(comparativoDesempenho);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario consultar os comparativos gerais salvos")
    public void usuario_consultar_comparativos_gerais_salvos() {
        try {
            comparativosSalvos = comparacaoDesempenhoServico.consultarComparativosSalvos();
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario atualizar o comparativo salvo")
    public void usuario_atualizar_comparativo_salvo() {
        try {
            var comparativoAtualizado = comparacaoDesempenhoServico.gerarComparativoJogadores(
                    comparativoDesempenho.getId(), JOGADOR_A_ID, JOGADOR_B_ID);
            comparativoDesempenho = comparacaoDesempenhoServico.atualizarComparativoSalvo(comparativoAtualizado);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario excluir o comparativo salvo")
    public void usuario_excluir_comparativo_salvo() {
        try {
            comparacaoDesempenhoServico.excluirComparativoSalvo(comparativoDesempenho.getId());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve exibir o comparativo temporario dos jogadores")
    public void sistema_deve_exibir_comparativo_temporario_dos_jogadores() {
        assertNull(excecaoCapturada);
        assertNotNull(comparativoDesempenho);
        assertEquals(TipoComparativoDesempenho.JOGADORES, comparativoDesempenho.getTipo());
    }

    @Entao("o sistema deve exibir o comparativo temporario dos times")
    public void sistema_deve_exibir_comparativo_temporario_dos_times() {
        assertNull(excecaoCapturada);
        assertNotNull(comparativoDesempenho);
        assertEquals(TipoComparativoDesempenho.TIMES, comparativoDesempenho.getTipo());
    }

    @Entao("o comparativo nao deve estar salvo ainda")
    public void comparativo_nao_deve_estar_salvo_ainda() {
        assertTrue(comparativoRepositorio.buscarPorId(comparativoDesempenho.getId()).isEmpty());
    }

    @Entao("o sistema deve armazenar o comparativo salvo")
    public void sistema_deve_armazenar_comparativo_salvo() {
        assertNull(excecaoCapturada);
        assertTrue(comparativoRepositorio.buscarPorId(comparativoDesempenho.getId()).isPresent());
    }

    @Entao("o sistema deve listar os comparativos salvos")
    public void sistema_deve_listar_comparativos_salvos() {
        assertNull(excecaoCapturada);
        assertNotNull(comparativosSalvos);
        assertFalse(comparativosSalvos.isEmpty());
    }

    @Entao("o sistema deve substituir o comparativo pelos dados atualizados")
    public void sistema_deve_substituir_comparativo_pelos_dados_atualizados() {
        assertNull(excecaoCapturada);
        var salvo = comparativoRepositorio.buscarPorId(comparativoDesempenho.getId()).orElseThrow();
        assertEquals(3, salvo.getPrimeiro().getGols());
        assertEquals("Joao Silva", salvo.getMelhorDesempenho().orElseThrow().getRotulo());
    }

    @Entao("o sistema deve remover o comparativo do historico")
    public void sistema_deve_remover_comparativo_do_historico() {
        assertNull(excecaoCapturada);
        assertTrue(comparativoRepositorio.buscarPorId(comparativoDesempenho.getId()).isEmpty());
    }

    @Entao("deve indicar vantagem por estatisticas historico e ranking")
    public void deve_indicar_vantagem_por_estatisticas_historico_e_ranking() {
        assertTrue(comparativoDesempenho.getMelhorDesempenho().isPresent());
        assertEquals("Pedro Santos", comparativoDesempenho.getMelhorDesempenho().orElseThrow().getRotulo());
        assertTrue(comparativoDesempenho.getPrimeiro().getPartidasComEventos() > 0);
        assertTrue(comparativoDesempenho.getSegundo().getPosicaoRanking() > 0);
    }

    @Entao("deve comparar gols assistencias cartoes historico e ranking")
    public void deve_comparar_gols_assistencias_cartoes_historico_e_ranking() {
        assertEquals(1, comparativoDesempenho.getPrimeiro().getGols());
        assertEquals(1, comparativoDesempenho.getPrimeiro().getAssistencias());
        assertEquals(2, comparativoDesempenho.getSegundo().getGols());
        assertEquals(1, comparativoDesempenho.getSegundo().getTotalCartoes());
        assertTrue(comparativoDesempenho.getPrimeiro().getPartidasComEventos() > 0);
        assertTrue(comparativoDesempenho.getSegundo().getPosicaoRanking() > 0);
    }

    @Entao("o sistema deve exibir os dois perfis com estatisticas zeradas")
    public void sistema_deve_exibir_perfis_com_estatisticas_zeradas() {
        assertNull(excecaoCapturada);
        assertNotNull(comparativoDesempenho);
        assertEquals(0, comparativoDesempenho.getPrimeiro().getGols());
        assertEquals(0, comparativoDesempenho.getSegundo().getGols());
        assertTrue(comparativoDesempenho.getMelhorDesempenho().isEmpty());
    }

    @Entao("o sistema deve impedir a comparacao com perfil que nao seja jogador")
    public void sistema_deve_impedir_comparacao_com_perfil_nao_jogador() {
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().contains("apenas perfis de jogador"));
    }
}
