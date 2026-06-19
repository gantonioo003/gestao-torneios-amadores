package com.torneios.dominio.participacao.solicitacao;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.time.Time;
import com.torneios.dominio.participacao.time.TimeRepositorio;

public class SolicitacaoParticipacaoServico {

    private final SolicitacaoParticipacaoRepositorio solicitacaoParticipacaoRepositorio;
    private final TimeRepositorio timeRepositorio;
    private final AutenticacaoServico autenticacaoServico;
    private final PoliticaParticipacaoTorneio politicaParticipacaoTorneio;

    public SolicitacaoParticipacaoServico(SolicitacaoParticipacaoRepositorio solicitacaoParticipacaoRepositorio,
                                          TimeRepositorio timeRepositorio,
                                          AutenticacaoServico autenticacaoServico,
                                          PoliticaParticipacaoTorneio politicaParticipacaoTorneio) {
        this.solicitacaoParticipacaoRepositorio = Objects.requireNonNull(solicitacaoParticipacaoRepositorio,
                "O repositorio de solicitacoes e obrigatorio.");
        this.timeRepositorio = Objects.requireNonNull(timeRepositorio, "O repositorio de times e obrigatorio.");
        this.autenticacaoServico = Objects.requireNonNull(autenticacaoServico,
                "O servico de autenticacao e obrigatorio.");
        this.politicaParticipacaoTorneio = Objects.requireNonNull(politicaParticipacaoTorneio,
                "A politica de participacao do torneio e obrigatoria.");
    }

    public SolicitacaoParticipacao solicitarParticipacao(SolicitacaoParticipacaoId solicitacaoId,
                                                         UsuarioId usuarioId,
                                                         TimeId timeId,
                                                         TorneioId torneioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = timeRepositorio.buscarPorId(timeId)
                .orElseThrow(() -> new RegraDeNegocioException("E necessario possuir um time cadastrado."));
        if (!time.getResponsavel().equals(usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "A solicitacao de participacao deve ser realizada pelo responsavel do time.");
        }
        if (!politicaParticipacaoTorneio.aceitaSolicitacoes(torneioId)) {
            throw new OperacaoNaoPermitidaException("O torneio nao aceita novas solicitacoes de participacao.");
        }
        validarNovaSolicitacao(timeId, torneioId);

        SolicitacaoParticipacao solicitacao = new SolicitacaoParticipacao(solicitacaoId, usuarioId, timeId, torneioId);
        solicitacaoParticipacaoRepositorio.salvar(solicitacao);
        return solicitacao;
    }

    public SolicitacaoParticipacao convidarTime(SolicitacaoParticipacaoId solicitacaoId,
                                                UsuarioId organizadorId,
                                                TimeId timeId,
                                                TorneioId torneioId) {
        autenticacaoServico.exigirAutenticacao(organizadorId);
        validarOrganizador(torneioId, organizadorId);
        validarTorneioNaoIniciado(torneioId);
        obterTime(timeId);
        validarNovaSolicitacao(timeId, torneioId);
        SolicitacaoParticipacao convite = new SolicitacaoParticipacao(
                solicitacaoId, organizadorId, timeId, torneioId, TipoSolicitacaoParticipacao.CONVITE);
        solicitacaoParticipacaoRepositorio.salvar(convite);
        return convite;
    }

    public SolicitacaoParticipacao aprovarSolicitacao(SolicitacaoParticipacaoId solicitacaoId, UsuarioId usuarioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        SolicitacaoParticipacao solicitacao = obterSolicitacao(solicitacaoId);
        validarAvaliador(solicitacao, usuarioId);
        validarTorneioNaoIniciado(solicitacao.getTorneioId());
        solicitacao.aprovar();
        solicitacaoParticipacaoRepositorio.salvar(solicitacao);
        politicaParticipacaoTorneio.adicionarParticipante(solicitacao.getTorneioId(), solicitacao.getTimeId());
        Time time = obterTime(solicitacao.getTimeId());
        time.vincularAoTorneio(solicitacao.getTorneioId());
        timeRepositorio.salvar(time);
        return solicitacao;
    }

    public void rejeitarSolicitacao(SolicitacaoParticipacaoId solicitacaoId, UsuarioId usuarioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        SolicitacaoParticipacao solicitacao = obterSolicitacao(solicitacaoId);
        validarAvaliador(solicitacao, usuarioId);
        validarTorneioNaoIniciado(solicitacao.getTorneioId());
        solicitacao.rejeitar();
        solicitacaoParticipacaoRepositorio.salvar(solicitacao);
    }

