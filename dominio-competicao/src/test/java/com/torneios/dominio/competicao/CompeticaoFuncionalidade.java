package com.torneios.dominio.competicao;

import java.util.ArrayList;
import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.EsquemaTatico;
import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.enumeracao.Posicao;
import com.torneios.dominio.compartilhado.evento.EventoBarramento;
import com.torneios.dominio.compartilhado.evento.EventoObservador;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.chaveamento.Chaveamento;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultado;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultadoId;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultadoRepositorio;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultadoServico;
import com.torneios.dominio.competicao.chaveamento.ChaveamentoServico;
import com.torneios.dominio.competicao.classificacao.Classificacao;
import com.torneios.dominio.competicao.classificacao.ClassificacaoServico;
import com.torneios.dominio.competicao.escalacao.Escalacao;
import com.torneios.dominio.competicao.escalacao.EscalacaoId;
import com.torneios.dominio.competicao.escalacao.EscalacaoRepositorio;
import com.torneios.dominio.competicao.escalacao.MesaTatica;
import com.torneios.dominio.competicao.escalacao.EscalacaoServico;
import com.torneios.dominio.competicao.escalacao.JogadorEscalado;
import com.torneios.dominio.competicao.geracao.GeradorPartidasServico;
import com.torneios.dominio.competicao.geracao.PreparacaoCompeticao;
import com.torneios.dominio.competicao.partida.AtualizacaoCompeticao;
import com.torneios.dominio.competicao.partida.Partida;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.competicao.partida.PartidaServico;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaSuporteEscalacaoMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaContestacaoResultadoMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaCompeticaoTorneioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.Repositorio;

public class CompeticaoFuncionalidade implements EventoBarramento {

    protected static final UsuarioId ORGANIZADOR_ID = new UsuarioId(1L);
    protected static final UsuarioId OUTRO_USUARIO_ID = new UsuarioId(99L);
    protected static final TorneioId TORNEIO_ID = new TorneioId(1L);
    protected static final TimeId TIME_A_ID = new TimeId(1L);
    protected static final TimeId TIME_B_ID = new TimeId(2L);
    protected static final TimeId TIME_C_ID = new TimeId(3L);
    protected static final TimeId TIME_D_ID = new TimeId(4L);
    protected static final PartidaId PARTIDA_ID = new PartidaId(1L);
    protected static final ContestacaoResultadoId CONTESTACAO_ID = new ContestacaoResultadoId(1L);
    protected static final ContestacaoResultadoId OUTRA_CONTESTACAO_ID = new ContestacaoResultadoId(2L);
    protected static final EscalacaoId ESCALACAO_ID = new EscalacaoId(1L);
    protected static final EscalacaoId ESCALACAO_TIME_B_ID = new EscalacaoId(2L);
    protected static final TecnicoId TECNICO_ID = new TecnicoId(1L);
    protected static final JogadorId JOGADOR_1_ID = new JogadorId(1L);
    protected static final JogadorId JOGADOR_2_ID = new JogadorId(2L);
    protected static final JogadorId JOGADOR_3_ID = new JogadorId(3L);
    protected static final JogadorId JOGADOR_4_ID = new JogadorId(4L);
    protected static final JogadorId JOGADOR_5_ID = new JogadorId(5L);
    protected static final JogadorId JOGADOR_6_ID = new JogadorId(6L);
    protected static final JogadorId JOGADOR_7_ID = new JogadorId(7L);
    protected static final JogadorId JOGADOR_FORA_ELENCO_ID = new JogadorId(999L);

    protected static Repositorio repositorio = new Repositorio();
    protected static PartidaRepositorio partidaRepositorio = repositorio;
    protected static EscalacaoRepositorio escalacaoRepositorio = repositorio;
    protected static ContestacaoResultadoRepositorio contestacaoRepositorio = repositorio;
    protected static ConsultaCompeticaoTorneioMemoria consultaCompeticaoTorneio = new ConsultaCompeticaoTorneioMemoria();
    protected static ConsultaSuporteEscalacaoMemoria consultaEscalacao = new ConsultaSuporteEscalacaoMemoria();
    protected static ConsultaContestacaoResultadoMemoria consultaContestacao = new ConsultaContestacaoResultadoMemoria();
    protected static GeradorPartidasServico geradorPartidasServico = new GeradorPartidasServico();
    protected static ClassificacaoServico classificacaoServico = new ClassificacaoServico();
    protected static ChaveamentoServico chaveamentoServico = new ChaveamentoServico();
    protected static PartidaServico partidaServico;
    protected static EscalacaoServico escalacaoServico = new EscalacaoServico(repositorio, consultaEscalacao);
    protected static ContestacaoResultadoServico contestacaoResultadoServico;

    protected static List<Object> eventos = new ArrayList<>();

