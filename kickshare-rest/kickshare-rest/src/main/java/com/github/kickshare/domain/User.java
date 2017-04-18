package com.github.kickshare.domain;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 8.4.2017
 */
@Data
public class User {
    private Long id;
    private String email;
    private String name;
    private String surname;
}
