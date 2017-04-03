package com.github.kickshare.db.dao;

import java.sql.Connection;
import java.util.List;

import com.github.kickshare.db.entity.Group;
import com.github.kickshare.db.entity.Project;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

/**
 * @author Jan.Kucera
 * @since 1.4.2017
 */
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private Connection connection;
    private Settings settings;

    public UserRepositoryCustomImpl(final Connection connection) {
        this.connection = connection;
        this.settings = new Settings()
                .withRenderMapping(new RenderMapping()
                        .withSchemata(new MappedSchema().withOutput("KICKSHARE")));
    }

    @Override
    public List<Project> getProjects(final Long userId) {
        List<Project> projects = DSL.using(connection, settings).select().from("PROJECT").fetchInto(Project.class);
        return projects;
    }

    @Override
    public List<Group> getAllGroups(Long userId) {
        List<Group> groups = DSL.using(connection, settings)
                .select()
                .from("`GROUP`")
                .join("user_2_group").on("id = group_id")
                .where("user_id = {0}", userId)
                .fetchInto(Group.class);
        return groups;
    }
}
