package com.github.kickshare.service;

import java.io.IOException;
import java.net.UnknownHostException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Client;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
public class SearchServiceTest {
    private Client client;

    @Before
    public void init() throws UnknownHostException {
        this.client = new TransportClientFactory().createClient();
    }

    @Test
    public void loadData() throws IOException {
        SearchServiceImpl service = new SearchServiceImpl(client, new ObjectMapper());
        GeoBoundary boundary = new GeoBoundary(new Location(51F, 13F), new Location(50F, 15F));
        final GroupSearchOptions options = GroupSearchOptions.builder().searchLocalOnly(false).geoBoundary(boundary).build();
        service.searchCities(options);
    }
}
