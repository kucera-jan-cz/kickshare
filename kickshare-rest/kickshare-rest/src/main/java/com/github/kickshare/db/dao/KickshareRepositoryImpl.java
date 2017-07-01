package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.BACKER;
import static com.github.kickshare.db.jooq.Tables.BACKER_2_GROUP;
import static com.github.kickshare.db.jooq.Tables.CITY;
import static com.github.kickshare.db.jooq.Tables.GROUP;
import static com.github.kickshare.db.jooq.Tables.GROUP_POST;
import static com.github.kickshare.db.jooq.Tables.PROJECT;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.val;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.kickshare.db.jooq.tables.Backer;
import com.github.kickshare.db.jooq.tables.Group;
import com.github.kickshare.db.jooq.tables.daos.LeaderDao;
import com.github.kickshare.db.jooq.tables.daos.ProjectDao;
import com.github.kickshare.db.jooq.tables.daos.ProjectPhotoDao;
import com.github.kickshare.db.jooq.tables.pojos.GroupPost;
import com.github.kickshare.db.jooq.tables.pojos.Project;
import com.github.kickshare.db.jooq.tables.pojos.ProjectPhoto;
import com.github.kickshare.db.jooq.tables.records.ProjectRecord;
import com.github.kickshare.domain.City;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.mapper.ExtendedMapper;
import com.github.kickshare.service.GroupSearchOptions;
import com.github.kickshare.service.Location;
import com.github.kickshare.service.entity.CityGrid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectField;
import org.jooq.TableLike;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private final LeaderDao leaderDao;
    private ExtendedMapper mapper;
