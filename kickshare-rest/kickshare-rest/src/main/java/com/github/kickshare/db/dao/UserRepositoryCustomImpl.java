package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.entity.Project;

/**
 * @author Jan.Kucera
 * @since 1.4.2017
 */
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @Override
    public List<Project> getProjects(final Long userId) {
//        DSL.using("").select()
        return null;
    }
}
