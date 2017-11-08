package com.github.kickshare.db.query;

import static com.github.kickshare.db.jooq.Tables.GROUP;

import java.math.BigDecimal;
import java.util.function.Function;

import com.github.kickshare.service.entity.Location;
import com.github.kickshare.service.entity.SearchOptions;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;

/**
 * @author Jan.Kucera
 * @since 7.11.2017
 */
public class GroupQueryBuilder implements Function<SearchOptions, Condition> {

    @Override
    public Condition apply(final SearchOptions ops) {
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
