package com.github.kickshare.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kickshare.rest.user.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 21.3.2017
 */
@Data
@AllArgsConstructor
public class City {
    @JsonProperty
    private String id;
    @JsonProperty
    private Location location;
}
