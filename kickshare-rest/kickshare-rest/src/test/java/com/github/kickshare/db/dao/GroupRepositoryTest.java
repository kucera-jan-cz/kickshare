package com.github.kickshare.db.dao;

import static org.testng.Assert.assertNotNull;

import java.sql.SQLException;

import com.github.kickshare.db.dao.impl.GroupRepositoryImpl;
import com.github.kickshare.db.jooq.tables.daos.GroupDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.GroupDB;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 6.4.2017
 */
public class GroupRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupRepositoryTest.class);
    private static DSLContext dsl;

    @BeforeClass
    public static void setUp() throws SQLException {
        dsl = DSLUtil.create();
    }

    @Test(enabled = false)
    public void load() {
//        GroupRepositoryImpl repository = new GroupRepositoryImpl(dsl);
//        Group group = repository.findOne(1L);
        GroupDaoDB dao = new GroupDaoDB(dsl.configuration());
        dao.fetchOneByIdDB(1L).getLeaderId();
    }


    @Test(enabled = false)
    public void create() {
//        GroupRepositoryImpl repository = new GroupRepositoryImpl();
        GroupRepositoryImpl repository = new GroupRepositoryImpl(dsl.configuration());
        GroupDB group = new GroupDB();
        group.setLeaderId(1L);
        group.setName("Boardgame 42 CZ");
        group.setProjectId(439380282L);
        Long key = repository.createReturningKey(group);

        GroupDB retrievedGroup = repository.findById(key);
        assertNotNull(group);
    }
}
