package com.github.kickshare.service;

import java.util.List;

import com.github.kickshare.domain.Tag;

/**
 * @author Jan.Kucera
 * @since 17.7.2017
 */
public interface TagService {
    public List<Tag> getTags(int categoryId);

    public Tag createTag(String name, int categoryId);
}
