package com.torneios;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.torneios.aplicacao.competicao.andamento.PartidaRepositorioAplicacao;
import com.torneios.aplicacao.competicao.andamento.PartidaServicoAplicacao;
import com.torneios.aplicacao.competicao.escalacao.EscalacaoServicoAplicacao;
import com.torneios.aplicacao.competicao.resultado.ResultadoCompeticaoServicoAplicacao;
import com.torneios.aplicacao.engajamento.chat.ChatPrivadoServicoAplicacao;
import com.torneios.aplicacao.engajamento.chat.GrupoChatServicoAplicacao;
import com.torneios.aplicacao.engajamento.desafio.DesafioServicoAplicacao;
import com.torneios.aplicacao.engajamento.feed.FeedServicoAplicacao;
import com.torneios.aplicacao.engajamento.feed.ModeracaoServicoAplicacao;
import com.torneios.aplicacao.engajamento.palpite.PalpiteServicoAplicacao;
import com.torneios.aplicacao.engajamento.palpite.ApuracaoAutomaticaPalpiteServicoAplicacao;
import com.torneios.aplicacao.estatisticas.comparacao.ComparativoDesempenhoServicoAplicacao;
import com.torneios.aplicacao.estatisticas.ranking.RankingServicoAplicacao;
import com.torneios.aplicacao.estatisticas.sumula.SumulaEstatisticaServicoAplicacao;
import com.torneios.aplicacao.participacao.acesso.AcessoPlataformaServicoAplicacao;
import com.torneios.aplicacao.participacao.candidatura.SolicitacaoRepositorioAplicacao;
import com.torneios.aplicacao.participacao.candidatura.SolicitacaoServicoAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaRepositorioAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaAtividadeRepositorioAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaAtividadeServicoAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaServicoAplicacao;
import com.torneios.aplicacao.participacao.conta.IdentidadeExternaVerificador;
import com.torneios.aplicacao.participacao.inscricao.InscricaoServicoAplicacao;
import com.torneios.aplicacao.participacao.notificacao.NotificacaoParticipacaoServicoAplicacao;
import com.torneios.aplicacao.participacao.profissional.ProfissionalRepositorioAplicacao;
import com.torneios.aplicacao.participacao.profissional.ProfissionalServicoAplicacao;
import com.torneios.aplicacao.participacao.time.TimeRepositorioAplicacao;
import com.torneios.aplicacao.participacao.time.TimeServicoAplicacao;
import com.torneios.aplicacao.torneio.criacao.TorneioRepositorioAplicacao;
import com.torneios.aplicacao.torneio.criacao.TorneioServicoAplicacao;
import com.torneios.aplicacao.torneio.preparacao.PreparacaoTorneioServicoAplicacao;
import com.torneios.dominio.compartilhado.evento.EventoBarramento;
import com.torneios.dominio.competicao.chaveamento.ChaveamentoServico;
import com.torneios.dominio.competicao.classificacao.ClassificacaoServico;
import com.torneios.dominio.competicao.contestacao.ConsultaContestacaoResultado;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultadoRepositorio;
import com.torneios.dominio.competicao.contestacao.ContestacaoResultadoServico;
import com.torneios.dominio.competicao.escalacao.ConsultaSuporteEscalacao;
import com.torneios.dominio.competicao.escalacao.EscalacaoRepositorio;
import com.torneios.dominio.competicao.escalacao.EscalacaoServico;
import com.torneios.dominio.competicao.geracao.GeradorPartidasServico;
import com.torneios.dominio.competicao.partida.ConsultaCompeticaoTorneio;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.competicao.partida.PartidaServico;
import com.torneios.dominio.engajamento.chat.ChatPrivadoServico;
import com.torneios.dominio.engajamento.chat.ConsultaSuporteChat;
import com.torneios.dominio.engajamento.chat.ConversaPrivadaRepositorio;
import com.torneios.dominio.engajamento.chat.GrupoChatRepositorio;
import com.torneios.dominio.engajamento.chat.GrupoChatServico;
import com.torneios.dominio.engajamento.desafio.ConsultaSuporteDesafioAmistoso;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoRepositorio;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoServico;
import com.torneios.dominio.engajamento.feed.ConsultaSuporteFeedTorneio;
import com.torneios.dominio.engajamento.feed.FeedTorneioRepositorio;
import com.torneios.dominio.engajamento.feed.FeedTorneioServico;
import com.torneios.dominio.engajamento.feed.DenunciaRepositorio;
import com.torneios.dominio.engajamento.feed.ModeracaoFeedServico;
import com.torneios.dominio.engajamento.palpite.ConsultaSuportePalpite;
import com.torneios.dominio.engajamento.palpite.PalpiteRepositorio;
import com.torneios.dominio.engajamento.palpite.PalpiteServico;
import com.torneios.dominio.engajamento.palpite.ProgressoPalpiteRepositorio;
import com.torneios.dominio.engajamento.palpite.ProgressoPalpiteServico;
import com.torneios.dominio.estatisticas.artilharia.ArtilhariaServico;
import com.torneios.dominio.estatisticas.comparacao.ComparacaoDesempenhoServico;
import com.torneios.dominio.estatisticas.comparacao.ComparativoDesempenhoRepositorio;
import com.torneios.dominio.estatisticas.comparacao.ConsultaComparacaoDesempenho;
import com.torneios.dominio.estatisticas.desempenho.EstatisticaServico;
import com.torneios.dominio.estatisticas.evento.ConsultaEstatisticaCompeticao;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoRepositorio;
import com.torneios.dominio.estatisticas.evento.EventoEstatisticoServico;
import com.torneios.dominio.estatisticas.nota.CalculadoraNotaEstatistica;
import com.torneios.dominio.participacao.acesso.AcessoGerenciamentoTorneioServico;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.acesso.CatalogoTorneiosDisponiveis;
import com.torneios.dominio.participacao.acesso.CodificadorSenha;
import com.torneios.dominio.participacao.acesso.ContaUsuarioRepositorio;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;
import com.torneios.dominio.participacao.acesso.Pbkdf2CodificadorSenha;
import com.torneios.dominio.participacao.acesso.NotificacaoRepositorio;
import com.torneios.dominio.participacao.acesso.NotificacaoServico;
import com.torneios.dominio.participacao.acesso.VisualizacaoTorneioServico;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoServico;
import com.torneios.dominio.participacao.responsavel.ConsultaUsuario;
import com.torneios.dominio.participacao.responsavel.ResponsavelTimeServico;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoRepositorio;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;
import com.torneios.dominio.participacao.time.TimeRepositorio;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;
import com.torneios.dominio.participacao.time.TimeServico;
import com.torneios.dominio.torneio.estrutura.GeradorEstruturaCompeticaoServico;
import com.torneios.dominio.torneio.organizador.OrganizadorTorneioServico;
import com.torneios.dominio.torneio.torneio.ConsultaElegibilidadeComCache;
import com.torneios.dominio.torneio.torneio.ConsultaElegibilidadeParticipanteTorneio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;
import com.torneios.dominio.torneio.torneio.TorneioServico;

