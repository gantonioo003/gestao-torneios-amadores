package com.torneios.aplicacao.participacao.conta;

import static org.apache.commons.lang3.Validate.notNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.ContaUsuario;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;
import com.torneios.dominio.participacao.acesso.ProvedorAutenticacao;
import com.torneios.dominio.participacao.acesso.TipoContaUsuario;

public class ContaServicoAplicacao {

    private final ContaRepositorioAplicacao repositorio;
    private final ContaUsuarioServico contaUsuarioServico;
    private final IdentidadeExternaVerificador identidadeExternaVerificador;

    public ContaServicoAplicacao(ContaRepositorioAplicacao repositorio) {
        this(repositorio, null, null);
    }

    public ContaServicoAplicacao(ContaRepositorioAplicacao repositorio, ContaUsuarioServico contaUsuarioServico) {
        this(repositorio, contaUsuarioServico, null);
    }

    public ContaServicoAplicacao(ContaRepositorioAplicacao repositorio,
                                 ContaUsuarioServico contaUsuarioServico,
                                 IdentidadeExternaVerificador identidadeExternaVerificador) {
        notNull(repositorio, "O repositorio nao pode ser nulo.");
        this.repositorio = repositorio;
        this.contaUsuarioServico = contaUsuarioServico;
        this.identidadeExternaVerificador = identidadeExternaVerificador;
    }

    public Optional<ContaUsuarioResumo> pesquisarPorId(long usuarioId) {
        return repositorio.pesquisarPorId(usuarioId);
    }

