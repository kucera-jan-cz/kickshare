package com.github.kickshare.db;

import javax.sql.DataSource;

import com.github.kickshare.db.multischema.MultiSchemaDataSource;
import com.github.kickshare.db.tools.SQLLogging;
import org.jooq.ConnectionProvider;
import org.jooq.ExecuteListenerProvider;
import org.jooq.SQLDialect;
import org.jooq.TransactionProvider;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Import(DatasourceConfiguration.class)
@EnableTransactionManagement
public class JooqConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(JooqConfiguration.class);

    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource dataSource) {
        assert(MultiSchemaDataSource.class.isInstance(dataSource));
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
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
