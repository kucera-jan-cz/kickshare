package com.github.kickshare.db.dao;

import static com.github.kickshare.db.h2.Tables.BACKER;
import static com.github.kickshare.db.h2.Tables.BACKER_2_GROUP;
import static com.github.kickshare.db.h2.Tables.GROUP;

import java.util.List;

import com.github.kickshare.db.h2.tables.daos.GroupDao;
import com.github.kickshare.db.h2.tables.pojos.Backer;
import com.github.kickshare.db.h2.tables.pojos.Group;
import com.github.kickshare.db.h2.tables.pojos.Project;
import com.github.kickshare.db.h2.tables.records.GroupRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(propagation = Propagation.REQUIRED)
    public Long createReturningKey(final Group group) {
        Long groupId = super.createReturningKey(group);
        if (groupId < 1) {
            throw new IllegalArgumentException("Failure");
        } else {
            dsl.insertInto(BACKER_2_GROUP).columns(BACKER_2_GROUP.GROUP_ID, BACKER_2_GROUP.BACKER_ID).values(groupId, group.getLeaderId());
            return groupId;
        }
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
                .insertInto(BACKER_2_GROUP)
                .columns(BACKER_2_GROUP.GROUP_ID, BACKER_2_GROUP.BACKER_ID)
                .values(groupId, userId)
                .execute();
    }

    @Override
    public List<Backer> findAllUsers(final Long groupId) {
        dsl
                .select()
                .from(BACKER)
                .join(BACKER_2_GROUP).on(BACKER.ID.eq(BACKER_2_GROUP.BACKER_ID))
                .where(BACKER_2_GROUP.GROUP_ID.eq(groupId))
                .fetchInto(Backer.class);
        return null;
    }


}
