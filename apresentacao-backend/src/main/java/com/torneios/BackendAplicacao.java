package com.torneios;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.torneios.aplicacao.participacao.candidatura.SolicitacaoRepositorioAplicacao;
import com.torneios.aplicacao.participacao.candidatura.SolicitacaoServicoAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaRepositorioAplicacao;
import com.torneios.aplicacao.participacao.conta.ContaServicoAplicacao;
import com.torneios.aplicacao.participacao.profissional.ProfissionalRepositorioAplicacao;
import com.torneios.aplicacao.participacao.profissional.ProfissionalServicoAplicacao;
import com.torneios.aplicacao.participacao.time.TimeRepositorioAplicacao;
import com.torneios.aplicacao.participacao.time.TimeServicoAplicacao;
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

    public static void main(String[] args) {
        run(BackendAplicacao.class, args);
    }
}
