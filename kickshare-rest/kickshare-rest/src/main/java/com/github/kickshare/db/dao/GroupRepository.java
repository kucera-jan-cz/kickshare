package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.Backer;
import com.github.kickshare.db.jooq.tables.pojos.Group;
import com.github.kickshare.db.jooq.tables.pojos.GroupPost;
import com.github.kickshare.db.jooq.tables.records.GroupRecord;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan.Kucera
 * @since 6.4.2017
 */
@Transactional
public interface GroupRepository extends EnhancedDAO<GroupRecord, Group, Long> {

    List<Group> findAllByProjectId(Long projectId);

    /**
     *
     * @param groupId
     * @return list of approved backers
     */
    List<Backer> findAllUsers(Long groupId);

    /**
     * @param groupId
     * @return list of backers waiting for approval
     */
    List<Backer> findWaitingUsers(final Long groupId);

    List<Group> findAllByUserId(final Long userId);

    List<GroupPost> getGroupPost(final Long groupId, int offset, int size);

    long getGroupPostCount(final Long groupId);

}
