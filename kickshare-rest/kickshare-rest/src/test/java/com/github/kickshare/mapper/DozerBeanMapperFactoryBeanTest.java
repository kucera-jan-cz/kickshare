package com.github.kickshare.mapper;

import static org.junit.Assert.assertEquals;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;

import com.github.kickshare.domain.Project;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Test;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */

public class DozerBeanMapperFactoryBeanTest {
    private static Instant NOW = Instant.now();
    private static final Class<com.github.kickshare.db.h2.tables.pojos.Project> DB_TYPE = com.github.kickshare.db.h2.tables.pojos.Project.class;

    @Test
    public void domain2DB() throws Exception {
        Mapper dozer = new DozerBeanMapper(Arrays.asList("dozer/db-2-domain-mappings.xml"));
        Project domain = new Project(1L, "Quodd Heroes", "DESC", "URL", NOW);
        com.github.kickshare.db.h2.tables.pojos.Project expected
                = new com.github.kickshare.db.h2.tables.pojos.Project(1L, "Quodd Heroes", "DESC", "URL", new Date(NOW.toEpochMilli()));
        assertReflectionEquals(expected, dozer.map(domain, DB_TYPE));
    }

    @Test
    public void db2Domain() throws Exception {
        Mapper dozer = new DozerBeanMapper(Arrays.asList("dozer/db-2-domain-mappings.xml"));
        Project domain = dozer.map(new com.github.kickshare.db.h2.tables.pojos.Project(1L, "Quodd Heroes", "DESC", "URL", new Date(NOW.toEpochMilli())), Project.class);
        Project expected = new Project(1L, "Quodd Heroes", "DESC", "URL", NOW);
        assertEquals(expected, domain);
    }
}
