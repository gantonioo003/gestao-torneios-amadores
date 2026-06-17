package com.torneios.infraestrutura.persistencia.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultado;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultadoId;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultadoRepositorio;
import com.torneios.dominio.competicao.contestacao.DecisaoContestacaoResultado;
import com.torneios.dominio.competicao.contestacao.HistoricoDecisaoContestacao;
import com.torneios.dominio.competicao.contestacao.StatusContestacaoResultado;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTESTACAO_RESULTADO")
class ContestacaoResultadoJpa {

    @Id
    Long id;

    Long torneioId;
    Long partidaId;
    Long timeSolicitanteId;
    Long usuarioSolicitanteId;
    String motivo;
    String justificativa;

    @Lob
    String evidenciasData;

    java.time.LocalDateTime dataHoraAbertura;
    java.time.LocalDateTime prazoLimite;
    String status;

    @Lob
    String historicoData;
}

interface ContestacaoResultadoJpaRepository extends JpaRepository<ContestacaoResultadoJpa, Long> {
    List<ContestacaoResultadoJpa> findByTorneioId(Long torneioId);
    List<ContestacaoResultadoJpa> findByPartidaId(Long partidaId);
    boolean existsByPartidaIdAndTimeSolicitanteIdAndStatus(Long partidaId, Long timeSolicitanteId, String status);
}

@Repository
class ContestacaoResultadoRepositorioImpl implements ContestacaoResultadoRepositorio {

    @Autowired
    ContestacaoResultadoJpaRepository repositorio;

    @Override
    public void salvar(ContestacaoResultado contestacao) {
        var jpa = repositorio.findById(contestacao.getId().valor()).orElse(new ContestacaoResultadoJpa());
        jpa.id = contestacao.getId().valor();
        jpa.torneioId = contestacao.getTorneioId().valor();
        jpa.partidaId = contestacao.getPartidaId().valor();
        jpa.timeSolicitanteId = contestacao.getTimeSolicitanteId().valor();
        jpa.usuarioSolicitanteId = contestacao.getUsuarioSolicitanteId().valor();
        jpa.motivo = contestacao.getMotivo();
        jpa.justificativa = contestacao.getJustificativa();
        jpa.evidenciasData = PersistenciaTextoUtil.serializarLista(contestacao.getEvidencias());
        jpa.dataHoraAbertura = contestacao.getDataHoraAbertura();
        jpa.prazoLimite = contestacao.getPrazoLimite();
        jpa.status = contestacao.getStatus().name();
        jpa.historicoData = PersistenciaTextoUtil.serializarLinhas(
                contestacao.getHistorico().stream()
                        .map(this::serializarHistorico)
                        .toList());
        repositorio.save(jpa);
    }

    @Override
    public Optional<ContestacaoResultado> buscarPorId(ContestacaoResultadoId contestacaoId) {
        return repositorio.findById(contestacaoId.valor()).map(this::paraDominio);
    }

    @Override
    public List<ContestacaoResultado> listarContestacoesPorTorneio(TorneioId torneioId) {
        return repositorio.findByTorneioId(torneioId.valor()).stream().map(this::paraDominio).toList();
    }

    @Override
    public List<ContestacaoResultado> listarContestacoesPorPartida(PartidaId partidaId) {
        return repositorio.findByPartidaId(partidaId.valor()).stream().map(this::paraDominio).toList();
    }

    @Override
    public boolean existePendentePorPartidaETime(PartidaId partidaId, TimeId timeId) {
        return repositorio.existsByPartidaIdAndTimeSolicitanteIdAndStatus(
                partidaId.valor(), timeId.valor(), StatusContestacaoResultado.PENDENTE.name());
    }

    private ContestacaoResultado paraDominio(ContestacaoResultadoJpa jpa) {
        ContestacaoResultado contestacao = new ContestacaoResultado(
                new ContestacaoResultadoId(jpa.id),
                new TorneioId(jpa.torneioId),
                new PartidaId(jpa.partidaId),
                new TimeId(jpa.timeSolicitanteId),
                new UsuarioId(jpa.usuarioSolicitanteId),
                jpa.motivo,
                jpa.justificativa,
                PersistenciaTextoUtil.desserializarLista(jpa.evidenciasData),
                jpa.dataHoraAbertura,
                jpa.prazoLimite);

        for (List<String> linha : PersistenciaTextoUtil.desserializarLinhas(jpa.historicoData)) {
            contestacao.registrarDecisao(
                    new UsuarioId(Long.parseLong(linha.get(0))),
                    DecisaoContestacaoResultado.valueOf(linha.get(1)),
                    linha.get(2),
                    criarResultado(linha.get(3), linha.get(4)),
                    criarResultado(linha.get(5), linha.get(6)),
                    PersistenciaTextoUtil.paraLocalDateTime(linha.get(7)));
        }
        return contestacao;
    }

    private List<String> serializarHistorico(HistoricoDecisaoContestacao historico) {
        List<String> linha = new ArrayList<>();
        linha.add(String.valueOf(historico.organizadorId().valor()));
        linha.add(historico.decisao().name());
        linha.add(historico.observacao());
        linha.add(PersistenciaTextoUtil.deInteger(historico.resultadoAnterior() == null ? null : historico.resultadoAnterior().golsMandante()));
        linha.add(PersistenciaTextoUtil.deInteger(historico.resultadoAnterior() == null ? null : historico.resultadoAnterior().golsVisitante()));
        linha.add(PersistenciaTextoUtil.deInteger(historico.resultadoCorrigido() == null ? null : historico.resultadoCorrigido().golsMandante()));
        linha.add(PersistenciaTextoUtil.deInteger(historico.resultadoCorrigido() == null ? null : historico.resultadoCorrigido().golsVisitante()));
        linha.add(PersistenciaTextoUtil.deLocalDateTime(historico.dataHoraDecisao()));
        return linha;
    }

    private ResultadoPartida criarResultado(String golsMandante, String golsVisitante) {
        Integer mandante = PersistenciaTextoUtil.paraInteger(golsMandante);
        Integer visitante = PersistenciaTextoUtil.paraInteger(golsVisitante);
        if (mandante == null || visitante == null) {
            return null;
        }
        return new ResultadoPartida(mandante, visitante);
    }
}
