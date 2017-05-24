package com.github.kickshare.kickstarter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 22.5.2017
 */
@AllArgsConstructor
@Data
public class User {
    private String email;
    private String password;
}
