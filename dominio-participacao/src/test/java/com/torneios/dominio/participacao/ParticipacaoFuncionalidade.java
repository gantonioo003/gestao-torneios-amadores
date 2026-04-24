package com.torneios.dominio.participacao;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.infraestrutura.persistencia.memoria.SolicitacaoParticipacaoRepositorioMemoria;
import com.torneios.infraestrutura.persistencia.memoria.TimeRepositorioMemoria;

/**
 * Classe base para compartilhar preparacao e estado comum entre os steps
 * dos cenarios de participacao.
 */
public abstract class ParticipacaoFuncionalidade {

    protected static final UsuarioId USUARIO_AUTENTICADO_ID = new UsuarioId(1L);
    protected static final UsuarioId ORGANIZADOR_ID = new UsuarioId(2L);
    protected static final UsuarioId USUARIO_NAO_AUTENTICADO_ID = null;
    protected static final TorneioId TORNEIO_ID = new TorneioId(1L);
    protected static final TimeId TIME_A_ID = new TimeId(1L);
    protected static final TimeId TIME_B_ID = new TimeId(2L);

    protected final SolicitacaoParticipacaoRepositorioMemoria solicitacaoRepositorio = new SolicitacaoParticipacaoRepositorioMemoria();
    protected final TimeRepositorioMemoria timeRepositorio = new TimeRepositorioMemoria();

    protected Exception excecaoCapturada;
}
