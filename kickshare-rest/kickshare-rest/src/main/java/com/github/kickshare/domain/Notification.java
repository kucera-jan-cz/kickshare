package com.github.kickshare.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 24.7.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Long      id;
    private Long      backerId;
    private Long      senderId;
    private Timestamp postCreated;
    private Boolean   isRead;
    private String    postText;

}
