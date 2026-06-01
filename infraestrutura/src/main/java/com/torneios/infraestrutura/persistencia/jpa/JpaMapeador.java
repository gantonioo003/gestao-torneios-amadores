package com.torneios.infraestrutura.persistencia.jpa;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.enumeracao.StatusSolicitacao;
import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.acesso.TipoContaUsuario;
import com.torneios.dominio.participacao.profissional.MotivoDeSaida;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreiraId;
import com.torneios.dominio.participacao.profissional.TipoProfissional;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;

@Component
class JpaMapeador extends ModelMapper {

    JpaMapeador() {
        var config = getConfiguration();
        config.setFieldMatchingEnabled(true);
        config.setFieldAccessLevel(AccessLevel.PRIVATE);

        // ── record Long → Long ──────────────────────────────────────
        addConverter(new AbstractConverter<Long, TimeId>() {
            @Override protected TimeId convert(Long s) { return new TimeId(s); }
        });
        addConverter(new AbstractConverter<Long, UsuarioId>() {
            @Override protected UsuarioId convert(Long s) { return new UsuarioId(s); }
        });
        addConverter(new AbstractConverter<Long, TorneioId>() {
            @Override protected TorneioId convert(Long s) { return new TorneioId(s); }
        });
        addConverter(new AbstractConverter<Long, SolicitacaoParticipacaoId>() {
            @Override protected SolicitacaoParticipacaoId convert(Long s) { return new SolicitacaoParticipacaoId(s); }
        });
        addConverter(new AbstractConverter<Long, ProfissionalEsportivoId>() {
            @Override protected ProfissionalEsportivoId convert(Long s) { return new ProfissionalEsportivoId(s); }
        });
        addConverter(new AbstractConverter<Long, RegistroDeCarreiraId>() {
            @Override protected RegistroDeCarreiraId convert(Long s) { return new RegistroDeCarreiraId(s); }
        });

        // ── String → Enum ───────────────────────────────────────────
        addConverter(new AbstractConverter<String, TipoProfissional>() {
            @Override protected TipoProfissional convert(String s) { return TipoProfissional.valueOf(s); }
        });
        addConverter(new AbstractConverter<String, MotivoDeSaida>() {
            @Override protected MotivoDeSaida convert(String s) { return s == null ? null : MotivoDeSaida.valueOf(s); }
        });
        addConverter(new AbstractConverter<String, StatusSolicitacao>() {
            @Override protected StatusSolicitacao convert(String s) { return StatusSolicitacao.valueOf(s); }
        });
        addConverter(new AbstractConverter<String, TipoContaUsuario>() {
            @Override protected TipoContaUsuario convert(String s) { return TipoContaUsuario.valueOf(s); }
        });
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        return source != null ? super.map(source, destinationType) : null;
    }
}
