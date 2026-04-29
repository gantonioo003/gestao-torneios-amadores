package com.torneios.dominio.competicao;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.EsquemaTatico;
import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.enumeracao.Posicao;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.chaveamento.Chaveamento;
import com.torneios.dominio.competicao.chaveamento.ChaveamentoServico;
import com.torneios.dominio.competicao.classificacao.Classificacao;
import com.torneios.dominio.competicao.classificacao.ClassificacaoServico;
import com.torneios.dominio.competicao.escalacao.Escalacao;
import com.torneios.dominio.competicao.escalacao.EscalacaoId;
import com.torneios.dominio.competicao.escalacao.EscalacaoServico;
import com.torneios.dominio.competicao.escalacao.JogadorEscalado;
import com.torneios.dominio.competicao.geracao.GeradorPartidasServico;
import com.torneios.dominio.competicao.geracao.PreparacaoCompeticao;
import com.torneios.dominio.competicao.partida.AtualizacaoCompeticao;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.partida.PartidaServico;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaSuporteEscalacaoMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaCompeticaoTorneioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.EscalacaoRepositorioMemoria;
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
    protected static final EscalacaoId ESCALACAO_ID = new EscalacaoId(1L);
    protected static final TecnicoId TECNICO_ID = new TecnicoId(1L);
    protected static final JogadorId JOGADOR_1_ID = new JogadorId(1L);
    protected static final JogadorId JOGADOR_2_ID = new JogadorId(2L);
    protected static final JogadorId JOGADOR_3_ID = new JogadorId(3L);
    protected static final JogadorId JOGADOR_4_ID = new JogadorId(4L);
    protected static final JogadorId JOGADOR_5_ID = new JogadorId(5L);
    protected static final JogadorId JOGADOR_6_ID = new JogadorId(6L);
    protected static final JogadorId JOGADOR_7_ID = new JogadorId(7L);
    protected static final JogadorId JOGADOR_FORA_ELENCO_ID = new JogadorId(999L);

    protected final PartidaRepositorioMemoria partidaRepositorio = new PartidaRepositorioMemoria();
    protected final ConsultaCompeticaoTorneioMemoria consultaCompeticaoTorneio = new ConsultaCompeticaoTorneioMemoria();
    protected final EscalacaoRepositorioMemoria escalacaoRepositorio = new EscalacaoRepositorioMemoria();
    protected final ConsultaSuporteEscalacaoMemoria consultaEscalacao = new ConsultaSuporteEscalacaoMemoria();
    protected final GeradorPartidasServico geradorPartidasServico = new GeradorPartidasServico();
    protected final ClassificacaoServico classificacaoServico = new ClassificacaoServico();
    protected final ChaveamentoServico chaveamentoServico = new ChaveamentoServico();
    protected final PartidaServico partidaServico = new PartidaServico(
            partidaRepositorio, consultaCompeticaoTorneio, geradorPartidasServico, classificacaoServico, chaveamentoServico);
    protected final EscalacaoServico escalacaoServico = new EscalacaoServico(escalacaoRepositorio, consultaEscalacao);

    protected List<Partida> partidasGeradas;
    protected PreparacaoCompeticao preparacaoCompeticao;
    protected AtualizacaoCompeticao atualizacaoCompeticao;
    protected List<Classificacao> classificacao;
    protected Chaveamento chaveamento;
    protected Escalacao escalacao;
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

    protected void configurarSuporteEscalacao(boolean partidaIniciada) {
        consultaEscalacao.registrarPartida(PARTIDA_ID, FormatoEquipe.CINCO_POR_CINCO, partidaIniciada);
        consultaEscalacao.registrarResponsavel(TIME_A_ID, ORGANIZADOR_ID);
        consultaEscalacao.registrarTecnico(TIME_A_ID, TECNICO_ID);
        consultaEscalacao.registrarElenco(TIME_A_ID, List.of(
                JOGADOR_1_ID, JOGADOR_2_ID, JOGADOR_3_ID, JOGADOR_4_ID,
                JOGADOR_5_ID, JOGADOR_6_ID, JOGADOR_7_ID));
    }

    protected List<JogadorEscalado> titularesCincoPorCinco() {
        return List.of(
                new JogadorEscalado(JOGADOR_1_ID, Posicao.GOLEIRO),
                new JogadorEscalado(JOGADOR_2_ID, Posicao.DEFENSOR),
                new JogadorEscalado(JOGADOR_3_ID, Posicao.DEFENSOR),
                new JogadorEscalado(JOGADOR_4_ID, Posicao.MEIO_CAMPISTA),
                new JogadorEscalado(JOGADOR_5_ID, Posicao.ATACANTE));
    }

    protected List<JogadorEscalado> titularesDoisDois() {
        return List.of(
                new JogadorEscalado(JOGADOR_1_ID, Posicao.GOLEIRO),
                new JogadorEscalado(JOGADOR_2_ID, Posicao.DEFENSOR),
                new JogadorEscalado(JOGADOR_3_ID, Posicao.DEFENSOR),
                new JogadorEscalado(JOGADOR_4_ID, Posicao.ATACANTE),
                new JogadorEscalado(JOGADOR_5_ID, Posicao.ATACANTE));
    }

    protected List<JogadorEscalado> titularesComJogadorForaDoElenco() {
        return List.of(
                new JogadorEscalado(JOGADOR_FORA_ELENCO_ID, Posicao.GOLEIRO),
                new JogadorEscalado(JOGADOR_2_ID, Posicao.DEFENSOR),
                new JogadorEscalado(JOGADOR_3_ID, Posicao.DEFENSOR),
                new JogadorEscalado(JOGADOR_4_ID, Posicao.MEIO_CAMPISTA),
                new JogadorEscalado(JOGADOR_5_ID, Posicao.ATACANTE));
    }

    protected List<JogadorId> reservasPadrao() {
        return List.of(JOGADOR_6_ID, JOGADOR_7_ID);
    }

    protected void definirEscalacaoPadrao(boolean partidaIniciadaAposCriacao) {
        configurarSuporteEscalacao(false);
        escalacao = escalacaoServico.definirEscalacaoPorResponsavel(
                ESCALACAO_ID, PARTIDA_ID, TIME_A_ID, ORGANIZADOR_ID,
                EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), reservasPadrao());
        if (partidaIniciadaAposCriacao) {
            consultaEscalacao.iniciarPartida(PARTIDA_ID);
        }
    }
}