    public Optional<ContaUsuarioResumo> pesquisarPorNomeUsuario(String nomeUsuario) {
        return repositorio.pesquisarPorNomeUsuario(nomeUsuario);
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

    public ContaUsuarioResumo cadastrarContaCompleta(long usuarioId,
                                                     String nome,
                                                     String nomeUsuario,
                                                     String email,
                                                     String telefone,
                                                     LocalDate dataNascimento,
                                                     String cidade,
                                                     String estado,
                                                     String biografia,
                                                     String senha,
                                                     String tipo) {
        return converter(exigirServicoDominio().cadastrarContaCompleta(
                new UsuarioId(usuarioId),
                nome,
                nomeUsuario,
                email,
                telefone,
                dataNascimento,
                cidade,
                estado,
                biografia,
                null,
                senha,
                TipoContaUsuario.valueOf(tipo),
                ProvedorAutenticacao.LOCAL));
    }

    public ContaUsuarioResumo autenticar(String email, String senha) {
        return converter(exigirServicoDominio().autenticar(email, senha));
    }

    public ContaUsuarioResumo autenticarGoogle(long novoUsuarioId,
                                               String credencial,
                                               String tipoNovaConta,
                                               String nomeUsuarioNovaConta) {
        IdentidadeExterna identidade = exigirVerificadorExterno().verificar(credencial);
        if (!identidade.emailVerificado()) {
            throw new IllegalArgumentException("O Google nao confirmou o e-mail desta conta.");
        }

        return repositorio.pesquisarPorNomeUsuario(
                        nomeUsuarioNovaConta == null ? "" : nomeUsuarioNovaConta.trim().toLowerCase())
                .filter(conta -> conta.getEmail().equalsIgnoreCase(identidade.email()))
                .orElseGet(() -> {
                    try {
                        return converter(exigirServicoDominio().autenticarExterno(identidade.email()));
                    } catch (com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException e) {
                        if (tipoNovaConta == null || tipoNovaConta.isBlank()) {
                            throw new IllegalArgumentException(
                                    "Conta ainda nao cadastrada. Abra a aba Criar conta e escolha seu perfil.");
                        }
                        String nomeUsuario = nomeUsuarioNovaConta;
                        if (nomeUsuario == null || nomeUsuario.isBlank()) {
                            nomeUsuario = gerarNomeUsuario(identidade.email(), novoUsuarioId);
                        }
                        return converter(exigirServicoDominio().cadastrarContaCompleta(
                                new UsuarioId(novoUsuarioId),
                                identidade.nome(),
                                nomeUsuario,
                                identidade.email(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                identidade.fotoPerfilUrl(),
                                UUID.randomUUID().toString(),
                                TipoContaUsuario.valueOf(tipoNovaConta),
                                ProvedorAutenticacao.GOOGLE));
                    }
                });
    }

    public ContaUsuarioResumo editarDados(long usuarioId, String novoNome, String novoEmail) {
        return converter(exigirServicoDominio().editarDados(new UsuarioId(usuarioId), novoNome, novoEmail));
    }

    public ContaUsuarioResumo editarDados(long usuarioId,
                                          String novoNome,
                                          String novoNomeUsuario,
                                          String novoEmail,
                                          String novoTelefone,
                                          LocalDate novaDataNascimento,
                                          String novaCidade,
                                          String novoEstado,
                                          String novaBiografia,
                                          String novaFotoPerfilUrl) {
        return converter(exigirServicoDominio().editarDados(
                new UsuarioId(usuarioId),
                novoNome,
                novoNomeUsuario,
                novoEmail,
                novoTelefone,
                novaDataNascimento,
                novaCidade,
                novoEstado,
                novaBiografia,
                novaFotoPerfilUrl));
    }

    public void alterarSenha(long usuarioId, String novaSenha) {
        exigirServicoDominio().alterarSenha(new UsuarioId(usuarioId), novaSenha);
    }

    public ContaUsuarioResumo salvarTorneio(long usuarioId, long torneioId) {
        return converter(exigirServicoDominio().salvarTorneio(
                new UsuarioId(usuarioId),
                new TorneioId(torneioId)));
    }

    public ContaUsuarioResumo removerTorneioSalvo(long usuarioId, long torneioId) {
        return converter(exigirServicoDominio().removerTorneioSalvo(
                new UsuarioId(usuarioId),
                new TorneioId(torneioId)));
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

    public boolean autenticacaoExternaConfigurada() {
        return identidadeExternaVerificador != null && identidadeExternaVerificador.configurado();
    }

    public String clientIdExternoPublico() {
        return autenticacaoExternaConfigurada() ? identidadeExternaVerificador.clientIdPublico() : "";
    }

    private IdentidadeExternaVerificador exigirVerificadorExterno() {
        if (!autenticacaoExternaConfigurada()) {
            throw new IllegalStateException("A entrada com Google ainda nao foi configurada no servidor.");
        }
        return identidadeExternaVerificador;
    }

    private ContaUsuarioResumo converter(ContaUsuario contaUsuario) {
        return new ContaUsuarioResumoAplicacao(
                contaUsuario.getId().valor(),
                contaUsuario.getNome(),
                contaUsuario.getNomeUsuario(),
                contaUsuario.getEmail(),
                contaUsuario.getTelefone(),
                contaUsuario.getDataNascimento(),
                contaUsuario.getCidade(),
                contaUsuario.getEstado(),
                contaUsuario.getBiografia(),
                contaUsuario.getFotoPerfilUrl(),
                contaUsuario.getTipo().name(),
                contaUsuario.getProvedor().name(),
                contaUsuario.podeCriarTorneio(),
                contaUsuario.podeGerenciarTimes(),
                contaUsuario.possuiPerfilProfissional(),
                contaUsuario.getTorneiosSalvos().stream()
                        .map(TorneioId::valor)
                        .toList());
    }

    private record ContaUsuarioResumoAplicacao(Long id,
                                               String nome,
                                               String nomeUsuario,
                                               String email,
                                               String telefone,
                                               LocalDate dataNascimento,
                                               String cidade,
                                               String estado,
                                               String biografia,
                                               String fotoPerfilUrl,
                                               String tipo,
                                               String provedor,
                                               boolean podeCriarTorneio,
                                               boolean podeGerenciarTimes,
                                               boolean possuiPerfilProfissional,
                                               List<Long> torneiosSalvos) implements ContaUsuarioResumo {
        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getNome() {
            return nome;
        }

        @Override
        public String getNomeUsuario() {
            return nomeUsuario;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getTelefone() {
            return telefone;
        }

        @Override
        public LocalDate getDataNascimento() {
            return dataNascimento;
        }

        @Override
        public String getCidade() {
            return cidade;
        }

        @Override
        public String getEstado() {
            return estado;
        }

        @Override
        public String getBiografia() {
            return biografia;
        }

        @Override
        public String getFotoPerfilUrl() {
            return fotoPerfilUrl;
        }

        @Override
        public String getTipo() {
            return tipo;
        }

        @Override
        public String getProvedor() {
            return provedor;
        }

        @Override
        public boolean isPodeCriarTorneio() {
            return podeCriarTorneio;
        }

        @Override
        public boolean isPodeGerenciarTimes() {
            return podeGerenciarTimes;
        }

        @Override
        public boolean isPossuiPerfilProfissional() {
            return possuiPerfilProfissional;
        }

        @Override
        public List<Long> getTorneiosSalvos() {
            return torneiosSalvos;
        }
    }

    private String gerarNomeUsuario(String email, long usuarioId) {
        String base = email.split("@", 2)[0].toLowerCase().replaceAll("[^a-z0-9._]", "");
        if (base.length() < 3) {
            base = "usuario";
        }
        String sufixo = String.valueOf(usuarioId);
        sufixo = sufixo.substring(Math.max(0, sufixo.length() - 12));
        int limite = Math.min(base.length(), 30 - sufixo.length() - 1);
        return base.substring(0, limite) + "_" + sufixo;
    }
}
