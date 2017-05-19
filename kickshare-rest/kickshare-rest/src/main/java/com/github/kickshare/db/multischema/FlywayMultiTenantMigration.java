package com.github.kickshare.db.multischema;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Jan.Kucera
 * @since 11.4.2017
 */
@AllArgsConstructor
public class FlywayMultiTenantMigration {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlywayMultiTenantMigration.class);
    private Supplier<Flyway> flywaySupplier;
    private ResourceLoader resourceLoader;
    private List<String> schemas;
    private FlywayProperties properties;
    private Boolean parallel;

    @PostConstruct
    void migrate() {
        migrateAllSchemas();
    }

    private void migrateAllSchemas() {
        final Stream<String> schemaStream = parallel ? schemas.parallelStream() : schemas.stream();
        schemaStream.forEach(this::migration);
    }

    private void migration(String schema) {
        LOGGER.info("Migrating schema: {}", schema);
        Flyway flyway = flywaySupplier.get();
        flyway.setSchemas(schema);
        String schemaSpecificLocation = MessageFormat.format("classpath:db/{0}/migration", schema.toLowerCase());
        if (resourceLoader.getResource(schemaSpecificLocation).exists()) {
            flyway.setLocations(ArrayUtils.add(properties.getLocations().toArray(new String[0]), schemaSpecificLocation));
        } else {
            flyway.setLocations(properties.getLocations().toArray(new String[0]));
        }
        flyway.migrate();
    }

}
