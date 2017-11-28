package com.github.kickshare.ext.service.kickstarter.campaign;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.ext.service.kickstarter.campaign.entity.CampaignProject;
import com.github.kickshare.ext.service.kickstarter.campaign.exception.AuthenticationException;


/**
 * @author Jan.Kucera
 * @since 23.3.2017
 */

public interface KickstarterCampaignService {

    //@TODO - consider using enum for categories
    List<CampaignProject> findProjects(String term, Integer category) throws IOException;

    Long verify(String user, String password) throws IOException, AuthenticationException;
}
