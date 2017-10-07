package com.github.kickshare.db;

import javax.sql.DataSource;

import com.github.kickshare.db.multischema.MultiSchemaDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jan.Kucera
 * @since 3.10.2017
 */
@Configuration
public class DatasourceConfiguration {
    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        DataSource dataSource = createDataSource(properties, org.apache.tomcat.jdbc.pool.DataSource.class);
        return new MultiSchemaDataSource(dataSource);
    }

    protected <T> T createDataSource(DataSourceProperties properties,
            Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }
}
