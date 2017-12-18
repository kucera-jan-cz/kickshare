package com.github.kickshare.service;

import static com.github.kickshare.db.jooq.Tables.BACKER;
import static com.github.kickshare.db.jooq.Tables.BACKER_2_GROUP;
import static com.github.kickshare.db.jooq.Tables.GROUP;
import static com.github.kickshare.mapper.EntityMapper.backer;
import static com.github.kickshare.mapper.EntityMapper.group;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.val;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.kickshare.db.dao.BackerRepository;
import com.github.kickshare.db.dao.GroupPostRepository;
import com.github.kickshare.db.dao.GroupRepository;
import com.github.kickshare.db.jooq.enums.GroupRequestStatusDB;
import com.github.kickshare.db.jooq.tables.daos.Backer_2GroupDaoDB;
import com.github.kickshare.db.jooq.tables.daos.CityDaoDB;
import com.github.kickshare.db.jooq.tables.daos.LeaderDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.Backer_2GroupDB;
import com.github.kickshare.db.jooq.tables.pojos.GroupPostDB;
import com.github.kickshare.db.multischema.SchemaContextHolder;
import com.github.kickshare.domain.Backer;
import com.github.kickshare.domain.City;
import com.github.kickshare.domain.Group;
import com.github.kickshare.domain.GroupDetail;
import com.github.kickshare.domain.GroupSummary;
import com.github.kickshare.domain.Leader;
import com.github.kickshare.domain.Post;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.security.GroupConstants;
import com.github.kickshare.service.entity.SearchOptions;
import com.github.kickshare.service.util.GroupNameFactory;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectField;
import org.jooq.TableLike;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan.Kucera
 * @since 26.4.2017
 */
