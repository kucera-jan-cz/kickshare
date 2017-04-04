package com.github.kickshare.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kickshare.service.entity.City;
import com.github.kickshare.service.entity.Group;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.jsfr.json.JsonPathListener;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.SurfingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jan.Kucera
 * @since 21.3.2017
 */
@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);
    private Client client;
    private ObjectMapper mapper;

    @Autowired
    public SearchServiceImpl(final Client client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public List<Group> searchCities(GroupSearchOptions options) throws IOException {
        QueryBuilder query = options.toQuery();
        LOGGER.info("Query:\n{}", query);
        SearchResponse response = client.prepareSearch("ks2").setQuery(query).get();
//        for(SearchHit hit : response.getHits().hits()) {
//            LOGGER.info("{}", hit.sourceAsString());
//        }

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        response.toXContent(builder, ToXContent.EMPTY_PARAMS);
        builder.endObject();
        final JsonPathListener listener = (o, context) -> {
            ObjectNode node = (ObjectNode) o;
            LOGGER.info("{}", o.getClass());

        };
        SurfingConfiguration configuration = SurfingConfiguration.builder().bind("$.hits.hits[*]._source", listener).build();
//        JsonSurfer.jackson().surf(builder.string(), configuration);
        Collection<Group> groups = JsonSurfer.jackson().collectAll(builder.string(), Group.class, "$.hits.hits[*]._source");
        LOGGER.info("{}", groups);
        return Collections.emptyList();
    }

    @Override
    public List<Object> searchGroups() {
        return null;
    }

    @Override
    public Object getUserLocation(final String userId) {
        return null;
    }

    @Override
    public Object getGroupById(final String groupId) {
        return null;
    }

    @Override
    public City getCityById(final String id) {
        final QueryBuilder query = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("id", id));
        SearchResponse response = client.prepareSearch("cities_raw").setTypes("data").setQuery(query).setSize(1).get();
        String result = response.getHits().getAt(0).getSourceAsString();
        LOGGER.info("{}", result);
//        ch.hsr.geohash.GeoHash.fromGeohashString("").getPoint().getLatitude();
        try {
            return mapper.readValue(result, City.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new City(id, null);
    }

    private GeoBoundingBoxQueryBuilder toQuery(GeoBoundary boundary, String field) {
        GeoPoint topLeft = new GeoPoint(boundary.getLeftTop().getLat(), boundary.getLeftTop().getLon());
        GeoPoint bottomRight = new GeoPoint(boundary.getRightBottom().getLat(), boundary.getRightBottom().getLon());
        return QueryBuilders.geoBoundingBoxQuery(field).setCorners(topLeft, bottomRight);
    }
}
