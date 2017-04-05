package com.github.kickshare.service.parser;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import com.github.kickshare.service.entity.CityGrid;
import com.github.kickshare.test.TestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 4.4.2017
 */
public class CityGridParserTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CityGridParserTest.class);

    @Test
    public void parseValidData() {
        CityGridParser parser = new CityGridParser();

        List<CityGrid> cities = parser.apply(TestUtil.toString("data/elastic/cities_aggregation_resp.json"));
        assertNotNull(cities);
        assertEquals(cities.size(), 3);
    }
}
