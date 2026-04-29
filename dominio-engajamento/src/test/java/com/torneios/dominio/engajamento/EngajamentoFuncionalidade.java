package com.torneios.dominio.engajamento;

import java.util.List;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistoso;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoServico;
import com.torneios.dominio.engajamento.feed.FeedTorneioServico;
import com.torneios.dominio.engajamento.feed.PublicacaoFeed;
import com.torneios.dominio.engajamento.feed.PublicacaoFeedId;
import com.torneios.dominio.engajamento.palpite.EventoAlvo;
import com.torneios.dominio.engajamento.palpite.Palpite;
import com.torneios.dominio.engajamento.palpite.PalpiteId;
import com.torneios.dominio.engajamento.palpite.PalpiteServico;
import com.torneios.dominio.engajamento.palpite.PercentuaisPalpite;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaSuporteFeedTorneioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaSuportePalpiteMemoria;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaSuporteDesafioAmistosoMemoria;
import com.torneios.infraestrutura.persistencia.memoria.DesafioAmistosoRepositorioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.FeedTorneioRepositorioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.PalpiteRepositorioMemoria;

public abstract class EngajamentoFuncionalidade {

    protected static final UsuarioId USUARIO_ID = new UsuarioId(1L);
    protected static final UsuarioId OUTRO_USUARIO_ID = new UsuarioId(2L);
    protected static final UsuarioId ORGANIZADOR_ID = new UsuarioId(10L);
    protected static final TorneioId TORNEIO_ID = new TorneioId(1L);
    protected static final PartidaId PARTIDA_ID = new PartidaId(1L);
    protected static final long TIME_A_ID = 1L;
    protected static final long TIME_B_ID = 2L;
    protected static final TimeId TIME_DESAFIANTE_ID = new TimeId(1L);
    protected static final TimeId TIME_DESAFIADO_ID = new TimeId(2L);
    protected static final long JOGADOR_A_ID = 11L;
    protected static final long JOGADOR_B_ID = 12L;

    protected final PalpiteRepositorioMemoria palpiteRepositorio = new PalpiteRepositorioMemoria();
    protected final ConsultaSuportePalpiteMemoria consultaPalpite = new ConsultaSuportePalpiteMemoria();
    protected final PalpiteServico palpiteServico = new PalpiteServico(palpiteRepositorio, consultaPalpite);

    protected final FeedTorneioRepositorioMemoria feedRepositorio = new FeedTorneioRepositorioMemoria();
    protected final ConsultaSuporteFeedTorneioMemoria consultaFeed = new ConsultaSuporteFeedTorneioMemoria();
    protected final FeedTorneioServico feedTorneioServico = new FeedTorneioServico(feedRepositorio, consultaFeed);

    protected final DesafioAmistosoRepositorioMemoria desafioAmistosoRepositorio = new DesafioAmistosoRepositorioMemoria();
    protected final ConsultaSuporteDesafioAmistosoMemoria consultaDesafio = new ConsultaSuporteDesafioAmistosoMemoria();
    protected final DesafioAmistosoServico desafioAmistosoServico = new DesafioAmistosoServico(
            desafioAmistosoRepositorio, consultaDesafio);

    protected EventoAlvo eventoAlvo;
    protected Palpite palpite;
    protected List<Palpite> palpitesApurados;
    protected PercentuaisPalpite percentuaisPalpite;
    protected long resultadoReal;

    protected PublicacaoFeed publicacaoFeed;
    protected List<PublicacaoFeed> publicacoesFeed;
    protected DesafioAmistoso desafioAmistoso;
    protected List<DesafioAmistoso> historicoAmistosos;
    protected Exception excecaoCapturada;

    protected void configurarEventoDePartidaAberto() {
        eventoAlvo = EventoAlvo.paraPartida(TORNEIO_ID, PARTIDA_ID);
        consultaPalpite.registrarOpcoesValidas(eventoAlvo, TIME_A_ID, TIME_B_ID);
    }

    protected void configurarEventoCampeaoAberto() {
        eventoAlvo = EventoAlvo.paraCampeao(TORNEIO_ID);
        consultaPalpite.registrarOpcoesValidas(eventoAlvo, TIME_A_ID, TIME_B_ID);
    }

    protected void configurarEventoArtilheiroAberto() {
        eventoAlvo = EventoAlvo.paraArtilheiro(TORNEIO_ID);
        consultaPalpite.registrarOpcoesValidas(eventoAlvo, JOGADOR_A_ID, JOGADOR_B_ID);
    }

    protected void configurarEventoLiderAssistenciasAberto() {
        eventoAlvo = EventoAlvo.paraLiderAssistencias(TORNEIO_ID);
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
}
