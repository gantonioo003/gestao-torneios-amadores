package com.torneios.dominio.participacao.steps;

import static org.junit.jupiter.api.Assertions.*;

import com.torneios.dominio.compartilhado.enumeracao.StatusSolicitacao;
import com.torneios.dominio.participacao.ParticipacaoFuncionalidade;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacao;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.TipoSolicitacaoParticipacao;
import com.torneios.dominio.participacao.time.Time;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F4Steps extends ParticipacaoFuncionalidade {

    @Dado("que existe um time cadastrado para outro treinador")
    public void existe_time_cadastrado_para_outro_treinador() {
        repositorio.salvar(new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID));
    }

    @Dado("que existe um convite pendente para o time")
    @Dado("que existe um convite pendente iniciado pelo organizador")
    public void existe_convite_pendente_para_time() {
        existe_time_cadastrado_para_outro_treinador();
        repositorio.salvar(new SolicitacaoParticipacao(
                SOLICITACAO_ID, ORGANIZADOR_ID, TIME_A_ID, TORNEIO_ID,
                TipoSolicitacaoParticipacao.CONVITE));
    }

    @Dado("que o usuario autenticado e o treinador responsavel pelo time")
    public void usuario_e_treinador_responsavel() {
        usuarioAtual = USUARIO_AUTENTICADO_ID;
    }

    @Quando("o organizador convidar o time para participar")
    public void organizador_convidar_time() {
        try {
            solicitacaoCapturada = solicitacaoServico.convidarTime(
                    SOLICITACAO_ID, ORGANIZADOR_ID, TIME_A_ID, TORNEIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o treinador aceitar o convite")
    public void treinador_aceitar_convite() {
        try {
            solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o treinador recusar o convite")
    public void treinador_recusar_convite() {
        try {
            solicitacaoServico.rejeitarSolicitacao(SOLICITACAO_ID, usuarioAtual);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Quando("o organizador cancelar o convite")
    public void organizador_cancelar_convite() {
        try {
            solicitacaoServico.cancelarCandidatura(SOLICITACAO_ID, ORGANIZADOR_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o sistema deve registrar um convite pendente")
    public void registrar_convite_pendente() {
        assertNull(excecaoCapturada);
        assertEquals(TipoSolicitacaoParticipacao.CONVITE, solicitacaoCapturada.getTipo());
        assertEquals(StatusSolicitacao.PENDENTE, solicitacaoCapturada.getStatus());
    }

    @Entao("o sistema deve aprovar o convite e vincular o time ao torneio")
    public void aprovar_convite_e_vincular_time() {
        assertNull(excecaoCapturada);
        assertEquals(StatusSolicitacao.APROVADA,
                repositorio.buscarPorId(SOLICITACAO_ID).orElseThrow().getStatus());
        assertTrue(repositorio.buscarPorId(TIME_A_ID).orElseThrow().estaVinculadoAoTorneio(TORNEIO_ID));
    }

    @Entao("o sistema deve registrar o convite como rejeitado")
    public void convite_rejeitado() {
        solicitacao_rejeitada();
    }

    @Entao("o sistema deve marcar o convite como cancelado")
    public void convite_cancelado() {
        candidatura_cancelada();
    }

    @Dado("que o torneio está com vagas abertas para solicitação de participação")
    @Dado("que o torneio esta com vagas abertas para solicitacao de participacao")
    public void torneio_com_vagas_abertas() {
        torneioAceitaSolicitacoes = true;
    }

    @Dado("que o torneio não está com vagas abertas para solicitação de participação")
    @Dado("que o torneio nao esta com vagas abertas para solicitacao de participacao")
    public void torneio_sem_vagas_abertas() {
        torneioAceitaSolicitacoes = false;
    }

    @Dado("que o usuário autenticado é o organizador do torneio")
    @Dado("que o usuario autenticado e o organizador do torneio")
    public void usuario_e_organizador() {
        usuarioAtual = ORGANIZADOR_ID;
        usuarioEhOrganizador = true;
    }

    @Dado("que existe uma solicitação pendente de um time para o torneio")
    @Dado("que existem solicitações pendentes de times para o torneio")
    @Dado("que existem solicitacoes pendentes de times para o torneio")
    public void solicitacao_pendente_existe() {
        if (repositorio.buscarPorId(TIME_A_ID).isEmpty())
            repositorio.salvar(new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID));
        SolicitacaoParticipacao sol = new SolicitacaoParticipacao(SOLICITACAO_ID, USUARIO_AUTENTICADO_ID, TIME_A_ID, TORNEIO_ID);
        repositorio.salvar(sol);
    }

    @Dado("que já existe uma solicitação pendente do time para esse torneio")
    @Dado("que ja existe uma solicitacao pendente do time para esse torneio")
    public void solicitacao_pendente_duplicada() {
        solicitacao_pendente_existe();
    }

    @Dado("que existe uma solicitação de participação aprovada pelo organizador para o time")
    public void solicitacao_aprovada_existe() {
        if (repositorio.buscarPorId(TIME_A_ID).isEmpty())
            repositorio.salvar(new Time(TIME_A_ID, "Time Alpha", usuarioAtual));
        SolicitacaoParticipacao sol = new SolicitacaoParticipacao(SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        sol.aprovar();
        repositorio.salvar(sol);
    }

    @Dado("que o time está vinculado a pelo menos um torneio")
    @Dado("que o time esta vinculado a pelo menos um torneio")
    @Dado("que o time está vinculado a um torneio")
    @Dado("que o time esta vinculado a um torneio")
    public void time_vinculado_a_torneio() {
        Time time = repositorio.buscarPorId(TIME_A_ID).orElseGet(() -> {
            Time t = new Time(TIME_A_ID, "Time Alpha", usuarioAtual != null ? usuarioAtual : USUARIO_AUTENTICADO_ID);
            repositorio.salvar(t);
            return t;
        });
        time.vincularAoTorneio(TORNEIO_ID);
        repositorio.salvar(time);
    }

    @Dado("que o time não está vinculado a nenhum torneio")
    @Dado("que o time nao esta vinculado a nenhum torneio")
    public void time_sem_torneio() { }

    @Quando("o usuário solicitar a participação do seu time no torneio")
    @Quando("o usuario solicitar a participacao do seu time no torneio")
    public void usuario_solicitar_participacao() {
        try { solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuário solicitar novamente a participação do seu time no torneio")
    @Quando("o usuario solicitar novamente a participacao do seu time no torneio")
    public void usuario_solicitar_participacao_novamente() {
        try { solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(new SolicitacaoParticipacaoId(101L), usuarioAtual, TIME_A_ID, TORNEIO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o organizador aprovar o time candidato")
    @Quando("o organizador aprovar a solicitacao")
    @Quando("o organizador aprovar a solicitação")
    public void organizador_aprovar() {
        try { solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o organizador rejeitar o time candidato")
    @Quando("o organizador rejeitar a solicitacao")
    @Quando("o organizador rejeitar a solicitação")
    public void organizador_rejeitar() {
        try { solicitacaoServico.rejeitarSolicitacao(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o organizador acessar a lista de candidatos")
    public void organizador_acessar_lista_candidatos() {
        try { solicitacoesCapturadas = solicitacaoServico.listarPendentesParaAvaliacao(TORNEIO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario acompanhar suas candidaturas")
    public void usuario_acompanhar_candidaturas() {
        try { solicitacoesCapturadas = solicitacaoServico.acompanharCandidaturas(usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario cancelar a candidatura pendente")
    public void usuario_cancelar_candidatura() {
        try { solicitacaoServico.cancelarCandidatura(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Entao("o sistema deve registrar a solicitação de participação como pendente")
    @Entao("o sistema deve registrar a candidatura como pendente")
    public void solicitacao_registrada_como_pendente() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacaoCapturada);
        assertEquals(StatusSolicitacao.PENDENTE, solicitacaoCapturada.getStatus());
    }

    @Entao("o sistema deve registrar a solicitação como aprovada")
    public void solicitacao_aprovada() {
        assertNull(excecaoCapturada);
        assertEquals(StatusSolicitacao.APROVADA, repositorio.buscarPorId(SOLICITACAO_ID).orElseThrow().getStatus());
    }

    @Entao("o sistema deve registrar a solicitação como rejeitada")
    @Entao("o sistema deve registrar a solicitacao como rejeitada")
    public void solicitacao_rejeitada() {
        assertNull(excecaoCapturada);
        assertEquals(StatusSolicitacao.REJEITADA, repositorio.buscarPorId(SOLICITACAO_ID).orElseThrow().getStatus());
    }

    @Entao("o sistema deve exibir os times com solicitações pendentes")
    @Entao("o sistema deve exibir os times com solicitacoes pendentes")
    public void exibir_times_pendentes() {
        assertNull(excecaoCapturada);
        assertFalse(solicitacoesCapturadas.isEmpty());
    }

    @Entao("o sistema deve impedir a solicitação")
    @Entao("o sistema deve impedir a solicitacao")
    @Entao("o sistema deve impedir a solicitação duplicada")
    @Entao("o sistema deve impedir a candidatura duplicada")
    public void impedir_solicitacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve marcar a candidatura como cancelada")
    public void candidatura_cancelada() {
        assertNull(excecaoCapturada);
        assertEquals(StatusSolicitacao.CANCELADA, repositorio.buscarPorId(SOLICITACAO_ID).orElseThrow().getStatus());
    }

    @Dado("que ele nao possui time cadastrado")
    public void ele_nao_possui_time_cadastrado() { }

    @Dado("que existe uma candidatura pendente do time")
    public void candidatura_pendente_do_time() {
        solicitacao_pendente_existe();
    }

    @Dado("que existe uma candidatura ja avaliada do time")
    public void candidatura_ja_avaliada_do_time() {
        if (repositorio.buscarPorId(TIME_A_ID).isEmpty())
            repositorio.salvar(new Time(TIME_A_ID, "Time Alpha", usuarioAtual));
        SolicitacaoParticipacao sol = new SolicitacaoParticipacao(SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID);
        sol.aprovar();
        repositorio.salvar(sol);
    }

    @Dado("que o torneio ainda nao foi iniciado")
    public void torneio_ainda_nao_iniciado() { torneioIniciado = false; }

    @Dado("que o torneio ja foi iniciado")
    public void torneio_ja_iniciado() { torneioIniciado = true; }

    @Dado("que existe um time aprovado na lista final de participantes")
    public void time_aprovado_lista_final() {
        Time time = repositorio.buscarPorId(TIME_A_ID)
            .orElseGet(() -> new Time(TIME_A_ID, "Time Alpha", USUARIO_AUTENTICADO_ID));
        time.vincularAoTorneio(TORNEIO_ID);
        repositorio.salvar(time);
    }

    @Dado("que existe uma solicitacao pendente de participacao para um torneio")
    public void solicitacao_pendente_para_torneio() {
        solicitacao_pendente_existe();
    }

    @Dado("que nao existe solicitacao pendente para o torneio")
    public void sem_solicitacao_pendente() { }

    @Quando("ele solicitar participacao em um torneio")
    public void ele_solicitar_participacao_em_torneio() {
        try { solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar aprovar a solicitacao")
    public void ele_tentar_aprovar_solicitacao() {
        try { solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar avaliar uma solicitacao")
    public void ele_tentar_avaliar_solicitacao() {
        try { solicitacaoServico.aprovarSolicitacao(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o organizador remover o time da lista final")
    public void organizador_remover_time_lista_final() {
        try { solicitacaoServico.removerParticipanteAprovado(TORNEIO_ID, TIME_A_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar alterar a lista final de participantes")
    public void ele_tentar_alterar_lista_final() {
        try { solicitacaoServico.removerParticipanteAprovado(TORNEIO_ID, TIME_A_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario tentar cancelar uma candidatura ja avaliada")
    public void usuario_tentar_cancelar_candidatura_avaliada() {
        try { solicitacaoServico.cancelarCandidatura(SOLICITACAO_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("o usuario solicitar a participacao em um torneio")
    public void usuario_solicitar_participacao_em_torneio() {
        try { solicitacaoCapturada = solicitacaoServico.solicitarParticipacao(SOLICITACAO_ID, usuarioAtual, TIME_A_ID, TORNEIO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Entao("o sistema deve exibir o status das candidaturas do time")
    public void exibir_status_candidaturas() {
        assertNull(excecaoCapturada);
        assertNotNull(solicitacoesCapturadas);
        assertFalse(solicitacoesCapturadas.isEmpty());
    }

    @Entao("o sistema deve impedir o cancelamento da candidatura")
    public void impedir_cancelamento_candidatura() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve registrar o time como participante aprovado do torneio")
    public void time_aprovado_no_torneio() {
        assertNull(excecaoCapturada);
        SolicitacaoParticipacao sol = repositorio.buscarPorId(SOLICITACAO_ID).orElseThrow();
        assertEquals(StatusSolicitacao.APROVADA, sol.getStatus());
    }

    @Entao("o sistema deve retirar o time da lista final do torneio")
    public void time_retirado_lista_final() {
        assertNull(excecaoCapturada);
        Time time = repositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertFalse(time.estaVinculadoAoTorneio(TORNEIO_ID));
    }

    @Entao("o sistema deve informar que nao ha solicitacao pendente para avaliacao")
    public void sem_solicitacao_para_avaliacao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("deve informar que e necessario possuir um time cadastrado")
    public void informar_necessidade_time_cadastrado() {
        assertNotNull(excecaoCapturada);
    }

    @Dado("que o usuario autenticado nao e o organizador do torneio")
    public void que_o_usuario_nao_e_organizador() {
        usuarioAtual = USUARIO_AUTENTICADO_ID;
        usuarioEhOrganizador = false;
    }

}
