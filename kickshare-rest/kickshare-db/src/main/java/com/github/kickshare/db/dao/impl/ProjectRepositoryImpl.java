package com.github.kickshare.db.dao.impl;

import static com.github.kickshare.db.jooq.Tables.CATEGORY;
import static com.github.kickshare.db.jooq.Tables.GROUP;
import static com.github.kickshare.db.jooq.Tables.PROJECT;

import java.util.List;

import com.github.kickshare.db.dao.ProjectRepository;
import com.github.kickshare.db.dao.common.AbstractRepository;
import com.github.kickshare.db.jooq.tables.daos.ProjectDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.ProjectDB;
import com.github.kickshare.db.jooq.tables.records.ProjectRecordDB;
import com.github.kickshare.db.query.GroupQueryBuilder;
import com.github.kickshare.db.query.SearchOptionsDB;
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

    public List<ProjectDB> searchProjects(SearchOptionsDB options) {
        final Integer categoryId = options.getCategoryId();
        final List<ProjectDB> projects = dsl.select(PROJECT.fields())
                .from(PROJECT)
                .join(CATEGORY).on(CATEGORY.ID.eq(PROJECT.CATEGORY_ID))
                .whereExists(
                        dsl.selectOne()
                                .from(GROUP)
                                .where(groupQuery.apply(options, true))
                                .and(GROUP.PROJECT_ID.eq(PROJECT.ID))
                )
                .and(CATEGORY.ID.eq(categoryId).or(CATEGORY.PARENT_ID.eq(categoryId)))
                .fetchInto(com.github.kickshare.db.jooq.tables.pojos.ProjectDB.class);
        return projects;
    }
}
