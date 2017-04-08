package com.github.kickshare.mapper.converter;

import java.sql.Date;
import java.time.Instant;

import org.dozer.DozerConverter;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
public class InstantCustomConverter extends DozerConverter<Instant, Date> {

    public InstantCustomConverter() {
        super(Instant.class, Date.class);
    }

    @Override
    public Date convertTo(final Instant source, final Date destination) {
        return new Date(source.toEpochMilli());
    }

    @Override
    public Instant convertFrom(final Date source, final Instant destination) {
        return Instant.ofEpochMilli(source.getTime());
    }
}
