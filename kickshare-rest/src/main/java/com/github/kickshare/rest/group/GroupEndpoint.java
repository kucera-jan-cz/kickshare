package com.github.kickshare.rest.group;

import java.util.Collections;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
    //Search for groups using user's location, using distance near (slider), tags, potentially campaign's name

    @PostMapping("/search")
    public List<Object> searchGroups(String userId, String categoryId, SearchGroupOptions options) {
        return Collections.emptyList();
    }

    @RequestMapping(value = "/search/jsonp", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeatureCollection getData(@RequestParam String callback) {

        FeatureCollection collection = new FeatureCollection()
                .add(point(49.1951F, 16.6068F, "Brno"))
                .add(point(50.083333F, 14.416667F, "Praha"));
        return collection;
    }

    private Feature point(Float lat, Float lon, String name) {
        Feature feature = new Feature();
        feature.setGeometry(new Point(lon, lat));
        feature.setProperty("name", "mypoint");
        return feature;
    }

}
