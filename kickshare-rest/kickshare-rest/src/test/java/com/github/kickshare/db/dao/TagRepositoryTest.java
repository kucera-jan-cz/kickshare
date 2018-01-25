package com.github.kickshare.db.dao;

import static org.testng.Assert.assertNotNull;

import java.sql.SQLException;

import com.github.kickshare.db.dao.impl.TagRepositoryImpl;
import com.github.kickshare.db.jooq.tables.daos.TagDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.TagDB;
import com.github.kickshare.db.jooq.tables.records.TagRecordDB;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 18.7.2017
 */
public class TagRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagRepositoryTest.class);

    @Test
    public static void setUp() throws SQLException {
        DSLContext dsl = DSLUtil.createDB();
        TagRepositoryImpl repository = new TagRepositoryImpl(new TagDaoDB(dsl.configuration()));
        TagRecordDB record = repository.createReturning(new TagDB(null, "Action"));
        TagDB tag = record.into(TagDB.class);
        LOGGER.info("{}", tag);
        assertNotNull(tag);
    }
}
