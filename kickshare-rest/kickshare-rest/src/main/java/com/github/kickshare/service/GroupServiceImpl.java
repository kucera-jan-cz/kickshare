package com.github.kickshare.service;

import java.util.List;
import java.util.stream.Collectors;

import com.github.kickshare.db.dao.BackerRepository;
import com.github.kickshare.db.dao.GroupRepository;
import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.db.dao.ProjectRepository;
import com.github.kickshare.db.jooq.enums.GroupRequestStatus;
import com.github.kickshare.db.jooq.tables.daos.Backer_2GroupDao;
import com.github.kickshare.db.jooq.tables.daos.CityDao;
import com.github.kickshare.db.jooq.tables.daos.LeaderDao;
import com.github.kickshare.db.jooq.tables.pojos.Backer_2Group;
import com.github.kickshare.db.jooq.tables.pojos.Group;
import com.github.kickshare.db.jooq.tables.pojos.GroupPosts;
import com.github.kickshare.domain.Backer;
import com.github.kickshare.domain.City;
import com.github.kickshare.domain.GroupDetail;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.Leader;
import com.github.kickshare.domain.Post;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.mapper.ExtendedMapper;
import com.github.kickshare.security.GroupConstants;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.Validate;
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
public class GroupServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);
    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;
    private final CityDao cityDao;
    private final BackerRepository backerRepository;
    private final Backer_2GroupDao backer2GroupDao;
    private final KickshareRepository kickshareRepository;

    private final LeaderDao leaderDao;
    private ExtendedMapper mapper;
    private JdbcUserDetailsManager userManager;

    @Transactional
    public Long createGroup(Long projectId, String groupName, Long leaderId, boolean isLocal, Integer limit) {
        Validate.isTrue(projectRepository.existsById(projectId), "Given project id ({0}) does not exists", projectId);
        City city = Validate.notNull(backerRepository.getPermanentAddress(leaderId), "Give leader ({0}) does not have permanent address", leaderId);
        Group group = new Group(null, leaderId, projectId, groupName, city.getId(), city.getLat(), city.getLon(), isLocal, limit);
        Long groupId = groupRepository.createReturningKey(group);
        backer2GroupDao.insert(new Backer_2Group(groupId, leaderId, GroupRequestStatus.APPROVED, null));
        return groupId;
    }

    public com.github.kickshare.domain.Group getGroup(Long groupId) {
        return mapper.map(groupRepository.findById(groupId), com.github.kickshare.domain.Group.class);
    }

    public List<com.github.kickshare.domain.Group> getUserGroups(final Long backerId) {
        return mapper.map(groupRepository.findAllByUserId(backerId), com.github.kickshare.domain.Group.class);
    }

    @Transactional
    public GroupInfo getGroupInfo(Long groupId) {
        return groupRepository.getGroupInfo(groupId);
    }

    @Transactional
    public GroupDetail getGroupDetail(Long groupId) {
        GroupInfo info = groupRepository.getGroupInfo(groupId);
        Long projectId = info.getGroup().getProjectId();
        ProjectInfo projectInfo = kickshareRepository.findProjectInfo(projectId);
        GroupDetail detail = new GroupDetail();
        detail.setGroup(info.getGroup());
        detail.setLeader(info.getLeader());
        detail.setBackers(info.getBackers());
        detail.setProject(projectInfo.getProject());
        detail.setPhoto(projectInfo.getPhoto());
        return detail;
    }

    @Transactional
    public List<GroupDetail> searchGroups(GroupSearchOptions options) {
        //@TODO - implement this temporal properly
        return kickshareRepository.searchGroups(options).stream()
                .map(g -> getGroupDetail(g.getId()))
                .collect(Collectors.toList());
    }

    //    @TODO - decide whether user type should stay or completely rewrite to Backer
    public List<Backer> getGroupUsers(Long groupId) {
        return mapper.map(groupRepository.findAllUsers(groupId), Backer.class);
    }

    @Transactional
    public void registerBacker(Long groupId, Long backerId) {
        //@TODO - add supporting message
        backer2GroupDao.update(new Backer_2Group(groupId, backerId, GroupRequestStatus.REQUESTED, null));
    }

    @Transactional
    public void saveLeader(final Long id, final String email, final Long kickstarterId) {
        Validate.isTrue(!leaderDao.existsById(id), "Backer is already registered as leader");
        leaderDao.insert(mapper.map(new Leader(id, email, kickstarterId), com.github.kickshare.db.jooq.tables.pojos.Leader.class));
        userManager.addUserToGroup(email, GroupConstants.LEADERS);
    }

    @Transactional
    public Page<Post> getPosts(final Long groupId, Pageable pageInfo) {
        List<GroupPosts> groupPosts = groupRepository.getGroupPosts(groupId, pageInfo.getOffset(), pageInfo.getPageSize());
        List<Post> posts = mapper.map(groupPosts, Post.class);
        long total = groupRepository.getGroupPostsCount(groupId);
        Page<Post> postsPage = new PageImpl<>(posts, pageInfo, total);
        return postsPage;
//        final List<Post> posts = new ArrayList<>();
//        posts.add(new Post(1L, 1L, 1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0, "A"));
//        posts.add(new Post(1L, 1L, 1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0, "B"));
//        posts.add(new Post(1L, 1L, 1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0, "C"));
//        posts.add(new Post(1L, 1L, 1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0, "D"));
//        final long total = 10L;
//        Page<Post> postsPage = new PageImpl<>(posts, pageInfo, total);
//        return postsPage;

//        public Page<GroupPosts> getGroupPosts(final Long groupId, Pageable pageable) {
//            dsl.select()
//                    .from(GROUP_POSTS)
//                    .where(GROUP_POSTS.GROUP_ID.eq(groupId))
//                    .orderBy(GROUP_POSTS.POST_ID.desc())
//                    .limit(pageable.getOffset(), pageable.getPageSize());
//        }
//        return null;
    }

    public boolean ownGroup(final Long leaderId, final Long groupId) {
        return backerRepository.ownGroup(leaderId, groupId);
    }

    public void updateGroupRequestStatus(final Long groupId, final Long backerId, GroupRequestStatus status) {
        //@TODO - add supporting message
        backer2GroupDao.update(new Backer_2Group(groupId, backerId, status, null));
    }
}
