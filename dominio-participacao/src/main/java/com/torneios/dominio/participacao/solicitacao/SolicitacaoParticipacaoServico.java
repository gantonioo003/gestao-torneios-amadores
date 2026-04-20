package com.torneios.dominio.participacao.solicitacao;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.time.TimeRepositorio;

public class SolicitacaoParticipacaoServico {

    private final SolicitacaoParticipacaoRepositorio solicitacaoParticipacaoRepositorio;
    private final TimeRepositorio timeRepositorio;
    private final AutenticacaoServico autenticacaoServico;

    public SolicitacaoParticipacaoServico(SolicitacaoParticipacaoRepositorio solicitacaoParticipacaoRepositorio,
                                          TimeRepositorio timeRepositorio,
                                          AutenticacaoServico autenticacaoServico) {
        this.solicitacaoParticipacaoRepositorio = Objects.requireNonNull(solicitacaoParticipacaoRepositorio,
                "O repositorio de solicitacoes e obrigatorio.");
        this.timeRepositorio = Objects.requireNonNull(timeRepositorio, "O repositorio de times e obrigatorio.");
        this.autenticacaoServico = Objects.requireNonNull(autenticacaoServico,
                "O servico de autenticacao e obrigatorio.");
    }

    public SolicitacaoParticipacao solicitarParticipacao(SolicitacaoParticipacaoId solicitacaoId,
                                                         UsuarioId usuarioId,
                                                         TimeId timeId,
                                                         TorneioId torneioId) {
        autenticacaoServico.exigirAutenticacao(usuarioId);
        timeRepositorio.buscarPorId(timeId)
                .orElseThrow(() -> new IllegalStateException("E necessario possuir um time cadastrado."));

        SolicitacaoParticipacao solicitacao = new SolicitacaoParticipacao(solicitacaoId, usuarioId, timeId, torneioId);
        solicitacaoParticipacaoRepositorio.salvar(solicitacao);
        return solicitacao;
    }

    public void aprovarSolicitacao(SolicitacaoParticipacaoId solicitacaoId) {
        SolicitacaoParticipacao solicitacao = obterSolicitacao(solicitacaoId);
        solicitacao.aprovar();
        solicitacaoParticipacaoRepositorio.salvar(solicitacao);
    }

    public void rejeitarSolicitacao(SolicitacaoParticipacaoId solicitacaoId) {
        SolicitacaoParticipacao solicitacao = obterSolicitacao(solicitacaoId);
        solicitacao.rejeitar();
        solicitacaoParticipacaoRepositorio.salvar(solicitacao);
    }

    public List<SolicitacaoParticipacao> listarPendentes(TorneioId torneioId) {
        return solicitacaoParticipacaoRepositorio.listarPendentesPorTorneio(torneioId);
    }

    public SolicitacaoParticipacao obterSolicitacao(SolicitacaoParticipacaoId solicitacaoId) {
        return solicitacaoParticipacaoRepositorio.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitacao de participacao nao encontrada."));
    }
}
