package com.torneios.dominio.engajamento;

import java.util.List;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.ChatPrivadoServico;
import com.torneios.dominio.engajamento.chat.ConversaPrivada;
import com.torneios.dominio.engajamento.chat.ConversaPrivadaId;
import com.torneios.dominio.engajamento.chat.MensagemChat;
import com.torneios.dominio.engajamento.chat.MensagemChatId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistoso;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoServico;
import com.torneios.dominio.engajamento.feed.FeedTorneioServico;
import com.torneios.dominio.engajamento.feed.PublicacaoFeed;
import com.torneios.dominio.engajamento.feed.PublicacaoFeedId;
import com.torneios.dominio.engajamento.palpite.EventoAlvoPalpite;
import com.torneios.dominio.engajamento.palpite.Palpite;
import com.torneios.dominio.engajamento.palpite.PalpiteId;
import com.torneios.dominio.engajamento.palpite.PalpiteServico;
import com.torneios.dominio.engajamento.palpite.PercentuaisPalpite;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaSuporteChatMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaSuporteFeedTorneioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaSuportePalpiteMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaSuporteDesafioAmistosoMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ConversaPrivadaRepositorioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.DesafioAmistosoRepositorioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.FeedTorneioRepositorioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.PalpiteRepositorioMemoria;

public abstract class EngajamentoFuncionalidade {

    protected static final UsuarioId USUARIO_ID = new UsuarioId(1L);
    protected static final UsuarioId OUTRO_USUARIO_ID = new UsuarioId(2L);
    protected static final UsuarioId ORGANIZADOR_ID = new UsuarioId(10L);
    protected static final String VISITANTE_ID = "visitante-123";
    protected static final TorneioId TORNEIO_ID = new TorneioId(1L);
    protected static final PartidaId PARTIDA_ID = new PartidaId(1L);
    protected static final long TIME_A_ID = 1L;
    protected static final long TIME_B_ID = 2L;
    protected static final TimeId TIME_DESAFIANTE_ID = new TimeId(1L);
    protected static final TimeId TIME_DESAFIADO_ID = new TimeId(2L);
    protected static final long JOGADOR_A_ID = 11L;
    protected static final long JOGADOR_B_ID = 12L;

    protected static PalpiteRepositorioMemoria palpiteRepositorio = new PalpiteRepositorioMemoria();
    protected static ConsultaSuportePalpiteMemoria consultaPalpite = new ConsultaSuportePalpiteMemoria();
    protected static PalpiteServico palpiteServico = new PalpiteServico(palpiteRepositorio, consultaPalpite);

    protected static FeedTorneioRepositorioMemoria feedRepositorio = new FeedTorneioRepositorioMemoria();
    protected static ConsultaSuporteFeedTorneioMemoria consultaFeed = new ConsultaSuporteFeedTorneioMemoria();
    protected static FeedTorneioServico feedTorneioServico = new FeedTorneioServico(feedRepositorio, consultaFeed);

    protected static DesafioAmistosoRepositorioMemoria desafioAmistosoRepositorio = new DesafioAmistosoRepositorioMemoria();
    protected static ConsultaSuporteDesafioAmistosoMemoria consultaDesafio = new ConsultaSuporteDesafioAmistosoMemoria();
    protected static DesafioAmistosoServico desafioAmistosoServico = new DesafioAmistosoServico(
            desafioAmistosoRepositorio, consultaDesafio);

    protected static ConversaPrivadaRepositorioMemoria conversaPrivadaRepositorio = new ConversaPrivadaRepositorioMemoria();
    protected static ConsultaSuporteChatMemoria consultaChat = new ConsultaSuporteChatMemoria();
    protected static ChatPrivadoServico chatPrivadoServico = new ChatPrivadoServico(
            conversaPrivadaRepositorio, consultaChat);

    protected static EventoAlvoPalpite eventoAlvo;
    protected static Palpite palpite;
    protected static List<Palpite> palpitesApurados;
    protected static PercentuaisPalpite percentuaisPalpite;
    protected static long resultadoReal;

