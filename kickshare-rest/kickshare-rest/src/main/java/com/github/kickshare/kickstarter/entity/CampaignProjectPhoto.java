package com.github.kickshare.kickstarter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 24.4.2017
 */
@Data
@AllArgsConstructor
public class CampaignProjectPhoto {

    private Long id;
    private String thumb;
    private String small;
    private String little;
    private String ed;
    private String med;
    private String full;

    public CampaignProjectPhoto() {
    }
}
