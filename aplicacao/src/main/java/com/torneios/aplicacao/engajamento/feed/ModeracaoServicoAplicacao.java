package com.torneios.aplicacao.engajamento.feed;

import java.time.LocalDateTime;
import java.util.List;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.feed.Denuncia;
import com.torneios.dominio.engajamento.feed.DenunciaId;
import com.torneios.dominio.engajamento.feed.ModeracaoFeedServico;
import com.torneios.dominio.engajamento.feed.TipoAlvoDenuncia;
import com.torneios.aplicacao.participacao.conta.ContaRepositorioAplicacao;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;

public class ModeracaoServicoAplicacao {
    private final ModeracaoFeedServico servico;
    private final ContaRepositorioAplicacao contas;

    public ModeracaoServicoAplicacao(ModeracaoFeedServico servico, ContaRepositorioAplicacao contas) {
        this.servico = servico;
        this.contas = contas;
    }

    public DenunciaResumo denunciar(long id, long usuarioId, String tipoAlvo, long alvoId, String motivo) {
        return converter(servico.denunciar(
                new DenunciaId(id),
                new UsuarioId(usuarioId),
                TipoAlvoDenuncia.valueOf(tipoAlvo),
                alvoId,
                motivo));
    }

    public List<DenunciaResumo> listarPendentes(long moderadorId) {
        exigirModerador(moderadorId);
        return servico.listarPendentes().stream().map(this::converter).toList();
    }

    public DenunciaResumo marcarAnalisada(long id, long moderadorId) {
        exigirModerador(moderadorId);
        return converter(servico.marcarAnalisada(new DenunciaId(id)));
    }

    private void exigirModerador(long usuarioId) {
        boolean permitido = contas.pesquisarPorId(usuarioId)
                .map(conta -> conta.isPodeCriarTorneio())
                .orElse(false);
        if (!permitido) {
            throw new OperacaoNaoPermitidaException("Apenas contas responsaveis pela moderacao podem analisar denuncias.");
        }
    }

    private DenunciaResumo converter(Denuncia denuncia) {
        return new DenunciaResumo(
                denuncia.getId().valor(),
                denuncia.getDenuncianteId().valor(),
                denuncia.getTipoAlvo().name(),
                denuncia.getAlvoId(),
                denuncia.getMotivo(),
                denuncia.getStatus().name(),
                denuncia.getCriadaEm());
    }

    public record DenunciaResumo(long id, long denuncianteId, String tipoAlvo, long alvoId,
                                 String motivo, String status, LocalDateTime criadaEm) {}
}
