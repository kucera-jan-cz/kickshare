package com.github.kickshare.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 24.7.2017
 */
@Data
@AllArgsConstructor
public class Notification {
    private Long      id;
    private Long      backerId;
    private Long      senderId;
    private Timestamp postCreated;
    private Boolean   isRead;
    private String    postText;

    public Notification() {

    }
}
