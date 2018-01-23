package com.github.kickshare.db.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoBoundaryDB {
    private LocationDB leftTop;
    private LocationDB rightBottom;
}
