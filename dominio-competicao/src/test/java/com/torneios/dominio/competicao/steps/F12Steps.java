package com.torneios.dominio.competicao.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.competicao.CompeticaoFuncionalidade;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F12Steps extends CompeticaoFuncionalidade {

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

    @Dado("que o usuario autenticado e o organizador do torneio")
    public void que_usuario_e_organizador_sem_acento() {
        que_usuario_e_organizador();
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

    @Entao("o sistema deve atualizar automaticamente a classificacao ou chaveamento")
    public void o_sistema_deve_atualizar_classificacao_ou_chaveamento_sem_acento() {
        o_sistema_deve_atualizar_classificacao_ou_chaveamento();
    }

    @Dado("que o usuário autenticado não é o organizador")
    public void que_usuario_nao_e_organizador() {
        assertFalse(consultaCompeticaoTorneio.usuarioEhOrganizador(TORNEIO_ID, OUTRO_USUARIO_ID));
    }

    @Dado("que o usuario autenticado nao e o organizador")
    public void que_usuario_nao_e_organizador_sem_acento() {
        que_usuario_nao_e_organizador();
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

    @Dado("que nao existe a partida informada")
    public void que_nao_existe_partida_sem_acento() {
        que_nao_existe_partida();
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

    @Quando("o usuario tentar registrar um resultado")
    public void o_usuario_tentar_registrar_resultado_sem_acento() {
        o_usuario_tentar_registrar_resultado();
    }

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

    @Entao("o sistema deve impedir a operacao")
    public void o_sistema_deve_impedir_operacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve impedir a operação")
    public void o_sistema_deve_impedir_operacao_com_acento() {
        o_sistema_deve_impedir_operacao();
    }
}
