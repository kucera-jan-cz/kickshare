package com.github.kickshare.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 16.1.2018
 */
@AllArgsConstructor
@Data
public class UserRating {
    List<Rating> backerRating;
    List<Rating> leaderRating;
}
