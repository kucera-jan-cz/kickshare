package com.github.kickshare.db;

import java.util.Arrays;

import javax.sql.DataSource;

import com.github.kickshare.db.dao.DAOConfiguration;
import com.github.kickshare.db.multischema.FlywayMultiTenantMigration;
import com.github.kickshare.db.multischema.MultiSchemaDataSource;
import com.github.kickshare.db.tools.SQLLogging;
import org.flywaydb.core.Flyway;
import org.jooq.ConnectionProvider;
import org.jooq.ExecuteListenerProvider;
import org.jooq.SQLDialect;
import org.jooq.TransactionProvider;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
@Configuration
@Import(DAOConfiguration.class)
@EnableTransactionManagement
public class JooqConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(JooqConfiguration.class);
//    @Bean
//    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean
//    public DSLContext dsl(org.jooq.Configuration config) {
//        return new DefaultDSLContext(config);
//    }
//
//    @Bean
//    public ConnectionProvider connectionProvider(DataSource dataSource) {
//        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
//    }

//    @Bean
//    public TransactionProvider transactionProvider() {
//        return new SpringTransactionProvider();
//    }
//
//    @Bean
//    public ExceptionTranslator exceptionTranslator() {
//        return new ExceptionTranslator();
//    }
//
//    @Bean
//    public ExecuteListenerProvider executeListenerProvider(ExceptionTranslator exceptionTranslator) {
//        return new DefaultExecuteListenerProvider(exceptionTranslator);
//    }

//    @Bean
//    public DataSource dataSource(DataSource actualDataSource) {
//        return new MultiSchemaDataSource(actualDataSource);
//    }

//    @Bean
//    @FlywayDataSource
//    @ConfigurationProperties(prefix = "spring.datasource.tomcat")
//    public DataSource dataSource(DataSourceProperties properties) {
//        org.apache.tomcat.jdbc.pool.DataSource dataSource = createDataSource(
//                properties, org.apache.tomcat.jdbc.pool.DataSource.class);
//        DatabaseDriver databaseDriver = DatabaseDriver
//                .fromJdbcUrl(properties.determineUrl());
//        String validationQuery = databaseDriver.getValidationQuery();
//        if (validationQuery != null) {
//            dataSource.setTestOnBorrow(true);
//            dataSource.setValidationQuery(validationQuery);
//        }
//        return dataSource;
//    }
//
//    @Bean
//    public MultiSchemaDataSource multiSchemaDataSource(DataSource dataSource) {
//        return new MultiSchemaDataSource(dataSource);
//    }

    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(new MultiSchemaDataSource(dataSource)));
    }
//
//    protected <T> T createDataSource(DataSourceProperties properties,
//            Class<? extends DataSource> type) {
//        return (T) properties.initializeDataSourceBuilder().type(type).build();
//    }

    @Bean
    public FlywayMultiTenantMigration migration(Flyway flyway, @Value("${kickshare.flyway.schemas}") String schemas) {
        return new FlywayMultiTenantMigration(flyway, () -> Arrays.asList(schemas.split(",")));
    }

    @Bean
    public org.jooq.Configuration jooqConfig(ConnectionProvider connectionProvider,
            TransactionProvider transactionProvider, ExecuteListenerProvider executeListenerProvider) {
        LOGGER.warn("Instantiating JOOQ configuration");

        org.jooq.Configuration configuration = new DefaultConfiguration()
                .derive(connectionProvider)
                .derive(transactionProvider)
                .derive(executeListenerProvider)
                .derive(SQLDialect.POSTGRES)
                .derive(new SQLLogging());
        configuration.settings().withRenderSchema(false);
        return configuration;
    }
}
