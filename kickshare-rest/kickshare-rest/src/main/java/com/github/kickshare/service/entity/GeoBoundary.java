package com.github.kickshare.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@TODO - move this to domain
/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoBoundary {
    private Location leftTop;
    private Location rightBottom;
}
