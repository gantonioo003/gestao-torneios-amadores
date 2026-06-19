package com.torneios.apresentacao.engajamento;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.engajamento.feed.FeedServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/feed")
class FeedControlador {

    @Autowired
    FeedServicoAplicacao feedServicoAplicacao;

    @RequestMapping(method = POST, path = "publicar-comunicado")
    FeedServicoAplicacao.PublicacaoResumo publicarComunicado(@RequestBody ComunicadoDto dto, HttpSession sessao) {
        return feedServicoAplicacao.publicarComunicado(
                System.currentTimeMillis(),
                dto.torneioId,
                SessaoUsuario.exigirUsuarioId(sessao),
                dto.conteudo);
    }

    @RequestMapping(method = POST, path = "publicar-social")
    FeedServicoAplicacao.PublicacaoResumo publicarSocial(@RequestBody PublicacaoSocialDto dto, HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        String tipoIdentidade = dto.tipoIdentidade == null ? "USUARIO" : dto.tipoIdentidade;
        long identidadeId = "USUARIO".equals(tipoIdentidade)
                ? usuarioId
                : dto.identidadeId == null ? usuarioId : dto.identidadeId;
        return feedServicoAplicacao.publicar(
                System.currentTimeMillis(),
                usuarioId,
                tipoIdentidade,
                identidadeId,
                dto.conteudo,
                dto.hashtags,
                dto.midias);
    }

    @RequestMapping(method = POST, path = "{id}/comentar")
    FeedServicoAplicacao.PublicacaoResumo comentar(@PathVariable long id,
                                                   @RequestBody ComentarioDto dto,
                                                   HttpSession sessao) {
        return feedServicoAplicacao.comentar(
                System.currentTimeMillis(), id, SessaoUsuario.exigirUsuarioId(sessao),
                dto.conteudo, dto.midias);
    }

    @RequestMapping(method = POST, path = "comentar-partida")
    FeedServicoAplicacao.PublicacaoResumo comentarPartida(
            @RequestBody ComentarioPartidaDto dto, HttpSession sessao) {
        return feedServicoAplicacao.comentarPartida(
                System.currentTimeMillis(),
                dto.torneioId,
                dto.partidaId,
                SessaoUsuario.exigirUsuarioId(sessao),
                dto.conteudo);
    }

    @RequestMapping(method = POST, path = "atualizacao-automatica")
    FeedServicoAplicacao.PublicacaoResumo atualizacaoAutomatica(@RequestBody AtualizacaoAutomaticaDto dto) {
        return feedServicoAplicacao.registrarAtualizacaoAutomatica(
                System.currentTimeMillis(),
                dto.torneioId,
                dto.partidaId,
                dto.conteudo);
    }

    @RequestMapping(method = POST, path = "{id}/editar")
    FeedServicoAplicacao.PublicacaoResumo editar(@PathVariable long id,
                                                 @RequestBody String novoConteudo,
                                                 HttpSession sessao) {
        return feedServicoAplicacao.editarPublicacao(id, SessaoUsuario.exigirUsuarioId(sessao), novoConteudo);
    }

    @RequestMapping(method = POST, path = "{id}/remover")
    void remover(@PathVariable long id, HttpSession sessao) {
        feedServicoAplicacao.removerPublicacao(id, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/curtir")
    FeedServicoAplicacao.PublicacaoResumo curtir(@PathVariable long id, HttpSession sessao) {
        return feedServicoAplicacao.curtirPublicacao(id, SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/reagir")
    FeedServicoAplicacao.PublicacaoResumo reagir(@PathVariable long id,
                                                 @RequestParam String tipoReacao,
                                                 HttpSession sessao) {
        return feedServicoAplicacao.reagirPublicacao(
                id, SessaoUsuario.exigirUsuarioId(sessao), tipoReacao);
    }

    @RequestMapping(method = GET, path = "torneio/{torneioId}")
    List<FeedServicoAplicacao.PublicacaoResumo> listarFeed(@PathVariable long torneioId) {
        return feedServicoAplicacao.listarFeed(torneioId);
    }

    @RequestMapping(method = GET, path = "geral")
    List<FeedServicoAplicacao.PublicacaoResumo> listarFeedGeral(HttpSession sessao) {
        return feedServicoAplicacao.listarFeedGeral(SessaoUsuario.usuarioIdOuNulo(sessao));
    }

    @RequestMapping(method = GET, path = "autor/{autorId}")
    List<FeedServicoAplicacao.PublicacaoResumo> listarPorAutor(
            @PathVariable long autorId, HttpSession sessao) {
        return feedServicoAplicacao.listarPorAutor(autorId, SessaoUsuario.usuarioIdOuNulo(sessao));
    }

    @RequestMapping(method = GET, path = "identidade/{tipoIdentidade}/{identidadeId}")
    List<FeedServicoAplicacao.PublicacaoResumo> listarPorIdentidade(
            @PathVariable String tipoIdentidade,
            @PathVariable long identidadeId,
            HttpSession sessao) {
        return feedServicoAplicacao.listarPorIdentidade(
                tipoIdentidade, identidadeId, SessaoUsuario.usuarioIdOuNulo(sessao));
    }

    @RequestMapping(method = GET, path = "{id}")
    FeedServicoAplicacao.PublicacaoResumo consultar(@PathVariable long id, HttpSession sessao) {
        return feedServicoAplicacao.consultar(id, SessaoUsuario.usuarioIdOuNulo(sessao));
    }

    @RequestMapping(method = GET, path = "identidades")
    List<FeedServicoAplicacao.IdentidadeResumo> identidades(HttpSession sessao) {
        return feedServicoAplicacao.listarIdentidades(SessaoUsuario.exigirUsuarioId(sessao));
    }

    @RequestMapping(method = GET, path = "assuntos")
    List<FeedServicoAplicacao.AssuntoResumo> assuntos() {
        return feedServicoAplicacao.listarAssuntosDoMomento();
    }

    @RequestMapping(method = GET, path = "hashtag")
    List<FeedServicoAplicacao.PublicacaoResumo> buscarPorHashtag(@RequestParam String hashtag) {
        return feedServicoAplicacao.buscarPorHashtag(hashtag);
    }

    static class ComunicadoDto {
        public long torneioId;
        public long organizadorId;
        public String conteudo;
    }

    static class PublicacaoSocialDto {
        public String tipoIdentidade;
        public Long identidadeId;
        public String conteudo;
        public List<String> hashtags;
        public List<String> midias;
    }

    static class ComentarioDto {
        public String conteudo;
        public List<String> midias;
    }

    static class ComentarioPartidaDto {
        public long torneioId;
        public long partidaId;
        public long usuarioId;
        public String conteudo;
    }

    static class AtualizacaoAutomaticaDto {
        public long torneioId;
        public long partidaId;
        public String conteudo;
    }
}
