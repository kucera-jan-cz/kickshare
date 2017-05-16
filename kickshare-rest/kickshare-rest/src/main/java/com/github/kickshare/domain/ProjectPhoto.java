package com.github.kickshare.domain;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 13.5.2017
 */
@Data
public class ProjectPhoto {
    private Long   projectId;
    private String thumb;
    private String small;
}
