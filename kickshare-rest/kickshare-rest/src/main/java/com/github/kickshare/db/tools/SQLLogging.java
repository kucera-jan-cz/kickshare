package com.github.kickshare.db.tools;

import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
public class SQLLogging extends DefaultExecuteListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLLogging.class);

    @Override
    public void renderEnd(ExecuteContext ctx) {
        if (LOGGER.isDebugEnabled()) {
            if (ctx.query() != null) {
                Configuration configuration = ctx.configuration();
                LOGGER.debug("Executing query: \n{}", DSL.using(configuration).renderInlined(ctx.query()));
            }
        }
    }
}
