package com.torneios.dominio.competicao;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.chaveamento.Chaveamento;
import com.torneios.dominio.competicao.chaveamento.ChaveamentoServico;
import com.torneios.dominio.competicao.classificacao.Classificacao;
import com.torneios.dominio.competicao.classificacao.ClassificacaoServico;
import com.torneios.dominio.competicao.geracao.GeradorPartidasServico;
import com.torneios.dominio.competicao.partida.AtualizacaoCompeticao;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.partida.PartidaServico;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaCompeticaoTorneioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.PartidaRepositorioMemoria;

/**
 * Classe base para compartilhar preparacao e estado comum entre os steps
 * dos cenarios de competicao.
 */
public abstract class CompeticaoFuncionalidade {

    protected static final UsuarioId ORGANIZADOR_ID = new UsuarioId(1L);
    protected static final UsuarioId OUTRO_USUARIO_ID = new UsuarioId(99L);
    protected static final TorneioId TORNEIO_ID = new TorneioId(1L);
    protected static final TimeId TIME_A_ID = new TimeId(1L);
    protected static final TimeId TIME_B_ID = new TimeId(2L);
    protected static final TimeId TIME_C_ID = new TimeId(3L);
    protected static final TimeId TIME_D_ID = new TimeId(4L);
    protected static final PartidaId PARTIDA_ID = new PartidaId(1L);

    protected final PartidaRepositorioMemoria partidaRepositorio = new PartidaRepositorioMemoria();
    protected final ConsultaCompeticaoTorneioMemoria consultaCompeticaoTorneio = new ConsultaCompeticaoTorneioMemoria();
    protected final GeradorPartidasServico geradorPartidasServico = new GeradorPartidasServico();
    protected final ClassificacaoServico classificacaoServico = new ClassificacaoServico();
    protected final ChaveamentoServico chaveamentoServico = new ChaveamentoServico();
    protected final PartidaServico partidaServico = new PartidaServico(
            partidaRepositorio, consultaCompeticaoTorneio, geradorPartidasServico, classificacaoServico, chaveamentoServico);

    protected List<Partida> partidasGeradas;
    protected AtualizacaoCompeticao atualizacaoCompeticao;
    protected List<Classificacao> classificacao;
    protected Chaveamento chaveamento;
    protected Exception excecaoCapturada;

    protected void configurarTorneioPontosCorridos(boolean estruturaGerada) {
        List<TimeId> participantes = List.of(TIME_A_ID, TIME_B_ID, TIME_C_ID);
        consultaCompeticaoTorneio.registrarTorneio(
                TORNEIO_ID, ORGANIZADOR_ID, FormatoTorneio.PONTOS_CORRIDOS, FormatoEquipe.CINCO_POR_CINCO,
                participantes, List.of(), estruturaGerada);
    }

    protected void configurarTorneioMataMata(boolean estruturaGerada) {
        List<TimeId> participantes = List.of(TIME_A_ID, TIME_B_ID);
        consultaCompeticaoTorneio.registrarTorneio(
                TORNEIO_ID, ORGANIZADOR_ID, FormatoTorneio.MATA_MATA, FormatoEquipe.CINCO_POR_CINCO,
                participantes, List.of(), estruturaGerada);
    }

    protected void configurarTorneioFaseGrupos(boolean estruturaGerada) {
        List<TimeId> participantes = List.of(TIME_A_ID, TIME_B_ID, TIME_C_ID, TIME_D_ID);
        List<List<TimeId>> grupos = List.of(List.of(TIME_A_ID, TIME_B_ID), List.of(TIME_C_ID, TIME_D_ID));
        consultaCompeticaoTorneio.registrarTorneio(
                TORNEIO_ID, ORGANIZADOR_ID, FormatoTorneio.FASE_DE_GRUPOS_COM_MATA_MATA, FormatoEquipe.CINCO_POR_CINCO,
                participantes, grupos, estruturaGerada);
    }
}
