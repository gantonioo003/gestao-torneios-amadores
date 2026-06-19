package com.torneios.aplicacao.participacao.notificacao;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.CategoriaNotificacao;
import com.torneios.dominio.participacao.acesso.Notificacao;
import com.torneios.dominio.participacao.acesso.NotificacaoId;
import com.torneios.dominio.participacao.acesso.NotificacaoServico;
import com.torneios.dominio.participacao.acesso.PreferenciasNotificacao;

public class NotificacaoParticipacaoServicoAplicacao {

    private final NotificacaoServico servico;

    public NotificacaoParticipacaoServicoAplicacao(NotificacaoServico servico) {
        this.servico = servico;
    }

    public void notificarTimeAceito(long id, long treinadorId, long timeId, long torneioId) {
        salvar(id, treinadorId, "Seu time foi aceito no torneio",
                "O organizador aceitou a solicitacao de participacao do seu time.",
                "/time/" + timeId + "/detalhes", CategoriaNotificacao.TORNEIO);
    }

    public void notificarConviteAceito(long id, long organizadorId, long timeId, long torneioId) {
        salvar(id, organizadorId, "O time aceitou o convite",
                "O treinador confirmou que o time participara do torneio.",
                "/torneio/" + torneioId + "?configurar=true", CategoriaNotificacao.TORNEIO);
    }

    public void notificarDesafioRecebido(long treinadorId, long timeId) {
        salvar(novoId(), treinadorId, "Novo desafio amistoso",
                "Outro time enviou uma proposta de amistoso. Confira data, horario e local.",
                "/time/" + timeId + "/detalhes?aba=confrontos&status=recebidos",
                CategoriaNotificacao.AMISTOSO);
    }

    public void notificarDesafioAceito(long treinadorId, long timeId) {
        salvar(novoId(), treinadorId, "Amistoso confirmado",
                "O desafio foi aceito e o confronto ja aparece entre os amistosos confirmados.",
                "/time/" + timeId + "/detalhes?aba=confrontos&status=confirmados",
                CategoriaNotificacao.AMISTOSO);
    }

    public List<NotificacaoResumo> listar(long usuarioId, boolean incluirArquivadas) {
        return servico.listar(new UsuarioId(usuarioId), incluirArquivadas).stream()
                .map(this::converter)
                .toList();
    }

    public void marcarComoLida(long notificacaoId, long usuarioId) {
        servico.marcarComoLida(new NotificacaoId(notificacaoId), new UsuarioId(usuarioId));
    }

    public void marcarTodasComoLidas(long usuarioId) {
        servico.marcarTodasComoLidas(new UsuarioId(usuarioId));
    }

    public void arquivar(long notificacaoId, long usuarioId) {
        servico.arquivar(new NotificacaoId(notificacaoId), new UsuarioId(usuarioId));
    }

    public PreferenciasResumo obterPreferencias(long usuarioId) {
        return converter(servico.obterPreferencias(new UsuarioId(usuarioId)));
    }

    public PreferenciasResumo atualizarPreferencias(long usuarioId, List<String> categoriasAtivas) {
        Set<CategoriaNotificacao> categorias = categoriasAtivas == null
                ? Set.of()
                : categoriasAtivas.stream()
                        .map(CategoriaNotificacao::valueOf)
                        .collect(Collectors.toSet());
        return converter(servico.atualizarPreferencias(new UsuarioId(usuarioId), categorias));
    }

    public List<String> listarCategorias() {
        return Arrays.stream(CategoriaNotificacao.values()).map(Enum::name).toList();
    }

    private void salvar(long id,
                        long usuarioId,
                        String titulo,
                        String mensagem,
                        String link,
                        CategoriaNotificacao categoria) {
        servico.notificar(
                new NotificacaoId(id),
                new UsuarioId(usuarioId),
                categoria,
                titulo,
                mensagem,
                link);
    }

    private long novoId() {
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return id == 0 ? 1L : id;
    }

    private NotificacaoResumo converter(Notificacao notificacao) {
        return new NotificacaoResumo(
                String.valueOf(notificacao.getId().valor()),
                notificacao.getCategoria().name(),
                notificacao.getTitulo(),
                notificacao.getMensagem(),
                notificacao.getLink(),
                notificacao.isLida(),
                notificacao.isArquivada(),
                notificacao.getCriadaEm());
    }

    private PreferenciasResumo converter(PreferenciasNotificacao preferencias) {
        return new PreferenciasResumo(
                preferencias.getCategoriasAtivas().stream()
                        .map(Enum::name)
                        .sorted()
                        .toList());
    }

    public record NotificacaoResumo(String id,
                                    String categoria,
                                    String titulo,
                                    String mensagem,
                                    String link,
                                    boolean lida,
                                    boolean arquivada,
                                    LocalDateTime criadaEm) {
    }

    public record PreferenciasResumo(List<String> categoriasAtivas) {
    }
}
