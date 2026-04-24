package com.torneios.dominio.competicao.partida;

import java.util.List;

import com.torneios.dominio.compartilhado.enumeracao.FormatoTorneio;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public interface ConsultaCompeticaoTorneio {

    boolean estruturaGerada(TorneioId torneioId);

    boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId);

    FormatoTorneio obterFormato(TorneioId torneioId);

    int obterQuantidadeJogadoresPorEquipe(TorneioId torneioId);

    List<TimeId> listarParticipantesAprovados(TorneioId torneioId);

    List<List<TimeId>> listarGrupos(TorneioId torneioId);
}
