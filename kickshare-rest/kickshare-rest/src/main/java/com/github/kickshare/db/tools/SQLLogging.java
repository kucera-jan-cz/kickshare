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

    //    13:24:32.317 [main] DEBUG org.jooq.tools.LoggerListener - -> with bind values      : select "g"."ID", "g"."NAME", "g"."PROJECT_ID", true "is_local", ("u"."NAME" || ' ' || "u"."SURNAME") "leader_name", 4 "leader_rating", "c"."num_of_participants" from "CZ"."GROUP" "g" join "CZ"."USER" "u" on "u"."ID" = "g"."LEADER_ID" join (select "CZ"."USER_2_GROUP"."GROUP_ID" "GROUP_ID", count(*) "num_of_participants" from "CZ"."USER_2_GROUP" group by "CZ"."USER_2_GROUP"."GROUP_ID") "c" on "g"."ID" = "c"."GROUP_ID" where "g"."PROJECT_ID" = 217227567
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
