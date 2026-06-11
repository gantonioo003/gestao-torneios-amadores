package com.torneios.dominio.participacao.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import com.torneios.dominio.participacao.ParticipacaoFuncionalidade;
import com.torneios.dominio.participacao.profissional.MotivoDeSaida;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivo;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreiraId;
import com.torneios.dominio.participacao.profissional.TipoProfissional;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F6Steps extends ParticipacaoFuncionalidade {

    @Dado("que existe um profissional esportivo cadastrado pelo usuario")
    public void profissional_cadastrado_pelo_usuario() {
        repositorio.salvar(new ProfissionalEsportivo(PROFISSIONAL_ID, "Joao Silva", TipoProfissional.JOGADOR, usuarioAtual));
    }

    @Dado("que existe um profissional esportivo cadastrado por outro usuario")
    public void profissional_cadastrado_por_outro_usuario() {
        repositorio.salvar(new ProfissionalEsportivo(PROFISSIONAL_ID, "Joao Silva", TipoProfissional.JOGADOR, OUTRO_USUARIO_ID));
    }

    @Dado("que o profissional ja possui um registro de carreira no periodo")
    public void profissional_com_registro() {
        profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, REGISTRO_ID, "Clube Anterior", LocalDate.of(2020, 1, 1), LocalDate.of(2021, 12, 31), MotivoDeSaida.FIM_DE_CONTRATO);
    }

    @Quando("ele cadastrar um profissional esportivo com nome e tipo validos")
    public void cadastrar_profissional_valido() {
        try { profissionalServico.cadastrar(PROFISSIONAL_ID, "Joao Silva", TipoProfissional.JOGADOR, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar cadastrar um profissional sem informar o nome")
    public void cadastrar_profissional_sem_nome() {
        try { profissionalServico.cadastrar(PROFISSIONAL_ID, "", TipoProfissional.JOGADOR, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar cadastrar um profissional sem informar o tipo")
    public void cadastrar_profissional_sem_tipo() {
        try { profissionalServico.cadastrar(PROFISSIONAL_ID, "Joao", null, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar cadastrar um profissional esportivo")
    public void tentar_cadastrar_profissional() {
        cadastrar_profissional_valido();
    }

    @Quando("ele editar o nome do profissional")
    public void editar_nome_profissional() {
        try { profissionalServico.editar(PROFISSIONAL_ID, usuarioAtual, "Joao Editado"); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar editar o profissional")
    public void tentar_editar_profissional() {
        editar_nome_profissional();
    }

    @Quando("ele remover o profissional esportivo")
    public void remover_profissional() {
        try { profissionalServico.remover(PROFISSIONAL_ID, usuarioAtual); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar remover o profissional")
    public void tentar_remover_profissional() {
        remover_profissional();
    }

    @Quando("ele adicionar um registro de carreira com nome do clube data de inicio e motivo de saida validos")
    public void adicionar_registro_valido() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(70L), "Clube Novo", LocalDate.of(2022, 1, 1), LocalDate.of(2023, 12, 31), MotivoDeSaida.TRANSFERENCIA); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar adicionar um registro de carreira sem informar o nome do clube")
    public void adicionar_registro_sem_clube() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(70L), "", LocalDate.of(2022, 1, 1), null, null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar adicionar um registro de carreira sem data de inicio")
    public void adicionar_registro_sem_data() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(70L), "Clube X", null, null, null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar adicionar um registro com data de fim anterior a data de inicio")
    public void adicionar_registro_data_invalida() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(70L), "Clube X", LocalDate.of(2022, 6, 1), LocalDate.of(2022, 1, 1), null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar adicionar um registro de carreira com periodo sobreposto")
    public void adicionar_registro_sobreposto() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(71L), "Outro Clube", LocalDate.of(2020, 6, 1), LocalDate.of(2021, 6, 1), null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar adicionar um registro de carreira com motivo de saida invalido")
    public void adicionar_registro_motivo_invalido() {
        try { profissionalServico.adicionarRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(72L), "Clube Z", LocalDate.of(2019, 1, 1), LocalDate.of(2019, 12, 31), null); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele remover o registro de carreira")
    public void remover_registro() {
        try { profissionalServico.removerRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, REGISTRO_ID); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Quando("ele tentar remover um registro de carreira que nao existe")
    public void remover_registro_inexistente() {
        try { profissionalServico.removerRegistroDeCarreira(PROFISSIONAL_ID, usuarioAtual, new RegistroDeCarreiraId(999L)); } catch (Exception e) { excecaoCapturada = e; }
    }

    @Entao("o sistema deve registrar o profissional esportivo")
    public void profissional_registrado() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(PROFISSIONAL_ID).isPresent());
    }

    @Entao("o sistema deve rejeitar o cadastro do profissional por nome invalido")
    @Entao("o sistema deve rejeitar o cadastro do profissional por tipo invalido")
    @Entao("o sistema deve rejeitar o registro por dados invalidos")
    @Entao("o sistema deve rejeitar o registro por periodo invalido")
    @Entao("o sistema deve rejeitar o registro por sobreposicao de periodos")
    public void rejeitar_operacao_invalida() {
        assertNotNull(excecaoCapturada);
    }

    @Entao("o sistema deve atualizar os dados do profissional")
    public void profissional_atualizado() {
        assertNull(excecaoCapturada);
        assertEquals("Joao Editado", repositorio.buscarPorId(PROFISSIONAL_ID).orElseThrow().getNome());
    }

    @Entao("o sistema deve excluir o profissional")
    public void profissional_excluido() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(PROFISSIONAL_ID).isEmpty());
    }

    @Entao("o sistema deve registrar o historico de carreira do profissional")
    public void historico_registrado() {
        assertNull(excecaoCapturada);
        assertFalse(repositorio.buscarPorId(PROFISSIONAL_ID).orElseThrow().getHistorico().isEmpty());
    }

    @Entao("o sistema deve excluir o registro de carreira do historico")
    public void registro_excluido() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(PROFISSIONAL_ID).orElseThrow().getHistorico().stream().noneMatch(r -> r.getId().equals(REGISTRO_ID)));
    }

    @Entao("o sistema deve informar que o registro nao foi encontrado")
    public void registro_nao_encontrado() {
        assertNotNull(excecaoCapturada);
    }

}
