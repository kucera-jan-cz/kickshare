package com.github.kickshare.ext.service.kickstarter.campaign.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 24.4.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignProjectPhoto {

    private Long id;
    private String thumb;
    private String small;
    private String little;
    private String ed;
    private String med;
    private String full;
}
