package com.github.kickshare.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 13.5.2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProjectPhoto {
    private Long   projectId;
    private String thumb;
    private String small;
}
