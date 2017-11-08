package com.github.kickshare.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

/**
 * @author Jan.Kucera
 * @since 9.6.2017
 */
@Component
public class DateMapper {
    public java.sql.Date toSQLDate(Instant instant) {
        return new java.sql.Date(instant.toEpochMilli());
    }

    public Instant toInstant(java.sql.Date date) {
        return Instant.ofEpochMilli(date.getTime());
    }
}
