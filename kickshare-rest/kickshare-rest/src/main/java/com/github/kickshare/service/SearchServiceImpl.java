package com.github.kickshare.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kickshare.service.entity.City;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
