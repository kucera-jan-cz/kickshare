package com.github.kickshare.db.multischema;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.flywaydb.core.Flyway;

/**
 * @author Jan.Kucera
 * @since 11.4.2017
 */
public class FlywayMultiTenantMigration {

    private Supplier<List<String>> schemaProvider;

    private Flyway flyway;

    public FlywayMultiTenantMigration(Flyway flyway, Supplier<List<String>> schemaProvider) {
        this.flyway = flyway;
        this.schemaProvider = schemaProvider;
    }

    @PostConstruct
    void migrate() {
        migrateAllSchemas();
    }

    private void migrateAllSchemas() {
        List<String> schemas = schemaProvider.get();
        for (String schema : schemas) {
            flyway.setSchemas(schema);
            flyway.migrate();
        }
    }

}