@Service
@AllArgsConstructor
//@TODO - extract interface
public class GroupServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);
    private static final String PARTICIPANT_COUNT = "num_of_participants";
    private static final String LEADER_NAME = "leader_name";
    private final GroupRepository groupRepository;
    private final BackerRepository backerRepository;
    private final Backer_2GroupDaoDB backer2GroupDao;
    private final GroupPostRepository groupPostRepository;
    private final ProjectService projectService;


    private final CityDaoDB cityDao;
    private final LeaderDaoDB leaderDao;
    private JdbcUserDetailsManager userManager;
    private DSLContext dsl;
    private final GroupNameFactory groupNameFactory = new GroupNameFactory();

    @Transactional
    public Long createGroup(Long projectId, String groupName, Long leaderId, boolean isLocal, Integer limit) {
        City city = Validate.notNull(backerRepository.getPermanentAddress(leaderId), "Give leader ({0}) does not have permanent address", leaderId);
        Long groupId = groupRepository.createReturningKey(
                new com.github.kickshare.db.jooq.tables.pojos.GroupDB(
                        null, leaderId, projectId, groupName, city.getId(), city.getLat(), city.getLon(), isLocal, limit)
        );
        backer2GroupDao.insert(new Backer_2GroupDB(groupId, leaderId, GroupRequestStatusDB.APPROVED, null));
        return groupId;
    }

    public com.github.kickshare.domain.Group getGroup(Long groupId) {
        return group().toDomain(groupRepository.findById(groupId));
    }

    public List<com.github.kickshare.domain.Group> getUserGroups(final Long backerId) {
        return group().toDomain(groupRepository.findAllByUserId(backerId));
    }

    @Transactional
    public GroupDetail getGroupDetail(Long groupId) {
        Group group = group().toDomain(groupRepository.fetchOne(GROUP.ID, groupId));
        Long projectId = group.getProjectId();
        ProjectInfo projectInfo = projectService.findProjectInfo(projectId);
        GroupDetail detail = new GroupDetail();
        List<Backer> backers = backer().toDomain(groupRepository.findAllUsers(groupId));
        Predicate<Backer> isLeader = (Backer b) -> b.getId().equals(group.getLeaderId());
        Map<Boolean, List<Backer>> backersByLeadership = backers.stream().collect(Collectors.partitioningBy(isLeader));
        detail.setGroup(group);
        detail.setLeader(backersByLeadership.get(Boolean.TRUE).get(0));
        detail.setBackers(backersByLeadership.get(Boolean.FALSE));
        detail.setProject(projectInfo.getProject());
        detail.setPhoto(projectInfo.getPhoto());
        return detail;
    }

    @Transactional
    public List<GroupSummary> findAllGroupInfo(final Long projectId) {
        com.github.kickshare.db.jooq.tables.BackerDB u = BACKER.as("u");
        com.github.kickshare.db.jooq.tables.GroupDB g = GROUP.as("g");
        /**
         * SELECT *
         FROM "GROUP" AS g
         JOIN USER AS u ON g.LEADER_ID = u.ID
         INNER JOIN (
         SELECT COUNT(*), GROUP_ID
         FROM BACKER_2_GROUP
         GROUP BY (GROUP_ID)
         ) AS c ON c.GROUP_ID = g.ID
         WHERE project_id = 217227567;
         */
        TableLike<?> c = dsl
                .select(BACKER_2_GROUP.GROUP_ID.as("GROUP_ID"), DSL.count().as(PARTICIPANT_COUNT))
                .from(BACKER_2_GROUP)
                .groupBy(BACKER_2_GROUP.GROUP_ID).asTable("c");

        SelectField<?>[] fields = {
                g.ID, g.NAME, g.PROJECT_ID, val(true).as("is_local"),
                concat(u.NAME, val(" "), u.SURNAME).as(LEADER_NAME), u.ID.as("leader_id"), val(4).as("leader_rating"),
                c.field(PARTICIPANT_COUNT)
        };
        Result<?> records = dsl
                .select(fields)
                .from(g)
                .join(u).on(u.ID.eq(g.LEADER_ID))
                .join(c).on(g.ID.eq(c.field("GROUP_ID", Long.class)))
                .where(g.PROJECT_ID.eq(projectId))
                .fetch();
        List<GroupSummary> infos = records.into(GroupSummary.class);
        return infos;
    }

    @Transactional
    public String suggestGroupName(final Long projectId, final Integer cityId) {
        ProjectInfo projectInfo = this.projectService.findProjectInfo(projectId);
        List<GroupSummary> groups = this.findAllGroupInfo(projectId);
        String location = cityId > 0 ? cityDao.fetchOneByIdDB(cityId).getName() : SchemaContextHolder.getSchema();
        return groupNameFactory.nextName(projectInfo.getName(), location, groups);
    }

    @Transactional
    public List<GroupDetail> searchGroups(SearchOptions options) {
        //@TODO - implement this temporal properly
        return groupRepository.searchGroups(options).stream()
                .map(g -> getGroupDetail(g.getId()))
                .collect(Collectors.toList());
    }

    //    @TODO - decide whether user type should stay or completely rewrite to Backer
    public List<Backer> getGroupUsers(Long groupId) {
        return backer().toDomain(groupRepository.findAllUsers(groupId));
    }

    @Transactional
    public void registerBacker(Long groupId, Long backerId) {
        //@TODO - add supporting message
        backer2GroupDao.update(new Backer_2GroupDB(groupId, backerId, GroupRequestStatusDB.REQUESTED, null));
    }

    @Transactional
    public void removeBacker(Long groupId, Long backerId) {
        backer2GroupDao.delete(new Backer_2GroupDB(groupId, backerId, GroupRequestStatusDB.REQUESTED, null));
    }

    @Transactional
    public void saveLeader(final Long id, final String email, final Long kickstarterId) {
        Validate.isTrue(!leaderDao.existsById(id), "Backer is already registered as leader");
        leaderDao.insert(backer().toDB(new Leader(id, email, kickstarterId)));
        userManager.addUserToGroup(email, GroupConstants.LEADERS);
    }

    @Transactional
    public Post insertPost(Post post) {
        GroupPostDB dbPost = group().toDB(post);
        Long id = groupPostRepository.createReturningKey(dbPost);
        dbPost = groupPostRepository.findById(id);
        return group().toDomain(dbPost);
    }

    public void updatePost(Post post) {
        groupPostRepository.updatePost(group().toDB(post));
    }

    @Transactional
    public Page<Post> getPosts(final Long groupId, Pageable pageInfo) {
        List<GroupPostDB> groupPost = groupRepository.getGroupPost(groupId, pageInfo.getOffset(), pageInfo.getPageSize());
        List<Post> posts = group().postsToDomain(groupPost);
        long total = groupRepository.getGroupPostCount(groupId);
        Page<Post> postsPage = new PageImpl<>(posts, pageInfo, total);
        return postsPage;
    }

    public boolean isGroupOwner(final Long leaderId, final Long groupId) {
        return backerRepository.ownGroup(leaderId, groupId);
    }

    public boolean isGroupMember(final Long backerId, final Long groupId) {
        return backerRepository.isGroupMember(backerId, groupId);
    }

    public void updateGroupRequestStatus(final Long groupId, final Long backerId, GroupRequestStatusDB status) {
        //@TODO - add supporting message
        backer2GroupDao.update(new Backer_2GroupDB(groupId, backerId, status, null));
    }
}
