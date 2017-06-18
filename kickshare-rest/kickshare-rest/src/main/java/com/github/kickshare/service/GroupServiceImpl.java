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
import com.github.kickshare.domain.Backer;
import com.github.kickshare.domain.City;
import com.github.kickshare.domain.GroupDetail;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.Leader;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.mapper.ExtendedMapper;
import com.github.kickshare.security.GroupConstants;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        backer2GroupDao.insert(new Backer_2Group(groupId, leaderId, GroupRequestStatus.APPROVED));
        return groupId;
    }

    public com.github.kickshare.domain.Group getGroup(Long groupId) {
        return mapper.map(groupRepository.findById(groupId), com.github.kickshare.domain.Group.class);
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
        backer2GroupDao.update(new Backer_2Group(groupId, backerId, GroupRequestStatus.REQUESTED));
    }

    @Transactional
    public void saveLeader(final Long id, final String email, final Long kickstarterId) {
        Validate.isTrue(!leaderDao.existsById(id), "Backer is already registered as leader");
        leaderDao.insert(mapper.map(new Leader(id, email, kickstarterId), com.github.kickshare.db.jooq.tables.pojos.Leader.class));
        userManager.addUserToGroup(email, GroupConstants.LEADERS);
    }

    public boolean ownGroup(final Long leaderId, final Long groupId) {
        return backerRepository.ownGroup(leaderId, groupId);
    }

    public void updateGroupRequestStatus(final Long groupId, final Long backerId, GroupRequestStatus status) {
        backer2GroupDao.update(new Backer_2Group(groupId, backerId, status));
    }
}
