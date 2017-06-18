package com.github.kickshare.domain;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 8.4.2017
 */
@Data
public class Backer {
    private final Long id;
    private final String email;
    private final String name;
    private final String surname;
    private final Float leaderRating;
    private final Float backerRating;
}
