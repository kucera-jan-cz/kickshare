package com.github.kickshare.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@TODO - move this to domain
/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @JsonProperty
    private Float lat;
    @JsonProperty
    private Float lon;

    public Location(Double lat, Double lon) {
        this.lat = lat.floatValue();
        this.lon = lon.floatValue();
    }
}
