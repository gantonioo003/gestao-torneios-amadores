package com.torneios.dominio.engajamento.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.torneios.dominio.engajamento.EngajamentoFuncionalidade;
import com.torneios.dominio.engajamento.feed.TipoPublicacaoFeed;
import com.torneios.dominio.engajamento.feed.TipoReacaoFeed;
import com.torneios.dominio.engajamento.feed.TipoIdentidadeFeed;
import com.torneios.dominio.compartilhado.time.TimeId;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F11Steps extends EngajamentoFuncionalidade {

    @Quando("ele publicar uma postagem no feed social com hashtag e midia")
    public void ele_publicar_postagem_feed_social_com_hashtag_midia() {
        try {
            publicacaoFeed = feedTorneioServico.publicarPostagemSocial(
                    publicacaoId(20L), USUARIO_ID, "Hoje tem pelada pesada #CopaBairro",
                    List.of("CopaBairro"), List.of("foto-jogo.jpg"));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve armazenar a postagem no feed geral")
    public void sistema_deve_armazenar_postagem_feed_geral() {
        assertNull(excecaoCapturada);
        assertEquals(TipoPublicacaoFeed.POSTAGEM_SOCIAL, publicacaoFeed.getTipo());
        assertTrue(publicacaoFeed.getHashtags().contains("copabairro"));
        assertEquals(1, publicacaoFeed.getMidias().size());
        assertTrue(feedTorneioServico.listarFeedGeral().contains(publicacaoFeed));
    }

    @Dado("que existe um torneio com organizador autenticado")
    public void que_existe_torneio_com_organizador_autenticado() {
        configurarTorneioComPartidaNoFeed();
        consultaFeed.autenticar(ORGANIZADOR_ID);
    }

    @Quando("o organizador publicar um comunicado oficial no feed do torneio")
    public void organizador_publicar_comunicado_feed() {
        try {
            publicacaoFeed = feedTorneioServico.publicarComunicado(
                    publicacaoId(1L), TORNEIO_ID, ORGANIZADOR_ID, "Final sera domingo as 10h");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve armazenar o comunicado no feed do torneio")
    public void sistema_deve_armazenar_comunicado_feed() {
        assertNull(excecaoCapturada);
        assertEquals(TipoPublicacaoFeed.COMUNICADO_OFICIAL, publicacaoFeed.getTipo());
        assertEquals(1, feedTorneioServico.listarFeed(TORNEIO_ID).size());
    }

    @Dado("que o usuario autenticado nao e o organizador do torneio")
    public void que_usuario_autenticado_nao_e_organizador() {
        consultaFeed.autenticar(OUTRO_USUARIO_ID);
    }

    @Quando("ele tentar publicar um comunicado oficial no feed do torneio")
    public void ele_tentar_publicar_comunicado_oficial() {
        try {
            publicacaoFeed = feedTorneioServico.publicarComunicado(
                    publicacaoId(2L), TORNEIO_ID, OUTRO_USUARIO_ID, "Comunicado indevido");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que existe uma partida cadastrada no torneio")
    public void que_existe_partida_cadastrada_no_torneio() {
        configurarTorneioComPartidaNoFeed();
    }

    @Quando("ele comentar sobre a partida no feed social")
    public void ele_comentar_partida_feed_social() {
        try {
            publicacaoFeed = feedTorneioServico.comentarPartida(
                    publicacaoId(3L), TORNEIO_ID, PARTIDA_ID, USUARIO_ID, "Jogao equilibrado");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve armazenar o comentario vinculado a partida")
    public void sistema_deve_armazenar_comentario_vinculado_partida() {
        assertNull(excecaoCapturada);
        assertEquals(TipoPublicacaoFeed.COMENTARIO, publicacaoFeed.getTipo());
        assertEquals(PARTIDA_ID, publicacaoFeed.getPartidaId().orElseThrow());
    }

    @Quando("ele tentar comentar sobre a partida no feed social")
    public void ele_tentar_comentar_partida_feed_social() {
        ele_comentar_partida_feed_social();
    }

    @Quando("o sistema registrar uma atualizacao automatica sobre o resultado do jogo")
    public void sistema_registrar_atualizacao_automatica_resultado() {
        try {
            publicacaoFeed = feedTorneioServico.registrarAtualizacaoAutomatica(
                    publicacaoId(4L), TORNEIO_ID, PARTIDA_ID, "Unidos do Bairro venceu por 2x1");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o feed deve exibir a atualizacao automatica da partida")
    public void feed_deve_exibir_atualizacao_automatica_partida() {
        assertNull(excecaoCapturada);
        assertEquals(TipoPublicacaoFeed.ATUALIZACAO_AUTOMATICA, publicacaoFeed.getTipo());
        assertTrue(feedTorneioServico.listarFeed(TORNEIO_ID).contains(publicacaoFeed));
    }

    @Dado("que existe um comentario publicado pelo usuario no feed social")
    public void que_existe_comentario_publicado_usuario() {
        configurarTorneioComPartidaNoFeed();
        consultaFeed.autenticar(USUARIO_ID);
        publicacaoFeed = feedTorneioServico.comentarPartida(
                publicacaoId(5L), TORNEIO_ID, PARTIDA_ID, USUARIO_ID, "Grande jogo");
    }

    @Quando("o usuario editar o proprio comentario")
    public void usuario_editar_proprio_comentario() {
        try {
            publicacaoFeed = feedTorneioServico.editarPublicacao(
                    publicacaoFeed.getId(), USUARIO_ID, "Grande jogo e muita rivalidade");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve atualizar o comentario no feed do torneio")
    public void sistema_deve_atualizar_comentario_feed() {
        assertNull(excecaoCapturada);
        assertEquals("Grande jogo e muita rivalidade", publicacaoFeed.getConteudo());
    }

    @Dado("que existem comunicados, comentarios e atualizacoes automaticas no torneio")
    public void que_existem_publicacoes_no_torneio() {
        configurarTorneioComPartidaNoFeed();
        consultaFeed.autenticar(ORGANIZADOR_ID);
        consultaFeed.autenticar(USUARIO_ID);
        feedTorneioServico.publicarComunicado(
                publicacaoId(6L), TORNEIO_ID, ORGANIZADOR_ID, "Partida confirmada");
        feedTorneioServico.comentarPartida(
                publicacaoId(7L), TORNEIO_ID, PARTIDA_ID, USUARIO_ID, "Hoje tem classico");
        feedTorneioServico.registrarAtualizacaoAutomatica(
                publicacaoId(8L), TORNEIO_ID, PARTIDA_ID, "Placar final publicado");
    }

    @Quando("o usuario acessar o feed social do torneio")
    public void usuario_acessar_feed_social_torneio() {
        publicacoesFeed = feedTorneioServico.listarFeed(TORNEIO_ID);
    }

    @Entao("o sistema deve listar as publicacoes do torneio")
    public void sistema_deve_listar_publicacoes_torneio() {
        assertEquals(3, publicacoesFeed.size());
        assertTrue(publicacoesFeed.stream().anyMatch(publicacao -> publicacao.getTipo() == TipoPublicacaoFeed.COMUNICADO_OFICIAL));
        assertTrue(publicacoesFeed.stream().anyMatch(publicacao -> publicacao.getTipo() == TipoPublicacaoFeed.COMENTARIO));
        assertTrue(publicacoesFeed.stream().anyMatch(publicacao -> publicacao.getTipo() == TipoPublicacaoFeed.ATUALIZACAO_AUTOMATICA));
    }

    @Dado("que existe uma postagem publicada no feed social geral")
    public void que_existe_postagem_publicada_feed_social_geral() {
        consultaFeed.autenticar(USUARIO_ID);
        publicacaoFeed = feedTorneioServico.publicarPostagemSocial(
                publicacaoId(30L), USUARIO_ID, "Unidos do Bairro venceu bonito #CopaBairro",
                List.of("CopaBairro"), List.of("foto-vitoria.jpg"));
    }

    @Quando("o visitante acessar o feed social geral")
    public void visitante_acessar_feed_social_geral() {
        publicacoesFeed = feedTorneioServico.listarFeedGeral();
    }

    @Entao("o sistema deve listar as publicacoes publicas")
    public void sistema_deve_listar_publicacoes_publicas() {
        assertNull(excecaoCapturada);
        assertFalse(publicacoesFeed.isEmpty());
    }

    @Quando("ele curtir e reagir a publicacao")
    public void ele_curtir_e_reagir_publicacao() {
        try {
            publicacaoFeed = feedTorneioServico.curtirPublicacao(publicacaoFeed.getId(), USUARIO_ID);
            publicacaoFeed = feedTorneioServico.reagirPublicacao(
                    publicacaoFeed.getId(), USUARIO_ID, TipoReacaoFeed.COMEMORACAO);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve registrar a curtida e a reacao")
    public void sistema_deve_registrar_curtida_e_reacao() {
        assertNull(excecaoCapturada);
        assertEquals(1, publicacaoFeed.getQuantidadeCurtidas());
        assertEquals(TipoReacaoFeed.COMEMORACAO, publicacaoFeed.getReacoes().get(USUARIO_ID));
    }

    @Quando("ele tentar curtir a publicacao")
    public void ele_tentar_curtir_publicacao() {
        try {
            publicacaoFeed = feedTorneioServico.curtirPublicacao(publicacaoFeed.getId(), null);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que existem postagens com hashtags diferentes no feed social geral")
    public void que_existem_postagens_hashtags_diferentes_feed() {
        consultaFeed.autenticar(USUARIO_ID);
        feedTorneioServico.publicarPostagemSocial(
                publicacaoId(40L), USUARIO_ID, "Jogo pegado #CopaBairro",
                List.of("CopaBairro"), List.of());
        feedTorneioServico.publicarPostagemSocial(
                publicacaoId(41L), USUARIO_ID, "Treino aberto #Amistoso",
                List.of("Amistoso"), List.of());
    }

    @Quando("o usuario filtrar o feed por uma hashtag")
    public void usuario_filtrar_feed_por_hashtag() {
        publicacoesFeed = feedTorneioServico.buscarPorHashtag("CopaBairro");
    }

    @Entao("o sistema deve listar apenas publicacoes daquela hashtag")
    public void sistema_deve_listar_apenas_publicacoes_da_hashtag() {
        assertEquals(1, publicacoesFeed.size());
        assertTrue(publicacoesFeed.get(0).getHashtags().contains("copabairro"));
    }

    @Dado("que o usuario autenticado e responsavel por um time")
    public void usuarioResponsavelTime() {
        consultaFeed.autenticar(USUARIO_ID);
        consultaFeed.registrarTime(new TimeId(TIME_A_ID), USUARIO_ID);
    }

    @Quando("ele publicar uma postagem representando o time")
    public void publicarRepresentandoTime() {
        publicacaoFeed = feedTorneioServico.publicarPostagem(
                publicacaoId(50L), USUARIO_ID, TipoIdentidadeFeed.TIME, TIME_A_ID,
                "Treino concluido #VilaFC", List.of("VilaFC"), List.of());
    }

    @Entao("o sistema deve salvar a publicacao com identidade do time")
    public void salvaIdentidadeTime() {
        assertEquals(TipoIdentidadeFeed.TIME, publicacaoFeed.getTipoIdentidade());
        assertEquals(TIME_A_ID, publicacaoFeed.getIdentidadeId());
    }

    @Dado("que existe um time administrado por outro usuario")
    public void timeOutroResponsavel() {
        consultaFeed.autenticar(USUARIO_ID);
        consultaFeed.registrarTime(new TimeId(TIME_A_ID), OUTRO_USUARIO_ID);
    }

    @Quando("o usuario tentar publicar representando esse time")
    public void tentarPublicarOutroTime() {
        try {
            feedTorneioServico.publicarPostagem(
                    publicacaoId(51L), USUARIO_ID, TipoIdentidadeFeed.TIME, TIME_A_ID,
                    "Publicacao indevida", List.of(), List.of());
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o usuario responder a publicacao apenas com uma foto")
    public void responderApenasFoto() {
        publicacaoFeed = feedTorneioServico.comentarPublicacao(
                publicacaoId(52L), publicacaoFeed.getId(), USUARIO_ID, "", List.of("foto-resposta.jpg"));
    }

    @Entao("o comentario com foto deve ser salvo na publicacao")
    public void comentarioFotoSalvo() {
        assertEquals(1, publicacaoFeed.getMidias().size());
        assertEquals("foto-resposta.jpg", publicacaoFeed.getMidias().get(0));
    }

    @Quando("o usuario curtir a publicacao duas vezes")
    public void curtirDuasVezes() {
        feedTorneioServico.curtirPublicacao(publicacaoFeed.getId(), USUARIO_ID);
        publicacaoFeed = feedTorneioServico.curtirPublicacao(publicacaoFeed.getId(), USUARIO_ID);
    }

    @Entao("o sistema deve remover a curtida no segundo clique sem duplicar")
    public void removerCurtidaSegundoClique() {
        assertEquals(0, publicacaoFeed.getQuantidadeCurtidas());
    }

    @Dado("que o usuario publicou como pessoa e representando um time")
    public void publicouPessoaETime() {
        usuarioResponsavelTime();
        feedTorneioServico.publicarPostagem(
                publicacaoId(53L), USUARIO_ID, TipoIdentidadeFeed.USUARIO, USUARIO_ID.valor(),
                "Post pessoal", List.of(), List.of());
        feedTorneioServico.publicarPostagem(
                publicacaoId(54L), USUARIO_ID, TipoIdentidadeFeed.TIME, TIME_A_ID,
                "Post do time", List.of(), List.of());
    }

    @Quando("o perfil consultar as publicacoes pessoais do usuario")
    public void perfilConsultarPublicacoes() {
        publicacoesFeed = feedTorneioServico.listarPorAutor(USUARIO_ID);
    }

    @Entao("o sistema deve listar apenas a postagem feita como usuario")
    public void listarSomentePessoal() {
        assertEquals(1, publicacoesFeed.size());
        assertEquals(TipoIdentidadeFeed.USUARIO, publicacoesFeed.get(0).getTipoIdentidade());
    }

    @Quando("o chat consultar a publicacao encaminhada")
    public void chatConsultarPublicacaoEncaminhada() {
        publicacaoFeed = feedTorneioServico.consultarPublicacao(publicacaoFeed.getId());
    }

    @Entao("o sistema deve retornar a publicacao ativa para o card da mensagem")
    public void retornarPublicacaoParaCardMensagem() {
        assertNotNull(publicacaoFeed);
        assertFalse(publicacaoFeed.estaRemovida());
        assertEquals("Unidos do Bairro venceu bonito #CopaBairro", publicacaoFeed.getConteudo());
    }
}
