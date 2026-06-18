package com.torneios.infraestrutura.persistencia.jpa;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class EstrategiaNomesJpa extends CamelCaseToUnderscoresNamingStrategy {

    @Override
    protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
        return false;
    }
}
