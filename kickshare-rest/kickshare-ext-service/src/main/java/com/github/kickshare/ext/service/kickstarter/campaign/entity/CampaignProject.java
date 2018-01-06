package com.github.kickshare.ext.service.kickstarter.campaign.entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignProject {
    private Long id;
    private Integer categoryId;
    private String name;
    private String description;
    private String url;
    private Instant deadline;

    private CampaignProjectPhoto photo;
}
