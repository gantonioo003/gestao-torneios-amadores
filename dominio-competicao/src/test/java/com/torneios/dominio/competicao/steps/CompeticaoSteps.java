package com.torneios.dominio.competicao.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.EsquemaTatico;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.competicao.CompeticaoFuncionalidade;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class CompeticaoSteps extends CompeticaoFuncionalidade {

    // =====================================================================
    // F8: Definir escalacao do time para uma partida
    // =====================================================================

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

    @Quando("ele definir a escalacao indicando os titulares por posicao e os reservas")
    public void ele_definir_escalacao_titulares_reservas() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve armazenar a escalacao para o time naquela partida")
    public void sistema_deve_armazenar_escalacao_time_partida() {
        assertNull(excecaoCapturada);
        assertTrue(escalacaoRepositorio.buscarPorPartidaETime(PARTIDA_ID, TIME_A_ID).isPresent());
        assertEquals(5, escalacao.getTitulares().size());
    }

    @Dado("que o tecnico esta associado ao time")
    public void que_tecnico_esta_associado_ao_time() {
        assertTrue(consultaEscalacao.tecnicoEstaAssociadoAoTime(TIME_A_ID, TECNICO_ID));
    }

    @Quando("ele definir a escalacao do time para a partida")
    public void ele_definir_escalacao_time_partida() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorTecnico(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, TECNICO_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que o usuario autenticado nao e responsavel nem tecnico do time")
    public void que_usuario_nao_e_responsavel_nem_tecnico() {
        assertFalse(consultaEscalacao.usuarioEhResponsavelDoTime(TIME_A_ID, OUTRO_USUARIO_ID));
    }

    @Quando("ele tentar definir a escalacao do time para a partida")
    public void ele_tentar_definir_escalacao_time_partida() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, OUTRO_USUARIO_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar definir a escalacao com quantidade de titulares diferente do formato de equipe")
    public void ele_tentar_escalacao_quantidade_titulares_invalida() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco().subList(0, 4), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar definir a escalacao com um esquema tatico incompativel com o formato de equipe")
    public void ele_tentar_escalacao_esquema_incompativel() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.QUATRO_QUATRO_DOIS, titularesCincoPorCinco(), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar incluir na escalacao um jogador que nao pertence ao elenco do time")
    public void ele_tentar_incluir_jogador_fora_elenco() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesComJogadorForaDoElenco(), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar incluir o mesmo jogador como titular e como reserva")
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

    @Quando("ele alterar o esquema tatico ou os jogadores da escalacao")
    public void ele_alterar_esquema_ou_jogadores_escalacao() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.DOIS_DOIS, titularesDoisDois(), reservasPadrao());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve atualizar a escalacao do time naquela partida")
    public void sistema_deve_atualizar_escalacao_time_partida() {
        assertNull(excecaoCapturada);
        assertEquals(EsquemaTatico.DOIS_DOIS, escalacao.getEsquemaTatico());
    }

    @Dado("que existe uma escalacao definida para uma partida que ja foi iniciada")
    public void que_existe_escalacao_partida_ja_iniciada() {
        definirEscalacaoPadrao(true);
    }

    @Quando("ele tentar alterar a escalacao do time para a partida")
    public void ele_tentar_alterar_escalacao_partida() {
        ele_alterar_esquema_ou_jogadores_escalacao();
    }

    @Quando("ele definir a escalacao sem incluir reservas")
    public void ele_definir_escalacao_sem_reservas() {
        try {
            escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                    ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                    EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), List.of());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve armazenar a escalacao com lista de reservas vazia")
    public void sistema_deve_armazenar_escalacao_reservas_vazia() {
        assertNull(excecaoCapturada);
        assertTrue(escalacao.getReservas().isEmpty());
    }

    // =====================================================================
    // F11: Preparar competicao do torneio
    // =====================================================================

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

    // =====================================================================
    // F13: Registrar resultado da partida
    // =====================================================================

    @Dado("que existe uma partida cadastrada no torneio")
    public void que_existe_partida_cadastrada() {
        configurarTorneioPontosCorridos(true);
        Partida partida = new Partida(PARTIDA_ID, TORNEIO_ID, TIME_A_ID, TIME_B_ID, "Pontos corridos", 5);
        partidaRepositorio.salvar(partida);
        configurarSuporteEscalacao(false);
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
    // F14: Gerenciar andamento da competicao
    // =====================================================================

    @Quando("o sistema gerenciar o andamento apos o resultado")
    public void o_sistema_gerenciar_andamento_apos_resultado() {
        try {
            atualizacaoCompeticao = partidaServico.gerenciarAndamento(TORNEIO_ID);
            classificacao = atualizacaoCompeticao.classificacaoAtualizada();
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve atualizar a classificacao e marcar a partida como encerrada")
    public void o_sistema_deve_atualizar_classificacao_e_status_partida() {
        assertNull(excecaoCapturada);
        Partida partida = partidaServico.obterPartida(new PartidaId(1L));
        assertTrue(partida.estaEncerrada());
        assertNotNull(classificacao);
        assertFalse(classificacao.isEmpty());
        assertEquals(TIME_A_ID, classificacao.get(0).getTimeId());
        assertEquals(3, classificacao.get(0).getPontos());
    }

    @Quando("o sistema gerenciar o chaveamento do torneio")
    public void o_sistema_gerenciar_chaveamento_torneio() {
        o_usuario_acessar_chaveamento();
    }

    @Entao("o sistema deve manter o chaveamento atualizado")
    public void o_sistema_deve_manter_chaveamento_atualizado() {
        o_sistema_deve_exibir_estrutura_eliminatoria();
    }

    @Quando("o usuario consultar o andamento por classificacao")
    public void o_usuario_consultar_andamento_por_classificacao() {
        o_usuario_acessar_classificacao();
    }

    @Entao("o sistema deve exibir a tabela com a pontuacao dos times")
    public void o_sistema_deve_exibir_tabela_pontuacao_sem_acento() {
        o_sistema_deve_exibir_tabela_pontuacao();
    }

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

    @Dado("que o torneio ainda nao possui estrutura definida")
    public void que_torneio_nao_possui_estrutura_sem_acento() {
        que_torneio_nao_possui_estrutura();
    }

    @Quando("o usuário acessar classificação ou chaveamento")
    public void o_usuario_acessar_classificacao_ou_chaveamento() {
        try {
            classificacao = partidaServico.visualizarClassificacao(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario consultar classificacao ou chaveamento")
    public void o_usuario_consultar_classificacao_ou_chaveamento() {
        o_usuario_acessar_classificacao_ou_chaveamento();
    }

    @Entao("o sistema deve informar que a competição ainda não foi iniciada")
    public void o_sistema_deve_informar_competicao_nao_iniciada() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve informar que a competicao ainda nao foi iniciada")
    public void o_sistema_deve_informar_competicao_nao_iniciada_sem_acento() {
        o_sistema_deve_informar_competicao_nao_iniciada();
    }

    // =====================================================================
    // Steps compartilhados
    // =====================================================================

    @Entao("o sistema deve impedir a operacao")
    public void o_sistema_deve_impedir_operacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve impedir a operação")
    public void o_sistema_deve_impedir_operacao_com_acento() {
        o_sistema_deve_impedir_operacao();
    }
}
