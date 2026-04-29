package com.torneios.dominio.engajamento.desafio;

import java.util.List;
import java.util.Optional;

import com.torneios.dominio.compartilhado.time.TimeId;

public interface DesafioAmistosoRepositorio {

    void salvar(DesafioAmistoso desafioAmistoso);

    Optional<DesafioAmistoso> buscarPorId(DesafioAmistosoId desafioAmistosoId);

    List<DesafioAmistoso> listarHistoricoDoTime(TimeId timeId);
}
