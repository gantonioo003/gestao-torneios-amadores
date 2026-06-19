package com.torneios.dominio.participacao.profissional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;

public class ProfissionalEsportivoServico {

    private final ProfissionalEsportivoRepositorio repositorio;
    private final AutenticacaoServico autenticacaoServico;

    public ProfissionalEsportivoServico(ProfissionalEsportivoRepositorio repositorio,
            AutenticacaoServico autenticacaoServico) {
        this.repositorio = Objects.requireNonNull(repositorio, "O repositorio e obrigatorio.");
        this.autenticacaoServico = Objects.requireNonNull(autenticacaoServico,
                "O servico de autenticacao e obrigatorio.");
    }

    public ProfissionalEsportivo cadastrar(ProfissionalEsportivoId id, String nome,
            TipoProfissional tipo, UsuarioId cadastranteId) {
        return cadastrar(id, nome, tipo, cadastranteId, null);
    }

    public ProfissionalEsportivo cadastrar(ProfissionalEsportivoId id, String nome,
            TipoProfissional tipo, UsuarioId cadastranteId, String fotoUrl) {
        autenticacaoServico.exigirAutenticacao(cadastranteId);
        ProfissionalEsportivo profissional = new ProfissionalEsportivo(
                id, nome, tipo, cadastranteId, fotoUrl);
        repositorio.salvar(profissional);
        return profissional;
    }

    public void editar(ProfissionalEsportivoId id, UsuarioId solicitanteId, String novoNome) {
        editar(id, solicitanteId, novoNome, null);
    }

    public void editar(ProfissionalEsportivoId id, UsuarioId solicitanteId,
            String novoNome, String fotoUrl) {
        autenticacaoServico.exigirAutenticacao(solicitanteId);
        ProfissionalEsportivo profissional = obter(id);
        validarCadastrante(profissional, solicitanteId);
        profissional.renomear(novoNome);
        profissional.alterarFoto(fotoUrl);
        repositorio.salvar(profissional);
    }

    public void remover(ProfissionalEsportivoId id, UsuarioId solicitanteId) {
        autenticacaoServico.exigirAutenticacao(solicitanteId);
        ProfissionalEsportivo profissional = obter(id);
        validarCadastrante(profissional, solicitanteId);
        repositorio.remover(id);
    }

    public void adicionarRegistroDeCarreira(ProfissionalEsportivoId id, UsuarioId solicitanteId,
            RegistroDeCarreiraId registroId, String nomeDoClube, LocalDate dataInicio,
            LocalDate dataFim, MotivoDeSaida motivoDeSaida, String descricao) {
        autenticacaoServico.exigirAutenticacao(solicitanteId);
        ProfissionalEsportivo profissional = obter(id);
        validarCadastrante(profissional, solicitanteId);
        profissional.adicionarRegistroDeCarreira(
            new RegistroDeCarreira(registroId, nomeDoClube, dataInicio, dataFim, motivoDeSaida, descricao));
        repositorio.salvar(profissional);
    }

    public void removerRegistroDeCarreira(ProfissionalEsportivoId id, UsuarioId solicitanteId,
            RegistroDeCarreiraId registroId) {
        autenticacaoServico.exigirAutenticacao(solicitanteId);
        ProfissionalEsportivo profissional = obter(id);
        validarCadastrante(profissional, solicitanteId);
        profissional.removerRegistroDeCarreira(registroId);
        repositorio.salvar(profissional);
    }

    public ProfissionalEsportivo obter(ProfissionalEsportivoId id) {
        return repositorio.buscarPorId(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Profissional nao encontrado."));
    }

    public List<ProfissionalEsportivo> pesquisar(String nome) {
        return repositorio.pesquisarPorNome(nome == null ? "" : nome);
    }

    private void validarCadastrante(ProfissionalEsportivo profissional, UsuarioId solicitanteId) {
        if (!profissional.getCadastranteId().equals(solicitanteId)) {
            throw new OperacaoNaoPermitidaException(
                "Apenas o cadastrante pode executar esta operacao.");
        }
    }
}
