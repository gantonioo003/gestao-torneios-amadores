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

@RestController
@RequestMapping("backend/feed")
class FeedControlador {

    @Autowired
    FeedServicoAplicacao feedServicoAplicacao;

    @RequestMapping(method = POST, path = "publicar-comunicado")
    FeedServicoAplicacao.PublicacaoResumo publicarComunicado(@RequestBody ComunicadoDto dto) {
        return feedServicoAplicacao.publicarComunicado(
                System.currentTimeMillis(),
                dto.torneioId,
                dto.organizadorId,
                dto.conteudo);
    }

    @RequestMapping(method = POST, path = "publicar-social")
    FeedServicoAplicacao.PublicacaoResumo publicarSocial(@RequestBody PublicacaoSocialDto dto) {
        return feedServicoAplicacao.publicarPostagemSocial(
                System.currentTimeMillis(),
                dto.autorId,
                dto.conteudo,
                dto.hashtags,
                dto.midias);
    }

    @RequestMapping(method = POST, path = "comentar-partida")
    FeedServicoAplicacao.PublicacaoResumo comentarPartida(@RequestBody ComentarioPartidaDto dto) {
        return feedServicoAplicacao.comentarPartida(
                System.currentTimeMillis(),
                dto.torneioId,
                dto.partidaId,
                dto.usuarioId,
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
                                                 @RequestParam long usuarioId,
                                                 @RequestBody String novoConteudo) {
        return feedServicoAplicacao.editarPublicacao(id, usuarioId, novoConteudo);
    }

    @RequestMapping(method = POST, path = "{id}/remover")
    void remover(@PathVariable long id, @RequestParam long usuarioId) {
        feedServicoAplicacao.removerPublicacao(id, usuarioId);
    }

    @RequestMapping(method = POST, path = "{id}/curtir")
    FeedServicoAplicacao.PublicacaoResumo curtir(@PathVariable long id, @RequestParam long usuarioId) {
        return feedServicoAplicacao.curtirPublicacao(id, usuarioId);
    }

    @RequestMapping(method = POST, path = "{id}/reagir")
    FeedServicoAplicacao.PublicacaoResumo reagir(@PathVariable long id,
                                                 @RequestParam long usuarioId,
                                                 @RequestParam String tipoReacao) {
        return feedServicoAplicacao.reagirPublicacao(id, usuarioId, tipoReacao);
    }

    @RequestMapping(method = GET, path = "torneio/{torneioId}")
    List<FeedServicoAplicacao.PublicacaoResumo> listarFeed(@PathVariable long torneioId) {
        return feedServicoAplicacao.listarFeed(torneioId);
    }

    @RequestMapping(method = GET, path = "geral")
    List<FeedServicoAplicacao.PublicacaoResumo> listarFeedGeral() {
        return feedServicoAplicacao.listarFeedGeral();
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
        public long autorId;
        public String conteudo;
        public List<String> hashtags;
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
