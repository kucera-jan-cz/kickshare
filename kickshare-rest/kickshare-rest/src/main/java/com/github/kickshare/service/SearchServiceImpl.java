package com.github.kickshare.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kickshare.service.entity.City;
import com.github.kickshare.service.entity.CityGrid;
import com.github.kickshare.service.entity.Group;
import com.github.kickshare.service.parser.CityGridParser;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.jsfr.json.JsonSurfer;
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

    public List<Group> searchGroups(GroupSearchOptions options) throws IOException {
        QueryBuilder query = options.toQuery();
        LOGGER.info("Query:\n{}", query);
        SearchResponse response = client.prepareSearch("ks2").setQuery(query).get();
        Collection<Group> groups = JsonSurfer.jackson().collectAll(response.toString(), Group.class, "$.hits.hits[*]._source");
        LOGGER.info("{}", groups);
        return Collections.emptyList();
    }

    public List<CityGrid> searchCityGrid(GroupSearchOptions options) throws IOException {
        QueryBuilder query = options.toQuery();
        //@TODO - change precision based on google zoom
        final TermsAggregationBuilder isLocalAgg = AggregationBuilders.terms("IS_LOCAL").field("is_local").size(2);
        final AggregationBuilder aggregation = AggregationBuilders
                .geohashGrid("CITIES").field("location").precision(4)
                .subAggregation(isLocalAgg);
        SearchResponse response = client.prepareSearch("ks2").setQuery(query).addAggregation(aggregation).get();
        CityGridParser parser = new CityGridParser();
        return parser.apply(response.toString());
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
}
