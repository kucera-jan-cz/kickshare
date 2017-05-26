package com.github.kickshare.test;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jan.Kucera
 * @since 25.5.2017
 */
public final class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {}

    public static <T> T fromJson(String jsonAsText, Class<T> type) {
        try {
            return mapper.readValue(jsonAsText, type);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
