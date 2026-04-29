package com.torneios.infraestrutura.persistencia.memoria;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.ContaUsuario;
import com.torneios.dominio.participacao.acesso.ContaUsuarioRepositorio;

public class ContaUsuarioRepositorioMemoria implements ContaUsuarioRepositorio {

    private final Map<UsuarioId, ContaUsuario> dados = new LinkedHashMap<>();

    @Override
    public void salvar(ContaUsuario contaUsuario) {
        dados.put(contaUsuario.getId(), contaUsuario);
    }

    @Override
    public Optional<ContaUsuario> buscarPorId(UsuarioId usuarioId) {
        return Optional.ofNullable(dados.get(usuarioId));
    }

    @Override
    public Optional<ContaUsuario> buscarPorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        String emailNormalizado = email.trim().toLowerCase();
        return dados.values().stream()
                .filter(contaUsuario -> contaUsuario.getEmail().equals(emailNormalizado))
                .findFirst();
    }

    @Override
    public void remover(UsuarioId usuarioId) {
        dados.remove(usuarioId);
    }

    public void limpar() {
        dados.clear();
    }
}
