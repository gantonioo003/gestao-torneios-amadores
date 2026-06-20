package com.torneios.aplicacao.competicao.escalacao;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.EsquemaTatico;
import com.torneios.dominio.compartilhado.enumeracao.Posicao;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.escalacao.Escalacao;
import com.torneios.dominio.competicao.escalacao.EscalacaoId;
import com.torneios.dominio.competicao.escalacao.EscalacaoServico;
import com.torneios.dominio.competicao.escalacao.JogadorEscalado;
import com.torneios.dominio.competicao.escalacao.JogadorPosicionadoMesaTatica;
import com.torneios.dominio.competicao.escalacao.MesaTatica;
import com.torneios.dominio.competicao.escalacao.TipoVisualizacaoEscalacao;

/**
 * Casos de uso de geracao e consulta da mesa tatica do time.
 */
public class EscalacaoServicoAplicacao {

    private final EscalacaoServico escalacaoServico;

    public EscalacaoServicoAplicacao(EscalacaoServico escalacaoServico) {
        notNull(escalacaoServico, "O servico de escalacao e obrigatorio.");
        this.escalacaoServico = escalacaoServico;
    }

    public EscalacaoResumo definirEscalacaoPorResponsavel(long escalacaoId,
                                                          long partidaId,
                                                          long timeId,
                                                          long usuarioId,
                                                          String tipoVisualizacao,
                                                          String esquemaTatico,
                                                          List<JogadorEscaladoEntrada> titulares,
                                                          List<Long> reservas) {
        return converter(escalacaoServico.definirEscalacaoPorResponsavel(
                new EscalacaoId(escalacaoId),
                new PartidaId(partidaId),
                new TimeId(timeId),
                new UsuarioId(usuarioId),
                TipoVisualizacaoEscalacao.valueOf(tipoVisualizacao),
                converterEsquema(esquemaTatico),
                converterTitulares(titulares),
                converterReservas(reservas)));
    }

    public EscalacaoResumo definirEscalacaoPorTecnico(long escalacaoId,
                                                      long partidaId,
                                                      long timeId,
                                                      long tecnicoId,
                                                      String tipoVisualizacao,
                                                      String esquemaTatico,
                                                      List<JogadorEscaladoEntrada> titulares,
                                                      List<Long> reservas) {
        return converter(escalacaoServico.definirEscalacaoPorTecnico(
                new EscalacaoId(escalacaoId),
                new PartidaId(partidaId),
                new TimeId(timeId),
                new TecnicoId(tecnicoId),
                TipoVisualizacaoEscalacao.valueOf(tipoVisualizacao),
                converterEsquema(esquemaTatico),
                converterTitulares(titulares),
                converterReservas(reservas)));
    }

    public EscalacaoResumo definirEscalacao(long escalacaoId,
                                            long partidaId,
                                            long timeId,
                                            long usuarioId,
                                            String tipoVisualizacao,
                                            String esquemaTatico,
                                            List<JogadorEscaladoEntrada> titulares,
                                            List<Long> reservas) {
        return converter(escalacaoServico.definirEscalacaoPorUsuario(
                new EscalacaoId(escalacaoId),
                new PartidaId(partidaId),
                new TimeId(timeId),
                new UsuarioId(usuarioId),
                TipoVisualizacaoEscalacao.valueOf(tipoVisualizacao),
                converterEsquema(esquemaTatico),
                converterTitulares(titulares),
                converterReservas(reservas)));
    }

    public EscalacaoResumo obterEscalacao(long partidaId, long timeId) {
        return converter(escalacaoServico.obterEscalacao(new PartidaId(partidaId), new TimeId(timeId)));
    }

    public List<EscalacaoResumo> listarPorPartida(long partidaId) {
        return escalacaoServico.listarPorPartida(new PartidaId(partidaId)).stream()
                .map(this::converter)
                .toList();
    }

    public EscalacaoResumo obterEscalacaoDoResponsavel(long partidaId, long timeId, long usuarioId) {
        return converter(escalacaoServico.obterEscalacaoDoResponsavel(
                new PartidaId(partidaId), new TimeId(timeId), new UsuarioId(usuarioId)));
    }

    public EscalacaoResumo obterEscalacaoDoUsuario(long partidaId, long timeId, long usuarioId) {
        return converter(escalacaoServico.obterEscalacaoDoUsuario(
                new PartidaId(partidaId), new TimeId(timeId), new UsuarioId(usuarioId)));
    }

    public VisualizacaoPublicaResumo visualizarPublicamente(long partidaId) {
        List<EscalacaoResumo> escalacoes = escalacaoServico.listarPublicasPorPartida(new PartidaId(partidaId))
                .stream()
                .map(this::converter)
                .toList();
        return new VisualizacaoPublicaResumo("INDEPENDENTES", escalacoes);
    }

