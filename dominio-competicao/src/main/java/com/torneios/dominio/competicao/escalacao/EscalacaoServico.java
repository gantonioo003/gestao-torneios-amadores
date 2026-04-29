package com.torneios.dominio.competicao.escalacao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.torneios.dominio.compartilhado.enumeracao.EsquemaTatico;
import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class EscalacaoServico {

    private final EscalacaoRepositorio escalacaoRepositorio;
    private final ConsultaSuporteEscalacao consultaSuporte;

    public EscalacaoServico(EscalacaoRepositorio escalacaoRepositorio,
                            ConsultaSuporteEscalacao consultaSuporte) {
        this.escalacaoRepositorio = Objects.requireNonNull(escalacaoRepositorio,
                "O repositorio de escalacao e obrigatorio.");
        this.consultaSuporte = Objects.requireNonNull(consultaSuporte,
                "A consulta de suporte para escalacao e obrigatoria.");
    }

    public Escalacao definirEscalacaoPorResponsavel(EscalacaoId id,
                                                     PartidaId partidaId,
                                                     TimeId timeId,
                                                     UsuarioId usuarioId,
                                                     EsquemaTatico esquemaTatico,
                                                     List<JogadorEscalado> titulares,
                                                     List<JogadorId> reservas) {
        validarUsuarioResponsavel(timeId, usuarioId);
        return criarOuAtualizar(id, partidaId, timeId, esquemaTatico, titulares, reservas);
    }

    public Escalacao definirEscalacaoPorTecnico(EscalacaoId id,
                                                 PartidaId partidaId,
                                                 TimeId timeId,
                                                 TecnicoId tecnicoId,
                                                 EsquemaTatico esquemaTatico,
                                                 List<JogadorEscalado> titulares,
                                                 List<JogadorId> reservas) {
        validarTecnicoAssociadoAoTime(timeId, tecnicoId);
        return criarOuAtualizar(id, partidaId, timeId, esquemaTatico, titulares, reservas);
    }

    public Escalacao obterEscalacao(PartidaId partidaId, TimeId timeId) {
        return escalacaoRepositorio.buscarPorPartidaETime(partidaId, timeId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Escalacao nao encontrada para a partida e o time informados."));
    }

    public List<Escalacao> listarPorPartida(PartidaId partidaId) {
        return escalacaoRepositorio.listarPorPartida(partidaId);
    }

    public void congelarEscalacoesDaPartida(PartidaId partidaId) {
        for (Escalacao escalacao : escalacaoRepositorio.listarPorPartida(partidaId)) {
            escalacao.congelar();
            escalacaoRepositorio.salvar(escalacao);
        }
    }

    private Escalacao criarOuAtualizar(EscalacaoId id,
                                        PartidaId partidaId,
                                        TimeId timeId,
                                        EsquemaTatico esquemaTatico,
                                        List<JogadorEscalado> titulares,
                                        List<JogadorId> reservas) {
        garantirPartidaNaoIniciada(partidaId);
        validarJogadoresDoElenco(timeId, titulares, reservas);

        FormatoEquipe formatoEquipe = consultaSuporte.obterFormatoEquipeDaPartida(partidaId);
        Optional<Escalacao> existente = escalacaoRepositorio.buscarPorPartidaETime(partidaId, timeId);
        Escalacao escalacao;
        if (existente.isPresent()) {
            escalacao = existente.get();
            escalacao.atualizar(esquemaTatico, titulares, reservas);
        } else {
            escalacao = new Escalacao(id, partidaId, timeId, formatoEquipe,
                    esquemaTatico, titulares, reservas);
        }
        escalacaoRepositorio.salvar(escalacao);
        return escalacao;
    }

    private void garantirPartidaNaoIniciada(PartidaId partidaId) {
        if (consultaSuporte.partidaIniciada(partidaId)) {
            throw new OperacaoNaoPermitidaException(
                    "Nao e permitido alterar a escalacao apos o inicio da partida.");
        }
    }

    private void validarUsuarioResponsavel(TimeId timeId, UsuarioId usuarioId) {
        Objects.requireNonNull(usuarioId, "O usuario responsavel pela escalacao e obrigatorio.");
        if (!consultaSuporte.usuarioEhResponsavelDoTime(timeId, usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o usuario responsavel pelo time pode definir a escalacao.");
        }
    }

    private void validarTecnicoAssociadoAoTime(TimeId timeId, TecnicoId tecnicoId) {
        Objects.requireNonNull(tecnicoId, "O tecnico responsavel pela escalacao e obrigatorio.");
        if (!consultaSuporte.tecnicoEstaAssociadoAoTime(timeId, tecnicoId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o tecnico associado ao time pode definir a escalacao.");
        }
    }

    private void validarJogadoresDoElenco(TimeId timeId,
                                          List<JogadorEscalado> titulares,
                                          List<JogadorId> reservas) {
        List<JogadorId> elenco = consultaSuporte.listarElencoDoTime(timeId);
        for (JogadorEscalado titular : titulares) {
            if (!elenco.contains(titular.jogadorId())) {
                throw new RegraDeNegocioException(
                        "O jogador titular " + titular.jogadorId().valor()
                                + " nao pertence ao elenco do time.");
            }
        }
        if (reservas != null) {
            for (JogadorId reserva : reservas) {
                if (!elenco.contains(reserva)) {
                    throw new RegraDeNegocioException(
                            "O jogador reserva " + reserva.valor()
                                    + " nao pertence ao elenco do time.");
                }
            }
        }
    }
}
