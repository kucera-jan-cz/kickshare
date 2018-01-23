package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.CATEGORY;
import static com.github.kickshare.db.jooq.Tables.CITY;
import static com.github.kickshare.db.jooq.Tables.GROUP;
import static com.github.kickshare.db.jooq.Tables.PROJECT;
import static org.jooq.impl.DSL.count;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import com.github.kickshare.db.jooq.tables.pojos.CityDB;
import com.github.kickshare.db.jooq.tables.pojos.GroupDB;
import com.github.kickshare.db.pojos.CityGridDB;
import com.github.kickshare.db.query.GroupQueryBuilder;
import com.github.kickshare.db.query.LocationDB;
import com.github.kickshare.db.query.SearchOptionsDB;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
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

    private DSLContext dsl;
    private final GroupQueryBuilder groupQuery = new GroupQueryBuilder();

    @Override
    public List<CityDB> findCitiesWithing(final LocationDB ne, final LocationDB sw) {
        return dsl.select()
                .from(CITY)
                .where(CITY.LAT.between(BigDecimal.valueOf(sw.getLat()), BigDecimal.valueOf(ne.getLat()))
                        .and(CITY.LON.between(BigDecimal.valueOf(sw.getLon()), BigDecimal.valueOf(ne.getLon())))
                )
                .limit(1_000)
                .fetchInto(CityDB.class);
    }

    @Override
    public List<GroupDB> searchGroups(SearchOptionsDB options) {
        List<GroupDB> groups = dsl.select(GROUP.fields())
                .from(GROUP)
                .join(PROJECT).on(GROUP.PROJECT_ID.eq(PROJECT.ID))
                .join(CATEGORY).on(CATEGORY.ID.eq(PROJECT.CATEGORY_ID))
                .where(groupQuery.apply(options))
                .fetchInto(GroupDB.class);
        LOGGER.info("Returning: {}", groups);
        return groups;
    }

    public List<CityGridDB> searchCityGrid(SearchOptionsDB options) {
        final Function<Record, CityGridDB> transformer = (rec) -> {
            CityGridDB grid = new CityGridDB();
            final Integer total = rec.get("count", Integer.class);
            grid.setGroupCount(total);
            grid.setLocalGroups(rec.get("is_local", Integer.class));
            grid.setGlobalGroups(grid.getGroupCount() - grid.getLocalGroups());
            LocationDB location = new LocationDB(rec.get(GROUP.LAT).floatValue(), rec.get(GROUP.LON).floatValue());
            grid.setLocation(location);
            if (grid.getLocalGroups() > 0) {
                if (grid.getGlobalGroups() > 0) {
                    grid.setType(CityGridDB.Type.MIXED);
                } else {
                    grid.setType(CityGridDB.Type.LOCAL);
                }
            } else {
                grid.setType(CityGridDB.Type.GLOBAL);
            }
            return grid;
        };
        Field<Integer> local = count().filterWhere(GROUP.IS_LOCAL.eq(true)).as("is_local");
        List<CityGridDB> cities = dsl.select(GROUP.LAT, GROUP.LON, local, count())
                .from(GROUP)
                .join(PROJECT).on(GROUP.PROJECT_ID.eq(PROJECT.ID))
                .join(CATEGORY).on(CATEGORY.ID.eq(PROJECT.CATEGORY_ID))
                .where(groupQuery.apply(options))
                .groupBy(GROUP.LAT, GROUP.LON)
                .fetch((row) -> transformer.apply(row));
        LOGGER.info("Returning: {}", cities);
        return cities;
    }
}

