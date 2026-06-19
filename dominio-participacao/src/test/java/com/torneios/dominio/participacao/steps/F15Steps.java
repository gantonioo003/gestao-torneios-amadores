package com.torneios.dominio.participacao.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.ParticipacaoFuncionalidade;
import com.torneios.dominio.participacao.acesso.CategoriaNotificacao;
import com.torneios.dominio.participacao.acesso.Notificacao;
import com.torneios.dominio.participacao.acesso.NotificacaoId;
import com.torneios.dominio.participacao.acesso.NotificacaoRepositorio;
import com.torneios.dominio.participacao.acesso.NotificacaoServico;
import com.torneios.dominio.participacao.acesso.PreferenciasNotificacao;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class F15Steps extends ParticipacaoFuncionalidade {

    private static final NotificacaoId NOTIFICACAO_ID = new NotificacaoId(1501L);
    private static final RepositorioMemoria notificacaoRepositorio = new RepositorioMemoria();
    private static final NotificacaoServico notificacaoServico =
            new NotificacaoServico(notificacaoRepositorio);

    private boolean notificacaoGerada;

    @Dado("que o usuario possui todas as categorias de notificacao habilitadas")
    public void todasCategoriasHabilitadas() {
        notificacaoRepositorio.limpar();
        notificacaoServico.atualizarPreferencias(
                USUARIO_AUTENTICADO_ID,
                EnumSet.allOf(CategoriaNotificacao.class));
    }

    @Dado("que o usuario possui uma notificacao nao lida")
    public void usuarioPossuiNotificacaoNaoLida() {
        todasCategoriasHabilitadas();
        enviar(NOTIFICACAO_ID, CategoriaNotificacao.TORNEIO);
    }

    @Dado("que o usuario possui duas notificacoes nao lidas")
    public void usuarioPossuiDuasNotificacoesNaoLidas() {
        todasCategoriasHabilitadas();
        enviar(NOTIFICACAO_ID, CategoriaNotificacao.TORNEIO);
        enviar(new NotificacaoId(1502L), CategoriaNotificacao.AMISTOSO);
    }

    @Dado("que o usuario desativou notificacoes de amistoso")
    public void usuarioDesativouAmistoso() {
        notificacaoRepositorio.limpar();
        notificacaoServico.atualizarPreferencias(
                USUARIO_AUTENTICADO_ID,
                EnumSet.of(
                        CategoriaNotificacao.TORNEIO,
                        CategoriaNotificacao.TIME,
                        CategoriaNotificacao.SOCIAL,
                        CategoriaNotificacao.SISTEMA));
    }

    @Quando("o sistema enviar uma notificacao de torneio")
    public void sistemaEnviarNotificacaoTorneio() {
        notificacaoGerada = enviar(NOTIFICACAO_ID, CategoriaNotificacao.TORNEIO);
    }

    @Quando("ele marcar a notificacao como lida")
    public void marcarComoLida() {
        notificacaoServico.marcarComoLida(NOTIFICACAO_ID, USUARIO_AUTENTICADO_ID);
    }

    @Quando("ele marcar todas as notificacoes como lidas")
    public void marcarTodasComoLidas() {
        notificacaoServico.marcarTodasComoLidas(USUARIO_AUTENTICADO_ID);
    }

    @Quando("ele arquivar a notificacao")
    public void arquivarNotificacao() {
        notificacaoServico.arquivar(NOTIFICACAO_ID, USUARIO_AUTENTICADO_ID);
    }

    @Quando("o sistema tentar enviar uma notificacao de amistoso")
    public void tentarEnviarNotificacaoAmistoso() {
        notificacaoGerada = enviar(NOTIFICACAO_ID, CategoriaNotificacao.AMISTOSO);
    }

    @Quando("outro usuario tentar marcar a notificacao como lida")
    public void outroUsuarioTentarMarcarComoLida() {
        try {
            notificacaoServico.marcarComoLida(NOTIFICACAO_ID, OUTRO_USUARIO_ID);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("a notificacao deve ser salva como nao lida")
    public void notificacaoSalvaNaoLida() {
        assertTrue(notificacaoGerada);
        Notificacao notificacao = notificacaoRepositorio.buscarPorId(NOTIFICACAO_ID).orElseThrow();
        assertFalse(notificacao.isLida());
        assertEquals(CategoriaNotificacao.TORNEIO, notificacao.getCategoria());
    }

    @Entao("a notificacao deve permanecer no historico como lida")
    public void notificacaoPermaneceLida() {
        Notificacao notificacao = notificacaoRepositorio.buscarPorId(NOTIFICACAO_ID).orElseThrow();
        assertTrue(notificacao.isLida());
        assertFalse(notificacao.isArquivada());
    }

    @Entao("nenhuma notificacao ativa deve permanecer nao lida")
    public void nenhumaAtivaNaoLida() {
        assertTrue(notificacaoServico.listar(USUARIO_AUTENTICADO_ID, false).stream()
                .allMatch(Notificacao::isLida));
    }

    @Entao("ela deve sair da lista ativa e permanecer no historico arquivado")
    public void saiDaListaAtivaPermaneceArquivada() {
        assertTrue(notificacaoServico.listar(USUARIO_AUTENTICADO_ID, false).isEmpty());
        List<Notificacao> historico = notificacaoServico.listar(USUARIO_AUTENTICADO_ID, true);
        assertEquals(1, historico.size());
        assertTrue(historico.get(0).isArquivada());
    }

    @Entao("nenhuma notificacao de amistoso deve ser salva")
    public void nenhumaNotificacaoAmistosoSalva() {
        assertFalse(notificacaoGerada);
        assertTrue(notificacaoRepositorio.listarPorUsuario(USUARIO_AUTENTICADO_ID).isEmpty());
    }

    private boolean enviar(NotificacaoId id, CategoriaNotificacao categoria) {
        return notificacaoServico.notificar(
                id,
                USUARIO_AUTENTICADO_ID,
                categoria,
                "Aviso de teste",
                "Uma atualizacao importante aconteceu.",
                "/notificacoes");
    }

    private static class RepositorioMemoria implements NotificacaoRepositorio {
        private final List<Notificacao> notificacoes = new ArrayList<>();
        private PreferenciasNotificacao preferencias;

        @Override
        public void salvar(Notificacao notificacao) {
            notificacoes.removeIf(item -> item.getId().equals(notificacao.getId()));
            notificacoes.add(notificacao);
        }

        @Override
        public Optional<Notificacao> buscarPorId(NotificacaoId id) {
            return notificacoes.stream().filter(item -> item.getId().equals(id)).findFirst();
        }

        @Override
        public List<Notificacao> listarPorUsuario(UsuarioId usuarioId) {
            return notificacoes.stream()
                    .filter(item -> item.getUsuarioId().equals(usuarioId))
                    .toList();
        }

        @Override
        public void salvarPreferencias(PreferenciasNotificacao preferencias) {
            this.preferencias = preferencias;
        }

        @Override
        public Optional<PreferenciasNotificacao> buscarPreferencias(UsuarioId usuarioId) {
            return Optional.ofNullable(preferencias)
                    .filter(item -> item.getUsuarioId().equals(usuarioId));
        }

        void limpar() {
            notificacoes.clear();
            preferencias = null;
        }
    }
}
