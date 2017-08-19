package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.BACKER;
import static com.github.kickshare.db.jooq.Tables.BACKER_2_GROUP;
import static com.github.kickshare.db.jooq.Tables.GROUP;
import static com.github.kickshare.db.jooq.Tables.GROUP_POST;

import java.util.List;

import com.github.kickshare.db.jooq.enums.GroupRequestStatus;
import com.github.kickshare.db.jooq.tables.daos.GroupDao;
import com.github.kickshare.db.jooq.tables.pojos.Group;
import com.github.kickshare.db.jooq.tables.pojos.GroupPost;
import com.github.kickshare.db.jooq.tables.pojos.Project;
import com.github.kickshare.db.jooq.tables.records.GroupRecord;
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
            dsl.insertInto(BACKER_2_GROUP)
                    .columns(BACKER_2_GROUP.GROUP_ID, BACKER_2_GROUP.BACKER_ID)
                    .values(groupId, group.getLeaderId());
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

    @Override
    public List<com.github.kickshare.db.jooq.tables.pojos.Backer> findAllUsers(final Long groupId) {
        return findUsersByStatus(groupId, GroupRequestStatus.APPROVED);
    }

    public List<com.github.kickshare.db.jooq.tables.pojos.Backer> findWaitingUsers(final Long groupId) {
        return findUsersByStatus(groupId, GroupRequestStatus.REQUESTED);
    }

    @Override
    public List<Group> findAllByUserId(final Long userId) {
        return dsl
                .select()
                .from(GROUP)
                .join(BACKER_2_GROUP).on(GROUP.ID.eq(BACKER_2_GROUP.GROUP_ID))
                .where(BACKER_2_GROUP.BACKER_ID.eq(userId))
                .fetchInto(Group.class);
    }

//    @Override
//    public GroupSummary getGroupInfo(final Long groupId) {
//        Map<com.github.kickshare.domain.Group, List<Backer>> usersByGroup = dsl.select()
//                .from(BACKER)
//                .join(BACKER_2_GROUP).on(BACKER.ID.eq(BACKER_2_GROUP.BACKER_ID))
//                .join(GROUP).on(GROUP.ID.eq(BACKER_2_GROUP.GROUP_ID))
//                .where(BACKER_2_GROUP.GROUP_ID.eq(groupId))
//                .and(BACKER_2_GROUP.STATUS.eq(GroupRequestStatus.APPROVED))
//                .fetchGroups(
//                        r -> r.into(GROUP).into(com.github.kickshare.domain.Group.class),
//                        r -> r.into(BACKER).into(Backer.class)
//                );
//        GroupSummary info = new GroupSummary();
//        //@TODO figure out miss
//        Map.Entry<com.github.kickshare.domain.Group, List<Backer>> entry = usersByGroup.entrySet().iterator().next();
//        com.github.kickshare.domain.Group group = entry.getKey();
//        info.setGroup(group);
////        List<Backer> backers = entry.getValue();
////        Predicate<Backer> isLeader = (Backer b) -> b.getId().equals(group.getLeaderId());
////        Map<Boolean, List<Backer>> backersByLeadership = backers.stream().collect(Collectors.partitioningBy(isLeader));
////        info.setBackers(backersByLeadership.get(false));
////        //@TODO - fix an issue with missing leader
////        info.setLeader(backersByLeadership.get(true).stream().findFirst().orElse(null));
//        return info;
//    }

    public List<GroupPost> getGroupPost(final Long groupId, int offset, int size) {
        return dsl.select()
                .from(GROUP_POST)
                .where(GROUP_POST.GROUP_ID.eq(groupId))
                .orderBy(GROUP_POST.POST_ID.desc())
                .limit(offset, size)
                .fetchInto(GroupPost.class);
    }

    public long getGroupPostCount(final Long groupId) {
        return dsl.fetchCount(GROUP_POST, GROUP_POST.GROUP_ID.eq(groupId));

    }

    private List<com.github.kickshare.db.jooq.tables.pojos.Backer> findUsersByStatus(final Long groupId, final GroupRequestStatus status) {
        return dsl
                .select()
                .from(BACKER)
                .join(BACKER_2_GROUP).on(BACKER.ID.eq(BACKER_2_GROUP.BACKER_ID))
                .where(BACKER_2_GROUP.GROUP_ID.eq(groupId))
                .and(BACKER_2_GROUP.STATUS.eq(status))
                .fetchInto(com.github.kickshare.db.jooq.tables.pojos.Backer.class);
    }
}
