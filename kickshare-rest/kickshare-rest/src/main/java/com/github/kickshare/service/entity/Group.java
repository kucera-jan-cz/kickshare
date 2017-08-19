package com.github.kickshare.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 4.4.2017
 */
@Data
public class Group {
    private String name;
    private String photo;
    @JsonProperty("campaign_id")
    private String campaignId;
    private String leader;
    private Location location;
    @JsonProperty("is_local")
    private Boolean isLocation;

    public Group() {

    }
}
