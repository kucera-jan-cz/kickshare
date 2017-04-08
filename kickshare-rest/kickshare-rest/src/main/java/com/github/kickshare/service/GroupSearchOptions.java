package com.github.kickshare.service;

import lombok.Builder;
import lombok.Data;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
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
    private Location localCity;

    public static class GroupSearchOptionsBuilder {
        private Boolean searchLocalOnly = Boolean.FALSE;
    }

    //@TODO consider wrapper query
    public QueryBuilder toQuery() {
        String name = getProjectName();
        QueryBuilder nameSearch = name == null ? QueryBuilders.matchAllQuery() : QueryBuilders.matchQuery("name", name);
        BoolQueryBuilder mustFilterCondition = QueryBuilders.boolQuery();
        BoolQueryBuilder boolFilter = QueryBuilders.boolQuery().must(mustFilterCondition);

        if (!getSearchLocalOnly()) {
            //Searching for global and local
            mustFilterCondition.should(QueryBuilders.termQuery("is_local", false));
        } else {
            //Searching for local only
            QueryBuilders.boolQuery()
                    .should(mustFilterCondition.should(QueryBuilders.termQuery("is_local", false)))
                    .should(toNearPointQuery(localCity));
        }
        //Limiting groups to the view
        GeoBoundingBoxQueryBuilder geoFilter = toQuery(getGeoBoundary(), "location");
        mustFilterCondition.should(geoFilter);


        QueryBuilder query = QueryBuilders.boolQuery().must(
                nameSearch
        ).filter(
                boolFilter
        );
        return query;
    }

    private GeoDistanceQueryBuilder toNearPointQuery(Location location) {
        //@TODO parametrize this
        return QueryBuilders.geoDistanceQuery("location").distance(10, DistanceUnit.KILOMETERS).point(toPoint(location));
    }

    private GeoBoundingBoxQueryBuilder toQuery(GeoBoundary boundary, String field) {
        GeoPoint topLeft = toPoint(boundary.getLeftTop());
        GeoPoint bottomRight = toPoint(boundary.getRightBottom());
        return QueryBuilders.geoBoundingBoxQuery(field).setCorners(topLeft, bottomRight);
    }

    private GeoPoint toPoint(Location location) {
        return new GeoPoint(location.getLat(), location.getLon());
    }
}
