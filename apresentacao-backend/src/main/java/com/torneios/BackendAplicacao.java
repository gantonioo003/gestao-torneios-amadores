package com.torneios;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.torneios.aplicacao.competicao.andamento.PartidaRepositorioAplicacao;
import com.torneios.aplicacao.competicao.andamento.PartidaServicoAplicacao;
import com.torneios.aplicacao.participacao.candidatura.SolicitacaoRepositorioAplicacao;
import com.torneios.aplicacao.participacao.candidatura.SolicitacaoServicoAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaRepositorioAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaServicoAplicacao;
import com.torneios.aplicacao.participacao.profissional.ProfissionalRepositorioAplicacao;
import com.torneios.aplicacao.participacao.profissional.ProfissionalServicoAplicacao;
import com.torneios.aplicacao.participacao.time.TimeRepositorioAplicacao;
import com.torneios.aplicacao.participacao.time.TimeServicoAplicacao;
import com.torneios.aplicacao.torneio.criacao.TorneioRepositorioAplicacao;
import com.torneios.aplicacao.torneio.criacao.TorneioServicoAplicacao;
import com.torneios.dominio.compartilhado.evento.EventoBarramento;
import com.torneios.dominio.competicao.chaveamento.ChaveamentoServico;
import com.torneios.dominio.competicao.classificacao.ClassificacaoServico;
import com.torneios.dominio.competicao.geracao.GeradorPartidasServico;
import com.torneios.dominio.competicao.partida.ConsultaCompeticaoTorneio;
import com.torneios.dominio.competicao.partida.PartidaRepositorio;
import com.torneios.dominio.competicao.partida.PartidaServico;
import com.torneios.dominio.participacao.acesso.AutenticacaoServico;
import com.torneios.dominio.participacao.acesso.ContaUsuarioRepositorio;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoServico;
import com.torneios.dominio.participacao.responsavel.ConsultaUsuario;
import com.torneios.dominio.participacao.responsavel.ResponsavelTimeServico;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoRepositorio;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoServico;
import com.torneios.dominio.participacao.time.TimeRepositorio;
import com.torneios.dominio.participacao.time.TimeServico;
import com.torneios.dominio.torneio.estrutura.GeradorEstruturaCompeticaoServico;
import com.torneios.dominio.torneio.organizador.OrganizadorTorneioServico;
import com.torneios.dominio.torneio.torneio.ConsultaElegibilidadeComCache;
import com.torneios.dominio.torneio.torneio.ConsultaElegibilidadeParticipanteTorneio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;
import com.torneios.dominio.torneio.torneio.TorneioServico;

@SpringBootApplication
public class BackendAplicacao {

    // ── Autenticação e acesso ──────────────────────────────────────
    @Bean
    public AutenticacaoServico autenticacaoServico() {
        return new AutenticacaoServico();
    }

    @Bean
    public ContaUsuarioServico contaUsuarioServico(ContaUsuarioRepositorio repositorio) {
        return new ContaUsuarioServico(repositorio);
    }

    @Bean
    public ContaServicoAplicacao contaServicoAplicacao(ContaRepositorioAplicacao repositorio) {
        return new ContaServicoAplicacao(repositorio);
    }

    // ── Time ───────────────────────────────────────────────────────
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
            ResponsavelTimeServico responsavelTimeServico) {
        return new TimeServico(timeRepositorio, autenticacaoServico, responsavelTimeServico);
    }

    @Bean
    public TimeServicoAplicacao timeServicoAplicacao(TimeRepositorioAplicacao repositorio) {
        return new TimeServicoAplicacao(repositorio);
    }

    // ── Profissional ───────────────────────────────────────────────
    @Bean
    public ProfissionalEsportivoServico profissionalServico(
            ProfissionalEsportivoRepositorio repositorio,
            AutenticacaoServico autenticacaoServico) {
        return new ProfissionalEsportivoServico(repositorio, autenticacaoServico);
    }

    @Bean
    public ProfissionalServicoAplicacao profissionalServicoAplicacao(
            ProfissionalRepositorioAplicacao repositorio) {
        return new ProfissionalServicoAplicacao(repositorio);
    }

    // ── Solicitação ────────────────────────────────────────────────
    @Bean
    public SolicitacaoParticipacaoServico solicitacaoServico(
            SolicitacaoParticipacaoRepositorio repositorio,
            TimeRepositorio timeRepositorio,
            AutenticacaoServico autenticacaoServico) {
        return new SolicitacaoParticipacaoServico(repositorio, timeRepositorio,
            autenticacaoServico, (torneioId) -> false);
    }

    @Bean
    public SolicitacaoServicoAplicacao solicitacaoServicoAplicacao(
            SolicitacaoRepositorioAplicacao repositorio) {
        return new SolicitacaoServicoAplicacao(repositorio);
    }

    // ── Torneio ────────────────────────────────────────────────────
    @Bean
    @Primary
    public ConsultaElegibilidadeParticipanteTorneio consultaElegibilidadeComCache(
            @Qualifier("consultaElegibilidadeJpa") ConsultaElegibilidadeParticipanteTorneio delegado) {
        return new ConsultaElegibilidadeComCache(delegado);
    }

    @Bean
    public TorneioServico torneioServico(TorneioRepositorio torneioRepositorio,
            ConsultaElegibilidadeParticipanteTorneio consultaElegibilidade,
            EventoBarramento barramento) {
        return new TorneioServico(torneioRepositorio,
            new OrganizadorTorneioServico(),
            new GeradorEstruturaCompeticaoServico(),
            consultaElegibilidade,
            barramento);
    }

    @Bean
    public TorneioServicoAplicacao torneioServicoAplicacao(TorneioRepositorioAplicacao repositorio) {
        return new TorneioServicoAplicacao(repositorio);
    }

    // ── Partida ────────────────────────────────────────────────────
    @Bean
    public PartidaServico partidaServico(PartidaRepositorio partidaRepositorio,
            ConsultaCompeticaoTorneio consultaCompeticaoTorneio,
            EventoBarramento barramento) {
        return new PartidaServico(partidaRepositorio,
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

    public static void main(String[] args) {
        run(BackendAplicacao.class, args);
    }
}
