package com.torneios.infraestrutura.persistencia.jpa;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.ContaUsuario;
import com.torneios.dominio.participacao.acesso.ContaUsuarioRepositorio;
import com.torneios.dominio.participacao.acesso.TipoContaUsuario;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTA_USUARIO")
class ContaUsuarioJpa {
    @Id
    Long id;
    String nome;
    String email;
    String senha;
    String tipo;
}

interface ContaUsuarioJpaRepository extends JpaRepository<ContaUsuarioJpa, Long> {
    Optional<ContaUsuarioJpa> findByEmail(String email);
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
        jpa.email = conta.getEmail();
        jpa.senha = ReflexaoDominioJpa.valorCampo(conta, "senha", String.class);
        jpa.tipo = conta.getTipo().name();
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
    public void remover(UsuarioId usuarioId) {
        repositorio.deleteById(usuarioId.valor());
    }

    private ContaUsuario toDomain(ContaUsuarioJpa jpa) {
        return new ContaUsuario(
            new UsuarioId(jpa.id),
            jpa.nome,
            jpa.email,
            jpa.senha,
            TipoContaUsuario.valueOf(jpa.tipo)
        );
    }
}
