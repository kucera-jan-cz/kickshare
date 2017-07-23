package com.github.kickshare.db.dao;

import static org.testng.Assert.assertNotNull;

import java.sql.SQLException;

import com.github.kickshare.db.jooq.tables.daos.TagDao;
import com.github.kickshare.db.jooq.tables.pojos.Tag;
import com.github.kickshare.db.jooq.tables.records.TagRecord;
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
        TagRepositoryImpl repository = new TagRepositoryImpl(new TagDao(dsl.configuration()));
        TagRecord record = repository.createReturning(new Tag(null, "Action"));
        Tag tag = record.into(Tag.class);
        LOGGER.info("{}", tag);
        assertNotNull(tag);
    }
}