@SpringBootApplication
public class BackendAplicacao {

    @Bean
    public AutenticacaoServico autenticacaoServico() {
        return new AutenticacaoServico();
    }

    @Bean
    public CodificadorSenha codificadorSenha() {
        return new Pbkdf2CodificadorSenha();
    }

    @Bean
    public ContaUsuarioServico contaUsuarioServico(ContaUsuarioRepositorio repositorio, CodificadorSenha codificadorSenha) {
        return new ContaUsuarioServico(repositorio, codificadorSenha);
    }

    @Bean
    public ContaServicoAplicacao contaServicoAplicacao(ContaRepositorioAplicacao repositorio,
                                                       ContaUsuarioServico contaUsuarioServico,
                                                       IdentidadeExternaVerificador identidadeExternaVerificador) {
        return new ContaServicoAplicacao(repositorio, contaUsuarioServico, identidadeExternaVerificador);
    }

    @Bean
    public ContaAtividadeServicoAplicacao contaAtividadeServicoAplicacao(
            ContaAtividadeRepositorioAplicacao repositorio) {
        return new ContaAtividadeServicoAplicacao(repositorio);
    }

    @Bean
    public VisualizacaoTorneioServico visualizacaoTorneioServico(CatalogoTorneiosDisponiveis catalogoTorneiosDisponiveis) {
        return new VisualizacaoTorneioServico(catalogoTorneiosDisponiveis);
    }

