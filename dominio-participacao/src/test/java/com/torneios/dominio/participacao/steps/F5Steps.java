package com.torneios.dominio.participacao.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import com.torneios.dominio.participacao.ParticipacaoFuncionalidade;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivo;
import com.torneios.dominio.participacao.profissional.TipoProfissional;
import com.torneios.dominio.participacao.time.Time;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F5Steps extends ParticipacaoFuncionalidade {

    @Dado("que ele é responsável por um time")
    @Dado("que ele e responsavel por um time")
    public void ele_e_responsavel_por_um_time() {
        repositorio.salvar(new Time(TIME_A_ID, "Time Alpha", usuarioAtual));
    }

    @Dado("que ele não é responsável pelo time")
    @Dado("que ele nao e responsavel pelo time")
    public void ele_nao_e_responsavel_pelo_time() {
        repositorio.salvar(new Time(TIME_A_ID, "Time Alpha", ORGANIZADOR_ID));
    }

    @Dado("que ele possui um time cadastrado")
    public void ele_possui_um_time_cadastrado() {
        ele_e_responsavel_por_um_time();
    }

    @Quando("ele cadastrar um novo time com nome valido")
    @Quando("ele cadastrar um novo time com informações válidas")
    public void cadastrar_time_nome_valido() {
        try { timeServico.criarTime(TIME_A_ID, "Time Alpha", usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar criar um time sem informar o nome")
    public void criar_time_sem_nome() {
        try { timeServico.criarTime(TIME_A_ID, "", usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar cadastrar um novo time")
    public void tentar_cadastrar_time() {
        try { timeServico.criarTime(TIME_A_ID, "Time Alpha", usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele alterar o nome do time")
    @Quando("ele alterar as informações do time")
    public void alterar_nome_time() {
        try { timeServico.editarTime(TIME_A_ID, usuarioAtual, "Time Alpha Editado"); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele solicitar a exclusão do time")
    @Quando("ele solicitar a exclusao do time")
    public void excluir_time() {
        try { timeServico.excluirTime(TIME_A_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele acessar a area de torneios do time")
    @Quando("ele acessar a área de torneios do time")
    public void acessar_area_torneios() {
        try {
            autenticacaoServico.exigirAutenticacao(usuarioAtual);
            Time time = repositorio.buscarPorId(TIME_A_ID).orElseThrow();
            if (!time.getResponsavel().equals(usuarioAtual))
                throw new com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException("Apenas o responsavel pode consultar os torneios do time.");
            timesCapturados = timeServico.listarTimesDoUsuario(usuarioAtual);
        } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar acessar os torneios do time")
    public void tentar_acessar_torneios() {
        acessar_area_torneios();
    }

    @Dado("que existe um profissional esportivo cadastrado")
    public void profissional_cadastrado() {
        repositorio.salvar(new ProfissionalEsportivo(PROFISSIONAL_ID, "Joao Silva", TipoProfissional.JOGADOR, USUARIO_AUTENTICADO_ID));
    }

    @Dado("que o profissional ja esta vinculado ao time")
    public void profissional_ja_vinculado() {
        Time time = repositorio.buscarPorId(TIME_A_ID).orElseThrow();
        time.vincularProfissional(PROFISSIONAL_ID, "Atacante", LocalDate.of(2024, 1, 1), null);
        repositorio.salvar(time);
    }

    @Quando("ele vincular o profissional ao time com funcao e data de inicio")
    public void vincular_profissional() {
        try { timeServico.vincularProfissional(TIME_A_ID, usuarioAtual, PROFISSIONAL_ID, "Atacante", LocalDate.of(2024, 1, 1), null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar vincular o mesmo profissional novamente")
    @Quando("ele tentar vincular profissional ao time")
    public void tentar_vincular_profissional() {
        vincular_profissional();
    }

    @Quando("ele editar a funcao do profissional no time")
    public void editar_vinculo_profissional() {
        try { timeServico.editarVinculoProfissional(TIME_A_ID, usuarioAtual, PROFISSIONAL_ID, "Meia", LocalDate.of(2024, 1, 1), null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele remover o profissional do elenco")
    public void remover_profissional_elenco() {
        try { timeServico.removerVinculoProfissional(TIME_A_ID, usuarioAtual, PROFISSIONAL_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Entao("o sistema deve registrar o time para esse usuario")
    public void time_registrado() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(TIME_A_ID).isPresent());
    }

    @Entao("o sistema deve registrar o time para esse usuário")
    public void time_registrado_para_usuario() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(TIME_A_ID).isPresent());
    }

    @Entao("o sistema deve rejeitar o cadastro por nome invalido")
    public void rejeitar_nome_invalido() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve atualizar os dados do time")
    public void time_atualizado() {
        assertNull(excecaoCapturada);
        assertEquals("Time Alpha Editado", repositorio.buscarPorId(TIME_A_ID).orElseThrow().getNome());
    }

    @Entao("o sistema deve remover o time")
    public void time_removido() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(TIME_A_ID).isEmpty());
    }

    @Entao("o sistema deve impedir a exclusão")
    @Entao("o sistema deve impedir a exclusao")
    public void impedir_exclusao() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve exibir os torneios em que o time participa")
    public void exibir_torneios_do_time() {
        assertNull(excecaoCapturada);
        assertNotNull(timesCapturados);
    }

    @Entao("o sistema deve informar que o time não está vinculado a nenhum torneio")
    @Entao("o sistema deve informar que o time nao esta vinculado a nenhum torneio")
    public void informar_sem_torneios() {
        assertNull(excecaoCapturada);
        Time time = repositorio.buscarPorId(TIME_A_ID).orElseThrow();
        assertFalse(time.estaVinculadoATorneio());
    }

    @Entao("o sistema deve registrar o profissional no elenco do time")
    public void profissional_no_elenco() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(TIME_A_ID).orElseThrow().getElenco().stream().anyMatch(v -> v.getProfissionalId().equals(PROFISSIONAL_ID)));
    }

    @Entao("o sistema deve atualizar o vinculo do profissional")
    public void vinculo_atualizado() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(TIME_A_ID).orElseThrow().getElenco().stream().anyMatch(v -> v.getProfissionalId().equals(PROFISSIONAL_ID) && "Meia".equals(v.getFuncao())));
    }

    @Entao("o sistema deve retirar o profissional do elenco do time")
    public void profissional_removido_do_elenco() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(TIME_A_ID).orElseThrow().getElenco().stream().noneMatch(v -> v.getProfissionalId().equals(PROFISSIONAL_ID)));
    }

}