    public void removerParticipanteAprovado(TorneioId torneioId, TimeId timeId, UsuarioId organizadorId) {
        autenticacaoServico.exigirAutenticacao(organizadorId);
        validarOrganizador(torneioId, organizadorId);
        validarTorneioNaoIniciado(torneioId);
        Time time = obterTime(timeId);
        if (!time.estaVinculadoAoTorneio(torneioId)) {
            throw new RegraDeNegocioException("O time informado nao esta aprovado no torneio.");
        }
        time.removerVinculoTorneio(torneioId);
        timeRepositorio.salvar(time);
        politicaParticipacaoTorneio.removerParticipante(torneioId, timeId);
    }

    public List<SolicitacaoParticipacao> acompanharCandidaturas(UsuarioId usuarioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        return solicitacaoParticipacaoRepositorio.listarPorSolicitante(usuarioId);
    }

    public List<SolicitacaoParticipacao> acompanharTorneio(TorneioId torneioId, UsuarioId organizadorId) {
        autenticacaoServico.exigirAutenticacao(organizadorId);
        validarOrganizador(torneioId, organizadorId);
        return solicitacaoParticipacaoRepositorio.listarPorTorneio(torneioId);
    }

    public List<SolicitacaoParticipacao> acompanharTime(TimeId timeId, UsuarioId usuarioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        Time time = obterTime(timeId);
        if (!time.getResponsavel().equals(usuarioId)) {
            throw new OperacaoNaoPermitidaException("Apenas o treinador responsavel pode acompanhar o time.");
        }
        return solicitacaoParticipacaoRepositorio.listarPorTime(timeId);
    }

    public void cancelarCandidatura(SolicitacaoParticipacaoId solicitacaoId, UsuarioId usuarioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        SolicitacaoParticipacao solicitacao = obterSolicitacao(solicitacaoId);
        solicitacao.cancelar(usuarioId);
        solicitacaoParticipacaoRepositorio.salvar(solicitacao);
    }

    public List<SolicitacaoParticipacao> listarPendentesParaAvaliacao(TorneioId torneioId, UsuarioId organizadorId) {
        autenticacaoServico.exigirAutenticacao(organizadorId);
        validarOrganizador(torneioId, organizadorId);
        return solicitacaoParticipacaoRepositorio.listarPendentesPorTorneio(torneioId);
    }

    public SolicitacaoParticipacao obterSolicitacao(SolicitacaoParticipacaoId solicitacaoId) {
        return solicitacaoParticipacaoRepositorio.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Solicitacao de participacao nao encontrada."));
    }

    public UsuarioId organizadorDoTorneio(TorneioId torneioId) {
        return politicaParticipacaoTorneio.organizadorDoTorneio(torneioId);
    }

    public UsuarioId responsavelDoTime(TimeId timeId) {
        return obterTime(timeId).getResponsavel();
    }

    private void validarOrganizador(TorneioId torneioId, UsuarioId organizadorId) {
        if (!politicaParticipacaoTorneio.usuarioEhOrganizador(torneioId, organizadorId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o organizador do torneio pode avaliar solicitacoes de participacao.");
        }
    }

    private void validarAvaliador(SolicitacaoParticipacao solicitacao, UsuarioId usuarioId) {
        if (solicitacao.getTipo() == TipoSolicitacaoParticipacao.CANDIDATURA) {
            validarOrganizador(solicitacao.getTorneioId(), usuarioId);
            return;
        }
        Time time = obterTime(solicitacao.getTimeId());
        if (!time.getResponsavel().equals(usuarioId)) {
            throw new OperacaoNaoPermitidaException("Apenas o treinador do time pode responder ao convite.");
        }
    }

    private void validarNovaSolicitacao(TimeId timeId, TorneioId torneioId) {
        if (politicaParticipacaoTorneio.possuiParticipante(torneioId, timeId)) {
            throw new RegraDeNegocioException("O time ja participa deste torneio.");
        }
        if (solicitacaoParticipacaoRepositorio.existePendentePorTimeETorneio(timeId, torneioId)) {
            throw new RegraDeNegocioException("Ja existe uma negociacao pendente entre o time e o torneio.");
        }
    }

    private void validarTorneioNaoIniciado(TorneioId torneioId) {
        if (politicaParticipacaoTorneio.torneioIniciado(torneioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Nao e permitido alterar participantes apos o inicio do torneio.");
        }
    }

    private Time obterTime(TimeId timeId) {
        return timeRepositorio.buscarPorId(timeId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Time nao encontrado."));
    }
}
