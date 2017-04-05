package com.github.kickshare.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * Util class for working with JSON and other resources.
 */
public class TestUtil {
    private TestUtil() {
    }

    public static String toString(String path) {
        try (InputStream ins = new ClassPathResource(path).getInputStream()) {
            return IOUtils.toString(ins, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read resource: " + path, e);
        }
    }
}
