package com.github.kickshare.db.pojos;

import com.github.kickshare.db.query.LocationDB;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 4.4.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityGridDB {
    public enum Type {LOCAL, GLOBAL, MIXED}
    private LocationDB location;
    private Type type;
    private Integer groupCount;
    private Integer localGroups;
    private Integer globalGroups;
}
