package com.github.kickshare.kickstarter;

import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kickshare.kickstarter.entity.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 24.4.2017
 */
public class ProjectServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceTest.class);
    @Test
    public void parseTest() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("data/kickstarter/discover_advanced.json");
        final ClientHttpRequestFactory requestFactory = new MockMvcClientHttpRequestFactory(stream);
        final ObjectMapper mapper = new ObjectMapper();
        ProjectService service = new ProjectServiceImpl(requestFactory, mapper);
        List<Project> projects = service.findProjects("Quodd Heroes", 34);
        LOGGER.info("{}", mapper.writeValueAsString(projects));
        assertNull(projects);

    }
}
