package com.github.kickshare.service.entity;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

//@TODO - move this to domain
/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
@Data
@Builder
public class SearchOptions {
    private Boolean searchLocalOnly;
    private Integer categoryId;
    private Long projectId;
    private GeoBoundary geoBoundary;
    private Location localCity;

    public static SearchOptions toOptions(Map<String, String> params) {
        SearchOptions.SearchOptionsBuilder builder = SearchOptions.builder();
        builder.searchLocalOnly(Boolean.valueOf(params.get("only_local")));
        //@TODO - change to required value once UI and BE will work
        builder.categoryId(Integer.valueOf(params.getOrDefault("category_id", "34")));
        builder.projectId(Long.valueOf(params.getOrDefault("project_id", "-1")));
        Location leftTop = new Location(
                Float.parseFloat(params.get("nw_lat")),
                Float.parseFloat(params.get("nw_lon"))
        );
        Location rightBottom = new Location(
                Float.parseFloat(params.get("se_lat")),
                Float.parseFloat(params.get("se_lon"))
        );
        builder.geoBoundary(new GeoBoundary(leftTop, rightBottom));
        return builder.build();
    }
}
