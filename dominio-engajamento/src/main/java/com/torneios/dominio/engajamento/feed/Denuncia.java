package com.torneios.dominio.engajamento.feed;

import java.time.LocalDateTime;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class Denuncia {
    private final DenunciaId id;
    private final UsuarioId denuncianteId;
    private final TipoAlvoDenuncia tipoAlvo;
    private final long alvoId;
    private final String motivo;
    private final LocalDateTime criadaEm;
    private StatusDenuncia status;

    public Denuncia(DenunciaId id, UsuarioId denuncianteId, TipoAlvoDenuncia tipoAlvo,
                    long alvoId, String motivo) {
        if (id == null || denuncianteId == null || tipoAlvo == null || alvoId <= 0) {
            throw new IllegalArgumentException("Os dados de identificacao da denuncia sao obrigatorios.");
        }
        if (motivo == null || motivo.isBlank() || motivo.trim().length() > 500) {
            throw new IllegalArgumentException("O motivo deve possuir entre 1 e 500 caracteres.");
        }
        this.id = id;
        this.denuncianteId = denuncianteId;
        this.tipoAlvo = tipoAlvo;
        this.alvoId = alvoId;
        this.motivo = motivo.trim();
        this.criadaEm = LocalDateTime.now();
        this.status = StatusDenuncia.PENDENTE;
    }

    public void marcarAnalisada() { status = StatusDenuncia.ANALISADA; }
    public void arquivar() { status = StatusDenuncia.ARQUIVADA; }
    public DenunciaId getId() { return id; }
    public UsuarioId getDenuncianteId() { return denuncianteId; }
    public TipoAlvoDenuncia getTipoAlvo() { return tipoAlvo; }
    public long getAlvoId() { return alvoId; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public StatusDenuncia getStatus() { return status; }
}
