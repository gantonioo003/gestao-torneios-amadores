package com.torneios.dominio.engajamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.engajamento.chat.ConsultaSuporteChat;
import com.torneios.dominio.engajamento.chat.GrupoChat;
import com.torneios.dominio.engajamento.chat.GrupoChatId;
import com.torneios.dominio.engajamento.chat.GrupoChatRepositorio;
import com.torneios.dominio.engajamento.chat.GrupoChatServico;
import com.torneios.dominio.engajamento.feed.Denuncia;
import com.torneios.dominio.engajamento.feed.DenunciaId;
import com.torneios.dominio.engajamento.feed.DenunciaRepositorio;
import com.torneios.dominio.engajamento.feed.ModeracaoFeedServico;
import com.torneios.dominio.engajamento.feed.StatusDenuncia;
import com.torneios.dominio.engajamento.feed.TipoAlvoDenuncia;
import com.torneios.dominio.engajamento.palpite.ProgressoPalpite;
import com.torneios.dominio.engajamento.palpite.ProgressoPalpiteRepositorio;
import com.torneios.dominio.engajamento.palpite.ProgressoPalpiteServico;
import com.torneios.dominio.engajamento.palpite.SeloPalpite;

class NovosRecursosEngajamentoTest {

    @Test
    void devePontuarParticipacaoSequenciaEAcerto() {
        ProgressoMemoria repositorio = new ProgressoMemoria();
        ProgressoPalpiteServico servico = new ProgressoPalpiteServico(repositorio);
        UsuarioId usuario = new UsuarioId(1L);

        servico.registrarNovoPalpite(usuario, LocalDate.of(2026, 6, 18));
        servico.registrarNovoPalpite(usuario, LocalDate.of(2026, 6, 18));
        servico.registrarNovoPalpite(usuario, LocalDate.of(2026, 6, 19));
        ProgressoPalpite progresso = servico.registrarApuracao(usuario, true);

        assertEquals(65, progresso.getPontos());
        assertEquals(2, progresso.getSequenciaAtual());
        assertEquals(3, progresso.getTotalPalpites());
        assertEquals(1, progresso.getTotalAcertos());
        assertTrue(progresso.getSelos().contains(SeloPalpite.PRIMEIRO_PALPITE));
    }

    @Test
    void deveSepararMembrosLiberadosDeConvitesPendentes() {
        UsuarioId criador = new UsuarioId(1L);
        UsuarioId contato = new UsuarioId(2L);
        UsuarioId comandado = new UsuarioId(3L);
        UsuarioId desconhecido = new UsuarioId(4L);
        GrupoMemoria repositorio = new GrupoMemoria();
        GrupoChatServico servico = new GrupoChatServico(repositorio, new ConsultaSuporteChat() {
            @Override public boolean usuarioEstaAutenticado(UsuarioId id) { return true; }
            @Override public boolean usuarioExiste(UsuarioId id) { return true; }
            @Override public boolean possuiConversaAprovada(UsuarioId a, UsuarioId b) { return b.equals(contato); }
            @Override public boolean usuarioEhComandadoPor(UsuarioId a, UsuarioId b) { return b.equals(comandado); }
        });

        GrupoChat grupo = servico.criar(
                new GrupoChatId(1L), "Elenco", criador, List.of(contato, comandado, desconhecido));

        assertTrue(grupo.possuiParticipante(contato));
        assertTrue(grupo.possuiParticipante(comandado));
        assertTrue(grupo.possuiConvitePendente(desconhecido));
        assertFalse(grupo.possuiParticipante(desconhecido));
        servico.aceitarConvite(grupo.getId(), desconhecido);
        assertTrue(grupo.possuiParticipante(desconhecido));
    }

    @Test
    void deveBloquearDenunciaPendenteDuplicada() {
        DenunciaMemoria repositorio = new DenunciaMemoria();
        ModeracaoFeedServico servico = new ModeracaoFeedServico(repositorio);
        UsuarioId usuario = new UsuarioId(1L);

        servico.denunciar(new DenunciaId(1L), usuario, TipoAlvoDenuncia.PUBLICACAO, 10L, "Conteudo ofensivo");

        assertThrows(RuntimeException.class, () -> servico.denunciar(
                new DenunciaId(2L), usuario, TipoAlvoDenuncia.PUBLICACAO, 10L, "Mesmo conteudo"));
    }

    private static class ProgressoMemoria implements ProgressoPalpiteRepositorio {
        private final Map<UsuarioId, ProgressoPalpite> dados = new LinkedHashMap<>();
        @Override public void salvar(ProgressoPalpite progresso) { dados.put(progresso.getUsuarioId(), progresso); }
        @Override public Optional<ProgressoPalpite> buscarPorUsuario(UsuarioId id) {
            return Optional.ofNullable(dados.get(id));
        }
        @Override public List<ProgressoPalpite> listarRanking() {
            return dados.values().stream().sorted((a, b) -> Integer.compare(b.getPontos(), a.getPontos())).toList();
        }
    }

    private static class GrupoMemoria implements GrupoChatRepositorio {
        private final Map<GrupoChatId, GrupoChat> dados = new LinkedHashMap<>();
        @Override public void salvar(GrupoChat grupo) { dados.put(grupo.getId(), grupo); }
        @Override public Optional<GrupoChat> buscarPorId(GrupoChatId id) { return Optional.ofNullable(dados.get(id)); }
        @Override public List<GrupoChat> listarPorUsuario(UsuarioId id) {
            return dados.values().stream()
                    .filter(grupo -> grupo.possuiParticipante(id) || grupo.possuiConvitePendente(id))
                    .toList();
        }
    }

    private static class DenunciaMemoria implements DenunciaRepositorio {
        private final List<Denuncia> dados = new ArrayList<>();
        @Override public void salvar(Denuncia denuncia) {
            dados.removeIf(item -> item.getId().equals(denuncia.getId()));
            dados.add(denuncia);
        }
        @Override public Optional<Denuncia> buscarPorId(DenunciaId id) {
            return dados.stream().filter(item -> item.getId().equals(id)).findFirst();
        }
        @Override public boolean existePendente(UsuarioId usuario, TipoAlvoDenuncia tipo, long alvoId) {
            return dados.stream().anyMatch(item -> item.getDenuncianteId().equals(usuario)
                    && item.getTipoAlvo() == tipo && item.getAlvoId() == alvoId
                    && item.getStatus() == StatusDenuncia.PENDENTE);
        }
        @Override public List<Denuncia> listarPendentes() {
            return dados.stream().filter(item -> item.getStatus() == StatusDenuncia.PENDENTE).toList();
        }
    }
}
