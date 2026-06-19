package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashSet;
import java.util.Set;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.ConsultaSuporteChat;

public class ConsultaSuporteChatMemoria implements ConsultaSuporteChat {

    private final Set<UsuarioId> usuariosAutenticados = new HashSet<>();
    private final Set<UsuarioId> usuariosCadastrados = new HashSet<>();
    private final Set<String> conversasAprovadas = new HashSet<>();
    private final Set<String> vinculosComando = new HashSet<>();

    public void cadastrarUsuario(UsuarioId usuarioId) {
        usuariosCadastrados.add(usuarioId);
    }

    public void autenticar(UsuarioId usuarioId) {
        usuariosCadastrados.add(usuarioId);
        usuariosAutenticados.add(usuarioId);
    }

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return usuariosAutenticados.contains(usuarioId);
    }

    @Override
    public boolean usuarioExiste(UsuarioId usuarioId) {
        return usuariosCadastrados.contains(usuarioId);
    }

    @Override
    public boolean possuiConversaAprovada(UsuarioId primeiroUsuarioId, UsuarioId segundoUsuarioId) {
        return conversasAprovadas.contains(chave(primeiroUsuarioId, segundoUsuarioId));
    }

    @Override
    public boolean usuarioEhComandadoPor(UsuarioId treinadorId, UsuarioId profissionalUsuarioId) {
        return vinculosComando.contains(treinadorId.valor() + ":" + profissionalUsuarioId.valor());
    }

    public void registrarConversaAprovada(UsuarioId primeiro, UsuarioId segundo) {
        conversasAprovadas.add(chave(primeiro, segundo));
    }

    public void registrarComandado(UsuarioId treinador, UsuarioId profissional) {
        vinculosComando.add(treinador.valor() + ":" + profissional.valor());
    }

    private String chave(UsuarioId primeiro, UsuarioId segundo) {
        long menor = Math.min(primeiro.valor(), segundo.valor());
        long maior = Math.max(primeiro.valor(), segundo.valor());
        return menor + ":" + maior;
    }
}
