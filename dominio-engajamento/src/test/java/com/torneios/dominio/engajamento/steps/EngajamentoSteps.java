package com.torneios.dominio.engajamento.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.desafio.ResultadoAmistoso;
import com.torneios.dominio.engajamento.desafio.StatusDesafioAmistoso;
import com.torneios.dominio.engajamento.EngajamentoFuncionalidade;
import com.torneios.dominio.engajamento.feed.TipoPublicacaoFeed;
import com.torneios.dominio.engajamento.palpite.OpcaoPalpite;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class EngajamentoSteps extends EngajamentoFuncionalidade {

    // =====================================================================
    // F1: Registrar palpites de usuarios autenticados
    // =====================================================================

    @Dado("que o usuario esta autenticado")
    public void que_o_usuario_esta_autenticado() {
        consultaPalpite.autenticar(USUARIO_ID);
        consultaFeed.autenticar(USUARIO_ID);
    }

    @Dado("que existe uma partida cadastrada com janela de votacao aberta")
    public void que_existe_partida_com_janela_aberta() {
        configurarEventoDePartidaAberto();
    }

    @Quando("ele registrar um palpite indicando o time vencedor da partida")
    public void ele_registrar_palpite_vencedor_partida() {
        try {
            palpite = palpiteServico.registrarOuAtualizar(
                    palpiteId(1L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(TIME_A_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve armazenar o palpite do usuario para a partida")
    public void o_sistema_deve_armazenar_palpite_partida() {
        assertNull(excecaoCapturada);
        assertTrue(palpiteRepositorio.buscarPorUsuarioEEvento(USUARIO_ID, eventoAlvo).isPresent());
    }

    @Dado("que existe um torneio que ainda nao foi iniciado")
    public void que_existe_torneio_nao_iniciado() {
        configurarEventoCampeaoAberto();
    }

    @Quando("ele registrar um palpite indicando o time campeao do torneio")
    public void ele_registrar_palpite_campeao() {
        try {
            configurarEventoCampeaoAberto();
            palpite = palpiteServico.registrarOuAtualizar(
                    palpiteId(2L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(TIME_A_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele registrar um palpite indicando o jogador artilheiro do torneio")
    public void ele_registrar_palpite_artilheiro() {
        try {
            configurarEventoArtilheiroAberto();
            palpite = palpiteServico.registrarOuAtualizar(
                    palpiteId(3L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(JOGADOR_A_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele registrar um palpite indicando o jogador lider de assistencias do torneio")
    public void ele_registrar_palpite_lider_assistencias() {
        try {
            configurarEventoLiderAssistenciasAberto();
            palpite = palpiteServico.registrarOuAtualizar(
                    palpiteId(4L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(JOGADOR_A_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve armazenar o palpite do usuario para o torneio")
    public void o_sistema_deve_armazenar_palpite_torneio() {
        assertNull(excecaoCapturada);
        assertTrue(palpiteRepositorio.buscarPorUsuarioEEvento(USUARIO_ID, eventoAlvo).isPresent());
    }

    @Dado("que o usuario nao esta autenticado")
    public void que_o_usuario_nao_esta_autenticado() {
        configurarEventoDePartidaAberto();
        configurarTorneioComPartidaNoFeed();
    }

    @Quando("ele tentar registrar um palpite")
    public void ele_tentar_registrar_palpite() {
        try {
            palpite = palpiteServico.registrarOuAtualizar(
                    palpiteId(5L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(TIME_A_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que ele ja registrou um palpite para um evento com janela de votacao aberta")
    public void que_ele_ja_registrou_palpite_evento_aberto() {
        configurarEventoDePartidaAberto();
        palpite = palpiteServico.registrarOuAtualizar(
                palpiteId(6L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(TIME_A_ID));
    }

    @Quando("ele registrar um novo palpite para o mesmo evento alvo")
    public void ele_registrar_novo_palpite_mesmo_evento() {
        try {
            palpite = palpiteServico.registrarOuAtualizar(
                    palpiteId(7L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(TIME_B_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve manter apenas o palpite mais recente do usuario para aquele evento")
    public void o_sistema_deve_manter_palpite_mais_recente() {
        assertNull(excecaoCapturada);
        assertEquals(1, palpiteRepositorio.listarPorEvento(eventoAlvo).size());
        assertEquals(TIME_B_ID, palpiteRepositorio.buscarPorUsuarioEEvento(USUARIO_ID, eventoAlvo).get().getOpcao().valor());
    }

    @Quando("ele alterar a opcao do palpite")
    public void ele_alterar_opcao_palpite() {
        try {
            palpite = palpiteServico.registrarOuAtualizar(
                    palpiteId(8L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(TIME_B_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve atualizar o palpite do usuario para o evento")
    public void o_sistema_deve_atualizar_palpite_usuario() {
        assertNull(excecaoCapturada);
        assertEquals(TIME_B_ID, palpiteRepositorio.buscarPorUsuarioEEvento(USUARIO_ID, eventoAlvo).get().getOpcao().valor());
    }

    @Dado("que existe uma partida que ja foi iniciada")
    public void que_existe_partida_ja_iniciada() {
        configurarEventoDePartidaAberto();
        consultaPalpite.iniciarPartida(PARTIDA_ID);
    }

    @Quando("ele tentar registrar ou alterar um palpite sobre o vencedor dessa partida")
    public void ele_tentar_registrar_alterar_palpite_partida_iniciada() {
        ele_tentar_registrar_palpite();
    }

    @Quando("ele tentar registrar um palpite indicando um time que nao participa da partida")
    public void ele_tentar_palpite_opcao_invalida() {
        try {
            palpite = palpiteServico.registrarOuAtualizar(
                    palpiteId(9L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(999L));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Dado("que existem multiplos palpites registrados para um mesmo evento alvo")
    public void que_existem_multiplos_palpites() {
        configurarEventoDePartidaAberto();
        consultaPalpite.autenticar(USUARIO_ID);
        consultaPalpite.autenticar(OUTRO_USUARIO_ID);
        palpiteServico.registrarOuAtualizar(palpiteId(10L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(TIME_A_ID));
        palpiteServico.registrarOuAtualizar(palpiteId(11L), OUTRO_USUARIO_ID, eventoAlvo, new OpcaoPalpite(TIME_B_ID));
    }

    @Quando("o sistema for consultado pelos percentuais do evento")
    public void sistema_consultar_percentuais_evento() {
        percentuaisPalpite = palpiteServico.obterPercentuais(eventoAlvo);
    }

    @Entao("ele deve retornar a distribuicao percentual de votos por opcao")
    public void deve_retornar_percentuais_votos() {
        assertEquals(2, percentuaisPalpite.getTotalPalpites());
        assertEquals(50.0, percentuaisPalpite.getPercentuaisPorOpcao().get(new OpcaoPalpite(TIME_A_ID)));
        assertEquals(50.0, percentuaisPalpite.getPercentuaisPorOpcao().get(new OpcaoPalpite(TIME_B_ID)));
    }

    @Dado("que existe um palpite registrado para um evento ja concluido")
    public void que_existe_palpite_evento_concluido() {
        consultaPalpite.autenticar(USUARIO_ID);
        configurarEventoDePartidaAberto();
        palpite = palpiteServico.registrarOuAtualizar(
                palpiteId(12L), USUARIO_ID, eventoAlvo, new OpcaoPalpite(TIME_A_ID));
        consultaPalpite.encerrarPartida(PARTIDA_ID);
    }

    @Dado("que a opcao escolhida pelo usuario corresponde ao resultado real")
    public void que_opcao_corresponde_resultado_real() {
        resultadoReal = TIME_A_ID;
    }

    @Dado("que a opcao escolhida pelo usuario nao corresponde ao resultado real")
    public void que_opcao_nao_corresponde_resultado_real() {
        resultadoReal = TIME_B_ID;
    }

    @Quando("o sistema apurar o evento alvo")
    public void o_sistema_apurar_evento_alvo() {
        try {
            palpitesApurados = palpiteServico.apurar(eventoAlvo, resultadoReal);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o palpite do usuario deve ser marcado como acertado")
    public void palpite_deve_ser_marcado_acertado() {
        assertNull(excecaoCapturada);
        assertTrue(palpitesApurados.get(0).acertou().orElse(false));
    }

    @Entao("o palpite do usuario deve ser marcado como nao acertado")
    public void palpite_deve_ser_marcado_nao_acertado() {
        assertNull(excecaoCapturada);
        assertFalse(palpitesApurados.get(0).acertou().orElse(true));
    }

    @Dado("que existe um palpite ja apurado")
    public void que_existe_palpite_ja_apurado() {
        que_existe_palpite_evento_concluido();
        resultadoReal = TIME_A_ID;
        palpitesApurados = palpiteServico.apurar(eventoAlvo, resultadoReal);
        palpite = palpitesApurados.get(0);
    }

    @Quando("o usuario tentar alterar a opcao desse palpite")
    public void usuario_tentar_alterar_opcao_palpite_apurado() {
        try {
            palpite.alterarOpcao(new OpcaoPalpite(TIME_B_ID));
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    // =====================================================================
    // F10: Gerenciar desafios e amistosos entre times
    // =====================================================================

    @Dado("que existe um time desafiante com responsavel autenticado")
    public void que_existe_time_desafiante_com_responsavel_autenticado() {
        consultaDesafio.autenticar(USUARIO_ID);
        consultaDesafio.definirResponsavel(TIME_DESAFIANTE_ID, USUARIO_ID);
        consultaDesafio.definirResponsavel(TIME_DESAFIADO_ID, OUTRO_USUARIO_ID);
    }

    @Dado("que existe um convite de amistoso pendente")
    public void que_existe_convite_amistoso_pendente() {
        que_existe_time_desafiante_com_responsavel_autenticado();
        desafioAmistoso = desafioAmistosoServico.proporConfronto(
                desafioId(1L),
                USUARIO_ID,
                TIME_DESAFIANTE_ID,
                TIME_DESAFIADO_ID,
                LocalDateTime.of(2026, 5, 10, 15, 0),
                "Campo do Bairro");
    }

    @Dado("que existe um amistoso aceito entre os times")
    public void que_existe_amistoso_aceito_entre_times() {
        que_existe_convite_amistoso_pendente();
        consultaDesafio.autenticar(OUTRO_USUARIO_ID);
        desafioAmistoso = desafioAmistosoServico.aceitarConvite(desafioId(1L), OUTRO_USUARIO_ID);
    }

    @Quando("ele propor um confronto amistoso para outro time")
    public void ele_propor_confronto_amistoso() {
        try {
            desafioAmistoso = desafioAmistosoServico.proporConfronto(
                    desafioId(2L),
                    USUARIO_ID,
                    TIME_DESAFIANTE_ID,
                    TIME_DESAFIADO_ID,
                    LocalDateTime.of(2026, 5, 12, 18, 30),
                    "Arena Comunitaria");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o responsavel do time desafiado aceitar o convite")
    public void responsavel_time_desafiado_aceitar_convite() {
        try {
            consultaDesafio.autenticar(OUTRO_USUARIO_ID);
            desafioAmistoso = desafioAmistosoServico.aceitarConvite(desafioId(1L), OUTRO_USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o responsavel do time desafiado recusar o convite")
    public void responsavel_time_desafiado_recusar_convite() {
        try {
            consultaDesafio.autenticar(OUTRO_USUARIO_ID);
            desafioAmistoso = desafioAmistosoServico.recusarConvite(desafioId(1L), OUTRO_USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("um responsavel reagendar o amistoso")
    public void responsavel_reagendar_amistoso() {
        try {
            desafioAmistoso = desafioAmistosoServico.reagendarAmistoso(
                    desafioId(1L),
                    USUARIO_ID,
                    LocalDateTime.of(2026, 5, 20, 20, 0),
                    "Campo Central");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("um responsavel registrar o resultado do amistoso")
    public void responsavel_registrar_resultado_amistoso() {
        try {
            desafioAmistoso = desafioAmistosoServico.registrarResultado(
                    desafioId(1L),
                    USUARIO_ID,
                    new ResultadoAmistoso(3, 2));
            historicoAmistosos = desafioAmistosoServico.listarHistoricoDoTime(TIME_DESAFIANTE_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("ele tentar desafiar o proprio time")
    public void ele_tentar_desafiar_proprio_time() {
        try {
            desafioAmistoso = desafioAmistosoServico.proporConfronto(
                    desafioId(3L),
                    USUARIO_ID,
                    TIME_DESAFIANTE_ID,
                    TIME_DESAFIANTE_ID,
                    LocalDateTime.of(2026, 5, 15, 10, 0),
                    "Campo do Bairro");
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("um usuario sem responsabilidade pelos times tentar aceitar o convite")
    public void usuario_sem_responsabilidade_tentar_aceitar_convite() {
        try {
            UsuarioId usuarioSemResponsabilidade = ORGANIZADOR_ID;
            consultaDesafio.autenticar(usuarioSemResponsabilidade);
            desafioAmistoso = desafioAmistosoServico.aceitarConvite(desafioId(1L), usuarioSemResponsabilidade);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve registrar o desafio como proposto")
    public void sistema_deve_registrar_desafio_proposto() {
        assertNull(excecaoCapturada);
        assertEquals(StatusDesafioAmistoso.PROPOSTO, desafioAmistoso.getStatus());
        assertTrue(desafioAmistosoRepositorio.buscarPorId(desafioAmistoso.getId()).isPresent());
    }

    @Entao("o sistema deve marcar o amistoso como aceito")
    public void sistema_deve_marcar_amistoso_aceito() {
        assertNull(excecaoCapturada);
        assertEquals(StatusDesafioAmistoso.ACEITO, desafioAmistoso.getStatus());
    }

    @Entao("o sistema deve marcar o convite como recusado")
    public void sistema_deve_marcar_convite_recusado() {
        assertNull(excecaoCapturada);
        assertEquals(StatusDesafioAmistoso.RECUSADO, desafioAmistoso.getStatus());
    }

    @Entao("o sistema deve atualizar data e local do amistoso")
    public void sistema_deve_atualizar_data_local_amistoso() {
        assertNull(excecaoCapturada);
        assertEquals(LocalDateTime.of(2026, 5, 20, 20, 0), desafioAmistoso.getDataHora());
        assertEquals("Campo Central", desafioAmistoso.getLocal());
    }

    @Entao("o sistema deve salvar o placar no historico dos times")
    public void sistema_deve_salvar_placar_historico_times() {
        assertNull(excecaoCapturada);
        assertEquals(StatusDesafioAmistoso.RESULTADO_REGISTRADO, desafioAmistoso.getStatus());
        assertTrue(desafioAmistoso.getResultado().isPresent());
        assertEquals(1, historicoAmistosos.size());
    }

    // =====================================================================
    // F12: Gerenciar comunicados e feed social do torneio
    // =====================================================================

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

    @Entao("o sistema deve impedir a operacao")
    public void sistema_deve_impedir_operacao() {
        assertNotNull(excecaoCapturada);
    }
}