    @Bean
    public AcessoGerenciamentoTorneioServico acessoGerenciamentoTorneioServico(AutenticacaoServico autenticacaoServico) {
        return new AcessoGerenciamentoTorneioServico(autenticacaoServico);
    }

    @Bean
    public AcessoPlataformaServicoAplicacao acessoPlataformaServicoAplicacao(
            VisualizacaoTorneioServico visualizacaoTorneioServico,
            AcessoGerenciamentoTorneioServico acessoGerenciamentoTorneioServico) {
        return new AcessoPlataformaServicoAplicacao(visualizacaoTorneioServico, acessoGerenciamentoTorneioServico);
    }

    @Bean
    public ConsultaUsuario consultaUsuario(ContaUsuarioRepositorio repositorio) {
        return usuarioId -> repositorio.buscarPorId(usuarioId).isPresent();
    }

    @Bean
    public ResponsavelTimeServico responsavelTimeServico(TimeRepositorio timeRepositorio,
                                                         ConsultaUsuario consultaUsuario) {
        return new ResponsavelTimeServico(timeRepositorio, consultaUsuario);
    }

    @Bean
    public TimeServico timeServico(TimeRepositorio timeRepositorio,
                                   AutenticacaoServico autenticacaoServico,
                                   ResponsavelTimeServico responsavelTimeServico,
                                   ProfissionalEsportivoRepositorio profissionalRepositorio) {
        return new TimeServico(
                timeRepositorio,
                autenticacaoServico,
                responsavelTimeServico,
                profissionalRepositorio,
                (time, usuarioId) -> time.getResponsavel().equals(usuarioId));
    }

    @Bean
    public TimeServicoAplicacao timeServicoAplicacao(TimeRepositorioAplicacao repositorio) {
        return new TimeServicoAplicacao(repositorio);
    }

    @Bean
    public ProfissionalEsportivoServico profissionalServico(ProfissionalEsportivoRepositorio repositorio,
                                                            AutenticacaoServico autenticacaoServico) {
        return new ProfissionalEsportivoServico(repositorio, autenticacaoServico);
    }

    @Bean
    public ProfissionalServicoAplicacao profissionalServicoAplicacao(ProfissionalRepositorioAplicacao repositorio) {
        return new ProfissionalServicoAplicacao(repositorio);
    }

