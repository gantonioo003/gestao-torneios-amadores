package com.torneios;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Serializa identificadores do tipo {@link Long} como string no JSON.
 *
 * <p>O JavaScript representa inteiros com seguranca apenas ate 2^53. Os IDs gerados
 * pelo backend podem ultrapassar esse limite, fazendo o navegador arredondar o numero
 * e perder os ultimos digitos. Enviando o ID como string, a precisao e preservada.
 * Campos numericos primitivos (estatisticas, contagens) usam {@code int}/{@code long}
 * e nao sao afetados, pois aqui so o wrapper {@code Long} e convertido.</p>
 */
@Configuration
class ConfiguracaoSerializacaoJson {

    @Bean
    Jackson2ObjectMapperBuilderCustomizer serializarLongComoString() {
        SimpleModule modulo = new SimpleModule();
        modulo.addSerializer(Long.class, new JsonSerializer<Long>() {
            @Override
            public void serialize(Long valor, JsonGenerator gerador, SerializerProvider provedor)
                    throws IOException {
                gerador.writeString(String.valueOf(valor));
            }
        });
        return builder -> builder.modulesToInstall(modulo);
    }
}
