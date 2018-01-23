package com.github.kickshare.db.dao;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.ProjectDB;
import com.github.kickshare.db.jooq.tables.records.ProjectRecordDB;
import com.github.kickshare.db.query.SearchOptionsDB;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 30.3.2017
 */
@Repository
public interface ProjectRepository extends EnhancedDAO<ProjectRecordDB, ProjectDB, Long> {

    List<ProjectDB> findProjects();

    List<ProjectDB> searchProjects(SearchOptionsDB options) throws IOException;
}