    @Bean
    public SolicitacaoParticipacaoServico solicitacaoServico(SolicitacaoParticipacaoRepositorio repositorio,
                                                             TimeRepositorio timeRepositorio,
                                                             AutenticacaoServico autenticacaoServico,
                                                             TorneioRepositorio torneioRepositorio,
                                                             TorneioServico torneioServico) {
        return new SolicitacaoParticipacaoServico(repositorio, timeRepositorio,
                autenticacaoServico, new com.torneios.dominio.participacao.solicitacao.PoliticaParticipacaoTorneio() {
                    @Override
                    public boolean aceitaSolicitacoes(com.torneios.dominio.compartilhado.torneio.TorneioId torneioId) {
                        return torneioRepositorio.buscarPorId(torneioId)
                                .map(t -> t.aceitaSolicitacoes())
                                .orElse(false);
                    }

                    @Override
                    public boolean usuarioEhOrganizador(
                            com.torneios.dominio.compartilhado.torneio.TorneioId torneioId,
                            com.torneios.dominio.compartilhado.usuario.UsuarioId usuarioId) {
                        return torneioRepositorio.buscarPorId(torneioId)
                                .map(t -> t.getOrganizadorId().equals(usuarioId))
                                .orElse(false);
                    }

                    @Override
                    public com.torneios.dominio.compartilhado.usuario.UsuarioId organizadorDoTorneio(
                            com.torneios.dominio.compartilhado.torneio.TorneioId torneioId) {
                        return torneioRepositorio.buscarPorId(torneioId)
                                .orElseThrow(() -> new IllegalArgumentException("Torneio nao encontrado."))
                                .getOrganizadorId();
                    }

                    @Override
                    public void adicionarParticipante(
                            com.torneios.dominio.compartilhado.torneio.TorneioId torneioId,
                            com.torneios.dominio.compartilhado.time.TimeId timeId) {
                        var organizadorId = organizadorDoTorneio(torneioId);
                        torneioServico.aprovarParticipante(torneioId, organizadorId, timeId);
                    }

                    @Override
                    public void removerParticipante(
                            com.torneios.dominio.compartilhado.torneio.TorneioId torneioId,
                            com.torneios.dominio.compartilhado.time.TimeId timeId) {
                        var organizadorId = organizadorDoTorneio(torneioId);
                        torneioServico.removerParticipante(torneioId, organizadorId, timeId);
                    }

                    @Override
                    public boolean possuiParticipante(
                            com.torneios.dominio.compartilhado.torneio.TorneioId torneioId,
                            com.torneios.dominio.compartilhado.time.TimeId timeId) {
                        return torneioRepositorio.buscarPorId(torneioId)
                                .map(torneio -> torneio.possuiParticipante(timeId))
                                .orElse(false);
                    }

                    @Override
                    public boolean torneioIniciado(
                            com.torneios.dominio.compartilhado.torneio.TorneioId torneioId) {
                        return torneioRepositorio.buscarPorId(torneioId)
                                .map(torneio -> torneio.getStatus()
                                        == com.torneios.dominio.compartilhado.enumeracao.StatusTorneio.INICIADO
                                        || torneio.getStatus()
                                        == com.torneios.dominio.compartilhado.enumeracao.StatusTorneio.FINALIZADO)
                                .orElse(false);
                    }
                });
    }

    @Bean
    public SolicitacaoServicoAplicacao solicitacaoServicoAplicacao(SolicitacaoRepositorioAplicacao repositorio) {
        return new SolicitacaoServicoAplicacao(repositorio);
    }

    @Bean
    public InscricaoServicoAplicacao inscricaoServicoAplicacao(
            SolicitacaoParticipacaoServico solicitacaoParticipacaoServico,
            NotificacaoParticipacaoServicoAplicacao notificacaoServicoAplicacao) {
        return new InscricaoServicoAplicacao(solicitacaoParticipacaoServico, notificacaoServicoAplicacao);
    }

    @Bean
    public NotificacaoServico notificacaoServico(NotificacaoRepositorio repositorio) {
        return new NotificacaoServico(repositorio);
    }

    @Bean
    public NotificacaoParticipacaoServicoAplicacao notificacaoParticipacaoServicoAplicacao(
            NotificacaoServico notificacaoServico) {
        return new NotificacaoParticipacaoServicoAplicacao(notificacaoServico);
    }

    @Bean
    @Primary
    public ConsultaElegibilidadeParticipanteTorneio consultaElegibilidadeComCache(
            @Qualifier("consultaElegibilidadeJpa") ConsultaElegibilidadeParticipanteTorneio delegado) {
        return new ConsultaElegibilidadeComCache(delegado);
    }

