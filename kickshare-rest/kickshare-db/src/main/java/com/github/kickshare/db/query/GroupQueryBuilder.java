package com.github.kickshare.db.query;

import static com.github.kickshare.db.jooq.Tables.CATEGORY;
import static com.github.kickshare.db.jooq.Tables.GROUP;

import java.math.BigDecimal;
import java.util.function.Function;

import org.jooq.Condition;

/**
 * @author Jan.Kucera
 * @since 7.11.2017
 */
public class GroupQueryBuilder implements Function<SearchOptionsDB, Condition> {


    public Condition apply(final SearchOptionsDB ops, boolean omitCategory) {
        Condition query = mapViewCondition(ops);
        //@TODO - remove this logic
        Long projectId = ops.getProjectId();
        Integer categoryId = ops.getCategoryId();
        if (projectId != null && projectId > 0) {
            query = query.and(GROUP.PROJECT_ID.eq(projectId));
        }
        if (!omitCategory && categoryId != null && categoryId > 0) {
            query = query.and(
                    CATEGORY.ID.eq(categoryId).or(CATEGORY.PARENT_ID.eq(categoryId))
            );
        }
        return query;
    }

    @Override
    public Condition apply(final SearchOptionsDB ops) {
        return apply(ops, false);
    }


    private Condition mapViewCondition(SearchOptionsDB ops) {
        LocationDB nw = ops.getGeoBoundary().getLeftTop();
        LocationDB se = ops.getGeoBoundary().getRightBottom();
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
