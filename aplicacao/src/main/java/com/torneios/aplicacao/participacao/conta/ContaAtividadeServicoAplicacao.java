package com.torneios.aplicacao.participacao.conta;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;

import com.torneios.aplicacao.participacao.conta.ContaAtividadeRepositorioAplicacao.ContaAtividadeResumo;

public class ContaAtividadeServicoAplicacao {

    private final ContaAtividadeRepositorioAplicacao repositorio;

    public ContaAtividadeServicoAplicacao(ContaAtividadeRepositorioAplicacao repositorio) {
        notNull(repositorio, "O repositorio de atividades da conta nao pode ser nulo.");
        this.repositorio = repositorio;
    }

    public ContaAtividadeResumo pesquisar(long usuarioId, boolean incluirDadosPrivados) {
        ContaAtividadeResumo atividade = repositorio.pesquisar(usuarioId);
        if (incluirDadosPrivados) {
            return atividade;
        }
        return new ContaAtividadeResumo(
                atividade.torneiosOrganizados(),
                atividade.torneiosParticipando(),
                List.of(),
                List.of());
    }
}
