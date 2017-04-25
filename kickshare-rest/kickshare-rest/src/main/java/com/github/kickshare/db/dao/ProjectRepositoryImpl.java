package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.h2.tables.daos.ProjectDao;
import com.github.kickshare.db.h2.tables.pojos.Project;
import com.github.kickshare.db.h2.tables.records.ProjectRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
@Repository
public class ProjectRepositoryImpl extends AbstractRepository<ProjectRecord, Project, Long> implements ProjectRepository {
    @Autowired
    public ProjectRepositoryImpl(Configuration jooqConfig) {
        super(new ProjectDao(jooqConfig));
    }

    @Override
    public List<Project> findProjects() {
        return dao.findAll();
    }
}
