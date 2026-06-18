package com.torneios.dominio.participacao.acesso;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class ContaUsuario {

    private final UsuarioId id;
    private String nome;
    private String nomeUsuario;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private String cidade;
    private String estado;
    private String biografia;
    private String fotoPerfilUrl;
    private String senha;
    private final TipoContaUsuario tipo;
    private final ProvedorAutenticacao provedor;
    private final Set<TorneioId> torneiosSalvos;

    public ContaUsuario(UsuarioId id, String nome, String email, String senha) {
        this(id, nome, email, senha, TipoContaUsuario.ORGANIZADOR);
    }

    public ContaUsuario(UsuarioId id, String nome, String email, String senha, TipoContaUsuario tipo) {
        this(id, nome, nomeUsuarioPadrao(email, id), email, null, null, null, null, null, null,
                senha, tipo, ProvedorAutenticacao.LOCAL);
    }

    public ContaUsuario(UsuarioId id,
                        String nome,
                        String nomeUsuario,
                        String email,
                        String telefone,
                        LocalDate dataNascimento,
                        String cidade,
                        String estado,
                        String biografia,
                        String fotoPerfilUrl,
                        String senha,
                        TipoContaUsuario tipo,
                        ProvedorAutenticacao provedor) {
        this.id = Objects.requireNonNull(id, "O id da conta e obrigatorio.");
        this.nome = validarNome(nome);
        this.nomeUsuario = validarNomeUsuario(nomeUsuario);
        this.email = validarEmail(email);
        this.telefone = normalizarOpcional(telefone, 25);
        this.dataNascimento = validarDataNascimento(dataNascimento);
        this.cidade = normalizarOpcional(cidade, 100);
        this.estado = normalizarOpcional(estado, 40);
        this.biografia = normalizarOpcional(biografia, 300);
        this.fotoPerfilUrl = normalizarOpcional(fotoPerfilUrl, 500);
        this.senha = validarSenha(senha);
        this.tipo = Objects.requireNonNull(tipo, "O tipo da conta e obrigatorio.");
        this.provedor = Objects.requireNonNull(provedor, "O provedor de autenticacao e obrigatorio.");
        this.torneiosSalvos = new LinkedHashSet<>();
    }

    public UsuarioId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public TipoContaUsuario getTipo() {
        return tipo;
    }

    public ProvedorAutenticacao getProvedor() {
        return provedor;
    }

    public boolean ehJogador() {
        return tipo == TipoContaUsuario.JOGADOR;
    }

    public boolean ehOrganizador() {
        return tipo == TipoContaUsuario.ORGANIZADOR;
    }

    public boolean podeCriarTorneio() {
        return tipo.podeCriarTorneio();
    }

    public boolean podeGerenciarTimes() {
        return tipo.podeGerenciarTimes();
    }

    public boolean possuiPerfilProfissional() {
        return tipo.possuiPerfilProfissional();
    }

    public Set<TorneioId> getTorneiosSalvos() {
        return Collections.unmodifiableSet(torneiosSalvos);
    }

    public void salvarTorneio(TorneioId torneioId) {
        torneiosSalvos.add(Objects.requireNonNull(torneioId, "O torneio salvo e obrigatorio."));
    }

    public void removerTorneioSalvo(TorneioId torneioId) {
        torneiosSalvos.remove(Objects.requireNonNull(torneioId, "O torneio salvo e obrigatorio."));
    }

    public boolean salvouTorneio(TorneioId torneioId) {
        return torneiosSalvos.contains(torneioId);
    }

    public void editarDados(String novoNome, String novoEmail) {
        editarDados(novoNome, nomeUsuario, novoEmail, telefone, dataNascimento, cidade, estado, biografia, fotoPerfilUrl);
    }

    public void editarDados(String novoNome,
                            String novoNomeUsuario,
                            String novoEmail,
                            String novoTelefone,
                            LocalDate novaDataNascimento,
                            String novaCidade,
                            String novoEstado,
                            String novaBiografia,
                            String novaFotoPerfilUrl) {
        nome = validarNome(novoNome);
        nomeUsuario = validarNomeUsuario(novoNomeUsuario);
        email = validarEmail(novoEmail);
        telefone = normalizarOpcional(novoTelefone, 25);
        dataNascimento = validarDataNascimento(novaDataNascimento);
        cidade = normalizarOpcional(novaCidade, 100);
        estado = normalizarOpcional(novoEstado, 40);
        biografia = normalizarOpcional(novaBiografia, 300);
        fotoPerfilUrl = normalizarOpcional(novaFotoPerfilUrl, 500);
    }

    public void alterarSenha(String novaSenha) {
        this.senha = validarSenha(novaSenha);
    }

    String getSenhaArmazenada() {
        return senha;
    }

    private String validarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome do usuario e obrigatorio.");
        }
        return valor.trim();
    }

    private String validarEmail(String valor) {
        if (valor == null || valor.isBlank() || !valor.contains("@")) {
            throw new IllegalArgumentException("O email do usuario deve ser valido.");
        }
        return valor.trim().toLowerCase();
    }

    private String validarNomeUsuario(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O nome de usuario e obrigatorio.");
        }
        String normalizado = valor.trim().toLowerCase();
        if (!normalizado.matches("[a-z0-9._]{3,30}")) {
            throw new IllegalArgumentException(
                    "O nome de usuario deve ter de 3 a 30 caracteres e usar apenas letras, numeros, ponto ou sublinhado.");
        }
        return normalizado;
    }

    private LocalDate validarDataNascimento(LocalDate valor) {
        if (valor != null && valor.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento nao pode estar no futuro.");
        }
        return valor;
    }

    private String normalizarOpcional(String valor, int tamanhoMaximo) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String normalizado = valor.trim();
        if (normalizado.length() > tamanhoMaximo) {
            throw new IllegalArgumentException("Um dos dados informados ultrapassa o tamanho permitido.");
        }
        return normalizado;
    }

    private String validarSenha(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("A senha do usuario e obrigatoria.");
        }
        return valor;
    }

    private static String nomeUsuarioPadrao(String email, UsuarioId id) {
        String base = email == null ? "usuario" : email.split("@", 2)[0].toLowerCase()
                .replaceAll("[^a-z0-9._]", "");
        if (base.length() < 3) {
            base = "usuario";
        }
        String sufixo = String.valueOf(id.valor());
        sufixo = sufixo.substring(Math.max(0, sufixo.length() - 12));
        int limite = Math.min(base.length(), 30 - sufixo.length() - 1);
        return base.substring(0, limite) + "_" + sufixo;
    }
}
