package com.github.kickshare.mapper;

import static com.github.kickshare.mapper.EntityMapper.project;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.kickshare.common.io.ResourceUtil;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.kickstarter.entity.CampaignProject;
import org.json.JSONException;
import org.mapstruct.factory.Mappers;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 24.4.2017
 */
public class ProjectMapperTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectMapperTest.class);
    private ObjectMapper mapper = new ObjectMapper();

    {
        mapper.registerModule(new JavaTimeModule());
    }

    @Test(enabled = false)
    //@TODO - rewrite test correctly
    public void projectMapper() throws IOException, JSONException {
        CampaignProject ksProject = mapper.readValue(ResourceUtil.toString("data/mapper/ks/project.json"), CampaignProject.class);
        ProjectInfo domainProject = project().toDomain(ksProject);
        String domain = mapper.writeValueAsString(domainProject);
        String expected = ResourceUtil.toString("data/mapper/domain/project_info.json");
        LOGGER.info("Comparing:\n{}\n{}", domain, expected);
        JSONAssert.assertEquals(domain, expected, false);
    }

    @Test
    public void projectMapStruct() throws IOException, JSONException {
        CampaignProject ksProject = mapper.readValue(ResourceUtil.toString("data/mapper/ks/project.json"), CampaignProject.class);
        ProjectMapper msMapper = Mappers.getMapper(ProjectMapper.class);
//        ProjectInfo domainProject = ProjectMapper.MAPPER.toDomain(ksProject);
        ProjectInfo domainProject = msMapper.toDomain(ksProject);
        String domain = mapper.writeValueAsString(domainProject);
        String expected = ResourceUtil.toString("data/mapper/domain/project_info.json");
        LOGGER.info("Comparing:\n{}\n{}", domain, expected);
        JSONAssert.assertEquals(domain, expected, false);
    }
}