    protected static List<Partida> partidasGeradas;
    protected static PreparacaoCompeticao preparacaoCompeticao;
    protected static AtualizacaoCompeticao atualizacaoCompeticao;
    protected static List<Classificacao> classificacao;
    protected static Chaveamento chaveamento;
    protected static Escalacao escalacao;
    protected static MesaTatica mesaTatica;
    protected static ContestacaoResultado contestacaoResultado;
    protected static java.util.List<ContestacaoResultado> contestacoes;
    protected static Exception excecaoCapturada;
    protected static UsuarioId usuarioSolicitanteId = ORGANIZADOR_ID;

    static {
        partidaServico = new PartidaServico(repositorio, consultaCompeticaoTorneio,
                geradorPartidasServico, classificacaoServico, chaveamentoServico,
                new EventoBarramento() {
                    @Override public <E> void adicionar(EventoObservador<E> obs) { throw new UnsupportedOperationException(); }
                    @Override public <E> void postar(E evento) { eventos.add(evento); }
                });
        contestacaoResultadoServico = new ContestacaoResultadoServico(
                repositorio, repositorio, consultaCompeticaoTorneio, consultaContestacao);
    }

    public void resetar() {
        repositorio = new Repositorio();
        partidaRepositorio = repositorio;
        escalacaoRepositorio = repositorio;
        contestacaoRepositorio = repositorio;
        consultaCompeticaoTorneio = new ConsultaCompeticaoTorneioMemoria();
        consultaEscalacao = new ConsultaSuporteEscalacaoMemoria();
        consultaContestacao = new ConsultaContestacaoResultadoMemoria();
        geradorPartidasServico = new GeradorPartidasServico();
        classificacaoServico = new ClassificacaoServico();
        chaveamentoServico = new ChaveamentoServico();
        partidaServico = new PartidaServico(repositorio, consultaCompeticaoTorneio,
                geradorPartidasServico, classificacaoServico, chaveamentoServico, this);
        escalacaoServico = new EscalacaoServico(repositorio, consultaEscalacao);
        contestacaoResultadoServico = new ContestacaoResultadoServico(
                repositorio, repositorio, consultaCompeticaoTorneio, consultaContestacao);

        eventos = new ArrayList<>();
        partidasGeradas = null;
        preparacaoCompeticao = null;
        atualizacaoCompeticao = null;
        classificacao = null;
        chaveamento = null;
        escalacao = null;
        mesaTatica = null;
        contestacaoResultado = null;
        contestacoes = null;
        excecaoCapturada = null;
        usuarioSolicitanteId = ORGANIZADOR_ID;
    }

    @Override
    public <E> void adicionar(EventoObservador<E> observador) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> void postar(E evento) {
        eventos.add(evento);
    }

    protected void configurarCenarioContestacaoPadrao(boolean resultadoRegistrado) {
        configurarTorneioPontosCorridos(true);
        consultaContestacao.registrarResponsavel(TIME_A_ID, ORGANIZADOR_ID);
        consultaContestacao.registrarResponsavel(TIME_B_ID, OUTRO_USUARIO_ID);
        consultaContestacao.registrarPrazo(TORNEIO_ID, 48);

        Partida partida = new Partida(PARTIDA_ID, TORNEIO_ID, TIME_A_ID, TIME_B_ID,
                "Rodada 1", 5);
        if (resultadoRegistrado) {
            partida.registrarResultado(new ResultadoPartida(2, 1), java.time.LocalDateTime.now().minusHours(2));
        }
        partidaServico.salvar(partida);
    }

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
        consultaEscalacao.registrarPartida(
                PARTIDA_ID,
                FormatoEquipe.CINCO_POR_CINCO,
                partidaIniciada,
                List.of(TIME_A_ID, TIME_B_ID));
        consultaEscalacao.registrarResponsavel(TIME_A_ID, ORGANIZADOR_ID);
        consultaEscalacao.registrarResponsavel(TIME_B_ID, OUTRO_USUARIO_ID);
        consultaEscalacao.registrarTecnico(TIME_A_ID, TECNICO_ID);
        consultaEscalacao.registrarElenco(TIME_A_ID, List.of(
                JOGADOR_1_ID, JOGADOR_2_ID, JOGADOR_3_ID, JOGADOR_4_ID,
                JOGADOR_5_ID, JOGADOR_6_ID, JOGADOR_7_ID));
        consultaEscalacao.registrarElenco(TIME_B_ID, List.of(
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

    protected void definirEscalacaoDoTimeB() {
        escalacaoServico.definirEscalacaoPorResponsavel(
                ESCALACAO_TIME_B_ID, PARTIDA_ID, TIME_B_ID, OUTRO_USUARIO_ID,
                EsquemaTatico.UM_DOIS_UM, titularesCincoPorCinco(), reservasPadrao());
    }
}
