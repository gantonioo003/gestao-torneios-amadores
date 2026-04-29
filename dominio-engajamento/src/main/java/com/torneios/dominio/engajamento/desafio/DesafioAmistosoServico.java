package com.torneios.dominio.engajamento.desafio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.torneios.dominio.compartilhado.excecao.EntidadeNaoEncontradaException;
import com.torneios.dominio.compartilhado.excecao.OperacaoNaoPermitidaException;
import com.torneios.dominio.compartilhado.excecao.RegraDeNegocioException;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class DesafioAmistosoServico {

    private final DesafioAmistosoRepositorio desafioAmistosoRepositorio;
    private final ConsultaSuporteDesafioAmistoso consultaSuporte;

    public DesafioAmistosoServico(DesafioAmistosoRepositorio desafioAmistosoRepositorio,
                                  ConsultaSuporteDesafioAmistoso consultaSuporte) {
        this.desafioAmistosoRepositorio = Objects.requireNonNull(desafioAmistosoRepositorio,
                "O repositorio de desafios amistosos e obrigatorio.");
        this.consultaSuporte = Objects.requireNonNull(consultaSuporte,
                "A consulta de suporte de desafios amistosos e obrigatoria.");
    }

    public DesafioAmistoso proporConfronto(DesafioAmistosoId desafioAmistosoId,
                                           UsuarioId usuarioId,
                                           TimeId timeDesafianteId,
                                           TimeId timeDesafiadoId,
                                           LocalDateTime dataHora,
                                           String local) {
        validarUsuarioAutenticado(usuarioId);
        validarResponsavelDoTime(timeDesafianteId, usuarioId);
        if (!consultaSuporte.timesPodemSeDesafiar(timeDesafianteId, timeDesafiadoId)) {
            throw new RegraDeNegocioException("Os times informados nao podem participar deste desafio.");
        }

        DesafioAmistoso desafioAmistoso = new DesafioAmistoso(
                desafioAmistosoId, usuarioId, timeDesafianteId, timeDesafiadoId, dataHora, local);
        desafioAmistosoRepositorio.salvar(desafioAmistoso);
        return desafioAmistoso;
    }

    public DesafioAmistoso aceitarConvite(DesafioAmistosoId desafioAmistosoId, UsuarioId usuarioId) {
        DesafioAmistoso desafioAmistoso = obterDesafio(desafioAmistosoId);
        validarUsuarioAutenticado(usuarioId);
        validarResponsavelDoTime(desafioAmistoso.getTimeDesafiadoId(), usuarioId);
        desafioAmistoso.aceitar();
        desafioAmistosoRepositorio.salvar(desafioAmistoso);
        return desafioAmistoso;
    }

    public DesafioAmistoso recusarConvite(DesafioAmistosoId desafioAmistosoId, UsuarioId usuarioId) {
        DesafioAmistoso desafioAmistoso = obterDesafio(desafioAmistosoId);
        validarUsuarioAutenticado(usuarioId);
        validarResponsavelDoTime(desafioAmistoso.getTimeDesafiadoId(), usuarioId);
        desafioAmistoso.recusar();
        desafioAmistosoRepositorio.salvar(desafioAmistoso);
        return desafioAmistoso;
    }

    public DesafioAmistoso reagendarAmistoso(DesafioAmistosoId desafioAmistosoId,
                                             UsuarioId usuarioId,
                                             LocalDateTime novaDataHora,
                                             String novoLocal) {
        DesafioAmistoso desafioAmistoso = obterDesafio(desafioAmistosoId);
        validarUsuarioAutenticado(usuarioId);
        validarResponsavelDeAlgumTimeDoDesafio(desafioAmistoso, usuarioId);
        desafioAmistoso.reagendar(novaDataHora, novoLocal);
        desafioAmistosoRepositorio.salvar(desafioAmistoso);
        return desafioAmistoso;
    }

    public DesafioAmistoso registrarResultado(DesafioAmistosoId desafioAmistosoId,
                                              UsuarioId usuarioId,
                                              ResultadoAmistoso resultadoAmistoso) {
        DesafioAmistoso desafioAmistoso = obterDesafio(desafioAmistosoId);
        validarUsuarioAutenticado(usuarioId);
        validarResponsavelDeAlgumTimeDoDesafio(desafioAmistoso, usuarioId);
        desafioAmistoso.registrarResultado(resultadoAmistoso);
        desafioAmistosoRepositorio.salvar(desafioAmistoso);
        return desafioAmistoso;
    }

    public List<DesafioAmistoso> listarHistoricoDoTime(TimeId timeId) {
        Objects.requireNonNull(timeId, "O time do historico e obrigatorio.");
        return desafioAmistosoRepositorio.listarHistoricoDoTime(timeId).stream()
                .filter(desafioAmistoso -> desafioAmistoso.getStatus() == StatusDesafioAmistoso.RESULTADO_REGISTRADO)
                .toList();
    }

    private DesafioAmistoso obterDesafio(DesafioAmistosoId desafioAmistosoId) {
        return desafioAmistosoRepositorio.buscarPorId(desafioAmistosoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Desafio amistoso nao encontrado."));
    }

    private void validarUsuarioAutenticado(UsuarioId usuarioId) {
        Objects.requireNonNull(usuarioId, "O usuario do desafio e obrigatorio.");
        if (!consultaSuporte.usuarioEstaAutenticado(usuarioId)) {
            throw new OperacaoNaoPermitidaException("Apenas usuarios autenticados podem gerenciar desafios.");
        }
    }

    private void validarResponsavelDoTime(TimeId timeId, UsuarioId usuarioId) {
        if (!consultaSuporte.usuarioEhResponsavelDoTime(timeId, usuarioId)) {
            throw new OperacaoNaoPermitidaException("Apenas o responsavel do time pode executar esta operacao.");
        }
    }

    private void validarResponsavelDeAlgumTimeDoDesafio(DesafioAmistoso desafioAmistoso, UsuarioId usuarioId) {
        if (!consultaSuporte.usuarioEhResponsavelDoTime(desafioAmistoso.getTimeDesafianteId(), usuarioId)
                && !consultaSuporte.usuarioEhResponsavelDoTime(desafioAmistoso.getTimeDesafiadoId(), usuarioId)) {
            throw new OperacaoNaoPermitidaException(
                    "Apenas responsaveis dos times envolvidos podem gerenciar o amistoso.");
        }
    }
}
