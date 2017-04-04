package com.github.kickshare.rest.group;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
@Data
public class GroupSearchOptions {
    private Boolean searchLocalOnly;
    private String projectName;
    private GeoBoundary geoBoundary;
}
