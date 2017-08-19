package com.github.kickshare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 8.4.2017
 */
@Data
@AllArgsConstructor
public class Backer {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private Float leaderRating;
    private Float backerRating;

    public Backer() {
        //JSON constructor
    }
}
