package com.github.kickshare.db.multischema;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Jan.Kucera
 * @since 11.4.2017
 */
@AllArgsConstructor
public class FlywayMultiTenantMigration {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlywayMultiTenantMigration.class);
    private Flyway flyway;
    private ResourceLoader resourceLoader;
    private Supplier<List<String>> schemaProvider;

    @PostConstruct
    void migrate() {
        migrateAllSchemas();
    }

    private void migrateAllSchemas() {
        String[] originalLocations = flyway.getLocations();

        List<String> schemas = schemaProvider.get();
        for (String schema : schemas) {
            LOGGER.info("Migrating schema: {}", schema);
            flyway.setSchemas(schema);
            String schemaSpecificLocation = MessageFormat.format("classpath:db/{0}/migration", schema.toLowerCase());
            if (resourceLoader.getResource(schemaSpecificLocation).exists()) {
                flyway.setLocations(ArrayUtils.add(originalLocations, schemaSpecificLocation));
            } else {
                flyway.setLocations(originalLocations);
            }
            flyway.migrate();
        }
    }

}
