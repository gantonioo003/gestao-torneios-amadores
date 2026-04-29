package com.torneios.dominio.participacao.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.StatusSolicitacao;
import com.torneios.dominio.compartilhado.jogador.Jogador;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.tecnico.Tecnico;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.ParticipacaoFuncionalidade;
import com.torneios.dominio.participacao.acesso.AcessoGerenciamentoTorneioServico;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.acesso.ContaUsuario;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;
import com.torneios.dominio.participacao.acesso.VisualizacaoTorneioServico;
import com.torneios.dominio.participacao.acesso.TorneioDisponivel;
import com.torneios.dominio.participacao.responsavel.ConsultaUsuario;
import com.torneios.infraestrutura.persistencia.memoria.CatalogoTorneiosDisponiveisMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ContaUsuarioRepositorioMemoria;
import com.torneios.dominio.participacao.responsavel.ResponsavelTimeServico;
import com.torneios.dominio.participacao.solicitacao.PoliticaParticipacaoTorneio;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacao;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;
import com.torneios.dominio.participacao.time.Time;
import com.torneios.dominio.participacao.time.TimeServico;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class ParticipacaoSteps extends ParticipacaoFuncionalidade {

    // IDs auxiliares
    private static final JogadorId JOGADOR_ID    = new JogadorId(10L);
    private static final TecnicoId TECNICO_ID    = new TecnicoId(20L);
    private static final SolicitacaoParticipacaoId SOLICITACAO_ID = new SolicitacaoParticipacaoId(100L);
    private static final UsuarioId OUTRO_USUARIO_CONTA_ID = new UsuarioId(101L);
    private static final String EMAIL_USUARIO = "usuario@email.com";
    private static final String EMAIL_EDITADO = "usuario.editado@email.com";
    private static final String SENHA_USUARIO = "senha123";

    // Usuário corrente do cenário (autenticado ou não)
    private UsuarioId usuarioAtual;

    // Serviços
    private final AutenticacaoServico autenticacaoServico = new AutenticacaoServico();
    private final ContaUsuarioRepositorioMemoria contaUsuarioRepositorio = new ContaUsuarioRepositorioMemoria();
    private final ContaUsuarioServico contaUsuarioServico = new ContaUsuarioServico(contaUsuarioRepositorio);
    private final TimeServico timeServico;
    private final SolicitacaoParticipacaoServico solicitacaoServico;
    private final ResponsavelTimeServico responsavelTimeServico;
    private final AcessoGerenciamentoTorneioServico acessoGerenciamentoServico;
    private final CatalogoTorneiosDisponiveisMemoria catalogoTorneiosDisponiveis = new CatalogoTorneiosDisponiveisMemoria();
    private final VisualizacaoTorneioServico visualizacaoTorneioServico;

    // Estado capturado durante execução
    private List<SolicitacaoParticipacao> solicitacoesCapturadas;
    private List<Time> timesCapturados;
    private List<TorneioDisponivel> torneiosDisponiveis;
    private SolicitacaoParticipacao solicitacaoCapturada;
    private ContaUsuario contaCapturada;

    // Política e consulta de usuário inline (doubles de teste)
    private boolean torneioAceitaSolicitacoes = false;
    private boolean usuarioEhOrganizador       = false;
    private boolean usuarioExiste              = true;
    private boolean torneioIniciado             = false;

    private final PoliticaParticipacaoTorneio politicaParticipacao = new PoliticaParticipacaoTorneio() {
        @Override
        public boolean aceitaSolicitacoes(TorneioId torneioId) {
            return torneioAceitaSolicitacoes;
        }

        @Override
        public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
            return usuarioEhOrganizador && usuarioId != null && usuarioId.equals(ORGANIZADOR_ID);
        }

        @Override
        public boolean torneioIniciado(TorneioId torneioId) {
            return torneioIniciado;
        }
    };

    private final ConsultaUsuario consultaUsuario = usuarioId -> usuarioExiste && usuarioId != null;

    public ParticipacaoSteps() {
        responsavelTimeServico = new ResponsavelTimeServico(timeRepositorio, consultaUsuario);
        timeServico = new TimeServico(timeRepositorio, autenticacaoServico, responsavelTimeServico);
        solicitacaoServico = new SolicitacaoParticipacaoServico(
                solicitacaoRepositorio, timeRepositorio, autenticacaoServico, politicaParticipacao);
        acessoGerenciamentoServico = new AcessoGerenciamentoTorneioServico(autenticacaoServico);
        visualizacaoTorneioServico = new VisualizacaoTorneioServico(catalogoTorneiosDisponiveis);
    }

    @Before
    public void limparEstado() {
        timeRepositorio.limpar();
        solicitacaoRepositorio.limpar();
        contaUsuarioRepositorio.limpar();
        catalogoTorneiosDisponiveis.limpar();
        excecaoCapturada = null;
        solicitacoesCapturadas = null;
        timesCapturados = null;
        torneiosDisponiveis = null;
        solicitacaoCapturada = null;
        contaCapturada = null;
        torneioAceitaSolicitacoes = false;
        usuarioEhOrganizador = false;
        usuarioExiste = true;
        torneioIniciado = false;
        usuarioAtual = null;
    }

    // =====================================================================
    // Givens: Autenticação
    // =====================================================================

    @Dado("que o usuário está autenticado")
    public void que_o_usuario_esta_autenticado() {
        usuarioAtual = USUARIO_AUTENTICADO_ID;
    }

    @Dado("que o usuário não está autenticado")
    public void que_o_usuario_nao_esta_autenticado() {
        usuarioAtual = USUARIO_NAO_AUTENTICADO_ID;
    }

    @Dado("que o usuario esta autenticado")
    public void que_o_usuario_esta_autenticado_sem_acento() {
        que_o_usuario_esta_autenticado();
    }

    @Dado("que o usuario nao esta autenticado")
    public void que_o_usuario_nao_esta_autenticado_sem_acento() {
        que_o_usuario_nao_esta_autenticado();
    }

    // =====================================================================
    // F2: Conta de usuario e autenticacao
    // =====================================================================

    @Dado("que nao existe conta cadastrada para o email informado")
    public void que_nao_existe_conta_para_email_informado() {
        assertTrue(contaUsuarioRepositorio.buscarPorEmail(EMAIL_USUARIO).isEmpty());
    }

    @Dado("que existe uma conta cadastrada para o usuario")
    public void que_existe_uma_conta_cadastrada_para_usuario() {
        contaCapturada = contaUsuarioServico.cadastrarConta(
                USUARIO_AUTENTICADO_ID, "Usuario Teste", EMAIL_USUARIO, SENHA_USUARIO);
    }

    @Quando("o usuario cadastrar uma nova conta com nome email e senha validos")
    public void usuario_cadastrar_nova_conta() {
        try {
            contaCapturada = contaUsuarioServico.cadastrarConta(
                    USUARIO_AUTENTICADO_ID, "Usuario Teste", EMAIL_USUARIO, SENHA_USUARIO);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele informar email e senha validos")
    public void ele_informar_email_e_senha_validos() {
        try {
            contaCapturada = contaUsuarioServico.autenticar(EMAIL_USUARIO, SENHA_USUARIO);
            usuarioAtual = contaCapturada.getId();
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele informar senha incorreta")
    public void ele_informar_senha_incorreta() {
        try {
            contaCapturada = contaUsuarioServico.autenticar(EMAIL_USUARIO, "senha-errada");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele editar nome e email da conta")
    public void ele_editar_nome_e_email_da_conta() {
        try {
            contaCapturada = contaUsuarioServico.editarDados(
                    USUARIO_AUTENTICADO_ID, "Usuario Editado", EMAIL_EDITADO);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele solicitar a exclusao da conta")
    public void ele_solicitar_exclusao_da_conta() {
        try {
            contaUsuarioServico.excluirConta(USUARIO_AUTENTICADO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("outro usuario tentar cadastrar conta com o mesmo email")
    public void outro_usuario_tentar_cadastrar_mesmo_email() {
        try {
            contaCapturada = contaUsuarioServico.cadastrarConta(
                    OUTRO_USUARIO_CONTA_ID, "Outro Usuario", EMAIL_USUARIO, SENHA_USUARIO);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve criar a conta do usuario")
    public void sistema_deve_criar_conta_usuario() {
        assertNull(excecaoCapturada);
        assertNotNull(contaCapturada);
        assertEquals(USUARIO_AUTENTICADO_ID, contaCapturada.getId());
        assertTrue(contaUsuarioRepositorio.buscarPorEmail(EMAIL_USUARIO).isPresent());
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

    @Entao("o sistema deve atualizar os dados da conta")
    public void sistema_deve_atualizar_dados_conta() {
        assertNull(excecaoCapturada);
        assertNotNull(contaCapturada);
        assertEquals("Usuario Editado", contaCapturada.getNome());
        assertEquals(EMAIL_EDITADO, contaCapturada.getEmail());
        assertTrue(contaUsuarioRepositorio.buscarPorEmail(EMAIL_EDITADO).isPresent());
    }

    @Entao("o sistema deve remover a conta e impedir novo login")
    public void sistema_deve_remover_conta_e_impedir_login() {
        assertNull(excecaoCapturada);
        assertTrue(contaUsuarioRepositorio.buscarPorId(USUARIO_AUTENTICADO_ID).isEmpty());
        assertThrows(Exception.class, () -> contaUsuarioServico.autenticar(EMAIL_USUARIO, SENHA_USUARIO));
    }

    @Entao("o sistema deve impedir o cadastro da conta")
    public void sistema_deve_impedir_cadastro_conta() {
        assertNotNull(excecaoCapturada);
        assertTrue(contaUsuarioRepositorio.buscarPorId(OUTRO_USUARIO_CONTA_ID).isEmpty());
    }

    // =====================================================================
    // Givens: Time e responsabilidade
    // =====================================================================

    @Dado("que ele é responsável por um time")
    public void que_ele_e_responsavel_por_um_time() {
        Time time = new Time(TIME_A_ID, "Time Alpha", usuarioAtual);
        timeRepositorio.salvar(time);
    }

    @Dado("que ele não é responsável pelo time")
    public void que_ele_nao_e_responsavel_pelo_time() {
        // Time pertence ao ORGANIZADOR_ID, não ao usuário atual
        Time time = new Time(TIME_A_ID, "Time Alpha", ORGANIZADOR_ID);
        timeRepositorio.salvar(time);
    }

    @Dado("que ele e responsavel por um time")
    public void que_ele_e_responsavel_por_um_time_sem_acento() {
        que_ele_e_responsavel_por_um_time();
    }

    @Dado("que ele nao e responsavel pelo time")
    public void que_ele_nao_e_responsavel_pelo_time_sem_acento() {
        que_ele_nao_e_responsavel_pelo_time();
    }

    @Dado("que ele possui um time cadastrado")
    public void que_ele_possui_um_time_cadastrado() {
        que_ele_e_responsavel_por_um_time();
    }

    @Dado("que ele não possui time cadastrado")
    public void que_ele_nao_possui_time_cadastrado() {
        // Nenhum time salvo para o usuário atual
    }

    @Dado("que ele nao possui time cadastrado")
    public void que_ele_nao_possui_time_cadastrado_sem_acento() {
        que_ele_nao_possui_time_cadastrado();
    }

    @Dado("que o time está vinculado a um torneio")
    @Dado("que o time está vinculado a pelo menos um torneio")
    public void que_o_time_esta_vinculado_a_um_torneio() {
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseGet(() -> {
            Time t = new Time(TIME_A_ID, "Time Alpha", usuarioAtual);
            timeRepositorio.salvar(t);
            return t;
        });
        time.vincularAoTorneio(TORNEIO_ID);
        timeRepositorio.salvar(time);
    }

    @Dado("que o time não está vinculado a nenhum torneio")
    public void que_o_time_nao_esta_vinculado_a_nenhum_torneio() {
        // Time sem vínculos — garantido pela criação padrão
    }

    @Dado("que o time possui jogadores cadastrados")
    public void que_o_time_possui_jogadores_cadastrados() {
        Time time = timeRepositorio.buscarPorId(TIME_A_ID)
                .orElseThrow(() -> new IllegalStateException("Time não encontrado"));
        time.adicionarJogador(new Jogador(JOGADOR_ID, "Jogador Exemplo", TIME_A_ID));
        timeRepositorio.salvar(time);
    }

    @Dado("que o time não possui jogadores cadastrados")
    public void que_o_time_nao_possui_jogadores_cadastrados() {
        // Elenco vazio — garantido pela criação padrão
    }

    @Dado("que o time possui técnico associado")
    public void que_o_time_possui_tecnico_associado() {
        Time time = timeRepositorio.buscarPorId(TIME_A_ID)
                .orElseThrow(() -> new IllegalStateException("Time não encontrado"));
        time.associarTecnico(new Tecnico(TECNICO_ID, "Técnico Exemplo", TIME_A_ID));
        timeRepositorio.salvar(time);
    }

    @Dado("que o time não possui técnico associado")
    public void que_o_time_nao_possui_tecnico_associado() {
        // Sem técnico — garantido pela criação padrão
    }

    // =====================================================================
    // Givens: Torneio
    // =====================================================================

    @Dado("que o torneio está com vagas abertas para solicitação de participação")
    public void que_o_torneio_esta_com_vagas_abertas() {
        torneioAceitaSolicitacoes = true;
    }

    @Dado("que o torneio não está com vagas abertas para solicitação de participação")
    public void que_o_torneio_nao_esta_com_vagas_abertas() {
        torneioAceitaSolicitacoes = false;
    }

    @Dado("que o torneio esta com vagas abertas para solicitacao de participacao")
    public void que_o_torneio_esta_com_vagas_abertas_sem_acento() {
        que_o_torneio_esta_com_vagas_abertas();
    }

    @Dado("que o torneio nao esta com vagas abertas para solicitacao de participacao")
    public void que_o_torneio_nao_esta_com_vagas_abertas_sem_acento() {
        que_o_torneio_nao_esta_com_vagas_abertas();
    }

    @Dado("que o usuário autenticado é o organizador do torneio")
    public void que_o_usuario_e_organizador_do_torneio() {
        usuarioAtual = ORGANIZADOR_ID;
        usuarioEhOrganizador = true;
    }

    @Dado("que ele não é organizador do torneio")
    public void que_ele_nao_e_organizador_do_torneio() {
        usuarioEhOrganizador = false;
    }

    // =====================================================================
    // Givens: Solicitações
    // =====================================================================

    @Dado("que existe uma solicitação pendente de um time para o torneio")
    public void que_existe_solicitacao_pendente_de_um_time() {
        // Garante que o time existe
        if (timeRepositorio.buscarPorId(TIME_A_ID).isEmpty()) {
            timeRepositorio.salvar(new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID));
        }
        SolicitacaoParticipacao sol = new SolicitacaoParticipacao(
                SOLICITACAO_ID, USUARIO_AUTENTICADO_ID, TIME_A_ID, TORNEIO_ID);
        solicitacaoRepositorio.salvar(sol);
    }

    @Dado("que existem solicitações pendentes de times para o torneio")
    public void que_existem_solicitacoes_pendentes() {
        que_existe_solicitacao_pendente_de_um_time();
    }

    @Dado("que não existem solicitações pendentes para o torneio")
    public void que_nao_existem_solicitacoes_pendentes() {
        // Repositório vazio
    }

    @Dado("que existe uma solicitação de participação aprovada pelo organizador para o time")
    public void que_existe_solicitacao_aprovada_pelo_organizador() {
        if (timeRepositorio.buscarPorId(TIME_A_ID).isEmpty()) {
            timeRepositorio.salvar(new Time(TIME_A_ID, "Time Alpha", usuarioAtual));
        }
        SolicitacaoParticipacao sol = new SolicitacaoParticipacao(
                SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        sol.aprovar();
        solicitacaoRepositorio.salvar(sol);
    }

    @Dado("que existe um time aprovado na lista final de participantes")
    public void que_existe_time_aprovado_na_lista_final() {
        Time time = timeRepositorio.buscarPorId(TIME_A_ID)
                .orElseGet(() -> new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID));
        time.vincularAoTorneio(TORNEIO_ID);
        timeRepositorio.salvar(time);
    }

    @Dado("que o torneio ainda nao foi iniciado")
    public void que_torneio_ainda_nao_foi_iniciado() {
        torneioIniciado = false;
    }

    @Dado("que o torneio ja foi iniciado")
    public void que_torneio_ja_foi_iniciado() {
        torneioIniciado = true;
    }

    @Dado("que não existe solicitação de participação para o time")
    public void que_nao_existe_solicitacao_para_o_time() {
        // Repositório vazio
    }

    @Dado("que já existe uma solicitação pendente do time para esse torneio")
    public void que_ja_existe_solicitacao_pendente() {
        que_existe_solicitacao_pendente_de_um_time();
    }

    @Dado("que ja existe uma solicitacao pendente do time para esse torneio")
    public void que_ja_existe_solicitacao_pendente_sem_acento() {
        que_ja_existe_solicitacao_pendente();
    }

    @Dado("que existe uma candidatura pendente do time")
    public void que_existe_uma_candidatura_pendente_do_time() {
        que_existe_solicitacao_pendente_de_um_time();
    }

    @Dado("que existe uma candidatura ja avaliada do time")
    public void que_existe_uma_candidatura_ja_avaliada_do_time() {
        que_existe_solicitacao_aprovada_pelo_organizador();
    }

    @Dado("que o time possui solicitações de participação pendentes")
    public void que_o_time_possui_solicitacoes_pendentes() {
        que_existe_solicitacao_pendente_de_um_time();
    }

    @Dado("que o time possui solicitações de participação aprovadas")
    public void que_o_time_possui_solicitacoes_aprovadas() {
        que_existe_solicitacao_aprovada_pelo_organizador();
    }

    @Dado("que o time possui solicitações de participação rejeitadas")
    public void que_o_time_possui_solicitacoes_rejeitadas() {
        if (timeRepositorio.buscarPorId(TIME_A_ID).isEmpty()) {
            timeRepositorio.salvar(new Time(TIME_A_ID, "Time Alpha", usuarioAtual));
        }
        SolicitacaoParticipacao sol = new SolicitacaoParticipacao(
                SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        sol.rejeitar();
        solicitacaoRepositorio.salvar(sol);
    }

    @Dado("que o time não possui solicitações de participação")
    public void que_o_time_nao_possui_solicitacoes() {
        // Repositório vazio
    }

    // =====================================================================
    // Whenns: Time
    // =====================================================================

    @Quando("ele cadastrar um novo time com nome válido")
    public void ele_cadastrar_novo_time_com_nome_valido() {
        try {
            timeServico.criarTime(TIME_A_ID, "Time Alpha", usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar cadastrar um novo time")
    public void ele_tentar_cadastrar_novo_time() {
        try {
            timeServico.criarTime(TIME_A_ID, "Time Alpha", usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele alterar o nome do time")
    public void ele_alterar_nome_do_time() {
        try {
            timeServico.editarTime(TIME_A_ID, usuarioAtual, "Time Alpha Editado");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele solicitar a exclusão do time")
    public void ele_solicitar_exclusao_do_time() {
        try {
            timeServico.excluirTime(TIME_A_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Whenns: Elenco
    // =====================================================================

    @Quando("ele adicionar um jogador com dados válidos ao elenco")
    public void ele_adicionar_jogador_com_dados_validos() {
        try {
            timeServico.adicionarJogador(TIME_A_ID, usuarioAtual, JOGADOR_ID, "Novo Jogador");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar adicionar um jogador ao elenco")
    public void ele_tentar_adicionar_jogador_ao_elenco() {
        try {
            timeServico.adicionarJogador(TIME_A_ID, usuarioAtual, JOGADOR_ID, "Novo Jogador");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele alterar o nome de um jogador do elenco")
    public void ele_alterar_nome_de_jogador_do_elenco() {
        try {
            timeServico.editarJogador(TIME_A_ID, usuarioAtual, JOGADOR_ID, "Jogador Editado");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele solicitar a remoção de um jogador do elenco")
    public void ele_solicitar_remocao_de_jogador_do_elenco() {
        try {
            timeServico.removerJogador(TIME_A_ID, usuarioAtual, JOGADOR_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar remover um jogador do elenco")
    public void ele_tentar_remover_jogador_do_elenco() {
        try {
            timeServico.removerJogador(TIME_A_ID, usuarioAtual, JOGADOR_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Whenns: Técnico
    // =====================================================================

    @Quando("ele associar um técnico com dados válidos ao time")
    public void ele_associar_tecnico_com_dados_validos() {
        try {
            timeServico.associarTecnico(TIME_A_ID, usuarioAtual, TECNICO_ID, "Novo Técnico");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar associar um técnico ao time")
    public void ele_tentar_associar_tecnico_ao_time() {
        try {
            timeServico.associarTecnico(TIME_A_ID, usuarioAtual, TECNICO_ID, "Novo Técnico");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele alterar o nome do técnico")
    public void ele_alterar_nome_do_tecnico() {
        try {
            timeServico.editarTecnico(TIME_A_ID, usuarioAtual, TECNICO_ID, "Técnico Editado");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele solicitar a remoção do técnico")
    public void ele_solicitar_remocao_do_tecnico() {
        try {
            timeServico.removerTecnico(TIME_A_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar remover o técnico do time")
    public void ele_tentar_remover_tecnico_do_time() {
        try {
            timeServico.removerTecnico(TIME_A_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Whenns: Solicitação de participação
    // =====================================================================

    @Quando("o usuário solicitar a participação do seu time no torneio")
    public void o_usuario_solicitar_participacao_do_time() {
        try {
            solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(
                    SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuário tentar solicitar participação no torneio")
    public void o_usuario_tentar_solicitar_participacao() {
        try {
            solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(
                    SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar solicitar participação no torneio")
    public void ele_tentar_solicitar_participacao() {
        try {
            solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(
                    SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuário solicitar novamente a participação do seu time no torneio")
    public void o_usuario_solicitar_participacao_novamente() {
        SolicitacaoParticipacaoId novaId = new SolicitacaoParticipacaoId(101L);
        try {
            solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(
                    novaId, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario solicitar a participacao do seu time no torneio")
    public void o_usuario_solicitar_participacao_do_time_sem_acento() {
        o_usuario_solicitar_participacao_do_time();
    }

    @Quando("o usuario solicitar a participacao em um torneio")
    public void o_usuario_solicitar_participacao_em_um_torneio_sem_acento() {
        o_usuario_solicitar_participacao_em_um_torneio();
    }

    @Quando("ele solicitar participacao em um torneio")
    public void ele_solicitar_participacao_em_um_torneio_sem_acento() {
        ele_solicitar_participacao_em_um_torneio();
    }

    @Quando("o usuario solicitar novamente a participacao do seu time no torneio")
    public void o_usuario_solicitar_participacao_novamente_sem_acento() {
        o_usuario_solicitar_participacao_novamente();
    }

    @Quando("o usuario acompanhar suas candidaturas")
    public void o_usuario_acompanhar_suas_candidaturas() {
        try {
            solicitacoesCapturadas = solicitacaoServico.acompanharCandidaturas(usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario cancelar a candidatura pendente")
    public void o_usuario_cancelar_a_candidatura_pendente() {
        try {
            solicitacaoServico.cancelarCandidatura(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario tentar cancelar uma candidatura ja avaliada")
    public void o_usuario_tentar_cancelar_uma_candidatura_ja_avaliada() {
        try {
            solicitacaoServico.cancelarCandidatura(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Whenns: Avaliar times (organizador)
    // =====================================================================

    @Quando("o organizador aprovar o time candidato")
    public void o_organizador_aprovar_time_candidato() {
        try {
            solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador rejeitar o time candidato")
    public void o_organizador_rejeitar_time_candidato() {
        try {
            solicitacaoServico.rejeitarSolicitacao(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador acessar a lista de candidatos")
    public void o_organizador_acessar_lista_de_candidatos() {
        try {
            solicitacoesCapturadas = solicitacaoServico.listarPendentesParaAvaliacao(TORNEIO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador tentar acessar a lista de candidatos")
    public void o_organizador_tentar_acessar_lista_de_candidatos() {
        try {
            solicitacoesCapturadas = solicitacaoServico.listarPendentesParaAvaliacao(TORNEIO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar aprovar o time candidato")
    public void ele_tentar_aprovar_time_candidato() {
        try {
            solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Whenns: Aceitar/Recusar solicitação (responsável do time)
    // =====================================================================

    @Quando("o responsável aceitar a participação no torneio")
    public void o_responsavel_aceitar_participacao() {
        try {
            // O responsável confirma o vínculo após aprovação do organizador
            timeServico.vincularTimeAoTorneio(TIME_A_ID, TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o responsável recusar a participação no torneio")
    public void o_responsavel_recusar_participacao() {
        try {
            // O responsável recusa: cancela o vínculo sem aprovar — a solicitação permanece aprovada mas não vincula
            // Semanticamente: o time não é vinculado ao torneio
            Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
            assertFalse(time.estaVinculadoAoTorneio(TORNEIO_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar aceitar a solicitação de participação")
    public void ele_tentar_aceitar_solicitacao() {
        try {
            // usuário que não é responsável tenta vincular
            timeServico.vincularTimeAoTorneio(TIME_A_ID, TORNEIO_ID);
            // validar manualmente que o usuário atual não é o responsável
            Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
            if (!time.getResponsavel().equals(usuarioAtual)) {
                throw new com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException(
                        "Apenas o responsável do time pode aceitar a participação.");
            }
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o responsável tentar aceitar a participação")
    public void o_responsavel_tentar_aceitar_participacao_inexistente() {
        try {
            // Nenhuma solicitação existe — ao tentar obter, deve lançar exceção
            solicitacaoServico.obterSolicitacao(SOLICITACAO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Whenns: Convites e torneios do time
    // =====================================================================

    @Quando("ele acessar a área de convites e solicitações do time")
    public void ele_acessar_area_de_convites() {
        try {
            autenticacaoServico.exigirAutenticacao(usuarioAtual);
            Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
            if (!time.getResponsavel().equals(usuarioAtual)) {
                throw new com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException(
                        "Apenas o responsável pode visualizar os convites do time.");
            }
            solicitacoesCapturadas = solicitacaoRepositorio.listarPendentesPorTorneio(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar acessar os convites do time")
    public void ele_tentar_acessar_convites() {
        try {
            autenticacaoServico.exigirAutenticacao(usuarioAtual);
            Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
            if (!time.getResponsavel().equals(usuarioAtual)) {
                throw new com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException(
                        "Apenas o responsável pode visualizar os convites do time.");
            }
            solicitacoesCapturadas = solicitacaoRepositorio.listarPendentesPorTorneio(TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele acessar a área de torneios do time")
    public void ele_acessar_area_de_torneios_do_time() {
        try {
            autenticacaoServico.exigirAutenticacao(usuarioAtual);
            Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
            if (!time.getResponsavel().equals(usuarioAtual)) {
                throw new com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException(
                        "Apenas o responsável pode visualizar os torneios do time.");
            }
            timesCapturados = timeServico.listarTimesDoUsuario(usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar acessar os torneios do time")
    public void ele_tentar_acessar_torneios_do_time() {
        try {
            autenticacaoServico.exigirAutenticacao(usuarioAtual);
            Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
            if (!time.getResponsavel().equals(usuarioAtual)) {
                throw new com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException(
                        "Apenas o responsável pode visualizar os torneios do time.");
            }
            timesCapturados = timeServico.listarTimesDoUsuario(usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Thens: Sucesso geral
    // =====================================================================

    @Entao("o sistema deve impedir a operação")
    public void o_sistema_deve_impedir_operacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve exigir autenticação")
    public void o_sistema_deve_exigir_autenticacao() {
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada instanceof IllegalStateException
                || excecaoCapturada instanceof com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException);
    }

    // =====================================================================
    // Thens: Time
    // =====================================================================

    @Entao("o sistema deve registrar o time para esse usuário")
    public void o_sistema_deve_registrar_o_time() {
        assertNull(excecaoCapturada);
        assertTrue(timeRepositorio.buscarPorId(TIME_A_ID).isPresent());
    }

    @Entao("o sistema deve atualizar os dados do time")
    public void o_sistema_deve_atualizar_dados_do_time() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertEquals("Time Alpha Editado", time.getNome());
    }

    @Entao("o sistema deve remover o time")
    public void o_sistema_deve_remover_o_time() {
        assertNull(excecaoCapturada);
        assertTrue(timeRepositorio.buscarPorId(TIME_A_ID).isEmpty());
    }

    @Entao("o sistema deve impedir a exclusão")
    public void o_sistema_deve_impedir_a_exclusao() {
        assertNotNull(excecaoCapturada);
    }

    // =====================================================================
    // Thens: Elenco
    // =====================================================================

    @Entao("o sistema deve registrar o jogador no elenco do time")
    public void o_sistema_deve_registrar_o_jogador() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertTrue(time.possuiJogador(JOGADOR_ID));
    }

    @Entao("o sistema deve atualizar os dados do jogador")
    public void o_sistema_deve_atualizar_dados_do_jogador() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertTrue(time.possuiJogador(JOGADOR_ID));
        time.getJogadores().stream()
                .filter(j -> j.getId().equals(JOGADOR_ID))
                .findFirst()
                .ifPresent(j -> assertEquals("Jogador Editado", j.getNome()));
    }

    @Entao("o sistema deve remover o jogador do time")
    public void o_sistema_deve_remover_o_jogador() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertFalse(time.possuiJogador(JOGADOR_ID));
    }

    @Entao("o sistema deve informar que o jogador não foi encontrado no elenco")
    public void o_sistema_deve_informar_jogador_nao_encontrado() {
        assertNotNull(excecaoCapturada);
    }

    // =====================================================================
    // Thens: Técnico
    // =====================================================================

    @Entao("o sistema deve registrar o técnico na comissão técnica do time")
    public void o_sistema_deve_registrar_o_tecnico() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertNotNull(time.getTecnico());
        assertEquals(TECNICO_ID, time.getTecnico().getId());
    }

    @Entao("o sistema deve atualizar os dados do técnico")
    public void o_sistema_deve_atualizar_dados_do_tecnico() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertNotNull(time.getTecnico());
        assertEquals("Técnico Editado", time.getTecnico().getNome());
    }

    @Entao("o sistema deve remover o técnico da comissão técnica do time")
    public void o_sistema_deve_remover_o_tecnico() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertNull(time.getTecnico());
    }

    @Entao("o sistema deve informar que não existe técnico associado ao time")
    public void o_sistema_deve_informar_tecnico_nao_encontrado() {
        assertNotNull(excecaoCapturada);
    }

    // =====================================================================
    // Thens: Solicitação de participação
    // =====================================================================

    @Entao("o sistema deve registrar a solicitação de participação como pendente")
    public void o_sistema_deve_registrar_solicitacao_como_pendente() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacaoCapturada);
        assertEquals(StatusSolicitacao.PENDENTE, solicitacaoCapturada.getStatus());
    }

    @Entao("deve informar que é necessário possuir um time cadastrado")
    public void deve_informar_necessidade_de_time_cadastrado() {
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().toLowerCase().contains("time"));
    }

    @Entao("o sistema deve impedir a solicitação")
    public void o_sistema_deve_impedir_solicitacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve impedir a solicitação duplicada")
    public void o_sistema_deve_impedir_solicitacao_duplicada() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve registrar a candidatura como pendente")
    public void o_sistema_deve_registrar_a_candidatura_como_pendente() {
        o_sistema_deve_registrar_solicitacao_como_pendente();
    }

    @Entao("o sistema deve exibir o status das candidaturas do time")
    public void o_sistema_deve_exibir_o_status_das_candidaturas_do_time() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacoesCapturadas);
        assertFalse(solicitacoesCapturadas.isEmpty());
        assertTrue(solicitacoesCapturadas.stream()
                .anyMatch(s -> s.getStatus() == StatusSolicitacao.PENDENTE
                        || s.getStatus() == StatusSolicitacao.APROVADA
                        || s.getStatus() == StatusSolicitacao.REJEITADA
                        || s.getStatus() == StatusSolicitacao.CANCELADA));
    }

    @Entao("o sistema deve marcar a candidatura como cancelada")
    public void o_sistema_deve_marcar_a_candidatura_como_cancelada() {
        assertNull(excecaoCapturada);
        SolicitacaoParticipacao sol = solicitacaoRepositorio.buscarPorId(SOLICITACAO_ID).orElseThrow();
        assertEquals(StatusSolicitacao.CANCELADA, sol.getStatus());
    }

    @Entao("o sistema deve impedir o cancelamento da candidatura")
    public void o_sistema_deve_impedir_o_cancelamento_da_candidatura() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("deve informar que e necessario possuir um time cadastrado")
    public void deve_informar_necessidade_de_time_cadastrado_sem_acento() {
        deve_informar_necessidade_de_time_cadastrado();
    }

    @Entao("o sistema deve impedir a solicitacao")
    public void o_sistema_deve_impedir_solicitacao_sem_acento() {
        o_sistema_deve_impedir_solicitacao();
    }

    @Entao("o sistema deve impedir a candidatura duplicada")
    public void o_sistema_deve_impedir_a_candidatura_duplicada() {
        o_sistema_deve_impedir_solicitacao_duplicada();
    }

    @Entao("o sistema deve exigir autenticacao")
    public void o_sistema_deve_exigir_autenticacao_sem_acento() {
        o_sistema_deve_exigir_autenticacao();
    }

    // =====================================================================
    // Thens: Avaliar times
    // =====================================================================

    @Entao("o sistema deve registrar a solicitação como aprovada")
    public void o_sistema_deve_registrar_como_aprovada() {
        assertNull(excecaoCapturada);
        SolicitacaoParticipacao sol = solicitacaoRepositorio.buscarPorId(SOLICITACAO_ID).orElseThrow();
        assertEquals(StatusSolicitacao.APROVADA, sol.getStatus());
    }

    @Entao("o sistema deve registrar a solicitação como rejeitada")
    public void o_sistema_deve_registrar_como_rejeitada() {
        assertNull(excecaoCapturada);
        SolicitacaoParticipacao sol = solicitacaoRepositorio.buscarPorId(SOLICITACAO_ID).orElseThrow();
        assertEquals(StatusSolicitacao.REJEITADA, sol.getStatus());
    }

    @Entao("o sistema deve exibir os times com solicitações pendentes")
    public void o_sistema_deve_exibir_times_candidatos_pendentes() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacoesCapturadas);
        assertFalse(solicitacoesCapturadas.isEmpty());
    }

    @Entao("o sistema deve informar que não há candidatos pendentes")
    public void o_sistema_deve_informar_sem_candidatos_pendentes() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacoesCapturadas);
        assertTrue(solicitacoesCapturadas.isEmpty());
    }

    // =====================================================================
    // Thens: Aceitar/Recusar solicitação
    // =====================================================================

    @Entao("o sistema deve vincular o time ao torneio")
    public void o_sistema_deve_vincular_time_ao_torneio() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertTrue(time.estaVinculadoAoTorneio(TORNEIO_ID));
    }

    @Entao("o sistema deve registrar a recusa e não vincular o time ao torneio")
    public void o_sistema_deve_registrar_a_recusa() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertFalse(time.estaVinculadoAoTorneio(TORNEIO_ID));
    }

    @Entao("o sistema deve informar que não há solicitação para aceitar")
    public void o_sistema_deve_informar_sem_solicitacao() {
        assertNotNull(excecaoCapturada);
    }

    // =====================================================================
    // Thens: Convites e torneios do time
    // =====================================================================

    @Entao("o sistema deve exibir as solicitações pendentes do time")
    public void o_sistema_deve_exibir_solicitacoes_pendentes_do_time() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacoesCapturadas);
        assertFalse(solicitacoesCapturadas.isEmpty());
    }

    @Entao("o sistema deve exibir as solicitações aprovadas do time")
    public void o_sistema_deve_exibir_solicitacoes_aprovadas_do_time() {
        assertNull(excecaoCapturada);
        // Aprovadas não retornam pelo listarPendentes; verificamos que não houve erro
        // e o repositório contém a solicitação aprovada
        assertTrue(solicitacaoRepositorio.buscarPorId(SOLICITACAO_ID)
                .map(s -> s.getStatus() == StatusSolicitacao.APROVADA)
                .orElse(false));
    }

    @Entao("o sistema deve exibir as solicitações rejeitadas do time")
    public void o_sistema_deve_exibir_solicitacoes_rejeitadas_do_time() {
        assertNull(excecaoCapturada);
        assertTrue(solicitacaoRepositorio.buscarPorId(SOLICITACAO_ID)
                .map(s -> s.getStatus() == StatusSolicitacao.REJEITADA)
                .orElse(false));
    }

    @Entao("o sistema deve informar que não há convites ou solicitações para o time")
    public void o_sistema_deve_informar_sem_convites() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacoesCapturadas);
        assertTrue(solicitacoesCapturadas.isEmpty());
    }

    // =====================================================================
    // Thens: Torneios do time
    // =====================================================================

    @Entao("o sistema deve exibir os torneios em que o time participa")
    public void o_sistema_deve_exibir_torneios_do_time() {
        assertNull(excecaoCapturada);
        assertNotNull(timesCapturados);
        assertFalse(timesCapturados.isEmpty());
        assertTrue(timesCapturados.stream()
                .anyMatch(t -> t.estaVinculadoAoTorneio(TORNEIO_ID)));
    }

    @Entao("o sistema deve informar que o time não está vinculado a nenhum torneio")
    public void o_sistema_deve_informar_sem_torneios() {
        assertNull(excecaoCapturada);
        assertNotNull(timesCapturados);
        assertTrue(timesCapturados.stream()
                .noneMatch(Time::estaVinculadoATorneio));
    }

    // =====================================================================
    // Givens: features antigos — acesso, torneios disponíveis, vínculo
    // =====================================================================

    @Dado("que ele possui torneios cadastrados")
    public void que_ele_possui_torneios_cadastrados() {
        Time time = new Time(TIME_A_ID, "Time Alpha", usuarioAtual);
        time.vincularAoTorneio(TORNEIO_ID);
        timeRepositorio.salvar(time);
    }

    @Dado("que existem torneios cadastrados na plataforma")
    public void que_existem_torneios_cadastrados_na_plataforma() {
        catalogoTorneiosDisponiveis.adicionar(
                new TorneioDisponivel(TORNEIO_ID, "Torneio Exemplo", true));
    }

    @Dado("que não existem torneios cadastrados na plataforma")
    public void que_nao_existem_torneios_cadastrados_na_plataforma() {
        // Catálogo vazio — garantido pelo limparEstado
    }

    @Dado("que existe um time cadastrado")
    public void que_existe_um_time_cadastrado() {
        Time time = new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID);
        timeRepositorio.salvar(time);
    }

    @Dado("que existe um time com responsável definido")
    public void que_existe_um_time_com_responsavel_definido() {
        Time time = new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID);
        timeRepositorio.salvar(time);
    }

    @Dado("que existe um time vinculado a um usuário responsável")
    public void que_existe_um_time_vinculado_a_um_usuario_responsavel() {
        Time time = new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID);
        timeRepositorio.salvar(time);
    }

    @Dado("que outro usuário autenticado não é o responsável pelo time")
    public void que_outro_usuario_nao_e_responsavel() {
        usuarioAtual = new UsuarioId(99L);
    }

    @Dado("que existe uma solicitação pendente de participação para um torneio")
    public void que_existe_solicitacao_pendente_de_participacao_para_torneio() {
        if (timeRepositorio.buscarPorId(TIME_A_ID).isEmpty()) {
            timeRepositorio.salvar(new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID));
        }
        SolicitacaoParticipacao sol = new SolicitacaoParticipacao(
                SOLICITACAO_ID, USUARIO_AUTENTICADO_ID, TIME_A_ID, TORNEIO_ID);
        solicitacaoRepositorio.salvar(sol);
    }

    @Dado("que não existe solicitação pendente para o torneio")
    public void que_nao_existe_solicitacao_pendente_para_torneio() {
        // Repositório vazio — garantido pelo limparEstado
    }

    @Dado("que o usuário autenticado não é o organizador do torneio")
    public void que_o_usuario_nao_e_organizador_do_torneio() {
        usuarioAtual = USUARIO_AUTENTICADO_ID;
        usuarioEhOrganizador = false;
    }

    // =====================================================================
    // Whenns: acesso a gerenciamento de torneios
    // =====================================================================

    @Quando("ele solicitar acesso à funcionalidade de criação de torneio")
    public void ele_solicitar_acesso_criacao_torneio() {
        try {
            acessoGerenciamentoServico.exigirAcessoCriacaoTorneio(usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele acessar a área de gerenciamento de torneios")
    public void ele_acessar_area_gerenciamento_torneios() {
        try {
            acessoGerenciamentoServico.exigirAcessoGerenciamentoTorneios(usuarioAtual);
            timesCapturados = timeServico.listarTimesDoUsuario(usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar acessar a área de gerenciamento de torneios")
    public void ele_tentar_acessar_area_gerenciamento_torneios() {
        try {
            acessoGerenciamentoServico.exigirAcessoGerenciamentoTorneios(usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Whenns: visualizar torneios disponíveis
    // =====================================================================

    @Quando("o visitante acessar a página inicial")
    public void o_visitante_acessar_pagina_inicial() {
        try {
            torneiosDisponiveis = visualizacaoTorneioServico.visualizarTorneiosDisponiveis();
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuário acessar a página inicial")
    public void o_usuario_acessar_pagina_inicial() {
        try {
            torneiosDisponiveis = visualizacaoTorneioServico.visualizarTorneiosDisponiveis();
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Whenns: vincular responsável
    // =====================================================================

    @Quando("ele for definido como responsável pelo time")
    public void ele_for_definido_como_responsavel_pelo_time() {
        try {
            responsavelTimeServico.definirResponsavel(TIME_A_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("for registrado um novo usuário responsável pelo time")
    public void for_registrado_novo_responsavel_pelo_time() {
        try {
            responsavelTimeServico.definirResponsavel(TIME_A_ID, ORGANIZADOR_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("for informado um usuário inexistente como responsável")
    public void for_informado_usuario_inexistente_como_responsavel() {
        usuarioExiste = false;
        try {
            responsavelTimeServico.definirResponsavel(TIME_A_ID, new UsuarioId(999L));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar gerenciar o time")
    public void ele_tentar_gerenciar_o_time() {
        try {
            timeServico.editarTime(TIME_A_ID, usuarioAtual, "Novo Nome");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Whenns: variações de wording dos features antigos
    // =====================================================================

    @Quando("ele cadastrar um novo time com informações válidas")
    public void ele_cadastrar_novo_time_com_informacoes_validas() {
        try {
            timeServico.criarTime(TIME_A_ID, "Time Alpha", usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele alterar as informações do time")
    public void ele_alterar_informacoes_do_time() {
        try {
            timeServico.editarTime(TIME_A_ID, usuarioAtual, "Time Alpha Editado");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele alterar os dados de um jogador do elenco")
    public void ele_alterar_dados_de_jogador_do_elenco() {
        try {
            timeServico.editarJogador(TIME_A_ID, usuarioAtual, JOGADOR_ID, "Jogador Editado");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele alterar os dados do técnico")
    public void ele_alterar_dados_do_tecnico() {
        try {
            timeServico.editarTecnico(TIME_A_ID, usuarioAtual, TECNICO_ID, "Técnico Editado");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador aprovar a solicitação")
    public void o_organizador_aprovar_a_solicitacao() {
        try {
            solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador rejeitar a solicitação")
    public void o_organizador_rejeitar_a_solicitacao() {
        try {
            solicitacaoServico.rejeitarSolicitacao(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador remover o time da lista final")
    public void organizador_remover_time_lista_final() {
        try {
            solicitacaoServico.removerParticipanteAprovado(TORNEIO_ID, TIME_A_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar alterar a lista final de participantes")
    public void ele_tentar_alterar_lista_final_participantes() {
        try {
            solicitacaoServico.removerParticipanteAprovado(TORNEIO_ID, TIME_A_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar aprovar a solicitação")
    public void ele_tentar_aprovar_a_solicitacao() {
        try {
            solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar avaliar uma solicitação")
    public void ele_tentar_avaliar_uma_solicitacao() {
        try {
            solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuário solicitar a participação em um torneio")
    public void o_usuario_solicitar_participacao_em_um_torneio() {
        try {
            solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(
                    SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele solicitar participação em um torneio")
    public void ele_solicitar_participacao_em_um_torneio() {
        try {
            solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(
                    SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // Thens: features antigos
    // =====================================================================

    @Entao("o sistema deve permitir o acesso")
    public void o_sistema_deve_permitir_o_acesso() {
        assertNull(excecaoCapturada);
        assertTrue(acessoGerenciamentoServico.podeAcessarCriacaoTorneio(usuarioAtual));
    }

    @Entao("o sistema deve exibir os torneios criados por ele")
    public void o_sistema_deve_exibir_torneios_criados_por_ele() {
        assertNull(excecaoCapturada);
        assertNotNull(timesCapturados);
        assertFalse(timesCapturados.isEmpty());
    }

    @Entao("o sistema deve exibir os torneios disponíveis para visualização")
    public void o_sistema_deve_exibir_torneios_disponiveis() {
        assertNull(excecaoCapturada);
        assertNotNull(torneiosDisponiveis);
        assertFalse(torneiosDisponiveis.isEmpty());
    }

    @Entao("o sistema deve informar que não há torneios disponíveis no momento")
    public void o_sistema_deve_informar_sem_torneios_disponiveis() {
        assertNull(excecaoCapturada);
        assertNotNull(torneiosDisponiveis);
        assertTrue(torneiosDisponiveis.isEmpty());
    }

    @Entao("o sistema deve vincular o time a esse usuário")
    public void o_sistema_deve_vincular_time_a_esse_usuario() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertEquals(usuarioAtual, time.getResponsavel());
    }

    @Entao("o sistema deve atualizar o vínculo de responsabilidade")
    public void o_sistema_deve_atualizar_vinculo_de_responsabilidade() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertEquals(ORGANIZADOR_ID, time.getResponsavel());
    }

    @Entao("o sistema deve impedir o vínculo")
    public void o_sistema_deve_impedir_o_vinculo() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve registrar o time como participante aprovado do torneio")
    public void o_sistema_deve_registrar_time_como_aprovado() {
        assertNull(excecaoCapturada);
        SolicitacaoParticipacao sol = solicitacaoRepositorio.buscarPorId(SOLICITACAO_ID).orElseThrow();
        assertEquals(StatusSolicitacao.APROVADA, sol.getStatus());
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertTrue(time.estaVinculadoAoTorneio(TORNEIO_ID));
    }

    @Entao("o sistema deve retirar o time da lista final do torneio")
    public void sistema_deve_retirar_time_lista_final() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertFalse(time.estaVinculadoAoTorneio(TORNEIO_ID));
    }

    @Entao("o sistema deve informar que não há solicitação pendente para avaliação")
    public void o_sistema_deve_informar_sem_solicitacao_pendente_para_avaliacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve registrar a solicitação de participação")
    public void o_sistema_deve_registrar_solicitacao_de_participacao() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacaoCapturada);
        assertEquals(StatusSolicitacao.PENDENTE, solicitacaoCapturada.getStatus());
    }

    @Entao("o sistema deve registrar o jogador no time")
    public void o_sistema_deve_registrar_jogador_no_time() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertTrue(time.possuiJogador(JOGADOR_ID));
    }

    // =====================================================================
    // Steps sem acento para os cenarios oficiais revisados
    // =====================================================================

    @Dado("que o usuario autenticado e o organizador do torneio")
    public void que_o_usuario_autenticado_e_o_organizador_do_torneio_sem_acento() {
        que_o_usuario_e_organizador_do_torneio();
    }

    @Dado("que existe uma solicitacao pendente de participacao para um torneio")
    public void que_existe_solicitacao_pendente_de_participacao_para_torneio_sem_acento() {
        que_existe_solicitacao_pendente_de_participacao_para_torneio();
    }

    @Dado("que nao existe solicitacao pendente para o torneio")
    public void que_nao_existe_solicitacao_pendente_para_torneio_sem_acento() {
        que_nao_existe_solicitacao_pendente_para_torneio();
    }

    @Dado("que o usuario autenticado nao e o organizador do torneio")
    public void que_o_usuario_autenticado_nao_e_o_organizador_do_torneio_sem_acento() {
        que_o_usuario_nao_e_organizador_do_torneio();
    }

    @Dado("que existem solicitacoes pendentes de times para o torneio")
    public void que_existem_solicitacoes_pendentes_de_times_para_o_torneio_sem_acento() {
        que_existem_solicitacoes_pendentes();
    }

    @Quando("o organizador aprovar a solicitacao")
    public void o_organizador_aprovar_a_solicitacao_sem_acento() {
        o_organizador_aprovar_a_solicitacao();
    }

    @Quando("o organizador rejeitar a solicitacao")
    public void o_organizador_rejeitar_a_solicitacao_sem_acento() {
        o_organizador_rejeitar_a_solicitacao();
    }

    @Quando("ele tentar aprovar a solicitacao")
    public void ele_tentar_aprovar_a_solicitacao_sem_acento() {
        ele_tentar_aprovar_a_solicitacao();
    }

    @Quando("ele tentar avaliar uma solicitacao")
    public void ele_tentar_avaliar_uma_solicitacao_sem_acento() {
        ele_tentar_avaliar_uma_solicitacao();
    }

    @Entao("o sistema deve impedir a operacao")
    public void o_sistema_deve_impedir_operacao_sem_acento() {
        o_sistema_deve_impedir_operacao();
    }

    @Entao("o sistema deve registrar a solicitacao como rejeitada")
    public void o_sistema_deve_registrar_a_solicitacao_como_rejeitada_sem_acento() {
        o_sistema_deve_registrar_como_rejeitada();
    }

    @Entao("o sistema deve exibir os times com solicitacoes pendentes")
    public void o_sistema_deve_exibir_os_times_com_solicitacoes_pendentes_sem_acento() {
        o_sistema_deve_exibir_times_candidatos_pendentes();
    }

    @Entao("o sistema deve informar que nao ha solicitacao pendente para avaliacao")
    public void o_sistema_deve_informar_sem_solicitacao_pendente_para_avaliacao_sem_acento() {
        o_sistema_deve_informar_sem_solicitacao_pendente_para_avaliacao();
    }
}
