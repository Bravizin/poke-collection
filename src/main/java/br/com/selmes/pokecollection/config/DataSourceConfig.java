package br.com.selmes.pokecollection.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");

        // Railway fornece URL no formato postgresql://, mas JDBC precisa de jdbc:postgresql://
        if (databaseUrl != null && databaseUrl.startsWith("postgresql://")) {
            databaseUrl = "jdbc:" + databaseUrl;
        }

        // Fallback para desenvolvimento local
        if (databaseUrl == null) {
            databaseUrl = "jdbc:postgresql://localhost:5432/postgres";
        }

        String username = System.getenv("DATABASE_USER");
        if (username == null) {
            username = "postgres";
        }

        String password = System.getenv("DATABASE_PASSWORD");
        if (password == null) {
            password = "postgres";
        }

        return DataSourceBuilder
                .create()
                .url(databaseUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
