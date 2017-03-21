package com.github.kickshare.rest.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@Data
public class Location {
    @JsonProperty
    private Float lat;
    @JsonProperty
    private Float lon;
}
