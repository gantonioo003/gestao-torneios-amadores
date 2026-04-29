package com.torneios.dominio.estatisticas;

import java.util.List;

import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.estatisticas.artilharia.ArtilhariaServico;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaJogador;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaServico;
import com.torneios.dominio.estatisticas.evento.EventoEstatistico;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoServico;
import com.torneios.dominio.estatisticas.nota.CalculadoraNotaEstatistica;
import com.torneios.dominio.estatisticas.nota.NotaEstatistica;
import com.torneios.infraestrutura.persistencia.memoria.ConsultaEstatisticaCompeticaoMemoria;
import com.torneios.infraestrutura.persistencia.memoria.EventoEstatisticoRepositorioMemoria;

/**
 * Classe base para compartilhar preparacao e estado comum entre os steps
 * dos cenarios de estatisticas.
 */
public abstract class EstatisticasFuncionalidade {

    protected static final UsuarioId ORGANIZADOR_ID = new UsuarioId(1L);
    protected static final UsuarioId OUTRO_USUARIO_ID = new UsuarioId(99L);
    protected static final TorneioId TORNEIO_ID = new TorneioId(1L);
    protected static final PartidaId PARTIDA_ID = new PartidaId(1L);
    protected static final JogadorId JOGADOR_A_ID = new JogadorId(10L);
    protected static final JogadorId JOGADOR_B_ID = new JogadorId(20L);
    protected static final JogadorId JOGADOR_INVALIDO_ID = new JogadorId(999L);

    protected final EventoEstatisticoRepositorioMemoria eventoRepositorio = new EventoEstatisticoRepositorioMemoria();
    protected final ConsultaEstatisticaCompeticaoMemoria consultaEstatisticaCompeticao = new ConsultaEstatisticaCompeticaoMemoria();
    protected final EventoEstatisticoServico eventoEstatisticoServico = new EventoEstatisticoServico(
            eventoRepositorio, consultaEstatisticaCompeticao);
    protected final CalculadoraNotaEstatistica calculadoraNotaEstatistica = new CalculadoraNotaEstatistica();
    protected final EstatisticaServico estatisticaServico = new EstatisticaServico(
            eventoRepositorio, calculadoraNotaEstatistica);
    protected final ArtilhariaServico artilhariaServico = new ArtilhariaServico(estatisticaServico);

    protected EventoEstatistico eventoRegistrado;
    protected NotaEstatistica notaEstatistica;
    protected List<EstatisticaJogador> rankingArtilharia;
    protected List<EstatisticaJogador> rankingAssistencias;
    protected List<EstatisticaJogador> estatisticasJogadores;
    protected List<EventoEstatistico> historicoJogador;
    protected Exception excecaoCapturada;

    protected void configurarCenarioPadrao() {
        consultaEstatisticaCompeticao.registrarOrganizador(TORNEIO_ID, ORGANIZADOR_ID);
        consultaEstatisticaCompeticao.registrarPartida(PARTIDA_ID, TORNEIO_ID);
        consultaEstatisticaCompeticao.registrarJogadorNaPartida(PARTIDA_ID, JOGADOR_A_ID);
        consultaEstatisticaCompeticao.registrarJogadorNaPartida(PARTIDA_ID, JOGADOR_B_ID);
    }
}
