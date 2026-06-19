package com.torneios.dominio.engajamento.feed;

import com.torneios.dominio.compartilhado.partida.PartidaId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.compartilhado.time.TimeId;

public interface ConsultaSuporteFeedTorneio {

    boolean usuarioEstaAutenticado(UsuarioId usuarioId);

    boolean usuarioEhOrganizador(TorneioId torneioId, UsuarioId usuarioId);

    boolean partidaPertenceAoTorneio(TorneioId torneioId, PartidaId partidaId);

    boolean usuarioEhResponsavelTime(TimeId timeId, UsuarioId usuarioId);
}
