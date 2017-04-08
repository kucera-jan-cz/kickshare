package com.github.kickshare.rest.city;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.kickshare.service.GeoBoundary;
import com.github.kickshare.service.GroupSearchOptions;
import com.github.kickshare.service.Location;
import com.github.kickshare.service.SearchService;
import com.github.kickshare.service.entity.CityGrid;
import lombok.AllArgsConstructor;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 5.4.2017
 */
@RestController("/cities")
@AllArgsConstructor
public class CityEndpoint {
    private SearchService service;

    @RequestMapping(value = "/search.jsonp", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeatureCollection getData(
            @RequestParam String callback,
            @RequestParam String options) throws IOException {
        GroupSearchOptions searchOptions = null;
        final List<CityGrid> cityGrids = service.searchCityGrid(searchOptions);
        FeatureCollection collection = new FeatureCollection();
        collection.addAll(cityGrids.stream().map(CityEndpoint::point).collect(Collectors.toList()));
        return collection;
    }

    private GroupSearchOptions toOptions(Map<String, String> params) {
        GroupSearchOptions.GroupSearchOptionsBuilder builder = GroupSearchOptions.builder();
        builder.searchLocalOnly(Boolean.valueOf(params.get("only_local")));
        builder.projectName(params.get("name"));
        Location leftTop = new Location(
                Float.parseFloat(params.get("ne_lat")),
                Float.parseFloat(params.get("ne_lon"))
        );
        Location rightBottom = new Location(
                Float.parseFloat(params.get("sw_lat")),
                Float.parseFloat(params.get("sw_lon"))
        );
        builder.geoBoundary(new GeoBoundary(leftTop, rightBottom));
        return builder.build();
    }

    public static Feature point(final CityGrid city) {
        Feature feature = new Feature();
        feature.setGeometry(new Point(city.getLocation().getLon(), city.getLocation().getLat()));
        feature.setProperty("type", city.getType().name());
        return feature;
    }
}
