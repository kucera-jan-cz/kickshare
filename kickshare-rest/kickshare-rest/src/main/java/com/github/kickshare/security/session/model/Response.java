package com.github.kickshare.security.session.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 17.5.2017
 */
@Data
@AllArgsConstructor
public class Response {
    private int code;

    private String message;

    private Error error;
}
