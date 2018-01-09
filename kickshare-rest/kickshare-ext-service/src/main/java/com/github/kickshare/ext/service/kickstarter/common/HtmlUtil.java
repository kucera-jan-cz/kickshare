package com.github.kickshare.ext.service.kickstarter.common;

/**
 * @author Jan.Kucera
 * @since 8.1.2018
 */
public final class HtmlUtil {
    private HtmlUtil() {
    }

    public static final String removeEmptyCharacters(String source) {
        return source.replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2");
    }
}
