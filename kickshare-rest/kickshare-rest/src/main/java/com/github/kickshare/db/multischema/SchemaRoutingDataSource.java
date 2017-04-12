package com.github.kickshare.db.multischema;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Jan.Kucera
 * @since 12.4.2017
 */
public class SchemaRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return SchemaContextHolder.getSchema();
    }
}
