package com.github.kickshare.security.session;

import javax.sql.DataSource;

import com.github.kickshare.db.multischema.SchemaContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.session.jdbc.JdbcOperationsSessionRepository;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Jan.Kucera
 * @since 3.8.2017
 */
public class MultiSchemaSessionRepository extends JdbcOperationsSessionRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiSchemaSessionRepository.class);
    private String[] schemas;

    public MultiSchemaSessionRepository(String[] schemas, final DataSource dataSource, final PlatformTransactionManager transactionManager) {
        super(dataSource, transactionManager);
        this.schemas = schemas;
    }

    @Scheduled(cron = "${spring.session.cleanup.cron.expression:0 * * * * *}")
    public void cleanUpExpiredSessions() {
        String previous = SchemaContextHolder.getSchema();
        try {
            for (String schema : this.schemas) {
                SchemaContextHolder.setSchema(schema);
                super.cleanUpExpiredSessions();
            }
        } catch (RuntimeException ex) {
            LOGGER.error("Failed to cleanup sessions", ex);
        } finally {
            SchemaContextHolder.setSchema(previous);
        }

    }
}
