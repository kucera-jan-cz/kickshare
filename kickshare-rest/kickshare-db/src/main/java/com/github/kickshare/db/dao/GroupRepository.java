package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.BackerDB;
import com.github.kickshare.db.jooq.tables.pojos.GroupDB;
import com.github.kickshare.db.jooq.tables.pojos.GroupPostDB;
import com.github.kickshare.db.jooq.tables.records.GroupRecordDB;
import com.github.kickshare.db.query.SearchOptionsDB;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan.Kucera
 * @since 6.4.2017
 */
@Transactional
public interface GroupRepository extends EnhancedDAO<GroupRecordDB, GroupDB, Long> {

    List<GroupDB> findAllByProjectId(Long projectId);

    /**
     *
     * @param groupId
     * @return list of approved backers
     */
    List<BackerDB> findAllUsers(Long groupId);

    /**
     * @param groupId
     * @return list of backers waiting for approval
     */
    List<BackerDB> findWaitingUsers(final Long groupId);

    List<GroupDB> findAllByUserId(final Long userId);

    List<GroupPostDB> getGroupPost(final Long groupId, int offset, int size);

    long getGroupPostCount(final Long groupId);

    List<GroupDB> searchGroups(SearchOptionsDB options);
}
