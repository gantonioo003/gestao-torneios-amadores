package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.Map;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.contestacao.ConsultaContestacaoResultado;

public class ConsultaContestacaoResultadoMemoria implements ConsultaContestacaoResultado {

    private final Map<TimeId, UsuarioId> responsaveisPorTime = new HashMap<>();
    private final Map<TorneioId, Integer> prazosPorTorneio = new HashMap<>();

    private UsuarioId organizadorId;
    private int prazoPadraoHoras = 24;

    public void registrarResponsavel(TimeId timeId, UsuarioId usuarioId) {
        responsaveisPorTime.put(timeId, usuarioId);
    }

    public void definirResponsavelTime(UsuarioId usuarioId) {
        this.responsaveisPorTime.clear();
        this.responsaveisPorTime.put(new TimeId(1L), usuarioId);
    }

    public void registrarPrazo(TorneioId torneioId, int prazoHoras) {
        prazosPorTorneio.put(torneioId, prazoHoras);
    }

    public void definirPrazoHoras(int prazoHoras) {
        this.prazoPadraoHoras = prazoHoras;
    }

    public void definirOrganizador(UsuarioId usuarioId) {
        this.organizadorId = usuarioId;
    }

    @Override
    public boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId) {
        return usuarioId != null && usuarioId.equals(responsaveisPorTime.get(timeId));
    }

    public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        return usuarioId != null && usuarioId.equals(organizadorId);
    }

    @Override
    public int prazoContestacaoEmHoras(TorneioId torneioId) {
        return prazosPorTorneio.getOrDefault(torneioId, prazoPadraoHoras);
    }
}