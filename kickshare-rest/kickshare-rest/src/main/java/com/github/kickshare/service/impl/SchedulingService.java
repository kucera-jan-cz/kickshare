package com.github.kickshare.service.impl;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Jan.Kucera
 * @since 29.9.2017
 */
public class SchedulingService {

    @Scheduled(cron = "${spring.session.cleanup.cron.expression:0 * * * * *}")
    public void deleteDeprecatedTokens() {
        //@TODO - add insert time to records and consider token as id??
    }

    @Scheduled(cron = "${spring.session.cleanup.cron.expression:0 * * * * *}")
    public void sendGeneratedPasswords() {

    }

    @Scheduled(cron = "${spring.session.cleanup.cron.expression:0 * * * * *}")
    public void sendPasswordResetLinks() {

    }
}
