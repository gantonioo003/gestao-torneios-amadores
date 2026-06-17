package com.torneios.aplicacao.participacao.conta;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.ContaUsuario;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;
import com.torneios.dominio.participacao.acesso.TipoContaUsuario;

public class ContaServicoAplicacao {

    private final ContaRepositorioAplicacao repositorio;
    private final ContaUsuarioServico contaUsuarioServico;

    public ContaServicoAplicacao(ContaRepositorioAplicacao repositorio) {
        this(repositorio, null);
    }

    public ContaServicoAplicacao(ContaRepositorioAplicacao repositorio, ContaUsuarioServico contaUsuarioServico) {
        notNull(repositorio, "O repositorio nao pode ser nulo.");
        this.repositorio = repositorio;
        this.contaUsuarioServico = contaUsuarioServico;
    }

    public Optional<ContaUsuarioResumo> pesquisarPorId(long usuarioId) {
        return repositorio.pesquisarPorId(usuarioId);
    }

    public ContaUsuarioResumo cadastrarConta(long usuarioId,
                                             String nome,
                                             String email,
                                             String senha,
                                             String tipo) {
        ContaUsuario contaUsuario = exigirServicoDominio().cadastrarConta(
                new UsuarioId(usuarioId),
                nome,
                email,
                senha,
                tipo == null ? TipoContaUsuario.ORGANIZADOR : TipoContaUsuario.valueOf(tipo));
        return converter(contaUsuario);
    }

    public ContaUsuarioResumo autenticar(String email, String senha) {
        return converter(exigirServicoDominio().autenticar(email, senha));
    }

    public ContaUsuarioResumo editarDados(long usuarioId, String novoNome, String novoEmail) {
        return converter(exigirServicoDominio().editarDados(new UsuarioId(usuarioId), novoNome, novoEmail));
    }

    public void alterarSenha(long usuarioId, String novaSenha) {
        exigirServicoDominio().alterarSenha(new UsuarioId(usuarioId), novaSenha);
    }

    public void excluirConta(long usuarioId) {
        exigirServicoDominio().excluirConta(new UsuarioId(usuarioId));
    }

    private ContaUsuarioServico exigirServicoDominio() {
        if (contaUsuarioServico == null) {
            throw new IllegalStateException("O servico de dominio de conta nao foi configurado nesta camada.");
        }
        return contaUsuarioServico;
    }

    private ContaUsuarioResumo converter(ContaUsuario contaUsuario) {
        return new ContaUsuarioResumoAplicacao(
                contaUsuario.getId().valor(),
                contaUsuario.getNome(),
                contaUsuario.getEmail(),
                contaUsuario.getTipo().name());
    }

    private record ContaUsuarioResumoAplicacao(Long id,
                                               String nome,
                                               String email,
                                               String tipo) implements ContaUsuarioResumo {
        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getNome() {
            return nome;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getTipo() {
            return tipo;
        }
    }
}
