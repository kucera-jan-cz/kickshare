package com.github.kickshare.mapper;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.kickshare.common.io.ResourceUtil;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.kickstarter.entity.Project;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.json.JSONException;
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

    @Test
    public void projectMapper() throws IOException, JSONException {
        final Mapper dozer = new DozerBeanMapper(
                Arrays.asList("dozer/db-2-domain-mappings.xml", "dozer/ks-2-domain-mappings.xml")
        );
        Project ksProject = mapper.readValue(ResourceUtil.toString("data/mapper/ks/project.json"), Project.class);
        ProjectInfo domainProject = dozer.map(ksProject, com.github.kickshare.domain.ProjectInfo.class);
        String domain = mapper.writeValueAsString(domainProject);
        String expected = ResourceUtil.toString("data/mapper/domain/project_info.json");
        LOGGER.info("Comparing:\n{}\n{}", domain, expected);
        JSONAssert.assertEquals(domain, expected, false);
    }
}