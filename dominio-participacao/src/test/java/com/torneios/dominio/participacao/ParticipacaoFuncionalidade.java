package com.torneios.dominio.participacao;

import java.util.ArrayList;
import java.util.List;

import com.torneios.dominio.compartilhado.evento.EventoBarramento;
import com.torneios.dominio.compartilhado.evento.EventoObservador;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.AcessoGerenciamentoTorneioServico;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;
import com.torneios.dominio.participacao.acesso.VisualizacaoTorneioServico;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoServico;
import com.torneios.dominio.participacao.responsavel.ConsultaUsuario;
import com.torneios.dominio.participacao.responsavel.ResponsavelTimeServico;
import com.torneios.dominio.participacao.solicitacao.PoliticaParticipacaoTorneio;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;
import com.torneios.dominio.participacao.time.TimeServico;
import com.torneios.infraestrutura.persistencia.memoria.CatalogoTorneiosDisponiveisMemoria;
import com.torneios.infraestrutura.persistencia.memoria.Repositorio;

public class ParticipacaoFuncionalidade implements EventoBarramento {

    protected static final UsuarioId USUARIO_AUTENTICADO_ID = new UsuarioId(1L);
    protected static final UsuarioId ORGANIZADOR_ID = new UsuarioId(2L);
    protected static final UsuarioId USUARIO_NAO_AUTENTICADO_ID = null;
    protected static final TorneioId TORNEIO_ID = new TorneioId(1L);
    protected static final TimeId TIME_A_ID = new TimeId(1L);
    protected static final TimeId TIME_B_ID = new TimeId(2L);

    protected boolean torneioAceitaSolicitacoes = false;
    protected boolean usuarioEhOrganizador = false;
    protected boolean usuarioExiste = true;
    protected boolean torneioIniciado = false;

    protected final Repositorio repositorio = new Repositorio();
    protected final CatalogoTorneiosDisponiveisMemoria catalogoTorneiosDisponiveis = new CatalogoTorneiosDisponiveisMemoria();

    protected final PoliticaParticipacaoTorneio politicaParticipacao = new PoliticaParticipacaoTorneio() {
        @Override public boolean aceitaSolicitacoes(TorneioId torneioId) { return torneioAceitaSolicitacoes; }
        @Override public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
            return usuarioEhOrganizador && usuarioId != null && usuarioId.equals(ORGANIZADOR_ID);
        }
        @Override public boolean torneioIniciado(TorneioId torneioId) { return torneioIniciado; }
    };

    protected final AutenticacaoServico autenticacaoServico = new AutenticacaoServico();
    protected final ConsultaUsuario consultaUsuario = usuarioId -> usuarioId != null;
    protected final ContaUsuarioServico contaUsuarioServico = new ContaUsuarioServico(repositorio);
    protected final ResponsavelTimeServico responsavelTimeServico = new ResponsavelTimeServico(repositorio, consultaUsuario);
    protected final TimeServico timeServico = new TimeServico(repositorio, autenticacaoServico, responsavelTimeServico);
    protected final ProfissionalEsportivoServico profissionalServico = new ProfissionalEsportivoServico(repositorio, autenticacaoServico);
    protected final SolicitacaoParticipacaoServico solicitacaoServico = new SolicitacaoParticipacaoServico(repositorio, repositorio, autenticacaoServico, politicaParticipacao);
    protected final AcessoGerenciamentoTorneioServico acessoGerenciamentoServico = new AcessoGerenciamentoTorneioServico(autenticacaoServico);
    protected final VisualizacaoTorneioServico visualizacaoTorneioServico = new VisualizacaoTorneioServico(catalogoTorneiosDisponiveis);

    protected List<Object> eventos = new ArrayList<>();
    protected Exception excecaoCapturada;

    @Override
    public <E> void adicionar(EventoObservador<E> observador) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> void postar(E evento) {
        eventos.add(evento);
    }
}
