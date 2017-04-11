package com.github.kickshare.db.dao;

import static com.github.kickshare.db.h2.Tables.GROUP;
import static com.github.kickshare.db.h2.Tables.USER;
import static com.github.kickshare.db.h2.Tables.USER_2_GROUP;

import java.util.List;

import com.github.kickshare.db.h2.tables.daos.GroupDao;
import com.github.kickshare.db.h2.tables.pojos.Group;
import com.github.kickshare.db.h2.tables.pojos.Project;
import com.github.kickshare.db.h2.tables.pojos.User;
import com.github.kickshare.db.h2.tables.records.GroupRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 6.4.2017
 */
@Repository
public class GroupRepositoryImpl extends AbstractRepository<GroupRecord, Group, Long> implements GroupRepository {

    @Autowired
    public GroupRepositoryImpl(Configuration jooqConfig) {
        super(new GroupDao(jooqConfig));
    }

    @Override
    public Long createReturningKey(final Group group) {
        Long groupId = super.createReturningKey(group);
        dsl.insertInto(USER_2_GROUP).columns(USER_2_GROUP.GROUP_ID, USER_2_GROUP.USER_ID).values(groupId, group.getLeaderId());
        return groupId;
    }

    public List<Group> findAllByProjectId(final Project project) {
        return findAllByProjectId(project.getId());
    }

    @Override
    public List<Group> findAllByProjectId(final Long projectId) {
        return dsl
                .select()
                .from(GROUP)
                .where(GROUP.PROJECT_ID.eq(projectId))
                .fetchInto(Group.class);
    }

    public void registerUser(Long groupId, Long userId) {
        dsl
                .insertInto(USER_2_GROUP)
                .columns(USER_2_GROUP.GROUP_ID, USER_2_GROUP.USER_ID)
                .values(groupId, userId)
                .execute();
    }

    @Override
    public List<User> findAllUsers(final Long groupId) {
        dsl
                .select()
                .from(USER)
                .join(USER_2_GROUP).on(USER.ID.eq(USER_2_GROUP.USER_ID))
                .where(USER_2_GROUP.GROUP_ID.eq(groupId))
                .fetchInto(User.class);
        return null;
    }


}
