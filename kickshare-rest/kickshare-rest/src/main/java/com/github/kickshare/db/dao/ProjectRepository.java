package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.h2.tables.pojos.Project;
import com.github.kickshare.db.h2.tables.records.ProjectRecord;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 30.3.2017
 */
@Repository
public interface ProjectRepository extends EnhancedDAO<ProjectRecord, Project, Long> {

    List<Project> findProjects();
}
