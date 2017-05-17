package com.github.kickshare.service;

import com.github.kickshare.db.dao.BackerRepository;
import com.github.kickshare.db.dao.GroupRepository;
import com.github.kickshare.db.dao.ProjectRepository;
import com.github.kickshare.db.h2.tables.daos.Backer_2GroupDao;
import com.github.kickshare.db.h2.tables.daos.CityDao;
import com.github.kickshare.db.h2.tables.pojos.Backer_2Group;
import com.github.kickshare.db.h2.tables.pojos.Group;
import com.github.kickshare.domain.City;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    @Transactional
    public Long createGroup(String groupName, Long leaderId, Long projectId, Boolean isLocal) {
        Validate.isTrue(projectRepository.existsById(projectId));
        com.github.kickshare.db.h2.tables.pojos.City city = cityDao.fetchOneById(1);
        Group group = new Group(null, leaderId, projectId, groupName, null, null, true);
        return groupRepository.createReturningKey(group);
    }

    public Long createGroup(Long projectId, String groupName, Long leaderId, boolean isLocal) {
        City city = backerRepository.getPermanentAddress(leaderId);
        Group group = new Group(null, leaderId, projectId, groupName, city.getLat(), city.getLon(), isLocal);
        Long groupId = groupRepository.createReturningKey(group);
        backer2GroupDao.insert(new Backer_2Group(groupId, leaderId));
        return groupId;
    }
}