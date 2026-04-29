package com.torneios.dominio.engajamento.feed;

import java.util.Objects;
import java.util.Optional;

import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class PublicacaoFeed {

    private final PublicacaoFeedId id;
    private final TorneioId torneioId;
    private final TipoPublicacaoFeed tipo;
    private final UsuarioId autorId;
    private final PartidaId partidaId;
    private String conteudo;
    private boolean removida;

    private PublicacaoFeed(PublicacaoFeedId id,
                           TorneioId torneioId,
                           TipoPublicacaoFeed tipo,
                           UsuarioId autorId,
                           PartidaId partidaId,
                           String conteudo) {
        this.id = Objects.requireNonNull(id, "O id da publicacao e obrigatorio.");
        this.torneioId = Objects.requireNonNull(torneioId, "O torneio da publicacao e obrigatorio.");
        this.tipo = Objects.requireNonNull(tipo, "O tipo da publicacao e obrigatorio.");
        this.autorId = autorId;
        this.partidaId = partidaId;
        this.conteudo = validarConteudo(conteudo);
        this.removida = false;
    }

    public static PublicacaoFeed comunicadoOficial(PublicacaoFeedId id,
                                                   TorneioId torneioId,
                                                   UsuarioId organizadorId,
                                                   String conteudo) {
        Objects.requireNonNull(organizadorId, "O organizador do comunicado e obrigatorio.");
        return new PublicacaoFeed(id, torneioId, TipoPublicacaoFeed.COMUNICADO_OFICIAL,
                organizadorId, null, conteudo);
    }

    public static PublicacaoFeed comentario(PublicacaoFeedId id,
                                            TorneioId torneioId,
                                            PartidaId partidaId,
                                            UsuarioId autorId,
                                            String conteudo) {
        Objects.requireNonNull(partidaId, "A partida comentada e obrigatoria.");
        Objects.requireNonNull(autorId, "O autor do comentario e obrigatorio.");
        return new PublicacaoFeed(id, torneioId, TipoPublicacaoFeed.COMENTARIO,
                autorId, partidaId, conteudo);
    }

    public static PublicacaoFeed atualizacaoAutomatica(PublicacaoFeedId id,
                                                       TorneioId torneioId,
                                                       PartidaId partidaId,
                                                       String conteudo) {
        Objects.requireNonNull(partidaId, "A partida da atualizacao automatica e obrigatoria.");
        return new PublicacaoFeed(id, torneioId, TipoPublicacaoFeed.ATUALIZACAO_AUTOMATICA,
                null, partidaId, conteudo);
    }

    public PublicacaoFeedId getId() {
        return id;
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public TipoPublicacaoFeed getTipo() {
        return tipo;
    }

    public Optional<UsuarioId> getAutorId() {
        return Optional.ofNullable(autorId);
    }

    public Optional<PartidaId> getPartidaId() {
        return Optional.ofNullable(partidaId);
    }

    public String getConteudo() {
        return conteudo;
    }

    public boolean estaRemovida() {
        return removida;
    }

    public void editarConteudo(UsuarioId usuarioId, String novoConteudo) {
        validarAlteracaoManual(usuarioId);
        this.conteudo = validarConteudo(novoConteudo);
    }

    public void remover(UsuarioId usuarioId) {
        validarAlteracaoManual(usuarioId);
        this.removida = true;
    }

    private void validarAlteracaoManual(UsuarioId usuarioId) {
        if (tipo == TipoPublicacaoFeed.ATUALIZACAO_AUTOMATICA) {
            throw new OperacaoNaoPermitidaException(
                    "Atualizacoes automaticas do feed nao podem ser editadas manualmente.");
        }
        if (removida) {
            throw new OperacaoNaoPermitidaException("Publicacao removida nao pode ser alterada.");
        }
        if (!Objects.equals(autorId, usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o autor da publicacao pode altera-la.");
        }
    }

    private static String validarConteudo(String conteudo) {
        if (conteudo == null || conteudo.isBlank()) {
            throw new IllegalArgumentException("O conteudo da publicacao e obrigatorio.");
        }
        return conteudo.trim();
    }
}
