package com.torneios.dominio.participacao.acesso;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class NotificacaoServico {

    private final NotificacaoRepositorio repositorio;

    public NotificacaoServico(NotificacaoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public boolean notificar(NotificacaoId id,
                             UsuarioId usuarioId,
                             CategoriaNotificacao categoria,
                             String titulo,
                             String mensagem,
                             String link) {
        PreferenciasNotificacao preferencias = obterPreferencias(usuarioId);
        if (!preferencias.permite(categoria)) {
            return false;
        }
        repositorio.salvar(new Notificacao(
                id, usuarioId, categoria, titulo, mensagem, link,
                false, false, LocalDateTime.now()));
        return true;
    }

    public List<Notificacao> listar(UsuarioId usuarioId, boolean incluirArquivadas) {
        return repositorio.listarPorUsuario(usuarioId).stream()
                .filter(item -> incluirArquivadas || !item.isArquivada())
                .toList();
    }

    public void marcarComoLida(NotificacaoId id, UsuarioId usuarioId) {
        Notificacao notificacao = obter(id);
        notificacao.marcarComoLida(usuarioId);
        repositorio.salvar(notificacao);
    }

    public void marcarTodasComoLidas(UsuarioId usuarioId) {
        listar(usuarioId, false).stream()
                .filter(item -> !item.isLida())
                .forEach(item -> {
                    item.marcarComoLida(usuarioId);
                    repositorio.salvar(item);
                });
    }

    public void arquivar(NotificacaoId id, UsuarioId usuarioId) {
        Notificacao notificacao = obter(id);
        notificacao.arquivar(usuarioId);
        repositorio.salvar(notificacao);
    }

    public PreferenciasNotificacao obterPreferencias(UsuarioId usuarioId) {
        return repositorio.buscarPreferencias(usuarioId)
                .orElseGet(() -> PreferenciasNotificacao.todasAtivas(usuarioId));
    }

    public PreferenciasNotificacao atualizarPreferencias(
            UsuarioId usuarioId,
            Set<CategoriaNotificacao> categoriasAtivas) {
        PreferenciasNotificacao preferencias = new PreferenciasNotificacao(usuarioId, categoriasAtivas);
        repositorio.salvarPreferencias(preferencias);
        return preferencias;
    }

    private Notificacao obter(NotificacaoId id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Notificacao nao encontrada."));
    }
}
