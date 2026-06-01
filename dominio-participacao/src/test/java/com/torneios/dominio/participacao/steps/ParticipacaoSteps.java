package com.torneios.dominio.participacao.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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
import com.torneios.dominio.participacao.acesso.TipoContaUsuario;
import com.torneios.dominio.participacao.acesso.TorneioDisponivel;
import com.torneios.dominio.participacao.acesso.VisualizacaoTorneioServico;
import com.torneios.dominio.participacao.profissional.MotivoDeSaida;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivo;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoServico;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreiraId;
import com.torneios.dominio.participacao.profissional.TipoProfissional;
import com.torneios.dominio.participacao.responsavel.ConsultaUsuario;
import com.torneios.dominio.participacao.responsavel.ResponsavelTimeServico;
import com.torneios.dominio.participacao.solicitacao.PoliticaParticipacaoTorneio;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacao;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;
import com.torneios.dominio.participacao.time.Time;
import com.torneios.dominio.participacao.time.TimeServico;
import com.torneios.infraestrutura.persistencia.memoria.CatalogoTorneiosDisponiveisMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ContaUsuarioRepositorioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ProfissionalEsportivoRepositorioMemoria;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class ParticipacaoSteps extends ParticipacaoFuncionalidade {

    // IDs auxiliares
    private static final JogadorId JOGADOR_ID = new JogadorId(10L);
    private static final TecnicoId TECNICO_ID = new TecnicoId(20L);
    private static final SolicitacaoParticipacaoId SOLICITACAO_ID = new SolicitacaoParticipacaoId(100L);
    private static final UsuarioId OUTRO_USUARIO_ID = new UsuarioId(99L);
    private static final ProfissionalEsportivoId PROFISSIONAL_ID = new ProfissionalEsportivoId(50L);
    private static final RegistroDeCarreiraId REGISTRO_ID = new RegistroDeCarreiraId(60L);
    private static final String EMAIL_USUARIO = "usuario@email.com";
    private static final String EMAIL_EDITADO = "usuario.editado@email.com";
    private static final String SENHA_USUARIO = "senha123";

    private UsuarioId usuarioAtual;

    // Serviços
    private final AutenticacaoServico autenticacaoServico = new AutenticacaoServico();
    private final ContaUsuarioRepositorioMemoria contaUsuarioRepositorio = new ContaUsuarioRepositorioMemoria();
    private final ContaUsuarioServico contaUsuarioServico = new ContaUsuarioServico(contaUsuarioRepositorio);
    private final ProfissionalEsportivoRepositorioMemoria profissionalRepositorio = new ProfissionalEsportivoRepositorioMemoria();
    private final ConsultaUsuario consultaUsuario = usuarioId -> usuarioId != null;
    private final ResponsavelTimeServico responsavelTimeServico;
    private final TimeServico timeServico;
    private final ProfissionalEsportivoServico profissionalServico;
    private final SolicitacaoParticipacaoServico solicitacaoServico;
    private final AcessoGerenciamentoTorneioServico acessoGerenciamentoServico;
    private final CatalogoTorneiosDisponiveisMemoria catalogoTorneiosDisponiveis = new CatalogoTorneiosDisponiveisMemoria();
    private final VisualizacaoTorneioServico visualizacaoTorneioServico;

    // Estado capturado durante execução
    private List<SolicitacaoParticipacao> solicitacoesCapturadas;
    private List<Time> timesCapturados;
    private List<TorneioDisponivel> torneiosDisponiveis;
    private SolicitacaoParticipacao solicitacaoCapturada;
    private ContaUsuario contaCapturada;

    // Flags de política inline
    private boolean torneioAceitaSolicitacoes = false;
    private boolean usuarioEhOrganizador = false;
    private boolean usuarioExiste = true;
    private boolean torneioIniciado = false;

    private final PoliticaParticipacaoTorneio politicaParticipacao = new PoliticaParticipacaoTorneio() {
        @Override public boolean aceitaSolicitacoes(TorneioId torneioId) { return torneioAceitaSolicitacoes; }
        @Override public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) { return usuarioEhOrganizador && usuarioId != null && usuarioId.equals(ORGANIZADOR_ID); }
        @Override public boolean torneioIniciado(TorneioId torneioId) { return torneioIniciado; }
    };

    public ParticipacaoSteps() {
        responsavelTimeServico = new ResponsavelTimeServico(timeRepositorio, consultaUsuario);
        timeServico = new TimeServico(timeRepositorio, autenticacaoServico, responsavelTimeServico);
        profissionalServico = new ProfissionalEsportivoServico(profissionalRepositorio, autenticacaoServico);
        solicitacaoServico = new SolicitacaoParticipacaoServico(solicitacaoRepositorio, timeRepositorio, autenticacaoServico, politicaParticipacao);
        acessoGerenciamentoServico = new AcessoGerenciamentoTorneioServico(autenticacaoServico);
        visualizacaoTorneioServico = new VisualizacaoTorneioServico(catalogoTorneiosDisponiveis);
    }

    @Before
    public void limparEstado() {
        timeRepositorio.limpar();
        solicitacaoRepositorio.limpar();
        contaUsuarioRepositorio.limpar();
        catalogoTorneiosDisponiveis.limpar();
        profissionalRepositorio.limpar();
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
    // Givens: Autenticação (compartilhado entre F2, F3, F4, F5, F6)
    // =====================================================================

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

    // =====================================================================
    // F2: Gerenciar conta de usuario e autenticacao
    // =====================================================================

    @Dado("que nao existe conta cadastrada para o email informado")
    public void que_nao_existe_conta_para_email_informado() {
        assertTrue(contaUsuarioRepositorio.buscarPorEmail(EMAIL_USUARIO).isEmpty());
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

    @Quando("ele informar email e senha validos")
    public void ele_informar_email_e_senha_validos() {
        try { contaCapturada = contaUsuarioServico.autenticar(EMAIL_USUARIO, SENHA_USUARIO); usuarioAtual = contaCapturada.getId(); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele informar senha incorreta")
    public void ele_informar_senha_incorreta() {
        try { contaCapturada = contaUsuarioServico.autenticar(EMAIL_USUARIO, "senha-errada"); } catch (Exception e) { excecaoCapturada = e; }
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
        try { contaCapturada = contaUsuarioServico.cadastrarConta(new UsuarioId(101L), "Outro Usuario", EMAIL_USUARIO, SENHA_USUARIO); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Entao("o sistema deve criar a conta do usuario")
    public void sistema_deve_criar_conta_usuario() {
        assertNull(excecaoCapturada);
        assertNotNull(contaCapturada);
        assertTrue(contaUsuarioRepositorio.buscarPorEmail(EMAIL_USUARIO).isPresent());
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
        assertEquals("Usuario Editado", contaCapturada.getNome());
        assertEquals(EMAIL_EDITADO, contaCapturada.getEmail());
    }

    @Entao("o sistema deve remover a conta e impedir novo login")
    public void sistema_deve_remover_conta() {
        assertNull(excecaoCapturada);
        assertTrue(contaUsuarioRepositorio.buscarPorId(USUARIO_AUTENTICADO_ID).isEmpty());
    }

    @Entao("o sistema deve impedir o cadastro da conta")
    public void sistema_deve_impedir_cadastro_conta() {
        assertNotNull(excecaoCapturada);
    }

    // =====================================================================
    // F3 / F4: Candidatura e inscricoes em torneio
    // =====================================================================

    @Dado("que ele é responsável por um time")
    @Dado("que ele e responsavel por um time")
    public void ele_e_responsavel_por_um_time() {
        timeRepositorio.salvar(new Time(TIME_A_ID, "Time Alpha", usuarioAtual));
    }

    @Dado("que ele não é responsável pelo time")
    @Dado("que ele nao e responsavel pelo time")
    public void ele_nao_e_responsavel_pelo_time() {
        timeRepositorio.salvar(new Time(TIME_A_ID, "Time Alpha", ORGANIZADOR_ID));
    }

    @Dado("que ele possui um time cadastrado")
    public void ele_possui_um_time_cadastrado() {
        ele_e_responsavel_por_um_time();
    }

    @Dado("que o torneio está com vagas abertas para solicitação de participação")
    @Dado("que o torneio esta com vagas abertas para solicitacao de participacao")
    public void torneio_com_vagas_abertas() {
        torneioAceitaSolicitacoes = true;
    }

    @Dado("que o torneio não está com vagas abertas para solicitação de participação")
    @Dado("que o torneio nao esta com vagas abertas para solicitacao de participacao")
    public void torneio_sem_vagas_abertas() {
        torneioAceitaSolicitacoes = false;
    }

    @Dado("que o usuário autenticado é o organizador do torneio")
    @Dado("que o usuario autenticado e o organizador do torneio")
    public void usuario_e_organizador() {
        usuarioAtual = ORGANIZADOR_ID;
        usuarioEhOrganizador = true;
    }

    @Dado("que existe uma solicitação pendente de um time para o torneio")
    @Dado("que existem solicitações pendentes de times para o torneio")
    @Dado("que existem solicitacoes pendentes de times para o torneio")
    public void solicitacao_pendente_existe() {
        if (timeRepositorio.buscarPorId(TIME_A_ID).isEmpty())
            timeRepositorio.salvar(new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID));
        SolicitacaoParticipacao sol = new SolicitacaoParticipacao(SOLICITACAO_ID, USUARIO_AUTENTICADO_ID, TIME_A_ID, TORNEIO_ID);
        solicitacaoRepositorio.salvar(sol);
    }

    @Dado("que já existe uma solicitação pendente do time para esse torneio")
    @Dado("que ja existe uma solicitacao pendente do time para esse torneio")
    public void solicitacao_pendente_duplicada() {
        solicitacao_pendente_existe();
    }

    @Dado("que existe uma solicitação de participação aprovada pelo organizador para o time")
    public void solicitacao_aprovada_existe() {
        if (timeRepositorio.buscarPorId(TIME_A_ID).isEmpty())
            timeRepositorio.salvar(new Time(TIME_A_ID, "Time Alpha", usuarioAtual));
        SolicitacaoParticipacao sol = new SolicitacaoParticipacao(SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        sol.aprovar();
        solicitacaoRepositorio.salvar(sol);
    }

    @Dado("que o time está vinculado a pelo menos um torneio")
    @Dado("que o time esta vinculado a pelo menos um torneio")
    @Dado("que o time está vinculado a um torneio")
    @Dado("que o time esta vinculado a um torneio")
    public void time_vinculado_a_torneio() {
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseGet(() -> {
            Time t = new Time(TIME_A_ID, "Time Alpha", usuarioAtual != null ? usuarioAtual : USUARIO_AUTENTICADO_ID);
            timeRepositorio.salvar(t);
            return t;
        });
        time.vincularAoTorneio(TORNEIO_ID);
        timeRepositorio.salvar(time);
    }

    @Dado("que o time não está vinculado a nenhum torneio")
    @Dado("que o time nao esta vinculado a nenhum torneio")
    public void time_sem_torneio() { /* criação padrão já não tem vínculo */ }

    @Quando("o usuário solicitar a participação do seu time no torneio")
    @Quando("o usuario solicitar a participacao do seu time no torneio")
    public void usuario_solicitar_participacao() {
        try { solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuário solicitar novamente a participação do seu time no torneio")
    @Quando("o usuario solicitar novamente a participacao do seu time no torneio")
    public void usuario_solicitar_participacao_novamente() {
        try { solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(new SolicitacaoParticipacaoId(101L), usuarioAtual, TIME_A_ID, TORNEIO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o organizador aprovar o time candidato")
    @Quando("o organizador aprovar a solicitacao")
    @Quando("o organizador aprovar a solicitação")
    public void organizador_aprovar() {
        try { solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o organizador rejeitar o time candidato")
    @Quando("o organizador rejeitar a solicitacao")
    @Quando("o organizador rejeitar a solicitação")
    public void organizador_rejeitar() {
        try { solicitacaoServico.rejeitarSolicitacao(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o organizador acessar a lista de candidatos")
    public void organizador_acessar_lista_candidatos() {
        try { solicitacoesCapturadas = solicitacaoServico.listarPendentesParaAvaliacao(TORNEIO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario acompanhar suas candidaturas")
    public void usuario_acompanhar_candidaturas() {
        try { solicitacoesCapturadas = solicitacaoServico.acompanharCandidaturas(usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario cancelar a candidatura pendente")
    public void usuario_cancelar_candidatura() {
        try { solicitacaoServico.cancelarCandidatura(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Entao("o sistema deve registrar a solicitação de participação como pendente")
    @Entao("o sistema deve registrar a candidatura como pendente")
    public void solicitacao_registrada_como_pendente() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacaoCapturada);
        assertEquals(StatusSolicitacao.PENDENTE, solicitacaoCapturada.getStatus());
    }

    @Entao("o sistema deve registrar a solicitação como aprovada")
    public void solicitacao_aprovada() {
        assertNull(excecaoCapturada);
        assertEquals(StatusSolicitacao.APROVADA, solicitacaoRepositorio.buscarPorId(SOLICITACAO_ID).orElseThrow().getStatus());
    }

    @Entao("o sistema deve registrar a solicitação como rejeitada")
    @Entao("o sistema deve registrar a solicitacao como rejeitada")
    public void solicitacao_rejeitada() {
        assertNull(excecaoCapturada);
        assertEquals(StatusSolicitacao.REJEITADA, solicitacaoRepositorio.buscarPorId(SOLICITACAO_ID).orElseThrow().getStatus());
    }

    @Entao("o sistema deve exibir os times com solicitações pendentes")
    @Entao("o sistema deve exibir os times com solicitacoes pendentes")
    public void exibir_times_pendentes() {
        assertNull(excecaoCapturada);
        assertFalse(solicitacoesCapturadas.isEmpty());
    }

    @Entao("o sistema deve impedir a solicitação")
    @Entao("o sistema deve impedir a solicitacao")
    @Entao("o sistema deve impedir a solicitação duplicada")
    @Entao("o sistema deve impedir a candidatura duplicada")
    public void impedir_solicitacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve marcar a candidatura como cancelada")
    public void candidatura_cancelada() {
        assertNull(excecaoCapturada);
        assertEquals(StatusSolicitacao.CANCELADA, solicitacaoRepositorio.buscarPorId(SOLICITACAO_ID).orElseThrow().getStatus());
    }

    // =====================================================================
    // F5: Gerenciar times do usuario
    // =====================================================================

    @Quando("ele cadastrar um novo time com nome valido")
    @Quando("ele cadastrar um novo time com informações válidas")
    public void cadastrar_time_nome_valido() {
        try { timeServico.criarTime(TIME_A_ID, "Time Alpha", usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar criar um time sem informar o nome")
    public void criar_time_sem_nome() {
        try { timeServico.criarTime(TIME_A_ID, "", usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar cadastrar um novo time")
    public void tentar_cadastrar_time() {
        try { timeServico.criarTime(TIME_A_ID, "Time Alpha", usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele alterar o nome do time")
    @Quando("ele alterar as informações do time")
    public void alterar_nome_time() {
        try { timeServico.editarTime(TIME_A_ID, usuarioAtual, "Time Alpha Editado"); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele solicitar a exclusão do time")
    @Quando("ele solicitar a exclusao do time")
    public void excluir_time() {
        try { timeServico.excluirTime(TIME_A_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele acessar a area de torneios do time")
    @Quando("ele acessar a área de torneios do time")
    public void acessar_area_torneios() {
        try {
            autenticacaoServico.exigirAutenticacao(usuarioAtual);
            Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
            if (!time.getResponsavel().equals(usuarioAtual))
                throw new com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException("Apenas o responsavel pode consultar os torneios do time.");
            timesCapturados = timeServico.listarTimesDoUsuario(usuarioAtual);
        } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar acessar os torneios do time")
    public void tentar_acessar_torneios() {
        acessar_area_torneios();
    }

    // F5: VinculoProfissional (RN8, RN9, RN10)

    @Dado("que existe um profissional esportivo cadastrado")
    public void profissional_cadastrado() {
        profissionalRepositorio.salvar(new ProfissionalEsportivo(PROFISSIONAL_ID, "Joao Silva", TipoProfissional.JOGADOR, USUARIO_AUTENTICADO_ID));
    }

    @Dado("que o profissional ja esta vinculado ao time")
    public void profissional_ja_vinculado() {
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        time.vincularProfissional(PROFISSIONAL_ID, "Atacante", LocalDate.of(2024, 1, 1), null);
        timeRepositorio.salvar(time);
    }

    @Quando("ele vincular o profissional ao time com funcao e data de inicio")
    public void vincular_profissional() {
        try { timeServico.vincularProfissional(TIME_A_ID, usuarioAtual, PROFISSIONAL_ID, "Atacante", LocalDate.of(2024, 1, 1), null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar vincular o mesmo profissional novamente")
    @Quando("ele tentar vincular profissional ao time")
    public void tentar_vincular_profissional() {
        vincular_profissional();
    }

    @Quando("ele editar a funcao do profissional no time")
    public void editar_vinculo_profissional() {
        try { timeServico.editarVinculoProfissional(TIME_A_ID, usuarioAtual, PROFISSIONAL_ID, "Meia", LocalDate.of(2024, 1, 1), null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele remover o profissional do elenco")
    public void remover_profissional_elenco() {
        try { timeServico.removerVinculoProfissional(TIME_A_ID, usuarioAtual, PROFISSIONAL_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Entao("o sistema deve registrar o time para esse usuario")
    public void time_registrado() {
        assertNull(excecaoCapturada);
        assertTrue(timeRepositorio.buscarPorId(TIME_A_ID).isPresent());
    }

    @Entao("o sistema deve rejeitar o cadastro por nome invalido")
    public void rejeitar_nome_invalido() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve atualizar os dados do time")
    public void time_atualizado() {
        assertNull(excecaoCapturada);
        assertEquals("Time Alpha Editado", timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow().getNome());
    }

    @Entao("o sistema deve remover o time")
    public void time_removido() {
        assertNull(excecaoCapturada);
        assertTrue(timeRepositorio.buscarPorId(TIME_A_ID).isEmpty());
    }

    @Entao("o sistema deve impedir a exclusão")
    @Entao("o sistema deve impedir a exclusao")
    public void impedir_exclusao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve exibir os torneios em que o time participa")
    public void exibir_torneios_do_time() {
        assertNull(excecaoCapturada);
        assertNotNull(timesCapturados);
    }

    @Entao("o sistema deve informar que o time não está vinculado a nenhum torneio")
    @Entao("o sistema deve informar que o time nao esta vinculado a nenhum torneio")
    public void informar_sem_torneios() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertFalse(time.estaVinculadoATorneio());
    }

    @Entao("o sistema deve registrar o profissional no elenco do time")
    public void profissional_no_elenco() {
        assertNull(excecaoCapturada);
        assertTrue(timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow().getElenco().stream().anyMatch(v -> v.getProfissionalId().equals(PROFISSIONAL_ID)));
    }

    @Entao("o sistema deve atualizar o vinculo do profissional")
    public void vinculo_atualizado() {
        assertNull(excecaoCapturada);
        assertTrue(timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow().getElenco().stream().anyMatch(v -> v.getProfissionalId().equals(PROFISSIONAL_ID) && "Meia".equals(v.getFuncao())));
    }

    @Entao("o sistema deve retirar o profissional do elenco do time")
    public void profissional_removido_do_elenco() {
        assertNull(excecaoCapturada);
        assertTrue(timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow().getElenco().stream().noneMatch(v -> v.getProfissionalId().equals(PROFISSIONAL_ID)));
    }

    // =====================================================================
    // F6: Cadastrar profissional esportivo com registro de carreira
    // =====================================================================

    @Dado("que existe um profissional esportivo cadastrado pelo usuario")
    public void profissional_cadastrado_pelo_usuario() {
        profissionalRepositorio.salvar(new ProfissionalEsportivo(PROFISSIONAL_ID, "Joao Silva", TipoProfissional.JOGADOR, usuarioAtual));
    }

    @Dado("que existe um profissional esportivo cadastrado por outro usuario")
    public void profissional_cadastrado_por_outro_usuario() {
        profissionalRepositorio.salvar(new ProfissionalEsportivo(PROFISSIONAL_ID, "Joao Silva", TipoProfissional.JOGADOR, OUTRO_USUARIO_ID));
    }

    @Dado("que o profissional ja possui um registro de carreira no periodo")
    public void profissional_com_registro() {
        profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, REGISTRO_ID, "Clube Anterior", LocalDate.of(2020, 1, 1), LocalDate.of(2021, 12, 31), MotivoDeSaida.FIM_DE_CONTRATO);
    }

    @Quando("ele cadastrar um profissional esportivo com nome e tipo validos")
    public void cadastrar_profissional_valido() {
        try { profissionalServico.cadastrar(PROFISSIONAL_ID, "Joao Silva", TipoProfissional.JOGADOR, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar cadastrar um profissional sem informar o nome")
    public void cadastrar_profissional_sem_nome() {
        try { profissionalServico.cadastrar(PROFISSIONAL_ID, "", TipoProfissional.JOGADOR, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar cadastrar um profissional sem informar o tipo")
    public void cadastrar_profissional_sem_tipo() {
        try { profissionalServico.cadastrar(PROFISSIONAL_ID, "Joao", null, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar cadastrar um profissional esportivo")
    public void tentar_cadastrar_profissional() {
        cadastrar_profissional_valido();
    }

    @Quando("ele editar o nome do profissional")
    public void editar_nome_profissional() {
        try { profissionalServico.editar(PROFISSIONAL_ID, usuarioAtual, "Joao Editado"); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar editar o profissional")
    public void tentar_editar_profissional() {
        editar_nome_profissional();
    }

    @Quando("ele remover o profissional esportivo")
    public void remover_profissional() {
        try { profissionalServico.remover(PROFISSIONAL_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar remover o profissional")
    public void tentar_remover_profissional() {
        remover_profissional();
    }

    @Quando("ele adicionar um registro de carreira com nome do clube data de inicio e motivo de saida validos")
    public void adicionar_registro_valido() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(70L), "Clube Novo", LocalDate.of(2022, 1, 1), LocalDate.of(2023, 12, 31), MotivoDeSaida.TRANSFERENCIA); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar adicionar um registro de carreira sem informar o nome do clube")
    public void adicionar_registro_sem_clube() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(70L), "", LocalDate.of(2022, 1, 1), null, null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar adicionar um registro de carreira sem data de inicio")
    public void adicionar_registro_sem_data() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(70L), "Clube X", null, null, null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar adicionar um registro com data de fim anterior a data de inicio")
    public void adicionar_registro_data_invalida() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(70L), "Clube X", LocalDate.of(2022, 6, 1), LocalDate.of(2022, 1, 1), null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar adicionar um registro de carreira com periodo sobreposto")
    public void adicionar_registro_sobreposto() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(71L), "Outro Clube", LocalDate.of(2020, 6, 1), LocalDate.of(2021, 6, 1), null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele remover o registro de carreira")
    public void remover_registro() {
        try { profissionalServico.removerRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, REGISTRO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar remover um registro de carreira que nao existe")
    public void remover_registro_inexistente() {
        try { profissionalServico.removerRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(999L)); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Entao("o sistema deve registrar o profissional esportivo")
    public void profissional_registrado() {
        assertNull(excecaoCapturada);
        assertTrue(profissionalRepositorio.buscarPorId(PROFISSIONAL_ID).isPresent());
    }

    @Entao("o sistema deve rejeitar o cadastro do profissional por nome invalido")
    @Entao("o sistema deve rejeitar o cadastro do profissional por tipo invalido")
    @Entao("o sistema deve rejeitar o registro por dados invalidos")
    @Entao("o sistema deve rejeitar o registro por periodo invalido")
    @Entao("o sistema deve rejeitar o registro por sobreposicao de periodos")
    public void rejeitar_operacao_invalida() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve atualizar os dados do profissional")
    public void profissional_atualizado() {
        assertNull(excecaoCapturada);
        assertEquals("Joao Editado", profissionalRepositorio.buscarPorId(PROFISSIONAL_ID).orElseThrow().getNome());
    }

    @Entao("o sistema deve excluir o profissional")
    public void profissional_excluido() {
        assertNull(excecaoCapturada);
        assertTrue(profissionalRepositorio.buscarPorId(PROFISSIONAL_ID).isEmpty());
    }

    @Entao("o sistema deve registrar o historico de carreira do profissional")
    public void historico_registrado() {
        assertNull(excecaoCapturada);
        assertFalse(profissionalRepositorio.buscarPorId(PROFISSIONAL_ID).orElseThrow().getHistorico().isEmpty());
    }

    @Entao("o sistema deve excluir o registro de carreira do historico")
    public void registro_excluido() {
        assertNull(excecaoCapturada);
        assertTrue(profissionalRepositorio.buscarPorId(PROFISSIONAL_ID).orElseThrow().getHistorico().stream().noneMatch(r -> r.getId().equals(REGISTRO_ID)));
    }

    @Entao("o sistema deve informar que o registro nao foi encontrado")
    public void registro_nao_encontrado() {
        assertNotNull(excecaoCapturada);
    }

    // =====================================================================
    // Thens: compartilhados
    // =====================================================================

    @Entao("o sistema deve impedir a operação")
    @Entao("o sistema deve impedir a operacao")
    public void impedir_operacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve exigir autenticação")
    @Entao("o sistema deve exigir autenticacao")
    public void exigir_autenticacao() {
        assertNotNull(excecaoCapturada);
    }

    // =====================================================================
    // F3 / F4: Steps complementares de candidatura e inscricao
    // =====================================================================

    @Dado("que ele nao possui time cadastrado")
    public void ele_nao_possui_time_cadastrado() { /* repositório vazio */ }

    @Dado("que existe uma candidatura pendente do time")
    public void candidatura_pendente_do_time() {
        solicitacao_pendente_existe();
    }

    @Dado("que existe uma candidatura ja avaliada do time")
    public void candidatura_ja_avaliada_do_time() {
        if (timeRepositorio.buscarPorId(TIME_A_ID).isEmpty())
            timeRepositorio.salvar(new Time(TIME_A_ID, "Time Alpha", usuarioAtual));
        SolicitacaoParticipacao sol = new SolicitacaoParticipacao(SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        sol.aprovar();
        solicitacaoRepositorio.salvar(sol);
    }

    @Dado("que o torneio ainda nao foi iniciado")
    public void torneio_ainda_nao_iniciado() { torneioIniciado = false; }

    @Dado("que o torneio ja foi iniciado")
    public void torneio_ja_iniciado() { torneioIniciado = true; }

    @Dado("que existe um time aprovado na lista final de participantes")
    public void time_aprovado_lista_final() {
        Time time = timeRepositorio.buscarPorId(TIME_A_ID)
            .orElseGet(() -> new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID));
        time.vincularAoTorneio(TORNEIO_ID);
        timeRepositorio.salvar(time);
    }

    @Dado("que existe uma solicitacao pendente de participacao para um torneio")
    public void solicitacao_pendente_para_torneio() {
        solicitacao_pendente_existe();
    }

    @Dado("que nao existe solicitacao pendente para o torneio")
    public void sem_solicitacao_pendente() { /* repositório vazio */ }

    @Quando("ele solicitar participacao em um torneio")
    public void ele_solicitar_participacao_em_torneio() {
        try { solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar aprovar a solicitacao")
    public void ele_tentar_aprovar_solicitacao() {
        try { solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar avaliar uma solicitacao")
    public void ele_tentar_avaliar_solicitacao() {
        try { solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o organizador remover o time da lista final")
    public void organizador_remover_time_lista_final() {
        try { solicitacaoServico.removerParticipanteAprovado(TORNEIO_ID, TIME_A_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar alterar a lista final de participantes")
    public void ele_tentar_alterar_lista_final() {
        try { solicitacaoServico.removerParticipanteAprovado(TORNEIO_ID, TIME_A_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario tentar cancelar uma candidatura ja avaliada")
    public void usuario_tentar_cancelar_candidatura_avaliada() {
        try { solicitacaoServico.cancelarCandidatura(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario solicitar a participacao em um torneio")
    public void usuario_solicitar_participacao_em_torneio() {
        try { solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Entao("o sistema deve exibir o status das candidaturas do time")
    public void exibir_status_candidaturas() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacoesCapturadas);
        assertFalse(solicitacoesCapturadas.isEmpty());
    }

    @Entao("o sistema deve impedir o cancelamento da candidatura")
    public void impedir_cancelamento_candidatura() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve registrar o time como participante aprovado do torneio")
    public void time_aprovado_no_torneio() {
        assertNull(excecaoCapturada);
        SolicitacaoParticipacao sol = solicitacaoRepositorio.buscarPorId(SOLICITACAO_ID).orElseThrow();
        assertEquals(StatusSolicitacao.APROVADA, sol.getStatus());
    }

    @Entao("o sistema deve retirar o time da lista final do torneio")
    public void time_retirado_lista_final() {
        assertNull(excecaoCapturada);
        Time time = timeRepositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertFalse(time.estaVinculadoAoTorneio(TORNEIO_ID));
    }

    @Entao("o sistema deve informar que nao ha solicitacao pendente para avaliacao")
    public void sem_solicitacao_para_avaliacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("deve informar que e necessario possuir um time cadastrado")
    public void informar_necessidade_time_cadastrado() {
        assertNotNull(excecaoCapturada);
    }
}