package com.github.kickshare.service;

import com.github.kickshare.db.dao.BackerRepository;
import com.github.kickshare.db.dao.GroupRepository;
import com.github.kickshare.db.dao.ProjectRepository;
import com.github.kickshare.db.h2.tables.daos.Backer_2GroupDao;
import com.github.kickshare.db.h2.tables.daos.CityDao;
import com.github.kickshare.db.h2.tables.daos.LeaderDao;
import com.github.kickshare.db.h2.tables.pojos.Backer_2Group;
import com.github.kickshare.db.h2.tables.pojos.Group;
import com.github.kickshare.domain.City;
import com.github.kickshare.domain.Leader;
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

    private final LeaderDao leaderDao;
    private ExtendedMapper mapper;
    private JdbcUserDetailsManager userManager;

    @Transactional
    public Long createGroup(String groupName, Long leaderId, Long projectId, Boolean isLocal) {
        Validate.isTrue(projectRepository.existsById(projectId));
        com.github.kickshare.db.h2.tables.pojos.City city = cityDao.fetchOneById(1);
        Group group = new Group(null, leaderId, projectId, groupName, city.getId(), city.getLat(), city.getLon(), true);
        return groupRepository.createReturningKey(group);
    }

    public Long createGroup(Long projectId, String groupName, Long leaderId, boolean isLocal) {
        Validate.isTrue(projectRepository.existsById(projectId), "Given project id ({0}) does not exists", projectId);
        City city = Validate.notNull(backerRepository.getPermanentAddress(leaderId), "Give leader ({0}) does not have permanent address", leaderId);
        Group group = new Group(null, leaderId, projectId, groupName, city.getId(), city.getLat(), city.getLon(), isLocal);
        Long groupId = groupRepository.createReturningKey(group);
        backer2GroupDao.insert(new Backer_2Group(groupId, leaderId));
        return groupId;
    }

    @Transactional
    public void registerBacker(Long groupId, Long backerId) {
//        @TODO - retrieve group limit and validate
//        groupRepository.getGroupInfo(groupId)
        groupRepository.registerUser(groupId, backerId);
    }

    @Transactional
    public void saveLeader(final Long id, final String email, final Long kickstarterId) {
        Validate.isTrue(!leaderDao.existsById(id), "User is already registered as leader");
        leaderDao.insert(mapper.map(new Leader(id, email, kickstarterId), com.github.kickshare.db.h2.tables.pojos.Leader.class));
        userManager.addUserToGroup(email, GroupConstants.LEADERS);
    }
}
