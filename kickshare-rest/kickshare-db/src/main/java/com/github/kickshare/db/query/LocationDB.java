package com.github.kickshare.db.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDB {
    private Float lat;
    private Float lon;

    public LocationDB(Double lat, Double lon) {
        this.lat = lat.floatValue();
        this.lon = lon.floatValue();
    }
}
