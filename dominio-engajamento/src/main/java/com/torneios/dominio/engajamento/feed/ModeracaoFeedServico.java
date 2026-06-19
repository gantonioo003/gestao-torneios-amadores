package com.torneios.dominio.engajamento.feed;

import java.util.List;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class ModeracaoFeedServico {
    private final DenunciaRepositorio repositorio;

    public ModeracaoFeedServico(DenunciaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Denuncia denunciar(DenunciaId id, UsuarioId denuncianteId, TipoAlvoDenuncia tipoAlvo,
                              long alvoId, String motivo) {
        if (repositorio.existePendente(denuncianteId, tipoAlvo, alvoId)) {
            throw new RegraDeNegocioException("Voce ja possui uma denuncia pendente para este conteudo.");
        }
        Denuncia denuncia = new Denuncia(id, denuncianteId, tipoAlvo, alvoId, motivo);
        repositorio.salvar(denuncia);
        return denuncia;
    }

    public List<Denuncia> listarPendentes() {
        return repositorio.listarPendentes();
    }

    public Denuncia marcarAnalisada(DenunciaId id) {
        Denuncia denuncia = repositorio.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Denuncia nao encontrada."));
        denuncia.marcarAnalisada();
        repositorio.salvar(denuncia);
        return denuncia;
    }
}
