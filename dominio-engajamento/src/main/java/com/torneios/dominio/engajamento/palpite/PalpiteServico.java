package com.torneios.dominio.engajamento.palpite;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class PalpiteServico {

    private final PalpiteRepositorio palpiteRepositorio;
    private final ConsultaSuportePalpite consultaSuporte;

    public PalpiteServico(PalpiteRepositorio palpiteRepositorio,
                          ConsultaSuportePalpite consultaSuporte) {
        this.palpiteRepositorio = Objects.requireNonNull(palpiteRepositorio,
                "O repositorio de palpites e obrigatorio.");
        this.consultaSuporte = Objects.requireNonNull(consultaSuporte,
                "A consulta de suporte para palpites e obrigatoria.");
    }

    public Palpite registrarOuAtualizar(PalpiteId novoIdSeNecessario,
                                         UsuarioId usuarioId,
                                         EventoAlvo eventoAlvo,
                                         OpcaoPalpite opcao) {
        validarUsuario(usuarioId);
        validarJanelaAberta(eventoAlvo);
        validarOpcao(eventoAlvo, opcao);

        Optional<Palpite> existente = palpiteRepositorio.buscarPorUsuarioEEvento(usuarioId, eventoAlvo);
        Palpite palpite;
        if (existente.isPresent()) {
            palpite = existente.get();
            palpite.alterarOpcao(opcao);
        } else {
            palpite = new Palpite(novoIdSeNecessario, usuarioId, eventoAlvo, opcao);
        }
        palpiteRepositorio.salvar(palpite);
        return palpite;
    }

    public PercentuaisPalpite obterPercentuais(EventoAlvo eventoAlvo) {
        return PercentuaisPalpite.calcular(eventoAlvo, palpiteRepositorio.listarPorEvento(eventoAlvo));
    }

    public List<Palpite> apurar(EventoAlvo eventoAlvo, long resultadoReal) {
        garantirEventoConcluido(eventoAlvo);
        List<Palpite> palpites = palpiteRepositorio.listarPorEvento(eventoAlvo);
        for (Palpite palpite : palpites) {
            if (!palpite.estaApurado()) {
                palpite.apurar(resultadoReal);
                palpiteRepositorio.salvar(palpite);
            }
        }
        return palpites;
    }

    private void validarUsuario(UsuarioId usuarioId) {
        Objects.requireNonNull(usuarioId, "O usuario do palpite e obrigatorio.");
        if (!consultaSuporte.usuarioEstaAutenticado(usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas usuarios autenticados podem registrar palpites.");
        }
    }

    private void validarJanelaAberta(EventoAlvo eventoAlvo) {
        if (eventoAlvo.ehPorPartida()) {
            if (consultaSuporte.partidaIniciada(eventoAlvo.getPartidaId())) {
                throw new OperacaoNaoPermitidaException(
                        "A janela de votacao do palpite ja foi encerrada para esta partida.");
            }
        } else {
            if (consultaSuporte.torneioIniciado(eventoAlvo.getTorneioId())) {
                throw new OperacaoNaoPermitidaException(
                        "A janela de votacao do palpite ja foi encerrada para este torneio.");
            }
        }
    }

    private void validarOpcao(EventoAlvo eventoAlvo, OpcaoPalpite opcao) {
        Objects.requireNonNull(opcao, "A opcao do palpite e obrigatoria.");
        if (!consultaSuporte.opcaoValidaParaEvento(eventoAlvo, opcao)) {
            throw new RegraDeNegocioException(
                    "A opcao informada nao e valida para o evento alvo do palpite.");
        }
    }

    private void garantirEventoConcluido(EventoAlvo eventoAlvo) {
        boolean concluido = eventoAlvo.ehPorPartida()
                ? consultaSuporte.partidaEncerrada(eventoAlvo.getPartidaId())
                : consultaSuporte.torneioFinalizado(eventoAlvo.getTorneioId());
        if (!concluido) {
            throw new OperacaoNaoPermitidaException(
                    "A apuracao so pode ser feita apos a conclusao do evento alvo.");
        }
    }
}
