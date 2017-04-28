package com.github.kickshare.db.dao;

import static com.github.kickshare.db.h2.Tables.ADDRESS;
import static com.github.kickshare.db.h2.Tables.BACKER;

import java.util.List;

import com.github.kickshare.db.h2.tables.daos.ProjectDao;
import com.github.kickshare.db.h2.tables.pojos.Project;
import com.github.kickshare.db.h2.tables.records.ProjectRecord;
import com.github.kickshare.domain.Address;
import com.github.kickshare.domain.BackerInfo;
import com.github.kickshare.domain.User;
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

    @Override
    public BackerInfo getBacker(final Long id) {
        return dsl.select()
                .from(BACKER)
                .join(ADDRESS).on(BACKER.ID.eq(ADDRESS.BACKER_ID))
                .fetchOne(
                        r -> {
                            User backer = r.into(BACKER).into(User.class);
                            Address address = r.into(ADDRESS).into(Address.class);
                            return new BackerInfo(backer, address);
                        }
                );
    }
}
