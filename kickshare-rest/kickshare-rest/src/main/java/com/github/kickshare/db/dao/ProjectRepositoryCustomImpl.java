package com.github.kickshare.db.dao;

import java.sql.Connection;
import java.util.List;

import com.github.kickshare.db.entity.Group;
import com.github.kickshare.db.entity.Project;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

/**
 * @author Jan.Kucera
 * @since 1.4.2017
 */
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {
    private Connection connection;
    private Settings settings;

    public ProjectRepositoryCustomImpl(final Connection connection) {
        this.connection = connection;
        this.settings = new Settings().withRenderSchema(false);
//                .withRenderMapping(new RenderMapping()
//                        .withSchemata(new MappedSchema().withInput("KICKSHARE").withOutput("KICKSHARE")));
    }

    @Override
    public List<Project> getProjects(final Long userId) {
        List<Project> projects = DSL.using(connection, settings).select().from("PROJECT").fetchInto(Project.class);
        return projects;
    }

    @Override
    public List<Group> findAllGroups(final Long projectId) {
        List<Group> groups = DSL.using(connection, SQLDialect.H2).select().from("`GROUP`").where("project_id = {0}", projectId).fetchInto(Group.class);
        return groups;
    }
}
