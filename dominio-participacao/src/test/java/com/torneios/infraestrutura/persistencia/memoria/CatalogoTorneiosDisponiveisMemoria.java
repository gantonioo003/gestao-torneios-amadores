package com.torneios.infraestrutura.persistencia.memoria;

import java.util.ArrayList;
import java.util.List;

import com.torneios.dominio.participacao.acesso.CatalogoTorneiosDisponiveis;
import com.torneios.dominio.participacao.acesso.TorneioDisponivel;

public class CatalogoTorneiosDisponiveisMemoria implements CatalogoTorneiosDisponiveis {

    private final List<TorneioDisponivel> torneios = new ArrayList<>();

    public void adicionar(TorneioDisponivel torneio) {
        torneios.add(torneio);
    }

    public void limpar() {
        torneios.clear();
    }

    @Override
    public List<TorneioDisponivel> listarTorneiosDisponiveis() {
        return List.copyOf(torneios);
    }
}
