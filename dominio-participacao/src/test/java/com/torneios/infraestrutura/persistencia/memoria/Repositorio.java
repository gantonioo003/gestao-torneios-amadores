package com.torneios.infraestrutura.persistencia.memoria;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.enumeracao.StatusSolicitacao;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.ContaUsuario;
import com.torneios.dominio.participacao.acesso.ContaUsuarioRepositorio;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivo;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacao;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoRepositorio;
import com.torneios.dominio.participacao.time.Time;
import com.torneios.dominio.participacao.time.TimeRepositorio;

public class Repositorio implements TimeRepositorio, SolicitacaoParticipacaoRepositorio,
        ContaUsuarioRepositorio, ProfissionalEsportivoRepositorio {


    private final List<Time> times = new ArrayList<>();

    @Override
    public void salvar(Time time) {
        notNull(time, "O time nao pode ser nulo.");
        times.removeIf(t -> t.getId().equals(time.getId()));
        times.add(time);
    }

    @Override
    public Optional<Time> buscarPorId(TimeId id) {
        notNull(id, "O id do time nao pode ser nulo.");
        return times.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    @Override
    public List<Time> listarPorResponsavel(UsuarioId usuarioId) {
        notNull(usuarioId, "O id do usuario nao pode ser nulo.");
        return times.stream().filter(t -> t.getResponsavel().equals(usuarioId)).toList();
    }

    @Override
    public List<Time> listarTodos() {
        return List.copyOf(times);
    }

    @Override
    public void remover(TimeId timeId) {
        notNull(timeId, "O id do time nao pode ser nulo.");
        times.removeIf(t -> t.getId().equals(timeId));
    }



    private final List<SolicitacaoParticipacao> solicitacoes = new ArrayList<>();

    @Override
    public void salvar(SolicitacaoParticipacao solicitacao) {
        notNull(solicitacao, "A solicitacao nao pode ser nula.");
        solicitacoes.removeIf(s -> s.getId().equals(solicitacao.getId()));
        solicitacoes.add(solicitacao);
    }

    @Override
    public Optional<SolicitacaoParticipacao> buscarPorId(SolicitacaoParticipacaoId id) {
        notNull(id, "O id da solicitacao nao pode ser nulo.");
        return solicitacoes.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    @Override
    public List<SolicitacaoParticipacao> listarPendentesPorTorneio(TorneioId torneioId) {
        notNull(torneioId, "O id do torneio nao pode ser nulo.");
        return solicitacoes.stream()
                .filter(s -> s.getTorneioId().equals(torneioId) && s.getStatus() == StatusSolicitacao.PENDENTE)
                .toList();
    }

    @Override
    public List<SolicitacaoParticipacao> listarPorSolicitante(UsuarioId usuarioId) {
        notNull(usuarioId, "O id do solicitante nao pode ser nulo.");
        return solicitacoes.stream().filter(s -> s.getSolicitante().equals(usuarioId)).toList();
    }

    @Override
    public boolean existePendentePorTimeETorneio(TimeId timeId, TorneioId torneioId) {
        return solicitacoes.stream()
                .anyMatch(s -> s.getTimeId().equals(timeId)
                        && s.getTorneioId().equals(torneioId)
                        && s.getStatus() == StatusSolicitacao.PENDENTE);
    }



    private final Map<UsuarioId, ContaUsuario> contas = new LinkedHashMap<>();

    @Override
    public void salvar(ContaUsuario contaUsuario) {
        notNull(contaUsuario, "A conta de usuario nao pode ser nula.");
        contas.put(contaUsuario.getId(), contaUsuario);
    }

    @Override
    public Optional<ContaUsuario> buscarPorId(UsuarioId usuarioId) {
        notNull(usuarioId, "O id do usuario nao pode ser nulo.");
        return Optional.ofNullable(contas.get(usuarioId));
    }

    @Override
    public Optional<ContaUsuario> buscarPorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        String emailNormalizado = email.trim().toLowerCase();
        return contas.values().stream()
                .filter(c -> c.getEmail().equals(emailNormalizado))
                .findFirst();
    }

    @Override
    public void remover(UsuarioId usuarioId) {
        notNull(usuarioId, "O id do usuario nao pode ser nulo.");
        contas.remove(usuarioId);
    }



    private final Map<ProfissionalEsportivoId, ProfissionalEsportivo> profissionais = new LinkedHashMap<>();

    @Override
    public void salvar(ProfissionalEsportivo profissional) {
        notNull(profissional, "O profissional nao pode ser nulo.");
        profissionais.put(profissional.getId(), profissional);
    }

    @Override
    public Optional<ProfissionalEsportivo> buscarPorId(ProfissionalEsportivoId id) {
        notNull(id, "O id do profissional nao pode ser nulo.");
        return Optional.ofNullable(profissionais.get(id));
    }

    @Override
    public List<ProfissionalEsportivo> pesquisarPorNome(String nome) {
        return profissionais.values().stream()
                .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }

    @Override
    public void remover(ProfissionalEsportivoId id) {
        notNull(id, "O id do profissional nao pode ser nulo.");
        profissionais.remove(id);
    }


    public void limpar() {
        times.clear();
        solicitacoes.clear();
        contas.clear();
        profissionais.clear();
    }
}
