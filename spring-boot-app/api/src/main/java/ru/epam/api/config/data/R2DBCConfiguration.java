package ru.epam.api.config.data;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
@EnableR2dbcRepositories
@RequiredArgsConstructor
public class R2DBCConfiguration extends AbstractR2dbcConfiguration {

    private final R2DBCConfigurationProperties properties;

    @Override
    @Bean
    @Qualifier("postgresql")
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(ConnectionFactoryOptions.DRIVER, "postgresql")
                        .option(ConnectionFactoryOptions.HOST, properties.getHost())
                        .option(ConnectionFactoryOptions.PORT, properties.getPort())
                        .option(ConnectionFactoryOptions.DATABASE, properties.getDb())
                        .option(ConnectionFactoryOptions.USER, properties.getUsername())
                        .option(ConnectionFactoryOptions.PASSWORD, properties.getPassword())
                        .build()
        );
    }

    @Bean
    public R2dbcEntityOperations postgresOperations(@Qualifier("postgresql") ConnectionFactory connectionFactory) {
        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);
        return new R2dbcEntityTemplate(databaseClient, PostgresDialect.INSTANCE);
    }

}
