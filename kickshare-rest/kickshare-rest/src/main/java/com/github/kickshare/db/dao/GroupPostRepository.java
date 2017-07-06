package com.github.kickshare.db.dao;

import com.github.kickshare.db.jooq.tables.pojos.GroupPost;
import com.github.kickshare.db.jooq.tables.records.GroupPostRecord;

/**
 * @author Jan.Kucera
 * @since 17.5.2017
 */
public interface GroupPostRepository extends EnhancedDAO<GroupPostRecord, GroupPost, Long> {
    public void updatePost(GroupPost entity);
}
