package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.Tag;
import com.github.kickshare.db.jooq.tables.records.TagRecord;

/**
 * @author Jan.Kucera
 * @since 18.7.2017
 */
public interface TagRepository extends EnhancedDAO<TagRecord, Tag, Short> {
    List<com.github.kickshare.domain.Tag> getTags(final int categoryId);
}
