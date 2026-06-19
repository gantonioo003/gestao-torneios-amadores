package com.torneios.dominio.torneio;

import java.util.ArrayList;
import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.evento.EventoBarramento;
import com.torneios.dominio.compartilhado.evento.EventoObservador;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.estrutura.EstruturaCompeticao;
import com.torneios.dominio.torneio.estrutura.GeradorEstruturaCompeticaoServico;
import com.torneios.dominio.torneio.organizador.OrganizadorTorneioServico;
import com.torneios.dominio.torneio.torneio.HistoricoEdicaoTorneio;
import com.torneios.dominio.torneio.torneio.Torneio;
import com.torneios.dominio.torneio.torneio.TorneioServico;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaElegibilidadeParticipanteTorneioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.Repositorio;

public class TorneioFuncionalidade implements EventoBarramento {

    protected static final UsuarioId ORGANIZADOR_ID = new UsuarioId(1L);
    protected static final UsuarioId OUTRO_USUARIO_ID = new UsuarioId(99L);
    protected static final TorneioId TORNEIO_ID = new TorneioId(1L);
    protected static final TimeId TIME_A_ID = new TimeId(1L);
    protected static final TimeId TIME_B_ID = new TimeId(2L);
    protected static final TimeId TIME_C_ID = new TimeId(3L);
    protected static final TimeId TIME_D_ID = new TimeId(4L);

    protected final Repositorio repositorio = new Repositorio();
    protected final OrganizadorTorneioServico organizadorTorneioServico = new OrganizadorTorneioServico();
    protected final GeradorEstruturaCompeticaoServico geradorEstruturaCompeticaoServico = new GeradorEstruturaCompeticaoServico();
    protected final ConsultaElegibilidadeParticipanteTorneioMemoria consultaElegibilidade = new ConsultaElegibilidadeParticipanteTorneioMemoria();
    protected final List<TorneioId> preparacoesInvalidadas = new ArrayList<>();
    protected final TorneioServico torneioServico = new TorneioServico(
            repositorio, organizadorTorneioServico, geradorEstruturaCompeticaoServico,
            consultaElegibilidade, preparacoesInvalidadas::add, this);

    protected List<Object> eventos = new ArrayList<>();

    protected Torneio torneio;
    protected EstruturaCompeticao estruturaCompeticao;
    protected HistoricoEdicaoTorneio historicoEdicaoTorneio;
    protected Exception excecaoCapturada;

    @Override
    public <E> void adicionar(EventoObservador<E> observador) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> void postar(E evento) {
        eventos.add(evento);
    }

    protected void configurarTimesElegiveis(int quantidadeJogadores) {
        consultaElegibilidade.adicionarTime(TIME_A_ID, true, quantidadeJogadores);
        consultaElegibilidade.adicionarTime(TIME_B_ID, true, quantidadeJogadores);
        consultaElegibilidade.adicionarTime(TIME_C_ID, true, quantidadeJogadores);
        consultaElegibilidade.adicionarTime(TIME_D_ID, true, quantidadeJogadores);
    }

    protected Torneio criarTorneioPadrao(FormatoTorneio formato, FormatoEquipe formatoEquipe, boolean aceitaSolicitacoes) {
        return torneioServico.criarTorneio(TORNEIO_ID, "Torneio Teste", formato, formatoEquipe, ORGANIZADOR_ID, aceitaSolicitacoes);
    }
}
