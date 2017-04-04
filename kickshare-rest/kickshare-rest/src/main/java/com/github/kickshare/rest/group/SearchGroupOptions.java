package com.github.kickshare.rest.group;

import java.util.List;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@Data
@Deprecated
public class SearchGroupOptions {
    private String campaignName;
    private Boolean findNearby;
    private Integer distanceLimit;

    private List<String> tags;


}
