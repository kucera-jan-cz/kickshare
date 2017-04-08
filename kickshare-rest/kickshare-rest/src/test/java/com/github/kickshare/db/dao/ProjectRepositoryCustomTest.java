package com.github.kickshare.db.dao;


import static org.testng.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.github.kickshare.db.entity.Group;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
public class ProjectRepositoryCustomTest {
    private EmbeddedDatabase db;
    @BeforeClass
    public void setUp() {
        //db = new EmbeddedDatabaseBuilder().addDefaultScripts().build();
        this.db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/migration/V001__init.sql")
                .addScript("db/migration/V101__init_data.sql")
                .build();
    }

    @Test
    public void load() throws SQLException {
        Connection connection = db.getConnection();
        ProjectRepositoryCustom custom = new ProjectRepositoryCustomImpl(connection);
        List<Group> groups = custom.findAllGroups(439380282L);
        assertNotNull(groups);

    }
}
