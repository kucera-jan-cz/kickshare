package com.github.kickshare.service.entity;

import com.github.kickshare.service.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 4.4.2017
 */
@Data
@AllArgsConstructor
public class CityGrid {
    public enum Type {LOCAL, GLOBAL, MIXED}
    private Location location;
    private Integer numberOfGroups;
    private Type type;
}
