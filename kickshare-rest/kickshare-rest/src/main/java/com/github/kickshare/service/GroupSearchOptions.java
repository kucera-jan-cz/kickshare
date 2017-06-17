package com.github.kickshare.service;

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
    private String projectName;
    private Long projectId;
    private GeoBoundary geoBoundary;
    private Location localCity;
}
