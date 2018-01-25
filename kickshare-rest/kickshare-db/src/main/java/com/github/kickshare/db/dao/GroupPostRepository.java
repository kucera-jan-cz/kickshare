package com.github.kickshare.db.dao;

import com.github.kickshare.db.dao.common.EnhancedDAO;
import com.github.kickshare.db.jooq.tables.pojos.GroupPostDB;
import com.github.kickshare.db.jooq.tables.records.GroupPostRecordDB;

/**
 * @author Jan.Kucera
 * @since 17.5.2017
 */
public interface GroupPostRepository extends EnhancedDAO<GroupPostRecordDB, GroupPostDB, Long> {
    void updatePost(GroupPostDB entity);
}
