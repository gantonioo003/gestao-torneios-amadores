package com.torneios.dominio.competicao.partida;

import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.chaveamento.Chaveamento;
import com.torneios.dominio.competicao.chaveamento.ChaveamentoServico;
import com.torneios.dominio.competicao.classificacao.Classificacao;
import com.torneios.dominio.competicao.classificacao.ClassificacaoServico;
import com.torneios.dominio.competicao.geracao.GeradorPartidasServico;
import com.torneios.dominio.competicao.geracao.PreparacaoCompeticao;
import com.torneios.dominio.competicao.resultado.ResultadoPartida;
import com.torneios.dominio.competicao.rodada.Rodada;

public class PartidaServico {

    private final PartidaRepositorio partidaRepositorio;
    private final ConsultaCompeticaoTorneio consultaCompeticaoTorneio;
    private final GeradorPartidasServico geradorPartidasServico;
    private final ClassificacaoServico classificacaoServico;
    private final ChaveamentoServico chaveamentoServico;

    public PartidaServico(PartidaRepositorio partidaRepositorio,
                          ConsultaCompeticaoTorneio consultaCompeticaoTorneio,
                          GeradorPartidasServico geradorPartidasServico,
                          ClassificacaoServico classificacaoServico,
                          ChaveamentoServico chaveamentoServico) {
        this.partidaRepositorio = Objects.requireNonNull(partidaRepositorio,
                "O repositorio de partidas e obrigatorio.");
        this.consultaCompeticaoTorneio = Objects.requireNonNull(consultaCompeticaoTorneio,
                "A consulta de competicao do torneio e obrigatoria.");
        this.geradorPartidasServico = Objects.requireNonNull(geradorPartidasServico,
                "O gerador de partidas e obrigatorio.");
        this.classificacaoServico = Objects.requireNonNull(classificacaoServico,
                "O servico de classificacao e obrigatorio.");
        this.chaveamentoServico = Objects.requireNonNull(chaveamentoServico,
                "O servico de chaveamento e obrigatorio.");
    }

    public void salvar(Partida partida) {
        partidaRepositorio.salvar(partida);
    }

    public List<Partida> gerarPartidas(TorneioId torneioId, UsuarioId usuarioId) {
        return prepararCompeticao(torneioId, usuarioId).getPartidas();
    }

    public PreparacaoCompeticao prepararCompeticao(TorneioId torneioId, UsuarioId usuarioId) {
        validarOrganizador(torneioId, usuarioId);
        if (!consultaCompeticaoTorneio.estruturaGerada(torneioId)) {
            throw new OperacaoNaoPermitidaException("Nao e permitido preparar a competicao sem estrutura previa.");
        }

        FormatoTorneio formatoTorneio = consultaCompeticaoTorneio.obterFormato(torneioId);
        int quantidadeJogadoresPorEquipe = consultaCompeticaoTorneio.obterQuantidadeJogadoresPorEquipe(torneioId);
        List<TimeId> participantes = consultaCompeticaoTorneio.listarParticipantesAprovados(torneioId);
        List<List<TimeId>> grupos = consultaCompeticaoTorneio.listarGrupos(torneioId);

        List<Partida> partidas = geradorPartidasServico.gerar(
                torneioId,
                formatoTorneio,
                quantidadeJogadoresPorEquipe,
                participantes,
                grupos);
        partidas.forEach(partidaRepositorio::salvar);
        List<Rodada> rodadas = geradorPartidasServico.distribuirEmRodadas(torneioId, partidas);
        return new PreparacaoCompeticao(torneioId, partidas, rodadas);
    }

    public Partida obterPartida(PartidaId partidaId) {
        return partidaRepositorio.buscarPorId(partidaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Partida nao encontrada."));
    }

    public AtualizacaoCompeticao registrarResultado(TorneioId torneioId,
                                                    PartidaId partidaId,
                                                    UsuarioId usuarioId,
                                                    ResultadoPartida resultadoPartida) {
        validarOrganizador(torneioId, usuarioId);
        Partida partida = obterPartida(partidaId);
        if (!partida.getTorneioId().equals(torneioId)) {
            throw new OperacaoNaoPermitidaException("A partida informada nao pertence ao torneio.");
        }
        validarParticipantesDaPartida(torneioId, partida);
        partida.registrarResultado(resultadoPartida);
        partidaRepositorio.salvar(partida);
        return atualizarCompeticaoAposResultado(torneioId);
    }

    public List<Partida> listarPorTorneio(TorneioId torneioId) {
        return partidaRepositorio.listarPorTorneio(torneioId);
    }

    public List<Classificacao> visualizarClassificacao(TorneioId torneioId) {
        if (!consultaCompeticaoTorneio.estruturaGerada(torneioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Nao e permitido visualizar a classificacao antes da geracao da estrutura.");
        }
        FormatoTorneio formatoTorneio = consultaCompeticaoTorneio.obterFormato(torneioId);
        if (!formatoTorneio.exigeClassificacao()) {
            throw new OperacaoNaoPermitidaException("O formato do torneio nao utiliza classificacao.");
        }
        return classificacaoServico.gerar(
                torneioId,
                consultaCompeticaoTorneio.listarParticipantesAprovados(torneioId),
                listarPorTorneio(torneioId));
    }

    public Chaveamento visualizarChaveamento(TorneioId torneioId) {
        if (!consultaCompeticaoTorneio.estruturaGerada(torneioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Nao e permitido visualizar o chaveamento antes da geracao da estrutura.");
        }
        FormatoTorneio formatoTorneio = consultaCompeticaoTorneio.obterFormato(torneioId);
        if (!formatoTorneio.exigeChaveamento()) {
            throw new OperacaoNaoPermitidaException("O formato do torneio nao utiliza chaveamento.");
        }
        return chaveamentoServico.gerar(torneioId, formatoTorneio, listarPorTorneio(torneioId));
    }

    public AtualizacaoCompeticao gerenciarAndamento(TorneioId torneioId) {
        return atualizarCompeticaoAposResultado(torneioId);
    }

    private AtualizacaoCompeticao atualizarCompeticaoAposResultado(TorneioId torneioId) {
        FormatoTorneio formatoTorneio = consultaCompeticaoTorneio.obterFormato(torneioId);
        if (formatoTorneio.exigeClassificacao()) {
            return AtualizacaoCompeticao.comClassificacao(visualizarClassificacao(torneioId));
        }
        return AtualizacaoCompeticao.comChaveamento(visualizarChaveamento(torneioId));
    }

    private void validarOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        if (!consultaCompeticaoTorneio.usuarioEhOrganizador(torneioId, usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas o organizador do torneio pode executar esta operacao.");
        }
    }

    private void validarParticipantesDaPartida(TorneioId torneioId, Partida partida) {
        List<TimeId> participantesAprovados = consultaCompeticaoTorneio.listarParticipantesAprovados(torneioId);
        if (!participantesAprovados.contains(partida.getMandante())
                || !participantesAprovados.contains(partida.getVisitante())) {
            throw new RegraDeNegocioException(
                    "Nao e permitido registrar resultado para times que nao pertencem ao torneio.");
        }
    }
}
