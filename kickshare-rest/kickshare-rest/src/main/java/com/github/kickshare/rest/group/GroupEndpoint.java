package com.github.kickshare.rest.group;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@RestController
@RequestMapping("/groups")
public class GroupEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupEndpoint.class);
    private static final Feature[] POINTS = {
            point(49.1951F, 16.6068F, "Brno", true),
            point(49.1951F, 16.6068F, "Brno", false),
            point(49.1951F, 16.6068F, "Brno", false),
            point(50.083333F, 14.416667F, "Praha", false),
            point(49.0520F, 15.8086F, "České Budějovice", true),
            point(49.5955F, 17.25175F, "Olomouc", false),
            point(49.83332F, 18.25F, "Ostrava", true),
            point(49.33818F, 15.0043F, "Benešov", false),
            point(50.16667F, 13F, "Karlovy Vary", true),
    };
    //Search for groups using user's location, using distance near (slider), tags, potentially campaign's name

    @PostMapping("/search")
    public List<Object> searchGroups(String userId, String categoryId, SearchGroupOptions options) {
        return Collections.emptyList();
    }

    @RequestMapping(value = "/search/jsonp", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeatureCollection getData(
            @RequestParam String callback,
            @RequestParam(required = false) Float lat,
            @RequestParam(required = false) Float lon) {
        LOGGER.warn("{}: {}", lat, lon);
        int items = RandomUtils.nextInt(1, POINTS.length);
        FeatureCollection collection = new FeatureCollection();
        for (int i = 0; i < items; i++) {
            collection.add(POINTS[RandomUtils.nextInt(0, POINTS.length)]);
        }
            return collection;
        }

    private static Feature point(Float lat, Float lon, String name, boolean isLocal) {
        Feature feature = new Feature();
        feature.setGeometry(new Point(lon, lat));
        String type = (isLocal) ? "local" : "global";
        feature.setProperty("type", type);
        return feature;
    }

}
