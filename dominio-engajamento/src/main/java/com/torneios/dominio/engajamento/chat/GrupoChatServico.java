package com.torneios.dominio.engajamento.chat;

import java.util.Collection;
import java.util.List;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class GrupoChatServico {

    private final GrupoChatRepositorio repositorio;
    private final ConsultaSuporteChat consultaSuporte;

    public GrupoChatServico(GrupoChatRepositorio repositorio, ConsultaSuporteChat consultaSuporte) {
        this.repositorio = repositorio;
        this.consultaSuporte = consultaSuporte;
    }

    public GrupoChat criar(GrupoChatId id, String nome, UsuarioId criadorId, Collection<UsuarioId> convidados) {
        validarAutenticado(criadorId);
        if (convidados == null || convidados.isEmpty()) {
            throw new IllegalArgumentException("Selecione pelo menos uma pessoa para criar o grupo.");
        }
        convidados.forEach(this::validarExistente);
        List<UsuarioId> participantesDiretos = convidados.stream()
                .filter(convidado -> consultaSuporte.possuiConversaAprovada(criadorId, convidado)
                        || consultaSuporte.usuarioEhComandadoPor(criadorId, convidado))
                .toList();
        List<UsuarioId> convitesPendentes = convidados.stream()
                .filter(convidado -> !participantesDiretos.contains(convidado))
                .toList();
        GrupoChat grupo = new GrupoChat(
                id, nome, criadorId, participantesDiretos, convitesPendentes, java.time.LocalDateTime.now());
        repositorio.salvar(grupo);
        return grupo;
    }

    public MensagemChat enviarMensagem(GrupoChatId grupoId, MensagemChatId mensagemId,
                                       UsuarioId autorId, String conteudo) {
        validarAutenticado(autorId);
        GrupoChat grupo = obter(grupoId);
        MensagemChat mensagem = grupo.enviarMensagem(mensagemId, autorId, conteudo);
        repositorio.salvar(grupo);
        return mensagem;
    }

    public List<GrupoChat> listar(UsuarioId usuarioId) {
        validarAutenticado(usuarioId);
        return repositorio.listarPorUsuario(usuarioId);
    }

    public GrupoChat consultar(GrupoChatId grupoId, UsuarioId usuarioId) {
        validarAutenticado(usuarioId);
        GrupoChat grupo = obter(grupoId);
        if (!grupo.possuiParticipante(usuarioId)) {
            throw new OperacaoNaoPermitidaException("Apenas participantes podem consultar o grupo.");
        }
        return grupo;
    }

    public GrupoChat aceitarConvite(GrupoChatId grupoId, UsuarioId usuarioId) {
        validarAutenticado(usuarioId);
        GrupoChat grupo = obter(grupoId);
        grupo.aceitarConvite(usuarioId);
        repositorio.salvar(grupo);
        return grupo;
    }

    public GrupoChat recusarConvite(GrupoChatId grupoId, UsuarioId usuarioId) {
        validarAutenticado(usuarioId);
        GrupoChat grupo = obter(grupoId);
        grupo.recusarConvite(usuarioId);
        repositorio.salvar(grupo);
        return grupo;
    }

    private GrupoChat obter(GrupoChatId id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Grupo de chat nao encontrado."));
    }
    private void validarAutenticado(UsuarioId id) {
        if (id == null || !consultaSuporte.usuarioEstaAutenticado(id)) {
            throw new OperacaoNaoPermitidaException("Apenas usuarios autenticados podem usar grupos.");
        }
    }
    private void validarExistente(UsuarioId id) {
        if (id == null || !consultaSuporte.usuarioExiste(id)) {
            throw new EntidadeNaoEncontradaException("Um dos participantes do grupo nao foi encontrado.");
        }
    }
}
