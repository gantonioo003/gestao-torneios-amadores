package com.torneios.dominio.engajamento.palpite;

import java.time.LocalDate;
import java.util.List;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class ProgressoPalpiteServico {

    private final ProgressoPalpiteRepositorio repositorio;

    public ProgressoPalpiteServico(ProgressoPalpiteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public ProgressoPalpite registrarNovoPalpite(UsuarioId usuarioId, LocalDate data) {
        ProgressoPalpite progresso = obterOuCriar(usuarioId);
        progresso.registrarNovoPalpite(data);
        repositorio.salvar(progresso);
        return progresso;
    }

    public ProgressoPalpite registrarApuracao(UsuarioId usuarioId, boolean acertou) {
        ProgressoPalpite progresso = obterOuCriar(usuarioId);
        progresso.registrarApuracao(acertou);
        repositorio.salvar(progresso);
        return progresso;
    }

    public ProgressoPalpite consultar(UsuarioId usuarioId) {
        return obterOuCriar(usuarioId);
    }

    public List<ProgressoPalpite> ranking() {
        return repositorio.listarRanking();
    }

    private ProgressoPalpite obterOuCriar(UsuarioId usuarioId) {
        return repositorio.buscarPorUsuario(usuarioId).orElseGet(() -> new ProgressoPalpite(usuarioId));
    }
}
