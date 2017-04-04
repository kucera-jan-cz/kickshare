package com.github.kickshare.service;

import lombok.Builder;
import lombok.Data;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
@Data
@Builder
public class GroupSearchOptions {
    private Boolean searchLocalOnly;
    private String projectName;
    private GeoBoundary geoBoundary;

    public static class GroupSearchOptionsBuilder {
        private Boolean searchLocalOnly = Boolean.FALSE;
    }

    public QueryBuilder toQuery() {
        String name = getProjectName();
        QueryBuilder nameSearch = name == null ? QueryBuilders.matchAllQuery() : QueryBuilders.matchQuery("name", name);
        BoolQueryBuilder mustFilterCondition = QueryBuilders.boolQuery();
        BoolQueryBuilder boolFilter = QueryBuilders.boolQuery().must(mustFilterCondition);

        if (!getSearchLocalOnly()) {
            mustFilterCondition.should(QueryBuilders.termQuery("is_local", false));
        }
        GeoBoundingBoxQueryBuilder geoFilter = toQuery(getGeoBoundary(), "location");
        mustFilterCondition.should(geoFilter);

        QueryBuilder query = QueryBuilders.boolQuery().must(
                nameSearch
        ).filter(
                boolFilter
        );
        return query;
    }

    private GeoBoundingBoxQueryBuilder toQuery(GeoBoundary boundary, String field) {
        GeoPoint topLeft = new GeoPoint(boundary.getLeftTop().getLat(), boundary.getLeftTop().getLon());
        GeoPoint bottomRight = new GeoPoint(boundary.getRightBottom().getLat(), boundary.getRightBottom().getLon());
        return QueryBuilders.geoBoundingBoxQuery(field).setCorners(topLeft, bottomRight);
    }
}
