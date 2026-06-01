package com.torneios.infraestrutura.persistencia.memoria;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.participacao.profissional.ProfissionalEsportivo;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoRepositorio;

public class ProfissionalEsportivoRepositorioMemoria implements ProfissionalEsportivoRepositorio {

    private final Map<ProfissionalEsportivoId, ProfissionalEsportivo> dados = new LinkedHashMap<>();

    @Override
    public void salvar(ProfissionalEsportivo profissional) {
        dados.put(profissional.getId(), profissional);
    }

    @Override
    public Optional<ProfissionalEsportivo> buscarPorId(ProfissionalEsportivoId id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<ProfissionalEsportivo> pesquisarPorNome(String nome) {
        return dados.values().stream()
            .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
            .toList();
    }

    @Override
    public void remover(ProfissionalEsportivoId id) {
        dados.remove(id);
    }

    public void limpar() {
        dados.clear();
    }
}
