package com.torneios.infraestrutura.persistencia.memoria;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistoso;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoId;
import com.torneios.dominio.engajamento.desafio.DesafioAmistosoRepositorio;

public class DesafioAmistosoRepositorioMemoria implements DesafioAmistosoRepositorio {

    private final Map<DesafioAmistosoId, DesafioAmistoso> dados = new LinkedHashMap<>();

    @Override
    public void salvar(DesafioAmistoso desafioAmistoso) {
        dados.put(desafioAmistoso.getId(), desafioAmistoso);
    }

    @Override
    public Optional<DesafioAmistoso> buscarPorId(DesafioAmistosoId desafioAmistosoId) {
        return Optional.ofNullable(dados.get(desafioAmistosoId));
    }

    @Override
    public List<DesafioAmistoso> listarHistoricoDoTime(TimeId timeId) {
        return dados.values().stream()
                .filter(desafioAmistoso -> desafioAmistoso.envolveTime(timeId))
                .toList();
    }

    public void limpar() {
        dados.clear();
    }
}
