package com.torneios.infraestrutura.persistencia.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.torneios.aplicacao.participacao.conta.ContaRepositorioAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaUsuarioResumo;

@Repository
class ContaRepositorioAplicacaoImpl implements ContaRepositorioAplicacao {

    @Autowired
    ContaUsuarioJpaRepository repositorio;

    @Override
    public Optional<ContaUsuarioResumo> pesquisarPorId(long usuarioId) {
        return repositorio.findById(usuarioId).map(ContaUsuarioJpaResumo::new);
    }

    @Override
    public Optional<ContaUsuarioResumo> pesquisarPorNomeUsuario(String nomeUsuario) {
        if (nomeUsuario == null || nomeUsuario.isBlank()) {
            return Optional.empty();
        }
        return repositorio.findByNomeUsuario(nomeUsuario.trim().toLowerCase())
                .map(ContaUsuarioJpaResumo::new);
    }

    @Override
    public List<ContaUsuarioResumo> pesquisarUsuarios(String termo, long usuarioIdAtual) {
        String filtro = termo == null ? "" : termo.trim().toLowerCase();
        return repositorio.findAll().stream()
                .filter(conta -> !conta.id.equals(usuarioIdAtual))
                .filter(conta -> filtro.isBlank()
                        || conta.nome.toLowerCase().contains(filtro)
                        || conta.nomeUsuario.toLowerCase().contains(filtro)
                        || conta.email.toLowerCase().contains(filtro))
                .sorted((primeira, segunda) -> primeira.nome.compareToIgnoreCase(segunda.nome))
                .limit(30)
                .map(ContaUsuarioJpaResumo::new)
                .map(ContaUsuarioResumo.class::cast)
                .toList();
    }
}

record ContaUsuarioJpaResumo(ContaUsuarioJpa jpa) implements ContaUsuarioResumo {
    @Override
    public Long getId() {
        return jpa.id;
    }

    @Override
    public String getNome() {
        return jpa.nome;
    }

    @Override
    public String getNomeUsuario() {
        return jpa.nomeUsuario;
    }

    @Override
    public String getEmail() {
        return jpa.email;
    }

    @Override
    public String getTelefone() {
        return jpa.telefone;
    }

    @Override
    public java.time.LocalDate getDataNascimento() {
        return jpa.dataNascimento;
    }

    @Override
    public String getCidade() {
        return jpa.cidade;
    }

    @Override
    public String getEstado() {
        return jpa.estado;
    }

    @Override
    public String getBiografia() {
        return jpa.biografia;
    }

    @Override
    public String getFotoPerfilUrl() {
        return jpa.fotoPerfilUrl;
    }

    @Override
    public String getTipo() {
        return jpa.tipo;
    }

    @Override
    public String getProvedor() {
        return jpa.provedor == null ? "LOCAL" : jpa.provedor;
    }

    @Override
    public boolean isPodeCriarTorneio() {
        return com.torneios.dominio.participacao.acesso.TipoContaUsuario.valueOf(jpa.tipo).podeCriarTorneio();
    }

    @Override
    public boolean isPodeGerenciarTimes() {
        return com.torneios.dominio.participacao.acesso.TipoContaUsuario.valueOf(jpa.tipo).podeGerenciarTimes();
    }

    @Override
    public boolean isPossuiPerfilProfissional() {
        return com.torneios.dominio.participacao.acesso.TipoContaUsuario.valueOf(jpa.tipo).possuiPerfilProfissional();
    }

    @Override
    public List<Long> getTorneiosSalvos() {
        return List.copyOf(jpa.torneiosSalvos);
    }
}
