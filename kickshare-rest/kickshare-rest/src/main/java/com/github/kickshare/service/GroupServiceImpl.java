package com.github.kickshare.service;

import com.github.kickshare.db.dao.GroupRepository;
import com.github.kickshare.db.dao.ProjectRepository;
import com.github.kickshare.db.h2.tables.daos.BackerDao;
import com.github.kickshare.db.h2.tables.daos.CityDao;
import com.github.kickshare.db.h2.tables.pojos.City;
import com.github.kickshare.db.h2.tables.pojos.Group;
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
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;
    private final CityDao cityDao;
    private final BackerDao dao;

    @Transactional
    public Long createGroup(String groupName, Long leaderId, Long projectId, Boolean isLocal) {
        Validate.isTrue(projectRepository.existsById(projectId));
        City city = cityDao.fetchOneById(1);
        Group group = new Group(null, leaderId, projectId, groupName, null, null, true);
        return groupRepository.createReturningKey(group);
    }
}
