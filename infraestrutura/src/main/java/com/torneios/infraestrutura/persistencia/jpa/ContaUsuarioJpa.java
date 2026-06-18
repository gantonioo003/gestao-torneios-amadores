package com.torneios.infraestrutura.persistencia.jpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.ContaUsuario;
import com.torneios.dominio.participacao.acesso.ContaUsuarioRepositorio;
import com.torneios.dominio.participacao.acesso.ProvedorAutenticacao;
import com.torneios.dominio.participacao.acesso.TipoContaUsuario;

import jakarta.persistence.Entity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTA_USUARIO")
class ContaUsuarioJpa {
    @Id
    Long id;
    String nome;
    String nomeUsuario;
    String email;
    String telefone;
    LocalDate dataNascimento;
    String cidade;
    String estado;
    String biografia;
    String fotoPerfilUrl;
    String senha;
    String tipo;
    String provedor;

    @ElementCollection
    @CollectionTable(name = "CONTA_TORNEIO_SALVO", joinColumns = @JoinColumn(name = "CONTA_ID"))
    @Column(name = "TORNEIO_ID")
    Set<Long> torneiosSalvos = new LinkedHashSet<>();
}

interface ContaUsuarioJpaRepository extends JpaRepository<ContaUsuarioJpa, Long> {
    Optional<ContaUsuarioJpa> findByEmail(String email);
    Optional<ContaUsuarioJpa> findByNomeUsuario(String nomeUsuario);
}

@Repository
class ContaUsuarioRepositorioImpl implements ContaUsuarioRepositorio {

    @Autowired
    ContaUsuarioJpaRepository repositorio;

    @Override
    public void salvar(ContaUsuario conta) {
        var jpa = repositorio.findById(conta.getId().valor()).orElse(new ContaUsuarioJpa());
        jpa.id = conta.getId().valor();
        jpa.nome = conta.getNome();
        jpa.nomeUsuario = conta.getNomeUsuario();
        jpa.email = conta.getEmail();
        jpa.telefone = conta.getTelefone();
        jpa.dataNascimento = conta.getDataNascimento();
        jpa.cidade = conta.getCidade();
        jpa.estado = conta.getEstado();
        jpa.biografia = conta.getBiografia();
        jpa.fotoPerfilUrl = conta.getFotoPerfilUrl();
        jpa.senha = ReflexaoDominioJpa.valorCampo(conta, "senha", String.class);
        jpa.tipo = conta.getTipo().name();
        jpa.provedor = conta.getProvedor().name();
        jpa.torneiosSalvos.clear();
        conta.getTorneiosSalvos().forEach(torneioId -> jpa.torneiosSalvos.add(torneioId.valor()));
        repositorio.save(jpa);
    }

    @Override
    public Optional<ContaUsuario> buscarPorId(UsuarioId usuarioId) {
        return repositorio.findById(usuarioId.valor()).map(this::toDomain);
    }

    @Override
    public Optional<ContaUsuario> buscarPorEmail(String email) {
        return repositorio.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<ContaUsuario> buscarPorNomeUsuario(String nomeUsuario) {
        return repositorio.findByNomeUsuario(nomeUsuario).map(this::toDomain);
    }

    @Override
    public void remover(UsuarioId usuarioId) {
        repositorio.deleteById(usuarioId.valor());
    }

    private ContaUsuario toDomain(ContaUsuarioJpa jpa) {
        ContaUsuario conta = new ContaUsuario(
            new UsuarioId(jpa.id),
            jpa.nome,
            jpa.nomeUsuario,
            jpa.email,
            jpa.telefone,
            jpa.dataNascimento,
            jpa.cidade,
            jpa.estado,
            jpa.biografia,
            jpa.fotoPerfilUrl,
            jpa.senha,
            TipoContaUsuario.valueOf(jpa.tipo),
            jpa.provedor == null ? ProvedorAutenticacao.LOCAL : ProvedorAutenticacao.valueOf(jpa.provedor)
        );
        jpa.torneiosSalvos.forEach(id -> conta.salvarTorneio(
                new com.torneios.dominio.compartilhado.torneio.TorneioId(id)));
        return conta;
    }
}