    protected static PublicacaoFeed publicacaoFeed;
    protected static List<PublicacaoFeed> publicacoesFeed;
    protected static DesafioAmistoso desafioAmistoso;
    protected static List<DesafioAmistoso> historicoAmistosos;
    protected static ConversaPrivada conversaPrivada;
    protected static MensagemChat mensagemChat;
    protected static List<ConversaPrivada> conversasPrivadas;
    protected static Exception excecaoCapturada;

    public void resetar() {
        palpiteRepositorio = new PalpiteRepositorioMemoria();
        consultaPalpite = new ConsultaSuportePalpiteMemoria();
        palpiteServico = new PalpiteServico(palpiteRepositorio, consultaPalpite);

        feedRepositorio = new FeedTorneioRepositorioMemoria();
        consultaFeed = new ConsultaSuporteFeedTorneioMemoria();
        feedTorneioServico = new FeedTorneioServico(feedRepositorio, consultaFeed);

        desafioAmistosoRepositorio = new DesafioAmistosoRepositorioMemoria();
        consultaDesafio = new ConsultaSuporteDesafioAmistosoMemoria();
        desafioAmistosoServico = new DesafioAmistosoServico(desafioAmistosoRepositorio, consultaDesafio);

        conversaPrivadaRepositorio = new ConversaPrivadaRepositorioMemoria();
        consultaChat = new ConsultaSuporteChatMemoria();
        chatPrivadoServico = new ChatPrivadoServico(conversaPrivadaRepositorio, consultaChat);

        eventoAlvo = null;
        palpite = null;
        palpitesApurados = null;
        percentuaisPalpite = null;
        resultadoReal = 0L;

        publicacaoFeed = null;
        publicacoesFeed = null;
        desafioAmistoso = null;
        historicoAmistosos = null;
        conversaPrivada = null;
        mensagemChat = null;
        conversasPrivadas = null;
        excecaoCapturada = null;
    }

    protected void configurarEventoDePartidaAberto() {
        eventoAlvo = EventoAlvoPalpite.paraPartida(TORNEIO_ID, PARTIDA_ID);
        consultaPalpite.registrarOpcoesValidas(eventoAlvo, TIME_A_ID, TIME_B_ID);
    }

    protected void configurarEventoCampeaoAberto() {
        eventoAlvo = EventoAlvoPalpite.paraCampeao(TORNEIO_ID);
        consultaPalpite.registrarOpcoesValidas(eventoAlvo, TIME_A_ID, TIME_B_ID);
    }

    protected void configurarEventoArtilheiroAberto() {
        eventoAlvo = EventoAlvoPalpite.paraArtilheiro(TORNEIO_ID);
        consultaPalpite.registrarOpcoesValidas(eventoAlvo, JOGADOR_A_ID, JOGADOR_B_ID);
    }

    protected void configurarEventoLiderAssistenciasAberto() {
        eventoAlvo = EventoAlvoPalpite.paraLiderAssistencias(TORNEIO_ID);
        consultaPalpite.registrarOpcoesValidas(eventoAlvo, JOGADOR_A_ID, JOGADOR_B_ID);
    }

    protected void configurarTorneioComPartidaNoFeed() {
        consultaFeed.registrarTorneio(TORNEIO_ID, ORGANIZADOR_ID);
        consultaFeed.registrarPartida(TORNEIO_ID, PARTIDA_ID);
    }

    protected PublicacaoFeedId publicacaoId(long valor) {
        return new PublicacaoFeedId(valor);
    }

    protected PalpiteId palpiteId(long valor) {
        return new PalpiteId(valor);
    }

    protected DesafioAmistosoId desafioId(long valor) {
        return new DesafioAmistosoId(valor);
    }

    protected ConversaPrivadaId conversaId(long valor) {
        return new ConversaPrivadaId(valor);
    }

    protected MensagemChatId mensagemId(long valor) {
        return new MensagemChatId(valor);
    }
}


