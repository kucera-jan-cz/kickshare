package com.github.kickshare.db.query;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
@Data
public class SearchOptionsDB {
    private Boolean searchLocalOnly;
    private Integer categoryId;
    private Long projectId;
    private GeoBoundaryDB geoBoundary;
    private LocationDB localCity;

}
