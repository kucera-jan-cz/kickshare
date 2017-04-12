package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.domain.Group;
import com.github.kickshare.domain.Project;

/**
 * @author Jan.Kucera
 * @since 31.3.2017
 */
public interface ProjectRepositoryCustom {
    public List<Project> getProjects(Long userId);

    public List<Group> findAllGroups(Long id);
}
