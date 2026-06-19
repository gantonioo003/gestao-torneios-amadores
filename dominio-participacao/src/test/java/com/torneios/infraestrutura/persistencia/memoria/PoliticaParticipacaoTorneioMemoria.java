package com.torneios.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.participacao.solicitacao.PoliticaParticipacaoTorneio;

public class PoliticaParticipacaoTorneioMemoria implements PoliticaParticipacaoTorneio {

    private final Set<TorneioId> torneiosAbertos = new HashSet<>();
    private final Set<TorneioId> torneiosIniciados = new HashSet<>();
    private final Map<TorneioId, UsuarioId> organizadores = new HashMap<>();

    public void abrirParaSolicitacoes(TorneioId torneioId) {
        torneiosAbertos.add(torneioId);
    }

    public void fecharParaSolicitacoes(TorneioId torneioId) {
        torneiosAbertos.remove(torneioId);
    }

    public void definirOrganizador(TorneioId torneioId, UsuarioId organizadorId) {
        organizadores.put(torneioId, organizadorId);
    }

    public void iniciarTorneio(TorneioId torneioId) {
        torneiosIniciados.add(torneioId);
    }

    public void limpar() {
        torneiosAbertos.clear();
        torneiosIniciados.clear();
        organizadores.clear();
    }

    @Override
    public boolean aceitaSolicitacoes(TorneioId torneioId) {
        return torneiosAbertos.contains(torneioId);
    }

    @Override
    public boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId) {
        return usuarioId != null && usuarioId.equals(organizadores.get(torneioId));
    }

    @Override
    public UsuarioId organizadorDoTorneio(TorneioId torneioId) {
        return organizadores.get(torneioId);
    }

    @Override
    public void adicionarParticipante(TorneioId torneioId, TimeId timeId) {
    }

    @Override
    public void removerParticipante(TorneioId torneioId, TimeId timeId) {
    }

    @Override
    public boolean possuiParticipante(TorneioId torneioId, TimeId timeId) {
        return false;
    }

    @Override
    public boolean torneioIniciado(TorneioId torneioId) {
        return torneiosIniciados.contains(torneioId);
    }
}
