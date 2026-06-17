package com.torneios.infraestrutura.persistencia.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.torneios.dominio.participacao.acesso.CatalogoTorneiosDisponiveis;
import com.torneios.dominio.participacao.acesso.TorneioDisponivel;
import com.torneios.dominio.torneio.torneio.Torneio;
import com.torneios.dominio.torneio.torneio.TorneioRepositorio;

@Component
class CatalogoTorneiosDisponiveisJpa implements CatalogoTorneiosDisponiveis {

    @Autowired
    TorneioRepositorio torneioRepositorio;

    @Override
    public List<TorneioDisponivel> listarTorneiosDisponiveis() {
        return torneioRepositorio.listarTodos().stream()
                .filter(Torneio::estaDisponivelParaVisualizacao)
                .map(torneio -> new TorneioDisponivel(torneio.getId(), torneio.getNome(), torneio.aceitaSolicitacoes()))
                .toList();
    }
}
