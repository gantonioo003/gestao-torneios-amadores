package com.torneios.infraestrutura.persistencia.jpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.enumeracao.StatusTorneio;
import com.torneios.dominio.compartilhado.evento.EventoBarramento;
import com.torneios.dominio.compartilhado.evento.EventoObservador;
import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.competicao.partida.ConsultaCompeticaoTorneio;
import com.torneios.dominio.participacao.profissional.TipoProfissional;
import com.torneios.dominio.participacao.time.TimeRepositorio;
import com.torneios.dominio.torneio.torneio.ConsultaElegibilidadeParticipanteTorneio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;

// ─────────────────────────────────────────────────────────────────────────────
// EventoBarramento — implementação simples em memória
// ─────────────────────────────────────────────────────────────────────────────

@Component
class EventoBarramentoImpl implements EventoBarramento {

    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, List<EventoObservador>> observadores = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <E> void adicionar(EventoObservador<E> observador) {
        observadores.computeIfAbsent(Object.class, k -> new ArrayList<>()).add(observador);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <E> void postar(E evento) {
        List<EventoObservador> lista = observadores.getOrDefault(Object.class, Collections.emptyList());
        for (EventoObservador obs : lista) {
            try { obs.aoOcorrer(evento); } catch (Exception ignored) { }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ConsultaElegibilidadeParticipanteTorneio — baseada no TimeRepositorio JPA
// ─────────────────────────────────────────────────────────────────────────────

@Component("consultaElegibilidadeJpa")
class ConsultaElegibilidadeParticipanteTorneioJpa implements ConsultaElegibilidadeParticipanteTorneio {

    @Autowired TimeRepositorio timeRepositorio;

    @Override
    public boolean timeExiste(TimeId timeId) {
        return timeRepositorio.buscarPorId(timeId).isPresent();
    }

    @Override
    public boolean timePossuiTecnico(TimeId timeId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(t -> t.getTecnico() != null)
                .orElse(false);
    }

    @Override
    public int quantidadeJogadores(TimeId timeId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(t -> (int) t.getElenco().stream()
                        .filter(v -> TipoProfissional.JOGADOR.name().equals(v.getFuncao()))
                        .count())
                .orElse(0);
    }

    @Override
    public void vincularTimeAoTorneio(TimeId timeId, TorneioId torneioId) {
        timeRepositorio.buscarPorId(timeId).ifPresent(t -> {
            t.vincularAoTorneio(torneioId);
            timeRepositorio.salvar(t);
        });
    }

    @Override
    public void removerVinculoDoTimeAoTorneio(TimeId timeId, TorneioId torneioId) {
        timeRepositorio.buscarPorId(timeId).ifPresent(t -> {
            t.removerVinculoTorneio(torneioId);
            timeRepositorio.salvar(t);
        });
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ConsultaCompeticaoTorneio — baseada no TorneioRepositorio JPA
// ─────────────────────────────────────────────────────────────────────────────

@Component
class ConsultaCompeticaoTorneioJpa implements ConsultaCompeticaoTorneio {

    @Autowired TorneioRepositorio torneioRepositorio;

    private com.torneios.dominio.torneio.torneio.Torneio obter(TorneioId torneioId) {
        return torneioRepositorio.buscarPorId(torneioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Torneio nao encontrado."));
    }

    @Override
    public boolean estruturaGerada(TorneioId torneioId) {
        return obter(torneioId).getStatus() == StatusTorneio.ESTRUTURA_GERADA
            || obter(torneioId).getStatus() == StatusTorneio.INICIADO
            || obter(torneioId).getStatus() == StatusTorneio.FINALIZADO;
    }

    @Override
    public boolean usuarioEhOrganizador(TorneioId torneioId,
            com.torneios.dominio.compartilhado.usuario.UsuarioId usuarioId) {
        return obter(torneioId).getOrganizadorId().equals(usuarioId);
    }

    @Override
    public FormatoTorneio obterFormato(TorneioId torneioId) {
        return obter(torneioId).getFormato();
    }

    @Override
    public int obterQuantidadeJogadoresPorEquipe(TorneioId torneioId) {
        return obter(torneioId).getFormatoEquipe().getQuantidadeJogadores();
    }

    @Override
    public List<TimeId> listarParticipantesAprovados(TorneioId torneioId) {
        return obter(torneioId).getParticipantesAprovados().stream()
                .map(p -> p.getTimeId())
                .toList();
    }

    @Override
    public List<List<TimeId>> listarGrupos(TorneioId torneioId) {
        var torneio = obter(torneioId);
        if (torneio.getFormato() != FormatoTorneio.FASE_DE_GRUPOS_COM_MATA_MATA) {
            return Collections.emptyList();
        }
        List<TimeId> participantes = listarParticipantesAprovados(torneioId);
        if (participantes.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<TimeId>> grupos = new ArrayList<>();
        grupos.add(new ArrayList<>());
        grupos.add(new ArrayList<>());
        for (int i = 0; i < participantes.size(); i++) {
            grupos.get(i % grupos.size()).add(participantes.get(i));
        }
        return grupos;
    }
}
