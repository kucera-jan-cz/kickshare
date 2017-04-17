package com.github.kickshare.service.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kickshare.service.Location;
import com.github.kickshare.service.entity.CityGrid;
import org.jsfr.json.JsonPathListener;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.SurfingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jan.Kucera
 * @since 4.4.2017
 */
public class CityGridParser implements Function<String, List<CityGrid>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CityGridParser.class);

    @Override
    public List<CityGrid> apply(final String elasticSearchResponse) {
        final List<CityGrid> cities = new ArrayList<>();
        final JsonPathListener listener = (o, context) -> {
            ObjectNode node = (ObjectNode) o;
            Location location = getLocation(node);
            CityGrid.Type type = getType(node);
            CityGrid city = new CityGrid(location, type, node.get("doc_count").asInt(), 0, 0);
            cities.add(city);

        };
        SurfingConfiguration configuration = SurfingConfiguration.builder().bind("$.aggregations.CITIES.buckets[*]", listener).build();
        JsonSurfer.jackson().surf(elasticSearchResponse, configuration);
        return cities;
    }

    private Location getLocation(final ObjectNode node) {
        WGS84Point point = GeoHash.fromGeohashString(node.get("key").asText()).getBoundingBoxCenterPoint();
        return new Location(point.getLatitude(), point.getLongitude());
    }

    private CityGrid.Type getType(final ObjectNode node) {
        JsonNode buckets = node.get("IS_LOCAL").get("buckets");
        CityGrid.Type firstType = Boolean.valueOf(buckets.get(0).get("key_as_string").asText()) ? CityGrid.Type.LOCAL : CityGrid.Type.GLOBAL;
        return buckets.path(1).isMissingNode() ? firstType : CityGrid.Type.MIXED;
    }
}
