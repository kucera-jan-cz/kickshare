package com.github.kickshare.common.io;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author Jan.Kucera
 * @since 23.9.2017
 */
public final class Lists {
    private Lists() {}

    public static <T> T first(List<T> list) {
        if(CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
