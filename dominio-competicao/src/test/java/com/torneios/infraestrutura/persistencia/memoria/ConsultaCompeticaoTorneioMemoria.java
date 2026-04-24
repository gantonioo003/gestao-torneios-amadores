package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.torneios.dominio.compartilhado.enumeracao.FormatoEquipe;
import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.competicao.partida.ConsultaCompeticaoTorneio;

public class ConsultaCompeticaoTorneioMemoria implements ConsultaCompeticaoTorneio {

    private final Map<TorneioId, DadosTorneio> torneios = new HashMap<>();

    public void registrarTorneio(TorneioId torneioId,
                                  UsuarioId organizadorId,
                                  FormatoTorneio formato,
                                  FormatoEquipe formatoEquipe,
                                  List<TimeId> participantes,
                                  List<List<TimeId>> grupos,
                                  boolean estruturaGerada) {
        torneios.put(torneioId, new DadosTorneio(organizadorId, formato, formatoEquipe, participantes, grupos, estruturaGerada));
    }

    @Override
    public boolean estruturaGerada(TorneioId torneioId) {
        DadosTorneio dados = torneios.get(torneioId);
        return dados != null && dados.estruturaGerada;
    }

    @Override
    public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        DadosTorneio dados = torneios.get(torneioId);
        return dados != null && dados.organizadorId.equals(usuarioId);
    }

    @Override
    public FormatoTorneio obterFormato(TorneioId torneioId) {
        return torneios.get(torneioId).formato;
    }

    @Override
    public int obterQuantidadeJogadoresPorEquipe(TorneioId torneioId) {
        return torneios.get(torneioId).formatoEquipe.getQuantidadeJogadores();
    }

    @Override
    public List<TimeId> listarParticipantesAprovados(TorneioId torneioId) {
        return torneios.get(torneioId).participantes;
    }

    @Override
    public List<List<TimeId>> listarGrupos(TorneioId torneioId) {
        return torneios.get(torneioId).grupos;
    }

    public void limpar() {
        torneios.clear();
    }

    private record DadosTorneio(
            UsuarioId organizadorId,
            FormatoTorneio formato,
            FormatoEquipe formatoEquipe,
            List<TimeId> participantes,
            List<List<TimeId>> grupos,
            boolean estruturaGerada) {
    }
}
