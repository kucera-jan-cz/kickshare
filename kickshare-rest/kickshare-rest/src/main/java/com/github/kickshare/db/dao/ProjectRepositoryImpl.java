package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.GROUP;
import static com.github.kickshare.db.jooq.Tables.PROJECT;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.db.jooq.tables.daos.ProjectDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.ProjectDB;
import com.github.kickshare.db.jooq.tables.records.ProjectRecordDB;
import com.github.kickshare.db.query.GroupQueryBuilder;
import com.github.kickshare.service.entity.SearchOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
@Repository
public class ProjectRepositoryImpl extends AbstractRepository<ProjectRecordDB, ProjectDB, Long> implements ProjectRepository {
    private final GroupQueryBuilder groupQuery = new GroupQueryBuilder();

    @Autowired
    public ProjectRepositoryImpl(ProjectDaoDB projectDao) {
        super(projectDao);
    }

    @Override
    public List<ProjectDB> findProjects() {
        return dao.findAll();
    }

    public List<ProjectDB> searchProjects(SearchOptions options) throws IOException {
        final List<ProjectDB> projects = dsl.select()
                .from(PROJECT)
                .whereExists(
                        dsl.selectOne()
                                .from(GROUP)
                                .where(groupQuery.apply(options))
                                .and(GROUP.PROJECT_ID.eq(PROJECT.ID))
                )
                .fetchInto(com.github.kickshare.db.jooq.tables.pojos.ProjectDB.class);
        return projects;
    }
}
