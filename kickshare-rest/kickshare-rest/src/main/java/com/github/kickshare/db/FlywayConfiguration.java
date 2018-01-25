package com.github.kickshare.db;

import java.util.Arrays;
import java.util.function.Supplier;

import javax.sql.DataSource;

import com.github.kickshare.db.multischema.FlywayMultiTenantMigration;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Jan.Kucera
 * @since 19.5.2017
 */
@Configuration
@Import(DatasourceConfiguration.class)
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfiguration {
    @Bean
    public Supplier<Flyway> flywaySupplier(
            @Autowired final FlywayProperties properties,
            @Autowired final DataSource dataSource) {
        return () -> {
            Flyway flyway = new Flyway();
            flyway.setDataSource(dataSource);
            flyway.setLocations(properties.getLocations().toArray(new String[0]));
            return flyway;
        };
    }

    @Bean
    public FlywayMultiTenantMigration migration(
            @Autowired Supplier<Flyway> flyway,
            ResourceLoader resourceLoader,
            @Autowired FlywayProperties properties,
            @Value("${kickshare.flyway.schemas}") String schemas,
            @Value("${kickshare.flyway.parallel}") Boolean parallel) {
        return new FlywayMultiTenantMigration(flyway, resourceLoader, Arrays.asList(schemas.split(",")), parallel);
    }
}
