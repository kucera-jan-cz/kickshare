package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.h2.tables.pojos.Group;
import com.github.kickshare.db.h2.tables.pojos.User;
import com.github.kickshare.db.h2.tables.records.GroupRecord;

/**
 * @author Jan.Kucera
 * @since 6.4.2017
 */
public interface GroupRepository extends EnhancedDAO<GroupRecord, Group, Long> {

    List<Group> findAllByProjectId(Long projectId);

    void registerUser(Long groupId, Long userId);

    List<User> findAllUsers(Long groupId);
}
