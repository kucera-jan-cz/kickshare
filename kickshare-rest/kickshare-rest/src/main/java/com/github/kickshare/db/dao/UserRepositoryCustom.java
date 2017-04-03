package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.entity.Group;
import com.github.kickshare.db.entity.Project;

/**
 * @author Jan.Kucera
 * @since 31.3.2017
 */
public interface UserRepositoryCustom {
    public List<Project> getProjects(Long userId);

    public List<Group> getAllGroups(Long userId);
}
