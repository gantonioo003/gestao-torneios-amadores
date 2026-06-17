package com.torneios.infraestrutura.persistencia.memoria;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.jogador.JogadorId;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.tecnico.TecnicoId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.escalacao.ConsultaSuporteEscalacao;
import com.torneios.dominio.competicao.escalacao.Escalacao;
import com.torneios.dominio.competicao.escalacao.EscalacaoRepositorio;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.participacao.profissional.TipoProfissional;
import com.torneios.dominio.participacao.time.TimeRepositorio;

@Component
class EscalacaoRepositorioMemoriaInfra implements EscalacaoRepositorio {

    private final Map<String, Escalacao> escalacoes = new LinkedHashMap<>();

    @Override
    public void salvar(Escalacao escalacao) {
        escalacoes.put(chave(escalacao.getPartidaId(), escalacao.getTimeId()), escalacao);
    }

    @Override
    public Optional<Escalacao> buscarPorPartidaETime(PartidaId partidaId, TimeId timeId) {
        return Optional.ofNullable(escalacoes.get(chave(partidaId, timeId)));
    }

    @Override
    public List<Escalacao> listarPorPartida(PartidaId partidaId) {
        return escalacoes.values().stream()
                .filter(escalacao -> escalacao.getPartidaId().equals(partidaId))
                .toList();
    }

    private String chave(PartidaId partidaId, TimeId timeId) {
        return partidaId.valor() + ":" + timeId.valor();
    }
}

@Component
class ConsultaSuporteEscalacaoInfra implements ConsultaSuporteEscalacao {

    @Autowired
    PartidaRepositorio partidaRepositorio;

    @Autowired
    TimeRepositorio timeRepositorio;

    @Override
    public boolean partidaIniciada(PartidaId partidaId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .map(partida -> partida.estaEncerrada())
                .orElse(false);
    }

    @Override
    public boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(time -> time.getResponsavel().equals(usuarioId))
                .orElse(false);
    }

    @Override
    public boolean tecnicoEstaAssociadoAoTime(TimeId timeId, TecnicoId tecnicoId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(time -> time.getElenco().stream()
                        .anyMatch(vinculo -> TipoProfissional.TREINADOR.name().equalsIgnoreCase(vinculo.getFuncao())
                                && vinculo.getProfissionalId().valor() == tecnicoId.valor()))
                .orElse(false);
    }

    @Override
    public List<JogadorId> listarElencoDoTime(TimeId timeId) {
        return timeRepositorio.buscarPorId(timeId)
                .map(time -> time.getElenco().stream()
                        .filter(vinculo -> TipoProfissional.JOGADOR.name().equalsIgnoreCase(vinculo.getFuncao()))
                        .map(vinculo -> new JogadorId(vinculo.getProfissionalId().valor()))
                        .toList())
                .orElse(List.of());
    }

    @Override
    public List<TimeId> listarTimesDaPartida(PartidaId partidaId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .map(partida -> List.of(partida.getMandante(), partida.getVisitante()))
                .orElse(List.of());
    }

    @Override
    public FormatoEquipe obterFormatoEquipeDaPartida(PartidaId partidaId) {
        int quantidade = partidaRepositorio.buscarPorId(partidaId)
                .map(partida -> partida.getQuantidadeJogadoresPorEquipe())
                .orElseThrow(() -> new com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException(
                        "Partida nao encontrada para obter o formato da equipe."));
        return java.util.Arrays.stream(FormatoEquipe.values())
                .filter(formato -> formato.getQuantidadeJogadores() == quantidade)
                .findFirst()
                .orElse(FormatoEquipe.ONZE_POR_ONZE);
    }
}
