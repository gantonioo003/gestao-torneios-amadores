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
        Objects.requireNonNull(usuarioId, "O usuario do palpite e obrigatorio.");
        return registrarOuAtualizarPorIdentificador(
                novoIdSeNecessario,
                "USUARIO:" + usuarioId.valor(),
                () -> new Palpite(novoIdSeNecessario, usuarioId, eventoAlvo, opcao),
                eventoAlvo,
                opcao);
    }

    public Palpite registrarOuAtualizarComoVisitante(PalpiteId novoIdSeNecessario,
                                                     String visitanteId,
                                                     EventoAlvo eventoAlvo,
                                                     OpcaoPalpite opcao) {
        if (visitanteId == null || visitanteId.isBlank()) {
            throw new RegraDeNegocioException("O identificador do visitante e obrigatorio.");
        }
        String identificadorVotante = "VISITANTE:" + visitanteId.trim();
        return registrarOuAtualizarPorIdentificador(
                novoIdSeNecessario,
                identificadorVotante,
                () -> new Palpite(novoIdSeNecessario, visitanteId, eventoAlvo, opcao),
                eventoAlvo,
                opcao);
    }

    private Palpite registrarOuAtualizarPorIdentificador(PalpiteId novoIdSeNecessario,
                                                         String identificadorVotante,
                                                         java.util.function.Supplier<Palpite> novoPalpite,
                                                         EventoAlvo eventoAlvo,
                                                         OpcaoPalpite opcao) {
        validarJanelaAberta(eventoAlvo);
        validarOpcao(eventoAlvo, opcao);

        Optional<Palpite> existente = palpiteRepositorio.buscarPorVotanteEEvento(identificadorVotante, eventoAlvo);
        Palpite palpite;
        if (existente.isPresent()) {
            palpite = existente.get();
            palpite.alterarOpcao(opcao);
        } else {
            palpite = novoPalpite.get();
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
