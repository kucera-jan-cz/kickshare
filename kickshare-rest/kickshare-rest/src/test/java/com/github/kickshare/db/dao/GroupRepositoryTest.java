package com.github.kickshare.db.dao;

import static org.testng.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.kickshare.db.h2.tables.daos.GroupDao;
import com.github.kickshare.db.h2.tables.pojos.Group;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 6.4.2017
 */
public class GroupRepositoryTest {
    private static EmbeddedDatabase db;
    private static DSLContext dsl;

//    @BeforeClass
    @BeforeClass
    public static void setUp() throws SQLException {
        //db = new EmbeddedDatabaseBuilder().addDefaultScripts().build();
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/migration/V001__init.sql")
                .addScript("db/migration/V101__init_data.sql")
                .build();
        Connection connection = db.getConnection();
        Settings settings = new Settings().withRenderSchema(false);
        dsl = DSL.using(connection, settings);
    }

    @Test
    public void load() throws SQLException {
//        GroupRepositoryImpl repository = new GroupRepositoryImpl(dsl);
//        Group group = repository.findOne(1L);
        GroupDao dao = new GroupDao(dsl.configuration());
        dao.fetchOneById(1L).getLeaderId();
    }


    @Test
    public void create() throws SQLException {
//        GroupRepositoryImpl repository = new GroupRepositoryImpl();
        GroupRepositoryImpl repository = new GroupRepositoryImpl(dsl.configuration());
        Group group = new Group();
        group.setLeaderId(1L);
        group.setName("Boardgame 42 CZ");
        group.setProjectId(439380282L);
        Long key = repository.createReturningKey(group);



        Group retrievedGroup = repository.findById(key);
        assertNotNull(group);
    }
}
