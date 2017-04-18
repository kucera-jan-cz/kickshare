package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.h2.tables.pojos.Backer;
import com.github.kickshare.db.h2.tables.pojos.Group;
import com.github.kickshare.db.h2.tables.records.GroupRecord;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan.Kucera
 * @since 6.4.2017
 */
@Transactional
public interface GroupRepository extends EnhancedDAO<GroupRecord, Group, Long> {

    List<Group> findAllByProjectId(Long projectId);

    void registerUser(Long groupId, Long userId);

    List<Backer> findAllUsers(Long groupId);
}
