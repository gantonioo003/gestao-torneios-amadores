package com.torneios.dominio.engajamento.palpite;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import com.torneios.dominio.compartilhado.usuario.UsuarioId;

public class ProgressoPalpite {

    private final UsuarioId usuarioId;
    private int pontos;
    private int sequenciaAtual;
    private int maiorSequencia;
    private int totalPalpites;
    private int totalAcertos;
    private LocalDate ultimaParticipacao;
    private final Set<SeloPalpite> selos = new LinkedHashSet<>();

    public ProgressoPalpite(UsuarioId usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("O usuario do progresso e obrigatorio.");
        }
        this.usuarioId = usuarioId;
    }

    public void registrarNovoPalpite(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("A data do palpite e obrigatoria.");
        }
        boolean primeiraParticipacaoDoDia = ultimaParticipacao == null || !ultimaParticipacao.equals(data);
        if (primeiraParticipacaoDoDia) {
            sequenciaAtual = ultimaParticipacao != null && ultimaParticipacao.plusDays(1).equals(data)
                    ? sequenciaAtual + 1
                    : 1;
            maiorSequencia = Math.max(maiorSequencia, sequenciaAtual);
            pontos += 5;
            ultimaParticipacao = data;
        }
        totalPalpites++;
        pontos += 10;
        atualizarSelos();
    }

    public void registrarApuracao(boolean acertou) {
        pontos += acertou ? 25 : 2;
        if (acertou) {
            totalAcertos++;
        }
        atualizarSelos();
    }

    private void atualizarSelos() {
        if (totalPalpites >= 1) selos.add(SeloPalpite.PRIMEIRO_PALPITE);
        if (maiorSequencia >= 3) selos.add(SeloPalpite.TRES_DIAS_SEGUIDOS);
        if (maiorSequencia >= 7) selos.add(SeloPalpite.SETE_DIAS_SEGUIDOS);
        if (totalAcertos >= 10) selos.add(SeloPalpite.DEZ_ACERTOS);
        if (totalPalpites >= 50) selos.add(SeloPalpite.CINQUENTA_PALPITES);
    }

    public UsuarioId getUsuarioId() { return usuarioId; }
    public int getPontos() { return pontos; }
    public int getSequenciaAtual() { return sequenciaAtual; }
    public int getMaiorSequencia() { return maiorSequencia; }
    public int getTotalPalpites() { return totalPalpites; }
    public int getTotalAcertos() { return totalAcertos; }
    public LocalDate getUltimaParticipacao() { return ultimaParticipacao; }
    public Set<SeloPalpite> getSelos() { return Set.copyOf(selos); }
}
