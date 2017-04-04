package com.github.kickshare.service;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
@Data
@AllArgsConstructor
public class GeoBoundary {
    private Location leftTop;
    private Location rightBottom;
}
