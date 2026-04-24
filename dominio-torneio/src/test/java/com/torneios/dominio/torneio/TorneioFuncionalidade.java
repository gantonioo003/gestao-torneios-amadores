package com.torneios.dominio.torneio;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.torneio.estrutura.EstruturaCompeticao;
import com.torneios.dominio.torneio.estrutura.GeradorEstruturaCompeticaoServico;
import com.torneios.dominio.torneio.organizador.OrganizadorTorneioServico;
import com.torneios.dominio.torneio.torneio.Torneio;
import com.torneios.dominio.torneio.torneio.TorneioServico;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaElegibilidadeParticipanteTorneioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.TorneioRepositorioMemoria;

/**
 * Classe base para compartilhar preparacao e estado comum entre os steps
 * dos cenarios de torneio.
 */
public abstract class TorneioFuncionalidade {

    protected static final UsuarioId ORGANIZADOR_ID = new UsuarioId(1L);
    protected static final UsuarioId OUTRO_USUARIO_ID = new UsuarioId(99L);
    protected static final TorneioId TORNEIO_ID = new TorneioId(1L);
    protected static final TimeId TIME_A_ID = new TimeId(1L);
    protected static final TimeId TIME_B_ID = new TimeId(2L);
    protected static final TimeId TIME_C_ID = new TimeId(3L);
    protected static final TimeId TIME_D_ID = new TimeId(4L);

    protected final TorneioRepositorioMemoria torneioRepositorio = new TorneioRepositorioMemoria();
    protected final OrganizadorTorneioServico organizadorTorneioServico = new OrganizadorTorneioServico();
    protected final GeradorEstruturaCompeticaoServico geradorEstruturaCompeticaoServico = new GeradorEstruturaCompeticaoServico();
    protected final ConsultaElegibilidadeParticipanteTorneioMemoria consultaElegibilidade = new ConsultaElegibilidadeParticipanteTorneioMemoria();
    protected final TorneioServico torneioServico = new TorneioServico(
            torneioRepositorio, organizadorTorneioServico, geradorEstruturaCompeticaoServico, consultaElegibilidade);

    protected Torneio torneio;
    protected EstruturaCompeticao estruturaCompeticao;
    protected Exception excecaoCapturada;

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
