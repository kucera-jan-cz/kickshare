package com.github.kickshare.rest;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.domain.City;
import com.github.kickshare.service.entity.Location;
import lombok.AllArgsConstructor;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 16.4.2017
 */
@RestController
@RequestMapping("/cities")
@AllArgsConstructor
public class CityEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(CityEndpoint.class);
    private KickshareRepository repository;

    @RequestMapping(value = "/search/jsonp", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeatureCollection getData(
            @RequestParam String callback,
            @RequestParam Map<String, String> params) throws IOException {
        Location leftBottom = new Location(
                Float.parseFloat(params.get("sw_lat")),
                Float.parseFloat(params.get("sw_lon"))
        );
        Location rightTop = new Location(
                Float.parseFloat(params.get("ne_lat")),
                Float.parseFloat(params.get("ne_lon"))
        );

        FeatureCollection collection = new FeatureCollection();
        collection.addAll(
                repository.findCitiesWithing(rightTop, leftBottom).stream().map(CityEndpoint::point).collect(Collectors.toList())
        );
        return collection;
    }

    public static Feature point(final City city) {
        Feature feature = new Feature();
        feature.setGeometry(new Point(city.getLon().floatValue(), city.getLat().floatValue()));
        feature.setProperty("name", city.getName());
        return feature;
    }

}
