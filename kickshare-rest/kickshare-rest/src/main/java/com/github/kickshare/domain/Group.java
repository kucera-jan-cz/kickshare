package com.github.kickshare.domain;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
@Data
public class Group {
    private Long   id;
    private Long   leaderId;
    private Long   projectId;
    private String name;
}
