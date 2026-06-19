package com.torneios.infraestrutura.persistencia.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.ConsultaSuporteChat;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.acesso.ContaUsuarioRepositorio;
import com.torneios.dominio.engajamento.chat.ConversaPrivadaRepositorio;
import com.torneios.dominio.participacao.time.TimeRepositorio;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;

@Component
class ConsultaSuporteChatJpa implements ConsultaSuporteChat {

    @Autowired
    AutenticacaoServico autenticacaoServico;

    @Autowired
    ContaUsuarioRepositorio contaUsuarioRepositorio;

    @Autowired
    ConversaPrivadaRepositorio conversaPrivadaRepositorio;

    @Autowired
    TimeRepositorio timeRepositorio;

    @Autowired
    ProfissionalEsportivoRepositorio profissionalRepositorio;

    @Override
    public boolean usuarioEstaAutenticado(UsuarioId usuarioId) {
        return autenticacaoServico.estaAutenticado(usuarioId);
    }

    @Override
    public boolean usuarioExiste(UsuarioId usuarioId) {
        return contaUsuarioRepositorio.buscarPorId(usuarioId).isPresent();
    }

    @Override
    public boolean possuiConversaAprovada(UsuarioId primeiroUsuarioId, UsuarioId segundoUsuarioId) {
        return conversaPrivadaRepositorio.listarAprovadasPorUsuario(primeiroUsuarioId).stream()
                .anyMatch(conversa -> conversa.envolveAmbos(primeiroUsuarioId, segundoUsuarioId));
    }

    @Override
    public boolean usuarioEhComandadoPor(UsuarioId treinadorId, UsuarioId profissionalUsuarioId) {
        return timeRepositorio.listarPorResponsavel(treinadorId).stream()
                .flatMap(time -> time.getElenco().stream())
                .flatMap(vinculo -> profissionalRepositorio.buscarPorId(vinculo.getProfissionalId()).stream())
                .anyMatch(profissional -> profissional.getCadastranteId().equals(profissionalUsuarioId));
    }
}
