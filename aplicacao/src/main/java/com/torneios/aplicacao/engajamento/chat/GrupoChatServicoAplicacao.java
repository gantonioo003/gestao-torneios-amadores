package com.torneios.aplicacao.engajamento.chat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.torneios.aplicacao.participacao.conta.ContaRepositorioAplicacao;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.GrupoChat;
import com.torneios.dominio.engajamento.chat.GrupoChatId;
import com.torneios.dominio.engajamento.chat.GrupoChatServico;
import com.torneios.dominio.engajamento.chat.MensagemChat;
import com.torneios.dominio.engajamento.chat.MensagemChatId;

public class GrupoChatServicoAplicacao {
    private final GrupoChatServico servico;
    private final ContaRepositorioAplicacao contas;

    public GrupoChatServicoAplicacao(GrupoChatServico servico, ContaRepositorioAplicacao contas) {
        this.servico = servico;
        this.contas = contas;
    }

    public GrupoResumo criar(long id, String nome, long criadorId, Collection<Long> participantes) {
        List<UsuarioId> convidados = participantes == null ? List.of() : participantes.stream()
                .filter(participanteId -> participanteId != criadorId)
                .distinct()
                .map(UsuarioId::new)
                .toList();
        return converter(servico.criar(new GrupoChatId(id), nome, new UsuarioId(criadorId), convidados));
    }

    public MensagemResumo enviarMensagem(long grupoId, long mensagemId, long autorId, String conteudo) {
        return converter(servico.enviarMensagem(
                new GrupoChatId(grupoId),
                new MensagemChatId(mensagemId),
                new UsuarioId(autorId),
                conteudo));
    }

    public List<GrupoResumo> listar(long usuarioId) {
        return servico.listar(new UsuarioId(usuarioId)).stream()
                .sorted(Comparator.comparing(GrupoChat::getUltimaAtividadeEm).reversed())
                .map(this::converter)
                .toList();
    }

    public GrupoResumo consultar(long grupoId, long usuarioId) {
        return converter(servico.consultar(new GrupoChatId(grupoId), new UsuarioId(usuarioId)));
    }

    public GrupoResumo aceitarConvite(long grupoId, long usuarioId) {
        return converter(servico.aceitarConvite(new GrupoChatId(grupoId), new UsuarioId(usuarioId)));
    }

    public GrupoResumo recusarConvite(long grupoId, long usuarioId) {
        return converter(servico.recusarConvite(new GrupoChatId(grupoId), new UsuarioId(usuarioId)));
    }

    private GrupoResumo converter(GrupoChat grupo) {
        List<ParticipanteResumo> participantes = grupo.getParticipantes().stream()
                .map(id -> contas.pesquisarPorId(id.valor())
                        .map(conta -> new ParticipanteResumo(id.valor(), conta.getNome(), conta.getEmail()))
                        .orElse(new ParticipanteResumo(id.valor(), "Usuario", "")))
                .toList();
        List<ParticipanteResumo> pendentes = grupo.getConvitesPendentes().stream()
                .map(id -> contas.pesquisarPorId(id.valor())
                        .map(conta -> new ParticipanteResumo(id.valor(), conta.getNome(), conta.getEmail()))
                        .orElse(new ParticipanteResumo(id.valor(), "Usuario", "")))
                .toList();
        return new GrupoResumo(
                grupo.getId().valor(),
                grupo.getNome(),
                grupo.getCriadorId().valor(),
                grupo.getCriadoEm(),
                grupo.getUltimaAtividadeEm(),
                participantes,
                pendentes,
                grupo.getMensagens().stream().map(this::converter).toList());
    }

    private MensagemResumo converter(MensagemChat mensagem) {
        return new MensagemResumo(
                mensagem.getId().valor(),
                mensagem.getAutorId().valor(),
                mensagem.getConteudo(),
                mensagem.getEnviadaEm());
    }

    public record GrupoResumo(long id, String nome, long criadorId, LocalDateTime criadoEm,
                              LocalDateTime ultimaAtividadeEm, List<ParticipanteResumo> participantes,
                              List<ParticipanteResumo> convitesPendentes,
                              List<MensagemResumo> mensagens) {}
    public record ParticipanteResumo(long id, String nome, String email) {}
    public record MensagemResumo(long id, long autorId, String conteudo, LocalDateTime enviadaEm) {}
}
