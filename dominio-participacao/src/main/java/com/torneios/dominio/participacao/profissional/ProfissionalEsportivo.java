package com.torneios.dominio.participacao.profissional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class ProfissionalEsportivo {

    private final ProfissionalEsportivoId id;
    private String nome;
    private final TipoProfissional tipo;
    private final UsuarioId cadastranteId;
    private final List<RegistroDeCarreira> historico;

    public ProfissionalEsportivo(ProfissionalEsportivoId id, String nome, TipoProfissional tipo,
            UsuarioId cadastranteId) {
        this.id = Objects.requireNonNull(id, "O id do profissional e obrigatorio.");
        this.nome = validarNome(nome);
        this.tipo = Objects.requireNonNull(tipo, "O tipo do profissional e obrigatorio.");
        this.cadastranteId = Objects.requireNonNull(cadastranteId, "O cadastrante e obrigatorio.");
        this.historico = new ArrayList<>();
    }

    public ProfissionalEsportivoId getId() { return id; }
    public String getNome() { return nome; }
    public TipoProfissional getTipo() { return tipo; }
    public UsuarioId getCadastranteId() { return cadastranteId; }
    public List<RegistroDeCarreira> getHistorico() { return Collections.unmodifiableList(historico); }

    public void renomear(String novoNome) {
        this.nome = validarNome(novoNome);
    }

    public void adicionarRegistroDeCarreira(RegistroDeCarreira registro) {
        Objects.requireNonNull(registro, "O registro de carreira e obrigatorio.");
        boolean sobreposicao = historico.stream()
            .anyMatch(r -> r.sobrepoeComPeriodo(registro.getDataInicio(), registro.getDataFim()));
        if (sobreposicao) {
            throw new RegraDeNegocioException(
                "O periodo informado se sobrepoe a um registro de carreira existente.");
        }
        historico.add(registro);
    }

    public void removerRegistroDeCarreira(RegistroDeCarreiraId registroId) {
        Objects.requireNonNull(registroId, "O id do registro e obrigatorio.");
        boolean removido = historico.removeIf(r -> r.getId().equals(registroId));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Registro de carreira nao encontrado.");
        }
    }

    private String validarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do profissional e obrigatorio.");
        }
        return valor.trim();
    }
}
