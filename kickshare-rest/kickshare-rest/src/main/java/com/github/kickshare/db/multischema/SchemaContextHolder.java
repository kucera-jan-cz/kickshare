package com.github.kickshare.db.multischema;

import org.springframework.util.Assert;

/**
 * @author Jan.Kucera
 * @since 12.4.2017
 */
public class SchemaContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setSchema(String schema) {
        Assert.notNull(schema, "Schema cannot be null");
        contextHolder.set(schema);
    }

    public static String getSchema() {
        return contextHolder.get();
    }

    public static void clearSchema() {
        contextHolder.remove();
    }
}