    public MesaTaticaResumo gerarMesaTatica(long partidaId, long timeId) {
        return converter(escalacaoServico.gerarMesaTatica(new PartidaId(partidaId), new TimeId(timeId)));
    }

    public MesaTaticaResumo gerarMesaTaticaPublica(long partidaId, long timeId) {
        Escalacao escalacao = escalacaoServico.listarPublicasPorPartida(new PartidaId(partidaId)).stream()
                .filter(item -> item.getTimeId().equals(new TimeId(timeId)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "A escalacao nao esta disponivel publicamente para esta partida e time."));
        return converter(escalacao.gerarMesaTatica());
    }

    public void congelarEscalacoesDaPartida(long partidaId) {
        escalacaoServico.congelarEscalacoesDaPartida(new PartidaId(partidaId));
    }

    private List<JogadorEscalado> converterTitulares(List<JogadorEscaladoEntrada> titulares) {
        return titulares == null ? List.of() : titulares.stream()
                .map(titular -> new JogadorEscalado(
                        new JogadorId(titular.jogadorId()),
                        titular.posicao() == null || titular.posicao().isBlank()
                                ? Posicao.ATACANTE
                                : Posicao.valueOf(titular.posicao())))
                .toList();
    }

    private EsquemaTatico converterEsquema(String esquemaTatico) {
        return esquemaTatico == null || esquemaTatico.isBlank()
                ? null
                : EsquemaTatico.valueOf(esquemaTatico);
    }

    private List<JogadorId> converterReservas(List<Long> reservas) {
        return reservas == null ? List.of() : reservas.stream().map(JogadorId::new).toList();
    }

    private EscalacaoResumo converter(Escalacao escalacao) {
        return new EscalacaoResumo(
                escalacao.getId().valor(),
                escalacao.getPartidaId().valor(),
                escalacao.getTimeId().valor(),
                escalacao.getFormatoEquipe().name(),
                escalacao.getTipoVisualizacao().name(),
                escalacao.getEsquemaTatico() == null ? null : escalacao.getEsquemaTatico().name(),
                escalacao.estaCongelada(),
                escalacao.getTitulares().stream()
                        .map(titular -> new JogadorEscaladoResumo(
                                titular.jogadorId().valor(),
                                titular.posicao().name()))
                        .toList(),
                escalacao.getTipoVisualizacao().usaMesaTatica()
                        ? escalacao.gerarMesaTatica().getTitularesPosicionados().stream()
                                .map(this::converterPosicionado)
                                .toList()
                        : List.of(),
                escalacao.getReservas().stream().map(JogadorId::valor).toList());
    }

    private MesaTaticaResumo converter(MesaTatica mesaTatica) {
        return new MesaTaticaResumo(
                mesaTatica.getPartidaId().valor(),
                mesaTatica.getTimeId().valor(),
                mesaTatica.getEsquemaTatico().name(),
                mesaTatica.getTitularesPosicionados().stream().map(this::converterPosicionado).toList(),
                mesaTatica.getReservas().stream().map(JogadorId::valor).toList());
    }

    private JogadorPosicionadoResumo converterPosicionado(JogadorPosicionadoMesaTatica jogadorPosicionadoMesaTatica) {
        return new JogadorPosicionadoResumo(
                jogadorPosicionadoMesaTatica.jogadorId().valor(),
                jogadorPosicionadoMesaTatica.posicao().name(),
                jogadorPosicionadoMesaTatica.coordenada().eixoX(),
                jogadorPosicionadoMesaTatica.coordenada().eixoY(),
                jogadorPosicionadoMesaTatica.ordemNaLinha());
    }

    public record JogadorEscaladoEntrada(long jogadorId, String posicao) {
    }

    public record EscalacaoResumo(long id,
                                  long partidaId,
                                  long timeId,
                                  String formatoEquipe,
                                  String tipoVisualizacao,
                                  String esquemaTatico,
                                  boolean congelada,
                                  List<JogadorEscaladoResumo> titulares,
                                  List<JogadorPosicionadoResumo> titularesPosicionados,
                                  List<Long> reservas) {
    }

    public record VisualizacaoPublicaResumo(String modoExibicao,
                                            List<EscalacaoResumo> escalacoes) {
    }

    public record JogadorEscaladoResumo(long jogadorId, String posicao) {
    }

    public record MesaTaticaResumo(long partidaId,
                                   long timeId,
                                   String esquemaTatico,
                                   List<JogadorPosicionadoResumo> titularesPosicionados,
                                   List<Long> reservas) {
    }

    public record JogadorPosicionadoResumo(long jogadorId,
                                           String posicao,
                                           double eixoX,
                                           double eixoY,
                                           int ordemNaLinha) {
    }
}
