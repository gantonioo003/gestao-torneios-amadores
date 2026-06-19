package com.torneios.dominio.torneio.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.enumeracao.StatusTorneio;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.torneio.TorneioFuncionalidade;
import com.torneios.dominio.torneio.estrutura.ModoGeracaoEstrutura;
import com.torneios.dominio.torneio.estrutura.TipoEstruturaCompeticao;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F9Steps extends TorneioFuncionalidade {

    @Dado("que o usuário está autenticado")
    public void que_o_usuario_esta_autenticado() {
        assertNotNull(ORGANIZADOR_ID);
    }

    @Quando("ele criar um torneio informando nome, formato válido e formato de equipe 5x5")
    public void ele_criar_torneio_5x5() {
        try {
            torneio = criarTorneioPadrao(FormatoTorneio.PONTOS_CORRIDOS, FormatoEquipe.CINCO_POR_CINCO, false);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("definir que o torneio aceita solicitações de participação")
    public void definir_torneio_aceita_solicitacoes() {
        torneioServico.abrirSolicitacoes(TORNEIO_ID, ORGANIZADOR_ID);
    }

    @Entao("o sistema deve registrar o torneio com sucesso")
    public void o_sistema_deve_registrar_torneio_com_sucesso() {
        assertNull(excecaoCapturada);
        assertNotNull(torneio);
        assertEquals(StatusTorneio.CONFIGURADO, torneio.getStatus());
    }

    @Entao("deve permitir entrada de times por solicitação")
    public void deve_permitir_entrada_de_times_por_solicitacao() {
        assertTrue(torneio.aceitaSolicitacoes());
    }

    @Quando("ele criar um torneio informando nome, formato válido e formato de equipe 11x11")
    public void ele_criar_torneio_11x11() {
        try {
            configurarTimesElegiveis(11);
            torneio = criarTorneioPadrao(FormatoTorneio.MATA_MATA, FormatoEquipe.ONZE_POR_ONZE, false);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("informar os participantes iniciais")
    public void informar_participantes_iniciais() {
        try {
            torneioServico.definirParticipantesIniciais(TORNEIO_ID, ORGANIZADOR_ID, List.of(TIME_A_ID, TIME_B_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve registrar o torneio com os times definidos")
    public void o_sistema_deve_registrar_torneio_com_times_definidos() {
        assertNull(excecaoCapturada);
        assertNotNull(torneio);
        assertTrue(torneio.possuiParticipante(TIME_A_ID));
        assertTrue(torneio.possuiParticipante(TIME_B_ID));
    }

    @Quando("ele tentar criar um torneio sem definir o formato da competição")
    public void ele_tentar_criar_torneio_sem_formato() {
        try {
            torneio = torneioServico.criarTorneio(TORNEIO_ID, "Torneio Invalido", null, FormatoEquipe.CINCO_POR_CINCO, ORGANIZADOR_ID, false);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve impedir a criação do torneio")
    public void o_sistema_deve_impedir_criacao_do_torneio() {
        assertNotNull(excecaoCapturada);
    }

    @Quando("ele tentar criar um torneio sem definir a quantidade de jogadores por equipe")
    public void ele_tentar_criar_torneio_sem_formato_equipe() {
        try {
            torneio = torneioServico.criarTorneio(TORNEIO_ID, "Torneio Invalido", FormatoTorneio.PONTOS_CORRIDOS, null, ORGANIZADOR_ID, false);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que o usuario esta autenticado")
    public void que_o_usuario_esta_autenticado_sem_acento() {
        que_o_usuario_esta_autenticado();
    }

    @Quando("ele criar um torneio informando nome, formato valido e formato de equipe 5x5")
    public void ele_criar_torneio_5x5_sem_acento() {
        ele_criar_torneio_5x5();
    }

    @Quando("definir que o torneio aceita solicitacoes de participacao")
    public void definir_torneio_aceita_solicitacoes_sem_acento() {
        definir_torneio_aceita_solicitacoes();
    }

    @Entao("deve permitir entrada de times por solicitacao")
    public void deve_permitir_entrada_de_times_por_solicitacao_sem_acento() {
        deve_permitir_entrada_de_times_por_solicitacao();
    }

    @Quando("ele criar um torneio informando nome, formato valido e formato de equipe 11x11")
    public void ele_criar_torneio_11x11_sem_acento() {
        ele_criar_torneio_11x11();
    }

    @Quando("o organizador gerar a estrutura da competicao por sorteio")
    public void o_organizador_gerar_estrutura_por_sorteio() {
        try {
            estruturaCompeticao = torneioServico.gerarEstruturaCompeticao(TORNEIO_ID, ORGANIZADOR_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve gerar a estrutura sorteando os times")
    public void sistema_deve_gerar_estrutura_sorteando_times() {
        assertNull(excecaoCapturada);
        assertNotNull(estruturaCompeticao);
        assertEquals(ModoGeracaoEstrutura.SORTEIO, estruturaCompeticao.getModoGeracao());
        assertTrue(estruturaCompeticao.foiGerada());
    }

    @Quando("o organizador gerar a estrutura da competicao escolhendo manualmente a ordem dos times")
    public void organizador_gerar_estrutura_manual() {
        try {
            estruturaCompeticao = torneioServico.gerarEstruturaManual(
                    TORNEIO_ID, ORGANIZADOR_ID, List.of(TIME_A_ID, TIME_B_ID, TIME_C_ID, TIME_D_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve gerar a estrutura respeitando a montagem manual")
    public void sistema_deve_gerar_estrutura_respeitando_manual() {
        assertNull(excecaoCapturada);
        assertNotNull(estruturaCompeticao);
        assertEquals(ModoGeracaoEstrutura.MANUAL, estruturaCompeticao.getModoGeracao());
        assertTrue(estruturaCompeticao.getGrupos().get(0).getParticipantes().contains(TIME_A_ID));
    }

    @Dado("que existe um torneio finalizado")
    public void que_existe_torneio_finalizado() {
        configurarTimesElegiveis(5);
        torneio = criarTorneioPadrao(FormatoTorneio.PONTOS_CORRIDOS, FormatoEquipe.CINCO_POR_CINCO, false);
        torneioServico.definirParticipantesIniciais(TORNEIO_ID, ORGANIZADOR_ID, List.of(TIME_A_ID, TIME_B_ID));
        torneioServico.gerarEstruturaCompeticao(TORNEIO_ID, ORGANIZADOR_ID);
        torneioServico.iniciarTorneio(TORNEIO_ID, ORGANIZADOR_ID);
        torneioServico.finalizarTorneio(TORNEIO_ID, ORGANIZADOR_ID);
    }

    @Quando("o organizador repetir o torneio para uma nova edicao")
    public void organizador_repetir_torneio_nova_edicao() {
        try {
            historicoEdicaoTorneio = torneioServico.repetirTorneio(TORNEIO_ID, ORGANIZADOR_ID, true);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve arquivar a edicao anterior e reiniciar o torneio sem participantes")
    public void sistema_deve_arquivar_edicao_e_reiniciar() {
        assertNull(excecaoCapturada);
        assertNotNull(historicoEdicaoTorneio);
        assertEquals(1, historicoEdicaoTorneio.getNumeroEdicao());
        assertEquals(2, historicoEdicaoTorneio.getParticipantes().size());
        assertEquals(2, torneio.getEdicaoAtual());
        assertTrue(torneio.getParticipantesAprovados().isEmpty());
        assertEquals(StatusTorneio.CONFIGURADO, torneio.getStatus());
    }

    @Quando("ele tentar criar um torneio sem definir o formato da competicao")
    public void ele_tentar_criar_torneio_sem_formato_sem_acento() {
        ele_tentar_criar_torneio_sem_formato();
    }

    @Entao("o sistema deve impedir a criacao do torneio")
    public void o_sistema_deve_impedir_criacao_do_torneio_sem_acento() {
        o_sistema_deve_impedir_criacao_do_torneio();
    }

    @Dado("que existe um torneio com formato mata-mata")
    public void que_existe_torneio_mata_mata() {
        configurarTimesElegiveis(5);
        torneio = criarTorneioPadrao(FormatoTorneio.MATA_MATA, FormatoEquipe.CINCO_POR_CINCO, false);
    }

    @Dado("que o torneio possui participantes suficientes")
    public void que_torneio_possui_participantes_suficientes() {
        torneioServico.definirParticipantesIniciais(TORNEIO_ID, ORGANIZADOR_ID, List.of(TIME_A_ID, TIME_B_ID, TIME_C_ID, TIME_D_ID));
    }

    @Quando("o organizador gerar a estrutura da competição")
    public void o_organizador_gerar_estrutura() {
        try {
            estruturaCompeticao = torneioServico.gerarEstruturaCompeticao(TORNEIO_ID, ORGANIZADOR_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve gerar o chaveamento eliminatório")
    public void o_sistema_deve_gerar_chaveamento_eliminatorio() {
        assertNull(excecaoCapturada);
        assertNotNull(estruturaCompeticao);
        assertEquals(TipoEstruturaCompeticao.CHAVEAMENTO, estruturaCompeticao.getTipo());
        assertTrue(estruturaCompeticao.foiGerada());
    }

    @Dado("que existe um torneio com formato pontos corridos")
    public void que_existe_torneio_pontos_corridos() {
        configurarTimesElegiveis(5);
        torneio = criarTorneioPadrao(FormatoTorneio.PONTOS_CORRIDOS, FormatoEquipe.CINCO_POR_CINCO, false);
    }

    @Entao("o sistema deve gerar a estrutura de tabela da competição")
    public void o_sistema_deve_gerar_estrutura_tabela() {
        assertNull(excecaoCapturada);
        assertNotNull(estruturaCompeticao);
        assertEquals(TipoEstruturaCompeticao.TABELA, estruturaCompeticao.getTipo());
        assertTrue(estruturaCompeticao.foiGerada());
    }

    @Dado("que existe um torneio com formato fase de grupos com mata-mata")
    public void que_existe_torneio_fase_grupos() {
        configurarTimesElegiveis(5);
        torneio = criarTorneioPadrao(FormatoTorneio.FASE_DE_GRUPOS_COM_MATA_MATA, FormatoEquipe.CINCO_POR_CINCO, false);
    }

    @Entao("o sistema deve gerar os grupos da competição")
    public void o_sistema_deve_gerar_grupos() {
        assertNull(excecaoCapturada);
        assertNotNull(estruturaCompeticao);
        assertEquals(TipoEstruturaCompeticao.GRUPOS, estruturaCompeticao.getTipo());
        assertFalse(estruturaCompeticao.getGrupos().isEmpty());
    }

    @Dado("que existe um torneio configurado")
    public void que_existe_torneio_configurado() {
        configurarTimesElegiveis(5);
        torneio = criarTorneioPadrao(FormatoTorneio.PONTOS_CORRIDOS, FormatoEquipe.CINCO_POR_CINCO, false);
    }

    @Dado("que o torneio não possui participantes suficientes")
    public void que_torneio_nao_possui_participantes_suficientes() {
    }

    @Entao("o sistema deve impedir a geração da estrutura")
    public void o_sistema_deve_impedir_geracao_estrutura() {
        assertNotNull(excecaoCapturada);
    }

    @Dado("que existe um torneio com vagas abertas")
    public void que_existe_torneio_com_vagas_abertas() {
        configurarTimesElegiveis(5);
        torneio = criarTorneioPadrao(FormatoTorneio.PONTOS_CORRIDOS, FormatoEquipe.CINCO_POR_CINCO, true);
    }

    @Dado("que existe uma solicitação pendente de participação")
    public void que_existe_solicitacao_pendente() {
    }

    @Dado("que o usuário autenticado é o organizador do torneio")
    public void que_usuario_e_organizador() {
        assertEquals(ORGANIZADOR_ID, torneio.getOrganizadorId());
    }

    @Quando("ele aprovar a solicitação do time")
    public void ele_aprovar_solicitacao_do_time() {
        try {
            torneioServico.aprovarParticipante(TORNEIO_ID, ORGANIZADOR_ID, TIME_A_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve incluir o time entre os participantes aprovados")
    public void o_sistema_deve_incluir_time_entre_participantes_aprovados() {
        assertNull(excecaoCapturada);
        assertTrue(torneio.possuiParticipante(TIME_A_ID));
    }

    @Dado("que existe um torneio ainda não iniciado")
    public void que_existe_torneio_nao_iniciado() {
        configurarTimesElegiveis(5);
        torneio = criarTorneioPadrao(FormatoTorneio.PONTOS_CORRIDOS, FormatoEquipe.CINCO_POR_CINCO, false);
        torneioServico.definirParticipantesIniciais(TORNEIO_ID, ORGANIZADOR_ID, List.of(TIME_A_ID, TIME_B_ID));
    }

    @Dado("que existe um time aprovado no torneio")
    public void que_existe_time_aprovado() {
        assertTrue(torneio.possuiParticipante(TIME_A_ID));
    }

    @Quando("ele remover o time da lista de participantes aprovados")
    public void ele_remover_time_dos_participantes() {
        try {
            torneioServico.removerParticipante(
                    TORNEIO_ID, ORGANIZADOR_ID, new TimeId(TIME_A_ID.valor()));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve retirar o time do torneio")
    public void o_sistema_deve_retirar_time() {
        assertNull(excecaoCapturada);
        assertFalse(torneio.possuiParticipante(TIME_A_ID));
    }

    @Dado("que existe um torneio")
    public void que_existe_torneio() {
        configurarTimesElegiveis(5);
        torneio = criarTorneioPadrao(FormatoTorneio.PONTOS_CORRIDOS, FormatoEquipe.CINCO_POR_CINCO, false);
    }

    @Dado("que o usuário autenticado não é o organizador do torneio")
    public void que_usuario_nao_e_organizador() {
        assertNotEquals(ORGANIZADOR_ID, OUTRO_USUARIO_ID);
    }

    @Quando("ele tentar aprovar ou remover participantes")
    public void ele_tentar_aprovar_ou_remover_participantes() {
        try {
            torneioServico.aprovarParticipante(TORNEIO_ID, OUTRO_USUARIO_ID, TIME_A_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve impedir a operação")
    public void o_sistema_deve_impedir_operacao() {
        assertNotNull(excecaoCapturada);
    }

    @Dado("que existe um torneio já iniciado")
    public void que_existe_torneio_ja_iniciado() {
        configurarTimesElegiveis(5);
        torneio = criarTorneioPadrao(FormatoTorneio.PONTOS_CORRIDOS, FormatoEquipe.CINCO_POR_CINCO, false);
        torneioServico.definirParticipantesIniciais(TORNEIO_ID, ORGANIZADOR_ID, List.of(TIME_A_ID, TIME_B_ID));
        torneioServico.gerarEstruturaCompeticao(TORNEIO_ID, ORGANIZADOR_ID);
        torneioServico.iniciarTorneio(TORNEIO_ID, ORGANIZADOR_ID);
    }

    @Quando("ele tentar alterar a lista de participantes aprovados")
    public void ele_tentar_alterar_participantes() {
        try {
            torneioServico.aprovarParticipante(TORNEIO_ID, ORGANIZADOR_ID, TIME_C_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }
}
