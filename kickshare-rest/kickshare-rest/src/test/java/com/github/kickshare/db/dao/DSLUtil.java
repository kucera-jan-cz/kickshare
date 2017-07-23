package com.github.kickshare.db.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * @author Jan.Kucera
 * @since 27.4.2017
 */
public class DSLUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DSLUtil.class);
    public static DSLContext create() throws SQLException {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("data/db/V000__test_init.sql")
                .addScript("db/migration/V001__init.sql")
                .addScript("db/migration/V002__group_init.sql")
                .addScript("db/cz/migration/V101__init_data.sql")
                .addScript("db/cz/migration/V103__mode_geo_data.sql")
                .addScript("db/cz/migration/V104__category_data.sql")
                .build();
        Settings settings = new Settings().withRenderSchema(false).withRenderNameStyle(RenderNameStyle.UPPER);
        return DSL.using(db.getConnection(), settings);
    }

    public static DSLContext createDB() throws SQLException {
        DataSource dataSource = createDataSource();
        DatabasePopulatorUtils.execute(createDatabasePopulator(), dataSource);
        Settings settings = new Settings().withRenderSchema(false);//.withRenderNameStyle(RenderNameStyle.UPPER);
        //.withRenderNameStyle(RenderNameStyle.UPPER);
        Connection conn = dataSource.getConnection();
        conn.setSchema("PUBLIC");
        LOGGER.info("Autocommit: {}", conn.getAutoCommit());
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
            LOGGER.info("{}", rs.getString(3));
        }
        return DSL.using(conn, settings);
    }

    private static DatabasePopulator createDatabasePopulator() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(true);
        databasePopulator.addScript(new ClassPathResource("data/db/V000__test_init.sql"));
        databasePopulator.addScript(new ClassPathResource("db/migration/V001__init.sql"));
        databasePopulator.addScript(new ClassPathResource("db/migration/V002__group_init.sql"));
        databasePopulator.addScript(new ClassPathResource("db/migration/V005__newsletter.sql"));
        databasePopulator.addScript(new ClassPathResource("db/migration/V006__tag.sql"));
        databasePopulator.addScript(new ClassPathResource("db/cz/migration/V101__init_data.sql"));
        databasePopulator.addScript(new ClassPathResource("db/cz/migration/V103__mode_geo_data.sql"));
        databasePopulator.addScript(new ClassPathResource("db/cz/migration/V104__category_data.sql"));
        return databasePopulator;
    }

    private static DataSource createDataSource() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
//        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClassName(org.h2.Driver.class.getCanonicalName());
        dataSource.setUrl("jdbc:h2:mem:~/target/h2test.db;MODE=PostgreSQL;INIT=CREATE DOMAIN IF NOT EXISTS enum as VARCHAR(255);AUTO_SERVER=FALSE");
        dataSource.setUsername("test");
        dataSource.setPassword("test");
        return dataSource;
    }
}
