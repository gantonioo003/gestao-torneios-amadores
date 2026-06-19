package com.torneios.dominio.engajamento.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.torneios.dominio.engajamento.chat.StatusConversa;
import com.torneios.dominio.engajamento.EngajamentoFuncionalidade;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F3Steps extends EngajamentoFuncionalidade {

    @Dado("que existe outro usuario cadastrado na plataforma")
    public void que_existe_outro_usuario_cadastrado_na_plataforma() {
        consultaChat.cadastrarUsuario(OUTRO_USUARIO_ID);
    }

    @Dado("que existe uma solicitacao de conversa pendente para o usuario")
    public void que_existe_solicitacao_de_conversa_pendente_para_usuario() {
        consultaChat.autenticar(OUTRO_USUARIO_ID);
        consultaChat.autenticar(USUARIO_ID);
        conversaPrivada = chatPrivadoServico.solicitarConversa(
                conversaId(1L), OUTRO_USUARIO_ID, USUARIO_ID);
    }

    @Dado("que existe uma conversa aprovada entre dois usuarios")
    public void que_existe_conversa_aprovada_entre_dois_usuarios() {
        que_existe_solicitacao_de_conversa_pendente_para_usuario();
        conversaPrivada = chatPrivadoServico.aprovarSolicitacao(conversaPrivada.getId(), USUARIO_ID);
    }

    @Quando("ele solicitar uma conversa privada com esse usuario")
    public void ele_solicitar_conversa_privada_com_esse_usuario() {
        try {
            conversaPrivada = chatPrivadoServico.solicitarConversa(
                    conversaId(2L), USUARIO_ID, OUTRO_USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar solicitar uma conversa privada com esse usuario")
    public void ele_tentar_solicitar_conversa_privada_com_esse_usuario() {
        ele_solicitar_conversa_privada_com_esse_usuario();
    }

    @Dado("que ele solicitou uma conversa privada com esse usuario")
    public void que_ele_solicitou_uma_conversa_privada_com_esse_usuario() {
        ele_solicitar_conversa_privada_com_esse_usuario();
    }

    @Dado("que existe um terceiro usuario autenticado")
    public void que_existe_um_terceiro_usuario_autenticado() {
        consultaChat.autenticar(ORGANIZADOR_ID);
    }

    @Quando("o destinatario aprovar a solicitacao de conversa")
    public void destinatario_aprovar_solicitacao_conversa() {
        try {
            conversaPrivada = chatPrivadoServico.aprovarSolicitacao(conversaPrivada.getId(), USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o destinatario recusar a solicitacao de conversa")
    public void destinatario_recusar_solicitacao_conversa() {
        try {
            conversaPrivada = chatPrivadoServico.recusarSolicitacao(conversaPrivada.getId(), USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele enviar uma mensagem na conversa aprovada")
    public void ele_enviar_mensagem_conversa_aprovada() {
        try {
            mensagemChat = chatPrivadoServico.enviarMensagem(
                    conversaPrivada.getId(), mensagemId(1L), USUARIO_ID, "Bora marcar um amistoso?");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar enviar uma mensagem antes da aprovacao")
    public void ele_tentar_enviar_mensagem_antes_aprovacao() {
        try {
            mensagemChat = chatPrivadoServico.enviarMensagem(
                    conversaPrivada.getId(), mensagemId(2L), OUTRO_USUARIO_ID, "Mensagem antes do aceite");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario consultar suas conversas aprovadas")
    public void usuario_consultar_conversas_aprovadas() {
        try {
            conversasPrivadas = chatPrivadoServico.listarConversasAprovadas(USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario consultar suas solicitacoes enviadas")
    public void usuario_consultar_solicitacoes_enviadas() {
        try {
            conversasPrivadas = chatPrivadoServico.listarSolicitacoesEnviadas(USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o terceiro usuario tentar consultar o historico da conversa")
    public void terceiro_usuario_tentar_consultar_historico() {
        try {
            chatPrivadoServico.consultarConversa(conversaPrivada.getId(), ORGANIZADOR_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve registrar a conversa como solicitada")
    public void sistema_deve_registrar_conversa_solicitada() {
        assertNull(excecaoCapturada);
        assertNotNull(conversaPrivada);
        assertEquals(StatusConversa.SOLICITADA, conversaPrivada.getStatus());
        assertTrue(conversaPrivadaRepositorio.buscarPorId(conversaPrivada.getId()).isPresent());
    }

    @Entao("deve exibir a conversa na aba de solicitados do destinatario")
    public void deve_exibir_conversa_solicitados_destinatario() {
        consultaChat.autenticar(OUTRO_USUARIO_ID);
        List<?> solicitadas = chatPrivadoServico.listarSolicitadas(OUTRO_USUARIO_ID);
        assertEquals(1, solicitadas.size());
    }

    @Entao("o sistema deve liberar a troca de mensagens")
    public void sistema_deve_liberar_troca_mensagens() {
        assertNull(excecaoCapturada);
        assertEquals(StatusConversa.APROVADA, conversaPrivada.getStatus());
    }

    @Entao("o sistema deve manter a conversa bloqueada para mensagens")
    public void sistema_deve_manter_conversa_bloqueada_mensagens() {
        assertNull(excecaoCapturada);
        assertEquals(StatusConversa.RECUSADA, conversaPrivada.getStatus());
    }

    @Entao("o sistema deve armazenar a mensagem na conversa")
    public void sistema_deve_armazenar_mensagem_na_conversa() {
        assertNull(excecaoCapturada);
        assertNotNull(mensagemChat);
        assertEquals(1, conversaPrivada.getMensagens().size());
    }

    @Entao("o sistema deve listar a conversa no historico do usuario")
    public void sistema_deve_listar_conversa_historico_usuario() {
        assertNull(excecaoCapturada);
        assertNotNull(conversasPrivadas);
        assertEquals(1, conversasPrivadas.size());
        assertEquals(StatusConversa.APROVADA, conversasPrivadas.get(0).getStatus());
    }

    @Entao("o sistema deve listar a solicitacao como pendente")
    public void sistema_deve_listar_solicitacao_pendente() {
        assertNull(excecaoCapturada);
        assertNotNull(conversasPrivadas);
        assertEquals(1, conversasPrivadas.size());
        assertEquals(StatusConversa.SOLICITADA, conversasPrivadas.get(0).getStatus());
    }

    @Entao("o sistema deve impedir a solicitacao de conversa")
    public void sistema_deve_impedir_solicitacao_conversa() {
        assertNotNull(excecaoCapturada);
    }

    @Dado("que o criador possui conversa aprovada com um usuario e nao possui com outro")
    public void criadorPossuiContatoELonge() {
        consultaChat.autenticar(USUARIO_ID);
        consultaChat.cadastrarUsuario(OUTRO_USUARIO_ID);
        consultaChat.cadastrarUsuario(ORGANIZADOR_ID);
        consultaChat.registrarConversaAprovada(USUARIO_ID, OUTRO_USUARIO_ID);
    }

    @Quando("ele criar um grupo com os dois usuarios")
    public void criarGrupoComDoisUsuarios() {
        grupoChat = grupoChatServico.criar(
                grupoId(1L), "Grupo da comunidade", USUARIO_ID, List.of(OUTRO_USUARIO_ID, ORGANIZADOR_ID));
    }

    @Entao("o contato aprovado deve entrar e o outro usuario deve receber convite")
    public void contatoEntraOutroRecebeConvite() {
        assertTrue(grupoChat.possuiParticipante(OUTRO_USUARIO_ID));
        assertTrue(grupoChat.possuiConvitePendente(ORGANIZADOR_ID));
        assertFalse(grupoChat.possuiParticipante(ORGANIZADOR_ID));
    }

    @Dado("que existe um grupo com convite pendente para o usuario")
    public void grupoComConvitePendente() {
        consultaChat.autenticar(USUARIO_ID);
        consultaChat.autenticar(OUTRO_USUARIO_ID);
        grupoChat = grupoChatServico.criar(grupoId(2L), "Grupo aberto", USUARIO_ID, List.of(OUTRO_USUARIO_ID));
    }

    @Quando("o usuario aceitar o convite do grupo")
    public void aceitarConviteGrupo() {
        grupoChat = grupoChatServico.aceitarConvite(grupoChat.getId(), OUTRO_USUARIO_ID);
    }

    @Entao("ele deve se tornar participante do grupo")
    public void tornaParticipanteGrupo() {
        assertTrue(grupoChat.possuiParticipante(OUTRO_USUARIO_ID));
        assertFalse(grupoChat.possuiConvitePendente(OUTRO_USUARIO_ID));
    }

    @Dado("que um treinador possui um profissional vinculado ao seu elenco")
    public void treinadorPossuiProfissional() {
        consultaChat.autenticar(USUARIO_ID);
        consultaChat.cadastrarUsuario(OUTRO_USUARIO_ID);
        consultaChat.registrarComandado(USUARIO_ID, OUTRO_USUARIO_ID);
    }

    @Quando("o treinador criar um grupo com esse profissional")
    public void treinadorCriaGrupoProfissional() {
        grupoChat = grupoChatServico.criar(
                grupoId(3L), "Elenco principal", USUARIO_ID, List.of(OUTRO_USUARIO_ID));
    }

    @Entao("o profissional deve entrar no grupo sem convite")
    public void profissionalEntraSemConvite() {
        assertTrue(grupoChat.possuiParticipante(OUTRO_USUARIO_ID));
        assertFalse(grupoChat.possuiConvitePendente(OUTRO_USUARIO_ID));
    }
}
