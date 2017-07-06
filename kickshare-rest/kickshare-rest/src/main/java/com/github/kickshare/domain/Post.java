package com.github.kickshare.domain;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @NotNull
    @Size(min = 1, max = 5000)
    private String postText;

    public Post() {
    }
}
