package com.github.kickshare.db.dao;

import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * @author Jan.Kucera
 * @since 27.4.2017
 */
public class DSLUtil {
    public static DSLContext create() throws SQLException {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("data/db/V000__test_init.sql")
                .addScript("db/migration/V001__init.sql")
                .addScript("db/cz/migration/V101__init_data.sql")
                .addScript("db/cz/migration/V103__mode_geo_data.sql")
                .build();
        Settings settings = new Settings().withRenderSchema(false).withRenderNameStyle(RenderNameStyle.UPPER);
        return DSL.using(db.getConnection(), settings);
    }
}
