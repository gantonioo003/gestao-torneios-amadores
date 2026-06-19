package com.torneios.dominio.participacao.acesso;

import java.time.LocalDateTime;
import java.util.Objects;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;

public class Notificacao {

    private final NotificacaoId id;
    private final UsuarioId usuarioId;
    private final CategoriaNotificacao categoria;
    private final String titulo;
    private final String mensagem;
    private final String link;
    private final LocalDateTime criadaEm;
    private boolean lida;
    private boolean arquivada;

    public Notificacao(NotificacaoId id,
                       UsuarioId usuarioId,
                       CategoriaNotificacao categoria,
                       String titulo,
                       String mensagem,
                       String link,
                       boolean lida,
                       boolean arquivada,
                       LocalDateTime criadaEm) {
        this.id = Objects.requireNonNull(id, "O id da notificacao e obrigatorio.");
        this.usuarioId = Objects.requireNonNull(usuarioId, "O usuario da notificacao e obrigatorio.");
        this.categoria = Objects.requireNonNull(categoria, "A categoria da notificacao e obrigatoria.");
        this.titulo = validarTexto(titulo, "O titulo da notificacao e obrigatorio.", 160);
        this.mensagem = validarTexto(mensagem, "A mensagem da notificacao e obrigatoria.", 500);
        this.link = validarTexto(link, "O link da notificacao e obrigatorio.", 500);
        this.lida = lida;
        this.arquivada = arquivada;
        this.criadaEm = Objects.requireNonNull(criadaEm, "A data da notificacao e obrigatoria.");
    }

    public NotificacaoId getId() {
        return id;
    }

    public UsuarioId getUsuarioId() {
        return usuarioId;
    }

    public CategoriaNotificacao getCategoria() {
        return categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getLink() {
        return link;
    }

    public boolean isLida() {
        return lida;
    }

    public boolean isArquivada() {
        return arquivada;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void marcarComoLida(UsuarioId usuario) {
        exigirProprietario(usuario);
        lida = true;
    }

    public void arquivar(UsuarioId usuario) {
        exigirProprietario(usuario);
        arquivada = true;
        lida = true;
    }

    private void exigirProprietario(UsuarioId usuario) {
        if (!usuarioId.equals(usuario)) {
            throw new OperacaoNaoPermitidaException(
                    "A notificacao so pode ser alterada pelo usuario destinatario.");
        }
    }

    private String validarTexto(String valor, String mensagemErro, int limite) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagemErro);
        }
        String normalizado = valor.trim();
        if (normalizado.length() > limite) {
            throw new IllegalArgumentException("O conteudo da notificacao ultrapassa o limite permitido.");
        }
        return normalizado;
    }
}
