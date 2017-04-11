package com.github.kickshare.db.dao;

import static com.github.kickshare.db.h2.Tables.GROUP;
import static com.github.kickshare.db.h2.Tables.USER;
import static com.github.kickshare.db.h2.Tables.USER_2_GROUP;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.val;

import java.util.List;

import com.github.kickshare.db.h2.tables.Group;
import com.github.kickshare.db.h2.tables.User;
import com.github.kickshare.db.h2.tables.daos.ProjectDao;
import com.github.kickshare.db.h2.tables.daos.ProjectPhotoDao;
import com.github.kickshare.db.h2.tables.pojos.Project;
import com.github.kickshare.db.h2.tables.pojos.ProjectPhoto;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.ProjectInfo;
import lombok.AllArgsConstructor;
import org.dozer.Mapper;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectField;
import org.jooq.TableLike;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
@Repository
@AllArgsConstructor
public class KickshareRepositoryImpl implements KickshareRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(KickshareRepositoryImpl.class);
    private static final String PARTICIPANT_COUNT = "num_of_participants";
    private static final String LEADER_NAME = "leader_name";

    private DSLContext dsl;
    private ProjectDao projectDao;
    private ProjectPhotoDao photoDao;
    private Mapper mapper;

    @Override
    public ProjectInfo findProjectInfo(final Long projectId) {
        final Project project = projectDao.fetchOneById(projectId);
        final ProjectPhoto projectPhoto = photoDao.fetchOneByProjectId(projectId);
        ProjectInfo info = new ProjectInfo();
        info.setProject(mapper.map(project, com.github.kickshare.domain.Project.class));
        info.setPhotoUrl(projectPhoto.getThumb());
        return info;
    }

    @Override
    //@TODO - make this transactional
    public List<GroupInfo> findAllGroupInfo(final Long projectId) {
        final Project project = projectDao.fetchOneById(projectId);
        final ProjectPhoto projectPhoto = photoDao.fetchOneByProjectId(projectId);
        User u = USER.as("u");
        Group g = GROUP.as("g");
        /**
         * SELECT *
         FROM "GROUP" AS g
         JOIN USER AS u ON g.LEADER_ID = u.ID
         INNER JOIN (
         SELECT COUNT(*), GROUP_ID
         FROM USER_2_GROUP
         GROUP BY (GROUP_ID)
         ) AS c ON c.GROUP_ID = g.ID
         WHERE project_id = 217227567;
         */
        TableLike<?> c = dsl
                .select(USER_2_GROUP.GROUP_ID.as("GROUP_ID"), DSL.count().as(PARTICIPANT_COUNT))
                .from(USER_2_GROUP)
                .groupBy(USER_2_GROUP.GROUP_ID).asTable("c");

        SelectField<?>[] fields = {
                g.ID, g.NAME, g.PROJECT_ID, val(true).as("is_local"),
                concat(u.NAME, val(" "), u.SURNAME).as(LEADER_NAME), val(4).as("leader_rating"),
                c.field(PARTICIPANT_COUNT)
        };
        Result<?> records = dsl
                .select(fields)
                .from(g)
                .join(u).on(u.ID.eq(g.LEADER_ID))
                .join(c).on(g.ID.eq(c.field("GROUP_ID", Long.class)))
                .where(g.PROJECT_ID.eq(projectId))
                .fetch();
        List<GroupInfo> infos = records.into(GroupInfo.class);

        for (GroupInfo info : infos) {
            info.setPhotoUrl(projectPhoto.getSmall());
            info.setUrl(project.getUrl());
        }
        return infos;
    }
}
