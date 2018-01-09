package com.github.kickshare.ext.service.kickstarter.backer.entity;

import java.time.OffsetDateTime;
import java.util.List;

import com.github.kickshare.ext.service.kickstarter.campaign.entity.CampaignProject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 8.1.2018
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Backer {
    private String id;
    private String fullName;
    private OffsetDateTime joined;
    private List<CampaignProject> backedProjects;
}
