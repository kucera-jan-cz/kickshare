package com.github.kickshare.db.dao;


import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
public class ProjectRepositoryCustomTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectRepositoryCustomTest.class);
    private static DSLContext dsl;

    @BeforeClass
    public void setUp() throws SQLException {
        dsl = DSLUtil.create();

        Result<?> tables = dsl.resultQuery("SELECT table_schema,table_name FROM information_schema.tables\n"
                + "ORDER BY table_schema,table_name").fetch();
        LOGGER.info("{}", tables);
    }

    @Test
    public void load() throws SQLException {
//        ProjectRepositoryCustom custom = new ProjectRepositoryCustomImpl(connection);
//        List<Group> groups = custom.findAllGroups(439380282L);
//        assertNotNull(groups);
        ProjectRepository repository = new ProjectRepositoryImpl(dsl.configuration());
        LOGGER.info("{}", repository.getBacker(1L));

    }
}
