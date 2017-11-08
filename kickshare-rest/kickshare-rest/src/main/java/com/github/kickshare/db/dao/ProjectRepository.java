package com.github.kickshare.db.dao;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.Project;
import com.github.kickshare.db.jooq.tables.records.ProjectRecord;
import com.github.kickshare.service.entity.SearchOptions;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 30.3.2017
 */
@Repository
public interface ProjectRepository extends EnhancedDAO<ProjectRecord, Project, Long> {

    List<Project> findProjects();

    List<Project> searchProjects(SearchOptions options) throws IOException;
}
