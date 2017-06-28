package com.github.kickshare.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 27.6.2017
 */
@Data
@AllArgsConstructor
public class Post {
    private Long postId;
    private Long groupId;
    private Long backerId;
    private Timestamp postCreated;
    private Timestamp postModified;
    private Integer postEditCount;
    private String postText;

    public Post() {}
}
