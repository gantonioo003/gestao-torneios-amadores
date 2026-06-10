package com.torneios.dominio.estatisticas.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.torneios.dominio.compartilhado.enumeracao.TipoEventoEstatistico;
import com.torneios.dominio.estatisticas.EstatisticasFuncionalidade;
import com.torneios.dominio.estatisticas.comparacao.TipoComparativoDesempenho;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaJogador;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class EstatisticasSteps extends EstatisticasFuncionalidade {

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
                    1L, TORNEIO_ID, JOGADOR_A_ID, JOGADOR_B_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario gerar um comparativo entre os times")
    public void usuario_gerar_comparativo_entre_times() {
        try {
            comparativoDesempenho = comparacaoDesempenhoServico.gerarComparativoTimes(
                    2L, TORNEIO_ID, TIME_A_ID, TIME_B_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario tentar gerar um comparativo entre os jogadores")
    public void usuario_tentar_gerar_comparativo_entre_jogadores() {
        usuario_gerar_comparativo_entre_jogadores();
    }

    @Dado("que existe um comparativo temporario gerado")
    public void que_existe_um_comparativo_temporario_gerado() {
        que_existem_estatisticas_registradas_para_dois_jogadores();
        comparativoDesempenho = comparacaoDesempenhoServico.gerarComparativoJogadores(
                10L, TORNEIO_ID, JOGADOR_A_ID, JOGADOR_B_ID);
    }

    @Dado("que existe um comparativo salvo para o torneio")
    public void que_existe_um_comparativo_salvo_para_o_torneio() {
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

    @Quando("o usuario consultar os comparativos salvos do torneio")
    public void usuario_consultar_comparativos_salvos_do_torneio() {
        try {
            comparativosSalvos = comparacaoDesempenhoServico.consultarComparativosSalvos(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario atualizar o comparativo salvo")
    public void usuario_atualizar_comparativo_salvo() {
        try {
            var comparativoAtualizado = comparacaoDesempenhoServico.gerarComparativoJogadores(
                    comparativoDesempenho.getId(), TORNEIO_ID, JOGADOR_A_ID, JOGADOR_B_ID);
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

    @Entao("o sistema deve impedir a comparacao de desempenho")
    public void sistema_deve_impedir_comparacao_de_desempenho() {
        assertNotNull(excecaoCapturada);
        assertTrue(comparativoRepositorio.listarTodos().isEmpty());
    }

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
}
