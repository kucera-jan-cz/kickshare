package com.github.kickshare.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Jan.Kucera
 * @since 5.4.2017
 */
public class ResourceUtil {

    public static String toString(String path) throws IOException {
        try (InputStream ins = new ClassPathResource(path).getInputStream()) {
            return IOUtils.toString(ins, StandardCharsets.UTF_8);
        }
    }
}
