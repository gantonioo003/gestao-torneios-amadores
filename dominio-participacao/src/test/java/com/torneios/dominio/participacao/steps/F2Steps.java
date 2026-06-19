package com.torneios.dominio.participacao.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.torneios.dominio.participacao.ParticipacaoFuncionalidade;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.participacao.acesso.TipoContaUsuario;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F2Steps extends ParticipacaoFuncionalidade {
    private static final String NOVA_SENHA = "novaSenha456";

    @Dado("que o usuario esta autenticado")
    public void que_o_usuario_esta_autenticado() {
        usuarioAtual = USUARIO_AUTENTICADO_ID;
    }

    @Dado("que o usuario nao esta autenticado")
    public void que_o_usuario_nao_esta_autenticado() {
        usuarioAtual = null;
    }

    @Dado("que o usuário está autenticado")
    public void que_o_usuario_esta_autenticado_com_acento() {
        usuarioAtual = USUARIO_AUTENTICADO_ID;
    }

    @Dado("que o usuário não está autenticado")
    public void que_o_usuario_nao_esta_autenticado_com_acento() {
        usuarioAtual = null;
    }

    @Dado("que nao existe conta cadastrada para o email informado")
    public void que_nao_existe_conta_para_email_informado() {
        assertTrue(repositorio.buscarPorEmail(EMAIL_USUARIO).isEmpty());
    }

    @Dado("que existe uma conta cadastrada para o usuario")
    public void que_existe_uma_conta_cadastrada_para_usuario() {
        contaCapturada = contaUsuarioServico.cadastrarConta(USUARIO_AUTENTICADO_ID, "Usuario Teste", EMAIL_USUARIO, SENHA_USUARIO);
    }

    @Quando("o usuario cadastrar uma nova conta com nome email e senha validos")
    public void usuario_cadastrar_nova_conta() {
        try { contaCapturada = contaUsuarioServico.cadastrarConta(USUARIO_AUTENTICADO_ID, "Usuario Teste", EMAIL_USUARIO, SENHA_USUARIO); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario cadastrar uma nova conta do tipo jogador")
    public void usuario_cadastrar_nova_conta_tipo_jogador() {
        try { contaCapturada = contaUsuarioServico.cadastrarConta(USUARIO_AUTENTICADO_ID, "Jogador Livre", EMAIL_USUARIO, SENHA_USUARIO, TipoContaUsuario.JOGADOR); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario cadastrar uma nova conta do tipo organizador")
    public void usuario_cadastrar_nova_conta_tipo_organizador() {
        try { contaCapturada = contaUsuarioServico.cadastrarConta(USUARIO_AUTENTICADO_ID, "Organizador", EMAIL_USUARIO, SENHA_USUARIO, TipoContaUsuario.ORGANIZADOR); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario cadastrar uma nova conta do tipo treinador")
    public void usuario_cadastrar_nova_conta_tipo_treinador() {
        try { contaCapturada = contaUsuarioServico.cadastrarConta(USUARIO_AUTENTICADO_ID, "Treinador", EMAIL_USUARIO, SENHA_USUARIO, TipoContaUsuario.TREINADOR); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario cadastrar uma conta comum sem funcao esportiva")
    public void usuario_cadastrar_conta_comum() {
        try { contaCapturada = contaUsuarioServico.cadastrarConta(USUARIO_AUTENTICADO_ID, "Torcedor", EMAIL_USUARIO, SENHA_USUARIO, TipoContaUsuario.USUARIO_COMUM); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele informar email e senha validos")
    public void ele_informar_email_e_senha_validos() {
        try { contaCapturada = contaUsuarioServico.autenticar(EMAIL_USUARIO, SENHA_USUARIO); usuarioAtual = contaCapturada.getId(); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele informar senha incorreta")
    public void ele_informar_senha_incorreta() {
        try { contaCapturada = contaUsuarioServico.autenticar(EMAIL_USUARIO, "senha-errada"); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele alterar a senha informando a senha atual correta")
    public void ele_alterar_senha_com_senha_atual_correta() {
        try {
            contaUsuarioServico.alterarSenha(USUARIO_AUTENTICADO_ID, SENHA_USUARIO, NOVA_SENHA);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar alterar a senha informando a senha atual incorreta")
    public void ele_tentar_alterar_senha_com_senha_atual_incorreta() {
        try {
            contaUsuarioServico.alterarSenha(USUARIO_AUTENTICADO_ID, "senha-incorreta", NOVA_SENHA);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele editar nome e email da conta")
    public void ele_editar_nome_e_email_da_conta() {
        try { contaCapturada = contaUsuarioServico.editarDados(USUARIO_AUTENTICADO_ID, "Usuario Editado", EMAIL_EDITADO); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele solicitar a exclusao da conta")
    public void ele_solicitar_exclusao_da_conta() {
        try { contaUsuarioServico.excluirConta(USUARIO_AUTENTICADO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("outro usuario tentar cadastrar conta com o mesmo email")
    public void outro_usuario_tentar_cadastrar_mesmo_email() {
        try { contaCapturada = contaUsuarioServico.cadastrarConta(new com.torneios.dominio.compartilhado.usuario.UsuarioId(101L), "Outro Usuario", EMAIL_USUARIO, SENHA_USUARIO); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Dado("que existe uma conta organizadora cadastrada")
    public void existe_conta_organizadora_cadastrada() {
        contaCapturada = contaUsuarioServico.cadastrarConta(
                USUARIO_AUTENTICADO_ID, "Organizador", EMAIL_USUARIO, SENHA_USUARIO, TipoContaUsuario.ORGANIZADOR);
    }

    @Dado("que existe uma conta de jogador cadastrada")
    public void existe_conta_jogador_cadastrada() {
        contaCapturada = contaUsuarioServico.cadastrarConta(
                USUARIO_AUTENTICADO_ID, "Jogador", EMAIL_USUARIO, SENHA_USUARIO, TipoContaUsuario.JOGADOR);
    }

    @Dado("que existe uma conta de treinador cadastrada")
    public void existe_conta_treinador_cadastrada() {
        contaCapturada = contaUsuarioServico.cadastrarConta(
                USUARIO_AUTENTICADO_ID, "Treinador", EMAIL_USUARIO, SENHA_USUARIO, TipoContaUsuario.TREINADOR);
    }

    @Dado("que existe uma conta comum cadastrada")
    public void existe_conta_comum_cadastrada() {
        contaCapturada = contaUsuarioServico.cadastrarConta(
                USUARIO_AUTENTICADO_ID, "Torcedor", EMAIL_USUARIO, SENHA_USUARIO, TipoContaUsuario.USUARIO_COMUM);
    }

    @Quando("o sistema verificar a permissao para criar torneios")
    public void verificar_permissao_criar_torneios() {
        try { contaCapturada = contaUsuarioServico.exigirPodeCriarTorneio(USUARIO_AUTENTICADO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o sistema verificar a permissao para gerenciar times")
    public void verificar_permissao_gerenciar_times() {
        try { contaCapturada = contaUsuarioServico.exigirPodeGerenciarTimes(USUARIO_AUTENTICADO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario salvar um torneio para acompanhar depois")
    public void usuario_salvar_torneio() {
        try { contaCapturada = contaUsuarioServico.salvarTorneio(USUARIO_AUTENTICADO_ID, TORNEIO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Dado("que a conta possui um torneio salvo")
    public void conta_possui_torneio_salvo() {
        contaCapturada = contaUsuarioServico.salvarTorneio(USUARIO_AUTENTICADO_ID, TORNEIO_ID);
    }

    @Quando("o usuario remover o torneio dos salvos")
    public void usuario_remover_torneio_salvo() {
        try { contaCapturada = contaUsuarioServico.removerTorneioSalvo(USUARIO_AUTENTICADO_ID, TORNEIO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Entao("o sistema deve criar a conta do usuario")
    public void sistema_deve_criar_conta_usuario() {
        assertNull(excecaoCapturada);
        assertNotNull(contaCapturada);
        assertTrue(repositorio.buscarPorEmail(EMAIL_USUARIO).isPresent());
    }

    @Entao("o sistema deve criar a conta como jogador")
    public void sistema_deve_criar_conta_como_jogador() {
        sistema_deve_criar_conta_usuario();
        assertEquals(TipoContaUsuario.JOGADOR, contaCapturada.getTipo());
    }

    @Entao("o sistema deve criar a conta como organizador")
    public void sistema_deve_criar_conta_como_organizador() {
        sistema_deve_criar_conta_usuario();
        assertEquals(TipoContaUsuario.ORGANIZADOR, contaCapturada.getTipo());
    }

    @Entao("o sistema deve criar a conta como treinador")
    public void sistema_deve_criar_conta_como_treinador() {
        sistema_deve_criar_conta_usuario();
        assertEquals(TipoContaUsuario.TREINADOR, contaCapturada.getTipo());
    }

    @Entao("o sistema deve criar a conta sem funcao esportiva")
    public void sistema_deve_criar_conta_comum() {
        sistema_deve_criar_conta_usuario();
        assertEquals(TipoContaUsuario.USUARIO_COMUM, contaCapturada.getTipo());
        assertFalse(contaCapturada.podeCriarTorneio());
        assertFalse(contaCapturada.podeGerenciarTimes());
        assertFalse(contaCapturada.possuiPerfilProfissional());
    }

    @Entao("a conta deve possuir permissao para criar torneios")
    public void conta_deve_possuir_permissao_criar_torneios() {
        assertNull(excecaoCapturada);
        assertTrue(contaCapturada.podeCriarTorneio());
    }

    @Entao("a conta deve possuir permissao para gerenciar times")
    public void conta_deve_possuir_permissao_gerenciar_times() {
        assertNull(excecaoCapturada);
        assertTrue(contaCapturada.podeGerenciarTimes());
    }

    @Entao("o sistema deve autenticar o usuario")
    public void sistema_deve_autenticar_usuario() {
        assertNull(excecaoCapturada);
        assertEquals(USUARIO_AUTENTICADO_ID, usuarioAtual);
    }

    @Entao("o sistema deve impedir a autenticacao")
    public void sistema_deve_impedir_autenticacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve autenticar o usuario com a nova senha")
    public void sistema_deve_autenticar_com_nova_senha() {
        assertNull(excecaoCapturada);
        assertEquals(
                USUARIO_AUTENTICADO_ID,
                contaUsuarioServico.autenticar(EMAIL_USUARIO, NOVA_SENHA).getId());
    }

    @Entao("o sistema deve impedir a alteracao da senha")
    public void sistema_deve_impedir_alteracao_senha() {
        assertNotNull(excecaoCapturada);
        assertEquals(
                USUARIO_AUTENTICADO_ID,
                contaUsuarioServico.autenticar(EMAIL_USUARIO, SENHA_USUARIO).getId());
    }

    @Entao("o sistema deve atualizar os dados da conta")
    public void sistema_deve_atualizar_dados_conta() {
        assertNull(excecaoCapturada);
        assertEquals("Usuario Editado", contaCapturada.getNome());
        assertEquals(EMAIL_EDITADO, contaCapturada.getEmail());
    }

    @Entao("o sistema deve remover a conta e impedir novo login")
    public void sistema_deve_remover_conta() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(USUARIO_AUTENTICADO_ID).isEmpty());
    }

    @Entao("o sistema deve impedir o cadastro da conta")
    public void sistema_deve_impedir_cadastro_conta() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve impedir a operação")
    @Entao("o sistema deve impedir a operacao")
    public void impedir_operacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o torneio deve aparecer entre os torneios salvos da conta")
    public void torneio_deve_aparecer_nos_salvos() {
        assertNull(excecaoCapturada);
        assertTrue(contaCapturada.salvouTorneio(new TorneioId(TORNEIO_ID.valor())));
    }

    @Entao("o torneio nao deve permanecer entre os torneios salvos da conta")
    public void torneio_nao_deve_permanecer_nos_salvos() {
        assertNull(excecaoCapturada);
        assertFalse(contaCapturada.salvouTorneio(new TorneioId(TORNEIO_ID.valor())));
    }

    @Entao("o sistema deve exigir autenticação")
    @Entao("o sistema deve exigir autenticacao")
    public void exigir_autenticacao() {
        assertNotNull(excecaoCapturada);
    }
}
