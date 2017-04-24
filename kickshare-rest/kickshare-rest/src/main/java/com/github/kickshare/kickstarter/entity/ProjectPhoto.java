package com.github.kickshare.kickstarter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 24.4.2017
 */
@Data
@AllArgsConstructor
public class ProjectPhoto {

    private Long id;
    private String thumb;
    private String small;

    public ProjectPhoto() {
    }
}
