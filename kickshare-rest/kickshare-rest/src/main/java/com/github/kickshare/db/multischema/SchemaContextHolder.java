package com.github.kickshare.db.multischema;

import org.apache.commons.lang3.ObjectUtils;

/**
 * @author Jan.Kucera
 * @since 12.4.2017
 */
public class SchemaContextHolder {
    private static final String DEFAULT_SCHEMA = "KS";
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static String setSchema(String schema) {
        String previous = contextHolder.get();
        contextHolder.set(schema);
        return previous;
    }

    public static String getSchema() {
        return ObjectUtils.defaultIfNull(contextHolder.get(), DEFAULT_SCHEMA);
    }

    public static void clearSchema() {
        contextHolder.remove();
    }
}