    @Bean
    public TorneioServico torneioServico(TorneioRepositorio torneioRepositorio,
                                         ConsultaElegibilidadeParticipanteTorneio consultaElegibilidade,
                                         com.torneios.dominio.torneio.torneio.PreparacaoCompeticaoInvalidador
                                                 preparacaoCompeticaoInvalidador,
                                         EventoBarramento barramento) {
        return new TorneioServico(
                torneioRepositorio,
                new OrganizadorTorneioServico(),
                new GeradorEstruturaCompeticaoServico(),
                consultaElegibilidade,
                preparacaoCompeticaoInvalidador,
                barramento);
    }

    @Bean
    public TorneioServicoAplicacao torneioServicoAplicacao(TorneioRepositorioAplicacao repositorio) {
        return new TorneioServicoAplicacao(repositorio);
    }

    @Bean
    public PartidaServico partidaServico(PartidaRepositorio partidaRepositorio,
                                         ConsultaCompeticaoTorneio consultaCompeticaoTorneio,
                                         EventoBarramento barramento) {
        return new PartidaServico(
                partidaRepositorio,
                consultaCompeticaoTorneio,
                new GeradorPartidasServico(),
                new ClassificacaoServico(),
                new ChaveamentoServico(),
                barramento);
    }

    @Bean
    public PartidaServicoAplicacao partidaServicoAplicacao(PartidaRepositorioAplicacao repositorio) {
        return new PartidaServicoAplicacao(repositorio);
    }

    @Bean
    public EscalacaoServico escalacaoServico(EscalacaoRepositorio escalacaoRepositorio,
                                             ConsultaSuporteEscalacao consultaSuporteEscalacao) {
        return new EscalacaoServico(escalacaoRepositorio, consultaSuporteEscalacao);
    }

    @Bean
    public EscalacaoServicoAplicacao escalacaoServicoAplicacao(EscalacaoServico escalacaoServico) {
        return new EscalacaoServicoAplicacao(escalacaoServico);
    }

    @Bean
    public PreparacaoTorneioServicoAplicacao preparacaoTorneioServicoAplicacao(TorneioServico torneioServico,
                                                                               PartidaServico partidaServico) {
        return new PreparacaoTorneioServicoAplicacao(torneioServico, partidaServico);
    }

    @Bean
    public ContestacaoResultadoServico contestacaoResultadoServico(PartidaRepositorio partidaRepositorio,
                                                                   ContestacaoResultadoRepositorio contestacaoResultadoRepositorio,
                                                                   ConsultaCompeticaoTorneio consultaCompeticaoTorneio,
                                                                   ConsultaContestacaoResultado consultaContestacaoResultado) {
        return new ContestacaoResultadoServico(
                partidaRepositorio,
                contestacaoResultadoRepositorio,
                consultaCompeticaoTorneio,
                consultaContestacaoResultado);
    }

    @Bean
    public ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServicoAplicacao(PartidaServico partidaServico,
                                                                                   ContestacaoResultadoServico contestacaoResultadoServico) {
        return new ResultadoCompeticaoServicoAplicacao(partidaServico, contestacaoResultadoServico);
    }

    @Bean
    public CalculadoraNotaEstatistica calculadoraNotaEstatistica() {
        return new CalculadoraNotaEstatistica();
    }

    @Bean
    public EventoEstatisticoServico eventoEstatisticoServico(EventoEstatisticoRepositorio eventoEstatisticoRepositorio,
                                                             ConsultaEstatisticaCompeticao consultaEstatisticaCompeticao) {
        return new EventoEstatisticoServico(eventoEstatisticoRepositorio, consultaEstatisticaCompeticao);
    }

    @Bean
    public SumulaEstatisticaServicoAplicacao sumulaEstatisticaServicoAplicacao(
            EventoEstatisticoServico eventoEstatisticoServico,
            EventoEstatisticoRepositorio eventoEstatisticoRepositorio) {
        return new SumulaEstatisticaServicoAplicacao(eventoEstatisticoServico, eventoEstatisticoRepositorio);
    }

