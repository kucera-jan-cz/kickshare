package com.github.kickshare.ext.service.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jan.Kucera
 * @since 28.11.2017
 */
public class ResourceStream implements ResourceBuilder {
    private static final Pattern SCHEME_REGEX = Pattern.compile("(([a-zA-Z](\\w|\\+|\\.|-)*):)?(.*)");
    public static final ResourceStream INSTANCE = new ResourceStream();

    @Override
    public InputStream apply(final String uri) {
        Matcher matcher = SCHEME_REGEX.matcher(uri);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid path format");
        }
        String scheme = matcher.group(1);
        if (StringUtils.isEmpty(scheme)) {
            return getClass().getClassLoader().getResourceAsStream(uri);
        }
        switch (scheme) {
            case "http":
            case "https":
                return urlStream(uri);
            case "classpath":
            default:
                return getClass().getClassLoader().getResourceAsStream(uri);
        }
    }

    public static InputStream of(String uri) {
        return INSTANCE.apply(uri);
    }

    private static InputStream urlStream(String uri) {
        try {
            return new URL(uri).openStream();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }
}
