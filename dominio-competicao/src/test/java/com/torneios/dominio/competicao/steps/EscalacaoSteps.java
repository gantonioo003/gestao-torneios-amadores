package com.torneios.dominio.competicao.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.EsquemaTatico;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.competicao.CompeticaoFuncionalidade;
import com.torneios.dominio.competicao.escalacao.Escalacao;
import com.torneios.dominio.competicao.escalacao.TipoVisualizacaoEscalacao;
import com.torneios.dominio.competicao.partida.Partida;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class EscalacaoSteps extends CompeticaoFuncionalidade {

    private List<Escalacao> escalacoesPublicas;

    @Dado("que existe uma partida cadastrada no torneio sem exigencia de escalacao")
    public void que_existe_partida_sem_exigencia_escalacao() {
        configurarTorneioPontosCorridos(true);
        Partida partida = new Partida(PARTIDA_ID, TORNEIO_ID, TIME_A_ID, TIME_B_ID, "Pontos corridos", 5);
        partidaRepositorio.salvar(partida);
        configurarSuporteEscalacao(false);
    }

    @Dado("que apenas um time informou a escalacao")
    public void que_apenas_um_time_informou_escalacao() {
        escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), reservasPadrao());
    }

    @Quando("o sistema congelar as escalacoes antes do inicio")
    public void o_sistema_congelar_escalacoes_antes_inicio() {
        try {
            escalacaoServico.congelarEscalacoesDaPartida(PARTIDA_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o sistema tentar congelar as escalacoes antes do inicio")
    public void o_sistema_tentar_congelar_escalacoes_antes_inicio() {
        o_sistema_congelar_escalacoes_antes_inicio();
    }

    @Entao("a partida deve seguir sem escalacao cadastrada")
    public void a_partida_deve_seguir_sem_escalacao_cadastrada() {
        assertNull(excecaoCapturada);
        assertTrue(escalacaoRepositorio.listarPorPartida(PARTIDA_ID).isEmpty());
    }

    @Entao("o sistema deve manter apenas a mesa tatica informada sem bloquear a partida")
    public void o_sistema_deve_manter_apenas_mesa_tatica_informada() {
        assertNull(excecaoCapturada);
        assertEquals(1, escalacaoRepositorio.listarPorPartida(PARTIDA_ID).size());
        assertTrue(escalacaoRepositorio.buscarPorPartidaETime(PARTIDA_ID, TIME_A_ID).isPresent());
        assertTrue(escalacaoRepositorio.buscarPorPartidaETime(PARTIDA_ID, TIME_B_ID).isEmpty());
    }

    @Dado("que existe uma partida cadastrada no torneio com formato de equipe definido")
    public void que_existe_partida_com_formato_equipe_definido() {
        configurarTorneioPontosCorridos(true);
        Partida partida = new Partida(PARTIDA_ID, TORNEIO_ID, TIME_A_ID, TIME_B_ID, "Pontos corridos", 5);
        partidaRepositorio.salvar(partida);
        configurarSuporteEscalacao(false);
    }

    @Dado("que o usuario autenticado e o responsavel pelo time")
    public void que_usuario_autenticado_e_responsavel_pelo_time() {
        assertTrue(consultaEscalacao.usuarioEhResponsavelDoTime(TIME_A_ID, ORGANIZADOR_ID));
    }

    @Dado("que o esquema tatico escolhido e compativel com o formato de equipe")
    public void que_esquema_tatico_compativel_com_formato_equipe() {
        assertTrue(EsquemaTatico.UM_DOIS_UM.ehCompativelCom(consultaEscalacao.obterFormatoEquipeDaPartida(PARTIDA_ID)));
    }

    @Quando("ele gerar a escalacao em mesa tatica indicando os titulares por posicao e os reservas")
    public void ele_definir_escalacao_titulares_reservas() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), reservasPadrao());
            mesaTatica = escalacaoServico.gerarMesaTatica(PARTIDA_ID, TIME_A_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve gerar a mesa tatica do time para aquela partida")
    public void sistema_deve_armazenar_escalacao_time_partida() {
        assertNull(excecaoCapturada);
        assertTrue(escalacaoRepositorio.buscarPorPartidaETime(PARTIDA_ID, TIME_A_ID).isPresent());
        assertEquals(5, escalacao.getTitulares().size());
        assertNotNull(mesaTatica);
        assertEquals(5, mesaTatica.getTitularesPosicionados().size());
    }

    @Entao("deve posicionar os titulares em campo conforme o esquema tatico")
    public void deve_posicionar_titulares_em_campo_conforme_esquema() {
        assertNotNull(mesaTatica);
        assertEquals(escalacao.getEsquemaTatico(), mesaTatica.getEsquemaTatico());
        assertEquals(escalacao.getTitulares().size(), mesaTatica.getTitularesPosicionados().size());
        assertTrue(mesaTatica.getTitularesPosicionados().stream()
                .allMatch(jogador -> jogador.coordenada().eixoX() >= 0.0
                        && jogador.coordenada().eixoX() <= 100.0
                        && jogador.coordenada().eixoY() >= 0.0
                        && jogador.coordenada().eixoY() <= 100.0));
    }

    @Dado("que o tecnico esta associado ao time")
    public void que_tecnico_esta_associado_ao_time() {
        assertTrue(consultaEscalacao.tecnicoEstaAssociadoAoTime(TIME_A_ID, TECNICO_ID));
    }

    @Quando("ele gerar a escalacao em mesa tatica do time para a partida")
    public void ele_definir_escalacao_time_partida() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorTecnico(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, TECNICO_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), reservasPadrao());
            mesaTatica = escalacaoServico.gerarMesaTatica(PARTIDA_ID, TIME_A_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que o usuario autenticado nao e responsavel nem tecnico do time")
    public void que_usuario_nao_e_responsavel_nem_tecnico() {
        assertFalse(consultaEscalacao.usuarioEhResponsavelDoTime(TIME_A_ID, OUTRO_USUARIO_ID));
    }

    @Quando("ele tentar gerar a escalacao em mesa tatica do time para a partida")
    public void ele_tentar_definir_escalacao_time_partida() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, OUTRO_USUARIO_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar gerar a escalacao em mesa tatica com quantidade de titulares diferente do formato de equipe")
    public void ele_tentar_escalacao_quantidade_titulares_invalida() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco().subList(0, 4), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar gerar a escalacao em mesa tatica com um esquema tatico incompativel com o formato de equipe")
    public void ele_tentar_escalacao_esquema_incompativel() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.QUATRO_QUATRO_DOIS, titularesCincoPorCinco(), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar incluir na mesa tatica um jogador que nao pertence ao elenco do time")
    public void ele_tentar_incluir_jogador_fora_elenco() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesComJogadorForaDoElenco(), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar incluir o mesmo jogador como titular e como reserva na mesa tatica")
    public void ele_tentar_mesmo_jogador_titular_e_reserva() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), List.of(JOGADOR_1_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que existe uma escalacao definida para uma partida que ainda nao foi iniciada")
    public void que_existe_escalacao_partida_nao_iniciada() {
        definirEscalacaoPadrao(false);
    }

    @Quando("ele alterar o esquema tatico ou os jogadores da mesa tatica")
    public void ele_alterar_esquema_ou_jogadores_escalacao() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.DOIS_DOIS, titularesDoisDois(), reservasPadrao());
            mesaTatica = escalacaoServico.gerarMesaTatica(PARTIDA_ID, TIME_A_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve atualizar a mesa tatica do time naquela partida")
    public void sistema_deve_atualizar_escalacao_time_partida() {
        assertNull(excecaoCapturada);
        assertEquals(EsquemaTatico.DOIS_DOIS, escalacao.getEsquemaTatico());
        assertNotNull(mesaTatica);
        assertEquals(EsquemaTatico.DOIS_DOIS, mesaTatica.getEsquemaTatico());
    }

    @Dado("que existe uma escalacao definida para uma partida que ja foi iniciada")
    public void que_existe_escalacao_partida_ja_iniciada() {
        definirEscalacaoPadrao(true);
        escalacaoServico.congelarEscalacoesDaPartida(PARTIDA_ID);
    }

    @Quando("ele tentar alterar a mesa tatica do time para a partida")
    public void ele_tentar_alterar_escalacao_partida() {
        ele_alterar_esquema_ou_jogadores_escalacao();
    }

    @Quando("ele gerar a escalacao em mesa tatica sem incluir reservas")
    public void ele_definir_escalacao_sem_reservas() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), List.of());
            mesaTatica = escalacaoServico.gerarMesaTatica(PARTIDA_ID, TIME_A_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve gerar a mesa tatica com lista de reservas vazia")
    public void sistema_deve_armazenar_escalacao_reservas_vazia() {
        assertNull(excecaoCapturada);
        assertTrue(escalacao.getReservas().isEmpty());
        assertNotNull(mesaTatica);
        assertTrue(mesaTatica.getReservas().isEmpty());
    }

    @Quando("ele informar a escalacao somente com os titulares em lista")
    public void ele_informar_escalacao_somente_titulares_lista() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    TipoVisualizacaoEscalacao.LISTA_TITULARES, null,
                    titularesCincoPorCinco(), List.of());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve salvar a escalacao em lista sem exigir esquema tatico")
    public void sistema_deve_salvar_escalacao_lista_sem_esquema() {
        assertNull(excecaoCapturada);
        assertEquals(TipoVisualizacaoEscalacao.LISTA_TITULARES, escalacao.getTipoVisualizacao());
        assertNull(escalacao.getEsquemaTatico());
        assertEquals(5, escalacao.getTitulares().size());
        assertTrue(escalacao.getReservas().isEmpty());
    }

    @Quando("um visitante tentar consultar as escalacoes publicas da partida")
    public void visitante_tentar_consultar_escalacoes_publicas() {
        try {
            escalacoesPublicas = escalacaoServico.listarPublicasPorPartida(PARTIDA_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve impedir a consulta publica da escalacao")
    public void sistema_deve_impedir_consulta_publica() {
        assertNotNull(excecaoCapturada);
        assertNull(escalacoesPublicas);
    }

    @Quando("um visitante consultar as escalacoes publicas da partida")
    public void visitante_consultar_escalacoes_publicas() {
        visitante_tentar_consultar_escalacoes_publicas();
    }

    @Entao("o sistema deve divulgar a escalacao congelada")
    public void sistema_deve_divulgar_escalacao_congelada() {
        assertNull(excecaoCapturada);
        assertNotNull(escalacoesPublicas);
        assertEquals(1, escalacoesPublicas.size());
        assertEquals(TIME_A_ID, escalacoesPublicas.get(0).getTimeId());
        assertTrue(escalacoesPublicas.get(0).estaCongelada());
    }

    @Entao("os jogadores de cada linha devem ficar distribuidos sem sobreposicao")
    public void jogadores_de_cada_linha_distribuidos_sem_sobreposicao() {
        assertNull(excecaoCapturada);
        assertNotNull(mesaTatica);
        mesaTatica.getTitularesPosicionados().stream()
                .collect(java.util.stream.Collectors.groupingBy(jogador -> jogador.posicao()))
                .values()
                .forEach(linha -> {
                    long coordenadasDistintas = linha.stream()
                            .map(jogador -> jogador.coordenada().eixoY())
                            .distinct()
                            .count();
                    assertEquals(linha.size(), coordenadasDistintas);
                });
    }
}
