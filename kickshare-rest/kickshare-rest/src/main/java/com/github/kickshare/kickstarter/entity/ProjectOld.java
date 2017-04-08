package com.github.kickshare.kickstarter.entity;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 23.3.2017
 */
@Data
@AllArgsConstructor
@Deprecated
public class ProjectOld {
    private Long id;
    private String name;
    private String description;
    private String url;

    private Instant deadline;

}
