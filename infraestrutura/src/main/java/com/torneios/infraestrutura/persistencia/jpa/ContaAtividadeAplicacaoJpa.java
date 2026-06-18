package com.torneios.infraestrutura.persistencia.jpa;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.torneios.aplicacao.participacao.conta.ContaAtividadeRepositorioAplicacao;
import com.torneios.dominio.compartilhado.enumeracao.StatusSolicitacao;
import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;

@Repository
class ContaAtividadeRepositorioAplicacaoImpl implements ContaAtividadeRepositorioAplicacao {

    @Autowired
    ContaUsuarioJpaRepository contaRepositorio;

    @Autowired
    TorneioJpaRepository torneioRepositorio;

    @Autowired
    TimeJpaRepository timeRepositorio;

    @Autowired
    SolicitacaoJpaRepository solicitacaoRepositorio;

    @Autowired
    PalpiteJpaRepository palpiteRepositorio;

    @Override
    public ContaAtividadeResumo pesquisar(long usuarioId) {
        ContaUsuarioJpa conta = contaRepositorio.findById(usuarioId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta de usuario nao encontrada."));

        List<TorneioAtividadeResumo> organizados = torneioRepositorio.findByOrganizadorId(usuarioId).stream()
                .map(this::resumir)
                .toList();

        Set<Long> participandoIds = new LinkedHashSet<>();
        timeRepositorio.findByResponsavelId(usuarioId).stream()
                .flatMap(time -> time.torneiosVinculados.stream())
                .forEach(participandoIds::add);
        solicitacaoRepositorio.findBySolicitanteId(usuarioId).stream()
                .filter(solicitacao -> StatusSolicitacao.APROVADA.name().equals(solicitacao.status))
                .map(solicitacao -> solicitacao.torneioId)
                .forEach(participandoIds::add);

        List<TorneioAtividadeResumo> participando = resumirTorneios(participandoIds);
        List<TorneioAtividadeResumo> salvos = resumirTorneios(conta.torneiosSalvos);

        List<PalpiteJpa> palpitesJpa = palpiteRepositorio.findByUsuarioIdOrderByIdDesc(usuarioId);
        Set<Long> torneiosDosPalpites = palpitesJpa.stream()
                .map(palpite -> palpite.torneioId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Map<Long, TorneioJpa> torneiosPorId = torneioRepositorio.findAllById(torneiosDosPalpites).stream()
                .collect(Collectors.toMap(torneio -> torneio.id, Function.identity()));
        List<PalpiteAtividadeResumo> palpites = palpitesJpa.stream()
                .map(palpite -> new PalpiteAtividadeResumo(
                        palpite.id,
                        palpite.tipo,
                        palpite.torneioId,
                        torneiosPorId.containsKey(palpite.torneioId)
                                ? torneiosPorId.get(palpite.torneioId).nome
                                : "Torneio #" + palpite.torneioId,
                        palpite.partidaId,
                        palpite.opcao,
                        palpite.apurado,
                        palpite.acertou))
                .toList();

        return new ContaAtividadeResumo(organizados, participando, salvos, palpites);
    }

    private List<TorneioAtividadeResumo> resumirTorneios(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        Map<Long, TorneioJpa> porId = torneioRepositorio.findAllById(ids).stream()
                .collect(Collectors.toMap(torneio -> torneio.id, Function.identity()));
        return ids.stream()
                .distinct()
                .filter(porId::containsKey)
                .map(porId::get)
                .map(this::resumir)
                .toList();
    }

    private TorneioAtividadeResumo resumir(TorneioJpa torneio) {
        return new TorneioAtividadeResumo(
                torneio.id,
                torneio.nome,
                torneio.formato == null ? null : torneio.formato.name(),
                torneio.status == null ? null : torneio.status.name(),
                torneio.aceitaSolicitacoes);
    }
}


