package com.github.kickshare.ext.service.kickstarter.backer.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.github.kickshare.ext.service.common.ResourceStream;
import com.github.kickshare.ext.service.kickstarter.backer.KickstarterBackerService;
import com.github.kickshare.ext.service.kickstarter.backer.entity.Backer;
import com.github.kickshare.ext.service.kickstarter.campaign.entity.CampaignProject;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 8.1.2018
 */
public class KickstarterHtmlBackerServiceTest {
    @Test
    public void parseTest() {
        InputStream html = ResourceStream.of("kickstarter/backer_profile.html");
        KickstarterBackerService service = new KickstarterHtmlBackerService((String) -> html, null);
        Backer backer = service.getBackerProjects("Xatrix");
        List<String> projects = backer.getBackedProjects().stream().map(CampaignProject::getName).collect(Collectors.toList());
        assertEquals(backer.getId(), "Xatrix");
        assertEquals(backer.getFullName(), "Jan Kučera");
        assertEquals(backer.getJoined(), OffsetDateTime.parse("2016-06-13T17:08:11-04:00"));
        assertTrue(projects.contains("Quodd Heroes"));
    }
}