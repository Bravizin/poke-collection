package br.com.selmes.pokecollection.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");

        HikariConfig config = new HikariConfig();

        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            try {
                // Parse da URL do Railway: postgresql://user:password@host:port/database
                URI dbUri = new URI(databaseUrl);

                String username = null;
                String password = null;

                // Extrai username e password da URI
                if (dbUri.getUserInfo() != null) {
                    String[] credentials = dbUri.getUserInfo().split(":");
                    username = credentials[0];
                    if (credentials.length > 1) {
                        password = credentials[1];
                    }
                }

                // Constrói a JDBC URL
                String jdbcUrl = String.format("jdbc:postgresql://%s:%d%s",
                        dbUri.getHost(),
                        dbUri.getPort(),
                        dbUri.getPath());

                config.setJdbcUrl(jdbcUrl);
                config.setUsername(username);
                config.setPassword(password);

            } catch (URISyntaxException e) {
                throw new RuntimeException("Failed to parse DATABASE_URL", e);
            }
        } else {
            // Fallback para desenvolvimento local
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
            config.setUsername("postgres");
            config.setPassword("postgres");
        }

        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}
