package com.torneios.apresentacao;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.torneios.dominio.compartilhado.time.TimeId;
import com.torneios.dominio.compartilhado.torneio.TorneioId;
import com.torneios.dominio.compartilhado.usuario.UsuarioId;
import com.torneios.dominio.participacao.profissional.ProfissionalEsportivoId;
import com.torneios.dominio.participacao.profissional.RegistroDeCarreiraId;
import com.torneios.dominio.participacao.solicitacao.SolicitacaoParticipacaoId;

@Component
public class BackendMapeador extends ModelMapper {

    public BackendMapeador() {
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
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        return source != null ? super.map(source, destinationType) : null;
    }
}
