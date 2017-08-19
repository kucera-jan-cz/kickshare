package com.github.kickshare.rest.group;

import com.github.kickshare.service.entity.Location;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
@Data
public class GeoBoundary {
    private Location leftTop;
    private Location rightBottom;
}
