package com.github.kickshare.db.dao;

import static com.github.kickshare.db.h2.Tables.BACKER;
import static com.github.kickshare.db.h2.Tables.BACKER_2_GROUP;
import static com.github.kickshare.db.h2.Tables.GROUP;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.kickshare.db.h2.tables.daos.GroupDao;
import com.github.kickshare.db.h2.tables.pojos.Backer;
import com.github.kickshare.db.h2.tables.pojos.Group;
import com.github.kickshare.db.h2.tables.pojos.Project;
import com.github.kickshare.db.h2.tables.records.GroupRecord;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.User;
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

    @Override
    public GroupInfo getGroupInfo(final Long groupId) {
        Map<com.github.kickshare.domain.Group, List<User>> usersByGroup = dsl.select()
                .from(BACKER)
                .join(BACKER_2_GROUP).on(BACKER.ID.eq(BACKER_2_GROUP.BACKER_ID))
                .join(GROUP).on(GROUP.ID.eq(BACKER_2_GROUP.GROUP_ID))
                .where(BACKER_2_GROUP.GROUP_ID.eq(groupId))
                .fetchGroups(
                        r -> r.into(GROUP).into(com.github.kickshare.domain.Group.class),
                        r -> r.into(BACKER).into(User.class)
                );
        GroupInfo info = new GroupInfo();
        //@TODO figure out miss
        Map.Entry<com.github.kickshare.domain.Group, List<User>> entry = usersByGroup.entrySet().iterator().next();
        com.github.kickshare.domain.Group group = entry.getKey();
        List<User> backers = entry.getValue();
        Predicate<User> isLeader = (User b) -> b.getId().equals(group.getLeaderId());
        Map<Boolean, List<User>> backersByLeadership = backers.stream().collect(Collectors.partitioningBy(isLeader));
        info.setGroup(group);
        info.setBackers(backersByLeadership.get(false));
        info.setLeader(backersByLeadership.get(true).get(0));
        return info;
    }

}
