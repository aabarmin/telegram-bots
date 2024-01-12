package dev.abarmin.bots.config;

import dev.abarmin.bots.core.UriJdbcConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.List;

@Configuration
public class CustomJdbcConfiguration extends AbstractJdbcConfiguration {
    @Override
    protected List<?> userConverters() {
        return List.of(
                new UriJdbcConverter.ReadUriConverter(),
                new UriJdbcConverter.WriteUriConverter()
        );
    }
}
