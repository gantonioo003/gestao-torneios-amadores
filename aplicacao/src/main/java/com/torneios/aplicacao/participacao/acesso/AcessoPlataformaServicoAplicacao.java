package com.torneios.aplicacao.participacao.acesso;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.AcessoGerenciamentoTorneioServico;
import com.torneios.dominio.participacao.acesso.TorneioDisponivel;
import com.torneios.dominio.participacao.acesso.VisualizacaoTorneioServico;

/**
 * Casos de uso iniciais da plataforma, cobrindo navegacao publica
 * e verificacao de acesso autenticado.
 */
public class AcessoPlataformaServicoAplicacao {

    private final VisualizacaoTorneioServico visualizacaoTorneioServico;
    private final AcessoGerenciamentoTorneioServico acessoGerenciamentoTorneioServico;

    public AcessoPlataformaServicoAplicacao(VisualizacaoTorneioServico visualizacaoTorneioServico,
                                            AcessoGerenciamentoTorneioServico acessoGerenciamentoTorneioServico) {
        notNull(visualizacaoTorneioServico, "O servico de visualizacao e obrigatorio.");
        notNull(acessoGerenciamentoTorneioServico, "O servico de acesso e obrigatorio.");
        this.visualizacaoTorneioServico = visualizacaoTorneioServico;
        this.acessoGerenciamentoTorneioServico = acessoGerenciamentoTorneioServico;
    }

    public List<TorneioDisponivelResumo> visualizarTorneiosDisponiveis() {
        return visualizacaoTorneioServico.visualizarTorneiosDisponiveis().stream()
                .map(this::converterTorneioDisponivel)
                .toList();
    }

    public boolean existemTorneiosDisponiveis() {
        return visualizacaoTorneioServico.existemTorneiosDisponiveis();
    }

    public boolean podeAcessarCriacaoTorneio(Long usuarioId) {
        return acessoGerenciamentoTorneioServico.podeAcessarCriacaoTorneio(usuarioId == null ? null : new UsuarioId(usuarioId));
    }

    public boolean podeAcessarGerenciamentoTorneios(Long usuarioId) {
        return acessoGerenciamentoTorneioServico.podeAcessarGerenciamentoTorneios(
                usuarioId == null ? null : new UsuarioId(usuarioId));
    }

    private TorneioDisponivelResumo converterTorneioDisponivel(TorneioDisponivel torneioDisponivel) {
        return new TorneioDisponivelResumo(
                torneioDisponivel.id().valor(),
                torneioDisponivel.nome(),
                torneioDisponivel.aceitaSolicitacoes());
    }

    public record TorneioDisponivelResumo(long id, String nome, boolean aceitaSolicitacoes) {
    }
}
