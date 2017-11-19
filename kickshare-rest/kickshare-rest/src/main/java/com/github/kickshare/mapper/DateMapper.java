package com.github.kickshare.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

/**
 * @author Jan.Kucera
 * @since 9.6.2017
 */
@Component
public class DateMapper {
    public java.sql.Date toSQLDate(Instant source) {
        if(source == null) {
            return null;
        } else {
            return new java.sql.Date(source.toEpochMilli());
        }
    }

    public Instant toInstant(java.sql.Date source) {
        if(source == null) {
            return null;
        } else {
            return Instant.ofEpochMilli(source.getTime());
        }

    }
}
