package com.github.kickshare.db.multischema;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;

/**
 * @author Jan.Kucera
 * @since 12.4.2017
 */
public class SchemaContextHolder {
    private static final String DEFAULT_SCHEMA = "KS";
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setSchema(String schema) {
        Assert.notNull(schema, "Schema cannot be null");
        contextHolder.set(schema);
    }

    public static String getSchema() {
        return ObjectUtils.defaultIfNull(contextHolder.get(), DEFAULT_SCHEMA);
    }

    public static void clearSchema() {
        contextHolder.remove();
    }
}