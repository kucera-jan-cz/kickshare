package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.TagDB;
import com.github.kickshare.db.jooq.tables.records.TagRecordDB;

/**
 * @author Jan.Kucera
 * @since 18.7.2017
 */
public interface TagRepository extends EnhancedDAO<TagRecordDB, TagDB, Short> {
    List<TagDB> getTags(final int categoryId);
}
