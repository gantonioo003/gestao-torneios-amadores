package com.torneios.dominio.engajamento.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.desafio.ResultadoAmistoso;
import com.torneios.dominio.engajamento.desafio.StatusDesafioAmistoso;
import com.torneios.dominio.engajamento.EngajamentoFuncionalidade;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F10Steps extends EngajamentoFuncionalidade {

    @Dado("que existe um time desafiante com responsavel autenticado")
    public void que_existe_time_desafiante_com_responsavel_autenticado() {
        consultaDesafio.autenticar(USUARIO_ID);
        consultaDesafio.definirResponsavel(TIME_DESAFIANTE_ID, USUARIO_ID);
        consultaDesafio.definirResponsavel(TIME_DESAFIADO_ID, OUTRO_USUARIO_ID);
    }

    @Dado("que existe um time desafiante ligado a uma conta comum")
    public void existe_time_desafiante_conta_comum() {
        que_existe_time_desafiante_com_responsavel_autenticado();
        consultaDesafio.bloquearGerenciamentoDeTimes(USUARIO_ID);
    }

    @Dado("que existe um convite de amistoso pendente")
    public void que_existe_convite_amistoso_pendente() {
        que_existe_time_desafiante_com_responsavel_autenticado();
        desafioAmistoso = desafioAmistosoServico.proporConfronto(
                desafioId(1L),
                USUARIO_ID,
                TIME_DESAFIANTE_ID,
                TIME_DESAFIADO_ID,
                LocalDateTime.of(2026, 5, 10, 15, 0),
                "Campo do Bairro");
    }

    @Dado("que existe um amistoso aceito entre os times")
    public void que_existe_amistoso_aceito_entre_times() {
        que_existe_convite_amistoso_pendente();
        consultaDesafio.autenticar(OUTRO_USUARIO_ID);
        desafioAmistoso = desafioAmistosoServico.aceitarConvite(desafioId(1L), OUTRO_USUARIO_ID);
    }

    @Quando("ele propor um confronto amistoso para outro time")
    public void ele_propor_confronto_amistoso() {
        try {
            desafioAmistoso = desafioAmistosoServico.proporConfronto(
                    desafioId(2L),
                    USUARIO_ID,
                    TIME_DESAFIANTE_ID,
                    TIME_DESAFIADO_ID,
                    LocalDateTime.of(2026, 5, 12, 18, 30),
                    "Arena Comunitaria");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o responsavel do time desafiado aceitar o convite")
    public void responsavel_time_desafiado_aceitar_convite() {
        try {
            consultaDesafio.autenticar(OUTRO_USUARIO_ID);
            desafioAmistoso = desafioAmistosoServico.aceitarConvite(desafioId(1L), OUTRO_USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o responsavel do time desafiado recusar o convite")
    public void responsavel_time_desafiado_recusar_convite() {
        try {
            consultaDesafio.autenticar(OUTRO_USUARIO_ID);
            desafioAmistoso = desafioAmistosoServico.recusarConvite(desafioId(1L), OUTRO_USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o responsavel acompanhar os confrontos do seu time")
    public void responsavel_acompanhar_confrontos_do_time() {
        try {
            confrontosAmistosos = desafioAmistosoServico.acompanharConfrontosDoTime(
                    TIME_DESAFIANTE_ID, USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o responsavel do time desafiante cancelar o desafio")
    public void responsavel_time_desafiante_cancelar_desafio() {
        try {
            desafioAmistoso = desafioAmistosoServico.cancelarDesafio(desafioId(1L), USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o responsavel tentar enviar outro desafio entre os mesmos times")
    public void responsavel_tentar_desafio_duplicado() {
        try {
            desafioAmistoso = desafioAmistosoServico.proporConfronto(
                    desafioId(4L),
                    USUARIO_ID,
                    TIME_DESAFIANTE_ID,
                    TIME_DESAFIADO_ID,
                    LocalDateTime.of(2026, 6, 12, 18, 30),
                    "Arena Comunitaria");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("um responsavel reagendar o amistoso")
    public void responsavel_reagendar_amistoso() {
        try {
            desafioAmistoso = desafioAmistosoServico.reagendarAmistoso(
                    desafioId(1L),
                    USUARIO_ID,
                    LocalDateTime.of(2026, 5, 20, 20, 0),
                    "Campo Central");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("um responsavel registrar o resultado do amistoso")
    public void responsavel_registrar_resultado_amistoso() {
        try {
            desafioAmistoso = desafioAmistosoServico.registrarResultado(
                    desafioId(1L),
                    USUARIO_ID,
                    new ResultadoAmistoso(3, 2));
            historicoAmistosos = desafioAmistosoServico.listarHistoricoDoTime(TIME_DESAFIANTE_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar desafiar o proprio time")
    public void ele_tentar_desafiar_proprio_time() {
        try {
            desafioAmistoso = desafioAmistosoServico.proporConfronto(
                    desafioId(3L),
                    USUARIO_ID,
                    TIME_DESAFIANTE_ID,
                    TIME_DESAFIANTE_ID,
                    LocalDateTime.of(2026, 5, 15, 10, 0),
                    "Campo do Bairro");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("um usuario sem responsabilidade pelos times tentar aceitar o convite")
    public void usuario_sem_responsabilidade_tentar_aceitar_convite() {
        try {
            UsuarioId usuarioSemResponsabilidade = ORGANIZADOR_ID;
            consultaDesafio.autenticar(usuarioSemResponsabilidade);
            desafioAmistoso = desafioAmistosoServico.aceitarConvite(desafioId(1L), usuarioSemResponsabilidade);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve registrar o desafio como proposto")
    public void sistema_deve_registrar_desafio_proposto() {
        assertNull(excecaoCapturada);
        assertEquals(StatusDesafioAmistoso.PROPOSTO, desafioAmistoso.getStatus());
        assertTrue(desafioAmistosoRepositorio.buscarPorId(desafioAmistoso.getId()).isPresent());
    }

    @Entao("o sistema deve marcar o amistoso como aceito")
    public void sistema_deve_marcar_amistoso_aceito() {
        assertNull(excecaoCapturada);
        assertEquals(StatusDesafioAmistoso.ACEITO, desafioAmistoso.getStatus());
    }

    @Entao("o sistema deve marcar o convite como recusado")
    public void sistema_deve_marcar_convite_recusado() {
        assertNull(excecaoCapturada);
        assertEquals(StatusDesafioAmistoso.RECUSADO, desafioAmistoso.getStatus());
    }

    @Entao("o sistema deve listar o convite de amistoso")
    public void sistema_deve_listar_convite_amistoso() {
        assertNull(excecaoCapturada);
        assertEquals(1, confrontosAmistosos.size());
        assertEquals(StatusDesafioAmistoso.PROPOSTO, confrontosAmistosos.get(0).getStatus());
    }

    @Entao("o sistema deve marcar o desafio como cancelado")
    public void sistema_deve_marcar_desafio_cancelado() {
        assertNull(excecaoCapturada);
        assertEquals(StatusDesafioAmistoso.CANCELADO, desafioAmistoso.getStatus());
    }

    @Entao("o sistema deve atualizar data e local do amistoso")
    public void sistema_deve_atualizar_data_local_amistoso() {
        assertNull(excecaoCapturada);
        assertEquals(LocalDateTime.of(2026, 5, 20, 20, 0), desafioAmistoso.getDataHora());
        assertEquals("Campo Central", desafioAmistoso.getLocal());
    }

    @Entao("o sistema deve salvar o placar no historico dos times")
    public void sistema_deve_salvar_placar_historico_times() {
        assertNull(excecaoCapturada);
        assertEquals(StatusDesafioAmistoso.RESULTADO_REGISTRADO, desafioAmistoso.getStatus());
        assertTrue(desafioAmistoso.getResultado().isPresent());
        assertEquals(1, historicoAmistosos.size());
    }
}
