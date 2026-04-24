package com.torneios.dominio.estatisticas.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.torneios.dominio.estatisticas.EstatisticasFuncionalidade;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaJogador;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class EstatisticasSteps extends EstatisticasFuncionalidade {

    // =====================================================================
    // F15: Registrar eventos estatísticos da partida
    // =====================================================================

    @Dado("que existe uma partida cadastrada")
    public void que_existe_partida_cadastrada() {
        configurarCenarioPadrao();
    }

    @Dado("que o usuário autenticado é o organizador")
    public void que_usuario_e_organizador() {
        assertTrue(consultaEstatisticaCompeticao.usuarioEhOrganizador(TORNEIO_ID, ORGANIZADOR_ID));
    }

    @Quando("ele registrar um gol e uma assistência para jogadores")
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
        assertFalse(eventos.isEmpty());
        assertEquals(2, eventos.size());
    }

    @Quando("ele registrar cartão amarelo ou vermelho para jogadores")
    public void ele_registrar_cartoes() {
        try {
            eventoEstatisticoServico.registrarCartaoAmarelo(3L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
            eventoEstatisticoServico.registrarCartaoVermelho(4L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que o usuário autenticado não é o organizador")
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

    @Entao("o sistema deve impedir a operação")
    public void o_sistema_deve_impedir_operacao() {
        assertNotNull(excecaoCapturada);
    }

    @Dado("que o jogador não pertence aos times da partida")
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
    // F16: Calcular e visualizar estatísticas
    // =====================================================================

    @Dado("que existem eventos registrados para um jogador")
    public void que_existem_eventos_registrados() {
        configurarCenarioPadrao();
        eventoEstatisticoServico.registrarGol(1L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarGol(2L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarAssistencia(3L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarCartaoAmarelo(4L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
    }

    @Quando("o sistema calcular a nota estatística")
    public void o_sistema_calcular_nota() {
        try {
            notaEstatistica = estatisticaServico.calcularNotaJogador(TORNEIO_ID, PARTIDA_ID, JOGADOR_A_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("a nota deve refletir os eventos positivos e negativos")
    public void a_nota_deve_refletir_eventos() {
        assertNull(excecaoCapturada);
        assertNotNull(notaEstatistica);
        assertEquals(10.0, notaEstatistica.valor(), 0.01);
    }

    @Dado("que existem gols registrados no torneio")
    public void que_existem_gols_registrados() {
        configurarCenarioPadrao();
        eventoEstatisticoServico.registrarGol(10L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarGol(11L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarGol(12L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
    }

    @Quando("o usuário acessar a artilharia")
    public void o_usuario_acessar_artilharia() {
        try {
            rankingArtilharia = artilhariaServico.gerarRanking(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve exibir os jogadores ordenados por número de gols")
    public void o_sistema_deve_exibir_jogadores_ordenados() {
        assertNull(excecaoCapturada);
        assertNotNull(rankingArtilharia);
        assertFalse(rankingArtilharia.isEmpty());
        assertEquals(JOGADOR_A_ID, rankingArtilharia.get(0).getJogadorId());
        assertEquals(2, rankingArtilharia.get(0).getGols());
        assertEquals(JOGADOR_B_ID, rankingArtilharia.get(1).getJogadorId());
        assertEquals(1, rankingArtilharia.get(1).getGols());
    }

    @Dado("que existem estatísticas registradas no torneio")
    public void que_existem_estatisticas_registradas() {
        configurarCenarioPadrao();
        eventoEstatisticoServico.registrarGol(20L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_A_ID);
        eventoEstatisticoServico.registrarAssistencia(21L, TORNEIO_ID, PARTIDA_ID, ORGANIZADOR_ID, JOGADOR_B_ID);
    }

    @Quando("o usuário acessar as estatísticas")
    public void o_usuario_acessar_estatisticas() {
        try {
            estatisticasJogadores = estatisticaServico.listarEstatisticasJogadores(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve exibir os dados dos jogadores")
    public void o_sistema_deve_exibir_dados_jogadores() {
        assertNull(excecaoCapturada);
        assertNotNull(estatisticasJogadores);
        assertFalse(estatisticasJogadores.isEmpty());
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
                .filter(e -> e.getJogadorId().equals(JOGADOR_A_ID)).findFirst().orElse(null);
        EstatisticaJogador estatB = estatisticasJogadores.stream()
                .filter(e -> e.getJogadorId().equals(JOGADOR_B_ID)).findFirst().orElse(null);
        assertNotNull(estatA);
        assertNotNull(estatB);
        assertEquals(1, estatA.getGols());
        assertEquals(1, estatB.getCartoesVermelhos());
    }
}
