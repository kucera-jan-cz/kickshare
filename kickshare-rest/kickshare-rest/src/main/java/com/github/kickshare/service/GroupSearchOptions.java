package com.github.kickshare.service;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
@Data
@Builder
public class GroupSearchOptions {
    private Boolean searchLocalOnly;
    @Deprecated //Will be used projectId
    private String projectName;
    private Long projectId;
    private GeoBoundary geoBoundary;
    private Location localCity;

    public static GroupSearchOptions toOptions(Map<String, String> params) {
        GroupSearchOptions.GroupSearchOptionsBuilder builder = GroupSearchOptions.builder();
        builder.searchLocalOnly(Boolean.valueOf(params.get("only_local")));
        builder.projectName(params.get("name"));
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
