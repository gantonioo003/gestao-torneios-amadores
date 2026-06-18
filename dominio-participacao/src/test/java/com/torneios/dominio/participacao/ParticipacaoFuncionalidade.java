package com.torneios.dominio.participacao;

import java.util.ArrayList;
import java.util.List;

import com.torneios.dominio.compartilhado.evento.EventoBarramento;
import com.torneios.dominio.compartilhado.evento.EventoObservador;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.AcessoGerenciamentoTorneioServico;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.acesso.CodificadorSenha;
import com.torneios.dominio.participacao.acesso.ContaUsuario;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;
import com.torneios.dominio.participacao.acesso.Pbkdf2CodificadorSenha;
import com.torneios.dominio.participacao.acesso.TorneioDisponivel;
import com.torneios.dominio.participacao.acesso.VisualizacaoTorneioServico;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoServico;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreiraId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.responsavel.ConsultaUsuario;
import com.torneios.dominio.participacao.responsavel.ResponsavelTimeServico;
import com.torneios.dominio.participacao.solicitacao.PoliticaParticipacaoTorneio;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacao;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;
import com.torneios.dominio.participacao.time.Time;
import com.torneios.dominio.participacao.time.TimeServico;
import com.torneios.infraestrutura.persistencia.memoria.CatalogoTorneiosDisponiveisMemoria;
import com.torneios.infraestrutura.persistencia.memoria.Repositorio;

public class ParticipacaoFuncionalidade implements EventoBarramento {

    protected static final UsuarioId USUARIO_AUTENTICADO_ID = new UsuarioId(1L);
    protected static final UsuarioId ORGANIZADOR_ID = new UsuarioId(2L);
    protected static final UsuarioId USUARIO_NAO_AUTENTICADO_ID = null;
    protected static final UsuarioId OUTRO_USUARIO_ID = new UsuarioId(99L);
    protected static final TorneioId TORNEIO_ID = new TorneioId(1L);
    protected static final TimeId TIME_A_ID = new TimeId(1L);
    protected static final TimeId TIME_B_ID = new TimeId(2L);
    protected static final JogadorId JOGADOR_ID = new JogadorId(10L);
    protected static final TecnicoId TECNICO_ID = new TecnicoId(20L);
    protected static final SolicitacaoParticipacaoId SOLICITACAO_ID = new SolicitacaoParticipacaoId(100L);
    protected static final ProfissionalEsportivoId PROFISSIONAL_ID = new ProfissionalEsportivoId(50L);
    protected static final RegistroDeCarreiraId REGISTRO_ID = new RegistroDeCarreiraId(60L);
    protected static final String EMAIL_USUARIO = "usuario@email.com";
    protected static final String EMAIL_EDITADO = "usuario.editado@email.com";
    protected static final String SENHA_USUARIO = "senha123";

    protected static boolean torneioAceitaSolicitacoes = false;
    protected static boolean usuarioEhOrganizador = false;
    protected static boolean usuarioExiste = true;
    protected static boolean torneioIniciado = false;

    protected static final Repositorio repositorio = new Repositorio();
    protected static final CatalogoTorneiosDisponiveisMemoria catalogoTorneiosDisponiveis = new CatalogoTorneiosDisponiveisMemoria();

    protected static final PoliticaParticipacaoTorneio politicaParticipacao = new PoliticaParticipacaoTorneio() {
        @Override public boolean aceitaSolicitacoes(TorneioId torneioId) { return torneioAceitaSolicitacoes; }
        @Override public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
            return usuarioEhOrganizador && usuarioId != null && usuarioId.equals(ORGANIZADOR_ID);
        }
        @Override public boolean torneioIniciado(TorneioId torneioId) { return torneioIniciado; }
    };

    protected static final AutenticacaoServico autenticacaoServico = new AutenticacaoServico();
    protected static final ConsultaUsuario consultaUsuario = usuarioId -> usuarioId != null;
    protected static final CodificadorSenha codificadorSenha = new Pbkdf2CodificadorSenha();
    protected static final ContaUsuarioServico contaUsuarioServico = new ContaUsuarioServico(repositorio, codificadorSenha);
    protected static final ResponsavelTimeServico responsavelTimeServico = new ResponsavelTimeServico(repositorio, consultaUsuario);
    protected static final TimeServico timeServico = new TimeServico(repositorio, autenticacaoServico, responsavelTimeServico);
    protected static final ProfissionalEsportivoServico profissionalServico = new ProfissionalEsportivoServico(repositorio, autenticacaoServico);
    protected static final SolicitacaoParticipacaoServico solicitacaoServico = new SolicitacaoParticipacaoServico(repositorio, repositorio, autenticacaoServico, politicaParticipacao);
    protected static final AcessoGerenciamentoTorneioServico acessoGerenciamentoServico = new AcessoGerenciamentoTorneioServico(autenticacaoServico);
    protected static final VisualizacaoTorneioServico visualizacaoTorneioServico = new VisualizacaoTorneioServico(catalogoTorneiosDisponiveis);

    protected static List<Object> eventos = new ArrayList<>();
    protected static Exception excecaoCapturada;

    protected static UsuarioId usuarioAtual;
    protected static List<SolicitacaoParticipacao> solicitacoesCapturadas;
    protected static List<Time> timesCapturados;
    protected static List<TorneioDisponivel> torneiosDisponiveis;
    protected static SolicitacaoParticipacao solicitacaoCapturada;
    protected static ContaUsuario contaCapturada;

    public void resetar() {
        repositorio.limpar();
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
        eventos = new ArrayList<>();
    }

    @Override
    public <E> void adicionar(EventoObservador<E> observador) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> void postar(E evento) {
        eventos.add(evento);
    }
}