//    private JdbcUserDetailsManager userManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProjectInfo findProjectInfo(final Long projectId) {
        final Project project = projectDao.fetchOneById(projectId);
        final ProjectPhoto projectPhoto = photoDao.fetchOneByProjectId(projectId);
        ProjectInfo info = new ProjectInfo();
        info.setProject(mapper.map(project, com.github.kickshare.domain.Project.class));
        info.setPhotoUrl(projectPhoto.getThumb());
        info.setPhoto(mapper.map(projectPhoto, com.github.kickshare.domain.ProjectPhoto.class));
        return info;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ProjectInfo> findProjectInfoByName(final String name) {
        final List<Project> projects = searchProject(name);
        final List<ProjectInfo> infos = projects.stream().map(project -> {
            final ProjectPhoto projectPhoto = photoDao.fetchOneByProjectId(project.getId());
            ProjectInfo info = new ProjectInfo();
            info.setProject(mapper.map(project, com.github.kickshare.domain.Project.class));
            info.setPhotoUrl(projectPhoto.getThumb());
            info.setPhoto(mapper.map(projectPhoto, com.github.kickshare.domain.ProjectPhoto.class));
            return info;
        }).collect(Collectors.toList());
        return infos;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveProjects(final List<ProjectInfo> projects) {
//        projects.stream()
//                .map(p -> mapper.map(p, Project.class))
//                .forEach(p -> dsl.insertInto(PROJECT).values(p).onDuplicateKeyIgnore().execute());
        for (ProjectInfo info : projects) {
            Project project = mapper.map(info, Project.class);
            ProjectRecord record = dsl.newRecord(PROJECT, project);
            int count = dsl.insertInto(PROJECT, record.fields()).values(record.valuesRow().fields()).onConflictDoNothing().execute();
            if (count > 0) {
                ProjectPhoto photo = mapper.map(info.getPhoto(), ProjectPhoto.class);
                photo.setProjectId(info.getId());
                photoDao.insert(photo);
            }
        }
    }

    @Override
    //@TODO - refactor to service?
    public List<GroupInfo> findAllGroupInfo(final Long projectId) {
        final Project project = projectDao.fetchOneById(projectId);
        final ProjectPhoto projectPhoto = photoDao.fetchOneByProjectId(projectId);
        Backer u = BACKER.as("u");
        Group g = GROUP.as("g");
        /**
         * SELECT *
         FROM "GROUP" AS g
         JOIN USER AS u ON g.LEADER_ID = u.ID
         INNER JOIN (
         SELECT COUNT(*), GROUP_ID
         FROM BACKER_2_GROUP
         GROUP BY (GROUP_ID)
         ) AS c ON c.GROUP_ID = g.ID
         WHERE project_id = 217227567;
         */
        TableLike<?> c = dsl
                .select(BACKER_2_GROUP.GROUP_ID.as("GROUP_ID"), DSL.count().as(PARTICIPANT_COUNT))
                .from(BACKER_2_GROUP)
                .groupBy(BACKER_2_GROUP.GROUP_ID).asTable("c");

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
        }
        return infos;
    }

    @Override
    public List<City> findCitiesWithing(final Location ne, final Location sw) {
        return dsl.select()
                .from(CITY)
                .where(CITY.LAT.between(BigDecimal.valueOf(sw.getLat()), BigDecimal.valueOf(ne.getLat()))
                        .and(CITY.LON.between(BigDecimal.valueOf(sw.getLon()), BigDecimal.valueOf(ne.getLon())))
                )
                .limit(1_000)
                .fetchInto(City.class);
    }

    @Override
    public List<com.github.kickshare.db.jooq.tables.pojos.Group> searchGroups(GroupSearchOptions options) {
        List<com.github.kickshare.db.jooq.tables.pojos.Group> groups = dsl.select()
                .from(GROUP)
                .where(where(options))
                .fetchInto(com.github.kickshare.db.jooq.tables.pojos.Group.class);
        LOGGER.info("Returning: {}", groups);
        return groups;
    }

    public List<GroupPost> getGroupPost(Long groupId, Pageable pageable) {
        return dsl.select()
                .from(GROUP_POST)
                .where(GROUP_POST.GROUP_ID.eq(groupId))
                .orderBy(GROUP_POST.POST_MODIFIED.desc())
                .fetchInto(com.github.kickshare.db.jooq.tables.pojos.GroupPost.class);
    }

    public List<Project> searchProjects(GroupSearchOptions options) throws IOException {
        final List<Project> projects = dsl.select()
                .from(PROJECT)
                .where(exists(
                        dsl.selectOne()
                                .from(GROUP)
                                .where(where(options)))
                )
                .fetchInto(com.github.kickshare.db.jooq.tables.pojos.Project.class);
        return projects;
    }

    public List<CityGrid> searchCityGrid(GroupSearchOptions options) throws IOException {
        final Function<Record, CityGrid> transformer = (rec) -> {
            CityGrid grid = new CityGrid();
            final Integer total = rec.get("count", Integer.class);
            grid.setGroupCount(total);
            grid.setLocalGroups(rec.get("is_local", Integer.class));
            grid.setGlobalGroups(grid.getGroupCount() - grid.getLocalGroups());
            Location location = new Location(rec.get(GROUP.LAT).floatValue(), rec.get(GROUP.LON).floatValue());
            grid.setLocation(location);
            if (grid.getLocalGroups() > 0) {
                if (grid.getGlobalGroups() > 0) {
                    grid.setType(CityGrid.Type.MIXED);
                } else {
                    grid.setType(CityGrid.Type.LOCAL);
                }
            } else {
                grid.setType(CityGrid.Type.GLOBAL);
            }
            return grid;
        };
        Field<Integer> local = count().filterWhere(GROUP.IS_LOCAL.eq(true)).as("is_local");
        List<CityGrid> cities = dsl.select(GROUP.LAT, GROUP.LON, local, count())
                .from(GROUP)
                .where(where(options))
                .groupBy(GROUP.LAT, GROUP.LON)
                .fetch((row) -> transformer.apply(row));
        LOGGER.info("Returning: {}", cities);
        return cities;
    }

    private Condition where(GroupSearchOptions ops) {
        Condition query = mapViewCondition(ops);
        String name = ops.getProjectName();
        Long projectId = ops.getProjectId();
        if (StringUtils.isNotBlank(name) && name.length() >= 3) {
            query = query.and(GROUP.NAME.like('%' + name + '%'));
        }
        if (projectId != null && projectId > 0) {
            query = query.and(GROUP.PROJECT_ID.eq(ops.getProjectId()));
        }
        return query;
    }


    private Condition mapViewCondition(GroupSearchOptions ops) {
        Location nw = ops.getGeoBoundary().getLeftTop();
        Location se = ops.getGeoBoundary().getRightBottom();
        Condition latCondition = GROUP.LAT.between(BigDecimal.valueOf(se.getLat()), BigDecimal.valueOf(nw.getLat()));
        Condition lonCondition = GROUP.LON.between(BigDecimal.valueOf(nw.getLon()), BigDecimal.valueOf(se.getLon()));
        Condition geoCondition = latCondition.and(lonCondition);
        if (!ops.getSearchLocalOnly()) {
            return geoCondition.or(GROUP.IS_LOCAL.eq(false));
        } else {
            return geoCondition;
        }
    }

    private List<Project> searchProject(String name) {
        return dsl.select().from(PROJECT).where(PROJECT.NAME.like('%' + name + '%')).fetchInto(Project.class);
    }


}
