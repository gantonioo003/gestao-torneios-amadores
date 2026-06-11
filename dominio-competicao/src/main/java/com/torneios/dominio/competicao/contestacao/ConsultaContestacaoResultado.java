package com.torneios.dominio.competicao.contestacao;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConsultaContestacaoResultado {

    boolean usuarioEhResponsavelDoTime(TimeId timeId, UsuarioId usuarioId);

    int prazoContestacaoEmHoras(TorneioId torneioId);
}