    @Bean
    public EstatisticaServico estatisticaServico(EventoEstatisticoRepositorio eventoEstatisticoRepositorio,
                                                 CalculadoraNotaEstatistica calculadoraNotaEstatistica) {
        return new EstatisticaServico(eventoEstatisticoRepositorio, calculadoraNotaEstatistica);
    }

    @Bean
    public ArtilhariaServico artilhariaServico(EstatisticaServico estatisticaServico) {
        return new ArtilhariaServico(estatisticaServico);
    }

    @Bean
    public RankingServicoAplicacao rankingServicoAplicacao(EstatisticaServico estatisticaServico,
                                                           ArtilhariaServico artilhariaServico) {
        return new RankingServicoAplicacao(estatisticaServico, artilhariaServico);
    }

    @Bean
    public ComparacaoDesempenhoServico comparacaoDesempenhoServico(
            EventoEstatisticoRepositorio eventoEstatisticoRepositorio,
            ComparativoDesempenhoRepositorio comparativoDesempenhoRepositorio,
            ConsultaComparacaoDesempenho consultaComparacaoDesempenho) {
        return new ComparacaoDesempenhoServico(
                eventoEstatisticoRepositorio,
                comparativoDesempenhoRepositorio,
                consultaComparacaoDesempenho);
    }

    @Bean
    public ComparativoDesempenhoServicoAplicacao comparativoDesempenhoServicoAplicacao(
            ComparacaoDesempenhoServico comparacaoDesempenhoServico) {
        return new ComparativoDesempenhoServicoAplicacao(comparacaoDesempenhoServico);
    }

    @Bean
    public ChatPrivadoServico chatPrivadoServico(ConversaPrivadaRepositorio conversaPrivadaRepositorio,
                                                 ConsultaSuporteChat consultaSuporteChat) {
        return new ChatPrivadoServico(conversaPrivadaRepositorio, consultaSuporteChat);
    }

    @Bean
    public ChatPrivadoServicoAplicacao chatPrivadoServicoAplicacao(ChatPrivadoServico chatPrivadoServico,
                                                                   ContaRepositorioAplicacao contaRepositorio) {
        return new ChatPrivadoServicoAplicacao(chatPrivadoServico, contaRepositorio);
    }

    @Bean
    public GrupoChatServico grupoChatServico(GrupoChatRepositorio repositorio,
                                             ConsultaSuporteChat consultaSuporteChat) {
        return new GrupoChatServico(repositorio, consultaSuporteChat);
    }

    @Bean
    public GrupoChatServicoAplicacao grupoChatServicoAplicacao(GrupoChatServico grupoChatServico,
                                                               ContaRepositorioAplicacao contaRepositorio) {
        return new GrupoChatServicoAplicacao(grupoChatServico, contaRepositorio);
    }

    @Bean
    public FeedTorneioServico feedTorneioServico(FeedTorneioRepositorio feedTorneioRepositorio,
                                                 ConsultaSuporteFeedTorneio consultaSuporteFeedTorneio) {
        return new FeedTorneioServico(feedTorneioRepositorio, consultaSuporteFeedTorneio);
    }

    @Bean
    public FeedServicoAplicacao feedServicoAplicacao(FeedTorneioServico feedTorneioServico,
                                                     ContaRepositorioAplicacao contaRepositorio,
                                                     TimeRepositorioAplicacao timeRepositorio,
                                                     TorneioRepositorioAplicacao torneioRepositorio,
                                                     com.torneios.aplicacao.participacao.profissional.ProfissionalRepositorioAplicacao
                                                             profissionalRepositorio,
                                                     ConversaPrivadaRepositorio conversaPrivadaRepositorio) {
        return new FeedServicoAplicacao(
                feedTorneioServico,
                contaRepositorio,
                timeRepositorio,
                torneioRepositorio,
                profissionalRepositorio,
                conversaPrivadaRepositorio);
    }

