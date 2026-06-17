package com.torneios.infraestrutura.persistencia.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.ConsultaSuporteChat;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.acesso.ContaUsuarioRepositorio;

@Component
class ConsultaSuporteChatJpa implements ConsultaSuporteChat {

    @Autowired
    AutenticacaoServico autenticacaoServico;

    @Autowired
    ContaUsuarioRepositorio contaUsuarioRepositorio;

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return autenticacaoServico.estaAutenticado(usuarioId);
    }

    @Override
    public boolean usuarioExiste(UsuarioId usuarioId) {
        return contaUsuarioRepositorio.buscarPorId(usuarioId).isPresent();
    }
}
