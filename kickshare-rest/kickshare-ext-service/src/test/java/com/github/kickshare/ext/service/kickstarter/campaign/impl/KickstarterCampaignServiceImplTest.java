package com.github.kickshare.ext.service.kickstarter.campaign.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kickshare.ext.service.common.ResourceStream;
import com.github.kickshare.ext.service.kickstarter.campaign.KickstarterCampaignService;
import com.github.kickshare.ext.service.kickstarter.campaign.entity.CampaignProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 28.11.2017
 */
public class KickstarterCampaignServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(KickstarterCampaignServiceImplTest.class);

    @Test
    public void parseTest() throws IOException {
        InputStream stream = ResourceStream.of("data/kickstarter/discover_advanced.json");
        final ClientHttpRequestFactory requestFactory = new MockMvcClientHttpRequestFactory(stream);
        final ObjectMapper mapper = new ObjectMapper();
        KickstarterCampaignService service = new KickstarterCampaignServiceImpl(requestFactory, mapper);
        List<CampaignProject> projects = service.findProjects("Quodd Heroes", 34);
        LOGGER.info("{}", mapper.writeValueAsString(projects));
        assertNotNull(projects);
        CampaignProject project = projects.get(0);
        assertEquals(439380282, project.getId().longValue());
        assertEquals(34, project.getCategoryId().intValue());
    }

}