package com.torneios.dominio.engajamento.feed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    private final List<String> midias;
    private final Set<String> hashtags;
    private final Set<UsuarioId> curtidas;
    private final Map<UsuarioId, TipoReacaoFeed> reacoes;
    private String conteudo;
    private boolean removida;

    private PublicacaoFeed(PublicacaoFeedId id,
                           TorneioId torneioId,
                           TipoPublicacaoFeed tipo,
                           UsuarioId autorId,
                           PartidaId partidaId,
                           String conteudo,
                           Collection<String> hashtags,
                           Collection<String> midias) {
        this.id = Objects.requireNonNull(id, "O id da publicacao e obrigatorio.");
        this.torneioId = torneioId;
        this.tipo = Objects.requireNonNull(tipo, "O tipo da publicacao e obrigatorio.");
        this.autorId = autorId;
        this.partidaId = partidaId;
        this.conteudo = validarConteudo(conteudo);
        this.hashtags = new LinkedHashSet<>(normalizarHashtags(hashtags, conteudo));
        this.midias = new ArrayList<>(normalizarMidias(midias));
        this.curtidas = new LinkedHashSet<>();
        this.reacoes = new LinkedHashMap<>();
        this.removida = false;
    }

    public static PublicacaoFeed comunicadoOficial(PublicacaoFeedId id,
                                                   TorneioId torneioId,
                                                   UsuarioId organizadorId,
                                                   String conteudo) {
        Objects.requireNonNull(torneioId, "O torneio do comunicado e obrigatorio.");
        Objects.requireNonNull(organizadorId, "O organizador do comunicado e obrigatorio.");
        return new PublicacaoFeed(id, torneioId, TipoPublicacaoFeed.COMUNICADO_OFICIAL,
                organizadorId, null, conteudo, List.of(), List.of());
    }

    public static PublicacaoFeed postagemSocial(PublicacaoFeedId id,
                                                UsuarioId autorId,
                                                String conteudo,
                                                Collection<String> hashtags,
                                                Collection<String> midias) {
        Objects.requireNonNull(autorId, "O autor da postagem e obrigatorio.");
        return new PublicacaoFeed(id, null, TipoPublicacaoFeed.POSTAGEM_SOCIAL,
                autorId, null, conteudo, hashtags, midias);
    }

    public static PublicacaoFeed comentario(PublicacaoFeedId id,
                                            TorneioId torneioId,
                                            PartidaId partidaId,
                                            UsuarioId autorId,
                                            String conteudo) {
        Objects.requireNonNull(torneioId, "O torneio do comentario e obrigatorio.");
        Objects.requireNonNull(partidaId, "A partida comentada e obrigatoria.");
        Objects.requireNonNull(autorId, "O autor do comentario e obrigatorio.");
        return new PublicacaoFeed(id, torneioId, TipoPublicacaoFeed.COMENTARIO,
                autorId, partidaId, conteudo, List.of(), List.of());
    }

    public static PublicacaoFeed atualizacaoAutomatica(PublicacaoFeedId id,
                                                       TorneioId torneioId,
                                                       PartidaId partidaId,
                                                       String conteudo) {
        Objects.requireNonNull(torneioId, "O torneio da atualizacao automatica e obrigatorio.");
        Objects.requireNonNull(partidaId, "A partida da atualizacao automatica e obrigatoria.");
        return new PublicacaoFeed(id, torneioId, TipoPublicacaoFeed.ATUALIZACAO_AUTOMATICA,
                null, partidaId, conteudo, List.of(), List.of());
    }

    public PublicacaoFeedId getId() {
        return id;
    }

    public TorneioId getTorneioId() {
        return torneioId;
    }

    public Optional<TorneioId> getTorneioIdOptional() {
        return Optional.ofNullable(torneioId);
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

    public List<String> getMidias() {
        return List.copyOf(midias);
    }

    public Set<String> getHashtags() {
        return Set.copyOf(hashtags);
    }

    public int getQuantidadeCurtidas() {
        return curtidas.size();
    }

    public Map<UsuarioId, TipoReacaoFeed> getReacoes() {
        return Map.copyOf(reacoes);
    }

    public boolean estaRemovida() {
        return removida;
    }

    public void editarConteudo(UsuarioId usuarioId, String novoConteudo) {
        validarAlteracaoManual(usuarioId);
        this.conteudo = validarConteudo(novoConteudo);
        this.hashtags.clear();
        this.hashtags.addAll(normalizarHashtags(this.hashtags, novoConteudo));
    }

    public void remover(UsuarioId usuarioId) {
        validarAlteracaoManual(usuarioId);
        this.removida = true;
    }

    public void curtir(UsuarioId usuarioId) {
        validarInteracao(usuarioId);
        curtidas.add(usuarioId);
    }

    public void removerCurtida(UsuarioId usuarioId) {
        validarInteracao(usuarioId);
        curtidas.remove(usuarioId);
    }

    public void reagir(UsuarioId usuarioId, TipoReacaoFeed tipoReacao) {
        validarInteracao(usuarioId);
        reacoes.put(usuarioId, Objects.requireNonNull(tipoReacao, "O tipo de reacao e obrigatorio."));
    }

    public void removerReacao(UsuarioId usuarioId) {
        validarInteracao(usuarioId);
        reacoes.remove(usuarioId);
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

    private void validarInteracao(UsuarioId usuarioId) {
        Objects.requireNonNull(usuarioId, "O usuario da interacao e obrigatorio.");
        if (removida) {
            throw new OperacaoNaoPermitidaException("Publicacao removida nao pode receber interacao.");
        }
    }

    private static String validarConteudo(String conteudo) {
        if (conteudo == null || conteudo.isBlank()) {
            throw new IllegalArgumentException("O conteudo da publicacao e obrigatorio.");
        }
        return conteudo.trim();
    }

    private static List<String> normalizarHashtags(Collection<String> hashtags, String conteudo) {
        Set<String> normalizadas = new LinkedHashSet<>();
        if (hashtags != null) {
            hashtags.stream()
                    .map(PublicacaoFeed::normalizarHashtag)
                    .filter(hashtag -> !hashtag.isBlank())
                    .forEach(normalizadas::add);
        }
        for (String termo : conteudo.split("\\s+")) {
            if (termo.startsWith("#") && termo.length() > 1) {
                normalizadas.add(normalizarHashtag(termo));
            }
        }
        return List.copyOf(normalizadas);
    }

    private static String normalizarHashtag(String hashtag) {
        return hashtag.replace("#", "").trim().toLowerCase();
    }

    private static List<String> normalizarMidias(Collection<String> midias) {
        if (midias == null) {
            return List.of();
        }
        return midias.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(midia -> !midia.isBlank())
                .toList();
    }
}