    @Bean
    public ModeracaoFeedServico moderacaoFeedServico(DenunciaRepositorio denunciaRepositorio) {
        return new ModeracaoFeedServico(denunciaRepositorio);
    }

    @Bean
    public ModeracaoServicoAplicacao moderacaoServicoAplicacao(ModeracaoFeedServico moderacaoFeedServico,
                                                               ContaRepositorioAplicacao contaRepositorio) {
        return new ModeracaoServicoAplicacao(moderacaoFeedServico, contaRepositorio);
    }

    @Bean
    public PalpiteServico palpiteServico(PalpiteRepositorio palpiteRepositorio,
                                         ConsultaSuportePalpite consultaSuportePalpite) {
        return new PalpiteServico(palpiteRepositorio, consultaSuportePalpite);
    }

    @Bean
    public ProgressoPalpiteServico progressoPalpiteServico(ProgressoPalpiteRepositorio repositorio) {
        return new ProgressoPalpiteServico(repositorio);
    }

    @Bean
    public PalpiteServicoAplicacao palpiteServicoAplicacao(PalpiteServico palpiteServico,
                                                           PalpiteRepositorio palpiteRepositorio,
                                                           TorneioRepositorio torneioRepositorio,
                                                           PartidaRepositorio partidaRepositorio,
                                                           TimeRepositorio timeRepositorio,
                                                           ProfissionalEsportivoRepositorio profissionalRepositorio,
                                                           ProgressoPalpiteServico progressoPalpiteServico,
                                                           ContaRepositorioAplicacao contaRepositorio) {
        return new PalpiteServicoAplicacao(
                palpiteServico,
                palpiteRepositorio,
                torneioRepositorio,
                partidaRepositorio,
                timeRepositorio,
                profissionalRepositorio,
                progressoPalpiteServico,
                contaRepositorio);
    }

    @Bean
    public ApuracaoAutomaticaPalpiteServicoAplicacao apuracaoAutomaticaPalpiteServicoAplicacao(
            PalpiteServico palpiteServico,
            PartidaRepositorio partidaRepositorio,
            ResultadoCompeticaoServicoAplicacao resultadoCompeticaoServicoAplicacao,
            RankingServicoAplicacao rankingServicoAplicacao,
            PalpiteRepositorio palpiteRepositorio,
            ProgressoPalpiteServico progressoPalpiteServico,
            TorneioRepositorio torneioRepositorio) {
        return new ApuracaoAutomaticaPalpiteServicoAplicacao(
                palpiteServico,
                partidaRepositorio,
                resultadoCompeticaoServicoAplicacao,
                rankingServicoAplicacao,
                palpiteRepositorio,
                progressoPalpiteServico,
                torneioRepositorio);
    }

    @Bean
    public DesafioAmistosoServico desafioAmistosoServico(DesafioAmistosoRepositorio desafioAmistosoRepositorio,
                                                         ConsultaSuporteDesafioAmistoso consultaSuporteDesafioAmistoso) {
        return new DesafioAmistosoServico(desafioAmistosoRepositorio, consultaSuporteDesafioAmistoso);
    }

    @Bean
    public DesafioServicoAplicacao desafioServicoAplicacao(
            DesafioAmistosoServico desafioAmistosoServico,
            ConsultaSuporteDesafioAmistoso consultaSuporteDesafioAmistoso,
            NotificacaoParticipacaoServicoAplicacao notificacaoServicoAplicacao) {
        return new DesafioServicoAplicacao(
                desafioAmistosoServico,
                consultaSuporteDesafioAmistoso,
                notificacaoServicoAplicacao);
    }

    public static void main(String[] args) {
        run(BackendAplicacao.class, args);
    }
}


