package com.github.kickshare.ext.service.kickstarter.backer;

import java.io.IOException;

import com.github.kickshare.ext.service.kickstarter.backer.entity.Backer;
import com.github.kickshare.ext.service.kickstarter.campaign.exception.AuthenticationException;

/**
 * @author Jan.Kucera
 * @since 8.1.2018
 */
public interface KickstarterBackerService {
    Backer getBackerProjects(String username);

    Long verify(String user, String password) throws IOException, AuthenticationException;
}
