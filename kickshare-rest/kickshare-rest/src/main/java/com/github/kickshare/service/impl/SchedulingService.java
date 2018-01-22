package com.github.kickshare.service.impl;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.common.io.ResourceUtil;
import com.github.kickshare.db.jooq.Tables;
import com.github.kickshare.db.jooq.enums.TokenTypeDB;
import com.github.kickshare.db.jooq.tables.TokenRequestDB;
import com.github.kickshare.gmail.GMailService;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.ResultQuery;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Jan.Kucera
 * @since 29.9.2017
 */
public class SchedulingService {
    private DSLContext dsl;
    private GMailService gmailService;
    private MailService mailService;

    private ResultQuery<Record1<String>> generatedPasswordQuery = dsl.select(Tables.TOKEN_REQUEST.TOKEN)
            .from(TokenRequestDB.TOKEN_REQUEST)
            .where(TokenRequestDB.TOKEN_REQUEST.TOKEN_TYPE.eq(TokenTypeDB.PASSWORD_RESET))
            .keepStatement(true);

    private ResultQuery<Record1<String>> generatedPasswordResetQuery = dsl.select(Tables.TOKEN_REQUEST.TOKEN)
            .from(TokenRequestDB.TOKEN_REQUEST)
            .where(TokenRequestDB.TOKEN_REQUEST.TOKEN_TYPE.eq(TokenTypeDB.PASSWORD_MAIL))
            .keepStatement(true);

    @Scheduled(cron = "* * * * * *")
    public void deleteDeprecatedTokens() {
        //@TODO - add insert time to records and consider token as id??
    }

    @Scheduled(cron = "* * * * * *")
    public void sendPasswordResetLinks() {
        final List<String> tokens = generatedPasswordQuery.fetchInto(String.class);
        for (String token : tokens) {
            mailService.resetPassword(token);
        }
    }

    @Scheduled(cron = "* * * * * *")
    public void sendGeneratedPasswords() {
        final List<String> tokens = generatedPasswordQuery.fetchInto(String.class);
        for (String token : tokens) {
            mailService.resetPassword(token);
        }
    }

    //@TODO - implement rating calculations
    public void updateLeaderRatings() throws IOException {
        String sql = ResourceUtil.toString("db/sql/update_leader_rating.sql");
        this.dsl.execute(sql);
    }

    public void updateBackerRatings() throws IOException {
        String sql = ResourceUtil.toString("db/sql/update_backer_rating.sql");
        this.dsl.execute(sql);
    }
}
