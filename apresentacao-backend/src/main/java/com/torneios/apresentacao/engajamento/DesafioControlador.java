package com.torneios.apresentacao.engajamento;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.torneios.aplicacao.engajamento.desafio.DesafioServicoAplicacao;
import com.torneios.apresentacao.SessaoUsuario;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.ContaUsuarioServico;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("backend/desafio-amistoso")
@Transactional
class DesafioControlador {

    @Autowired
    DesafioServicoAplicacao desafioServicoAplicacao;
    @Autowired
    ContaUsuarioServico contaUsuarioServico;

    @RequestMapping(method = POST, path = "propor")
    DesafioServicoAplicacao.DesafioResumo propor(@RequestBody PropostaDesafioDto dto, HttpSession sessao) {
        return desafioServicoAplicacao.proporConfronto(
                gerarId(),
                exigirTecnico(sessao),
                dto.timeDesafianteId,
                dto.timeDesafiadoId,
                dto.dataHora,
                dto.local);
    }

    @RequestMapping(method = POST, path = "{id}/aceitar")
    DesafioServicoAplicacao.DesafioResumo aceitar(@PathVariable long id, HttpSession sessao) {
        return desafioServicoAplicacao.aceitarConvite(id, exigirTecnico(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/recusar")
    DesafioServicoAplicacao.DesafioResumo recusar(@PathVariable long id, HttpSession sessao) {
        return desafioServicoAplicacao.recusarConvite(id, exigirTecnico(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/cancelar")
    DesafioServicoAplicacao.DesafioResumo cancelar(@PathVariable long id, HttpSession sessao) {
        return desafioServicoAplicacao.cancelarDesafio(id, exigirTecnico(sessao));
    }

    @RequestMapping(method = POST, path = "{id}/reagendar")
    DesafioServicoAplicacao.DesafioResumo reagendar(@PathVariable long id,
                                                    @RequestBody ReagendamentoDto dto,
                                                    HttpSession sessao) {
        return desafioServicoAplicacao.reagendarAmistoso(
                id, exigirTecnico(sessao), dto.novaDataHora, dto.novoLocal);
    }

    @RequestMapping(method = POST, path = "{id}/registrar-resultado")
    DesafioServicoAplicacao.DesafioResumo registrarResultado(@PathVariable long id,
                                                             @RequestBody ResultadoDesafioDto dto,
                                                             HttpSession sessao) {
        return desafioServicoAplicacao.registrarResultado(
                id, exigirTecnico(sessao), dto.golsDesafiante, dto.golsDesafiado);
    }

    @RequestMapping(method = GET, path = "time")
    List<DesafioServicoAplicacao.DesafioResumo> acompanhar(
            @RequestParam long timeId,
            HttpSession sessao) {
        return desafioServicoAplicacao.acompanharConfrontosDoTime(
                timeId, exigirTecnico(sessao));
    }

    @RequestMapping(method = GET, path = "historico")
    List<DesafioServicoAplicacao.DesafioResumo> historico(@RequestParam long timeId) {
        return desafioServicoAplicacao.listarHistoricoDoTime(timeId);
    }

    static class PropostaDesafioDto {
        public long timeDesafianteId;
        public long timeDesafiadoId;
        public LocalDateTime dataHora;
        public String local;
    }

    static class ReagendamentoDto {
        public LocalDateTime novaDataHora;
        public String novoLocal;
    }

    static class ResultadoDesafioDto {
        public int golsDesafiante;
        public int golsDesafiado;
    }

    private long exigirTecnico(HttpSession sessao) {
        long usuarioId = SessaoUsuario.exigirUsuarioId(sessao);
        contaUsuarioServico.exigirPodeGerenciarTimes(new UsuarioId(usuarioId));
        return usuarioId;
    }

    private long gerarId() {
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return id == 0 ? 1L : id;
    }
}
