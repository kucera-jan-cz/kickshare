package com.github.kickshare.ext.service.kickstarter.reward;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */

@AllArgsConstructor
@ToString
public class Reward {
    private String title;
    private Price price;
    private String description;
    //@TODO - place conversion here?
}
