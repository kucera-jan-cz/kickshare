package com.github.kickshare.db.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.github.kickshare.db.tools.SQLLogging;
import com.github.kickshare.domain.GroupSummary;
import com.github.kickshare.mapper.ExtendedMapper;
import com.github.kickshare.service.GroupServiceImpl;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
public class DatabaseIntegrationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseIntegrationTest.class);

    @Test
    public void groupInfo() throws SQLException {
        DSLContext context = createFileDSL();
        Configuration config = context.configuration();
        ExtendedMapper mapper = null;
        GroupServiceImpl repository = null;
        List<GroupSummary> infos = repository.findAllGroupInfo(217227567L);
        assertNotNull(infos);
        assertEquals(infos.size(), 2);
        LOGGER.info("{}", infos);
    }

    private static DSLContext createFileDSL() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:~/target/h2test.db;MODE=PostgreSQL;INIT=CREATE DOMAIN IF NOT EXISTS enum as VARCHAR(255);AUTO_SERVER=FALSE", "test", "test");
        Settings settings = new Settings()
                .withRenderSchema(false);

        DSLContext context = DSL.using(connection, settings);
        context.configuration().set(new SQLLogging());
        return context;
    }
}
