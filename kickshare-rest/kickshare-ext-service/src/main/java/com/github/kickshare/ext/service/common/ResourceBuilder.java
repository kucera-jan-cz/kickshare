package com.github.kickshare.ext.service.common;

import java.io.InputStream;

/**
 * @author Jan.Kucera
 * @since 28.11.2017
 */
@FunctionalInterface
public interface ResourceBuilder {

    InputStream apply(String uri);
}
