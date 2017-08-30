package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.CITY;
import static com.github.kickshare.db.jooq.Tables.GROUP;
import static com.github.kickshare.db.jooq.Tables.GROUP_POST;
import static com.github.kickshare.db.jooq.Tables.PROJECT;
import static org.jooq.impl.DSL.count;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import com.github.kickshare.db.jooq.tables.daos.ProjectPhotoDao;
import com.github.kickshare.db.jooq.tables.pojos.GroupPost;
import com.github.kickshare.db.jooq.tables.pojos.Project;
import com.github.kickshare.db.jooq.tables.pojos.ProjectPhoto;
import com.github.kickshare.db.jooq.tables.records.ProjectRecord;
import com.github.kickshare.domain.City;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.mapper.ExtendedMapper;
import com.github.kickshare.mapper.ProjectPhotoMapper;
import com.github.kickshare.service.entity.CityGrid;
import com.github.kickshare.service.entity.Location;
import com.github.kickshare.service.entity.SearchOptions;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
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

    private DSLContext dsl;
    private ProjectPhotoDao photoDao;
    private ExtendedMapper mapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveProjects(final List<ProjectInfo> projects) {
        for (ProjectInfo info : projects) {
            Project project = mapper.map(info, Project.class);
            ProjectRecord record = dsl.newRecord(PROJECT, project);
            int count = dsl.insertInto(PROJECT, record.fields()).values(record.valuesRow().fields()).onConflictDoNothing().execute();
            if (count > 0) {
                ProjectPhoto photo = ProjectPhotoMapper.INSTANCE.toDB(info.getPhoto());
                photo.setProjectId(info.getId());
                photoDao.insert(photo);
            }
        }
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
    public List<com.github.kickshare.db.jooq.tables.pojos.Group> searchGroups(SearchOptions options) {
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

    public List<Project> searchProjects(SearchOptions options) throws IOException {
        final List<Project> projects = dsl.select()
                .from(PROJECT)
                .whereExists(
                        dsl.selectOne()
                                .from(GROUP)
                                .where(where(options))
                                .and(GROUP.PROJECT_ID.eq(PROJECT.ID))
                )
                .fetchInto(com.github.kickshare.db.jooq.tables.pojos.Project.class);
        return projects;
    }

    public List<CityGrid> searchCityGrid(SearchOptions options) throws IOException {
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

    private Condition where(SearchOptions ops) {
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


    private Condition mapViewCondition(SearchOptions ops) {
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

}
