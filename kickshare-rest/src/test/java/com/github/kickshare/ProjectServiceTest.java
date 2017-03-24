package com.github.kickshare;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kickshare.kickstarter.entity.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 23.3.2017
 */
public class ProjectServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceTest.class);

    @Test
    public void searchProjects() throws IOException {
        InputStream jsonAsStream = this.getClass().getClassLoader().getResourceAsStream("data/kickstarter/discover_advanced.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonAsStream);
        ArrayNode projectsNode = (ArrayNode) root.get("projects");
        List<Project> projects = new ArrayList<>();
        for (Iterator<JsonNode> it = projectsNode.elements(); it.hasNext(); ) {
            final ObjectNode projectNode = (ObjectNode) it.next();
            Project project = new Project(
                    BigDecimal.valueOf(projectNode.get("id").longValue()),
                    projectNode.get("name").textValue(),
                    projectNode.get("blurb").textValue(),
                    projectNode.path("urls").path("web").path("project").textValue(),
                    Instant.ofEpochMilli(projectNode.get("deadline").longValue())
            );
            LOGGER.info("{}", project);
            projects.add(project);
        }
//        final JsonPathListener projectPathListener = (Object nodeAsObject, ParsingContext context) -> {
//            ObjectNode node = (ObjectNode) nodeAsObject;
//            node.get("id").asLong();
//        };
//        final SurfingConfiguration configuration = SurfingConfiguration.builder().bind("", projectPathListener).build();
//
//        final com.jayway.jsonpath.spi.json.JsonProvider provider = new JacksonJsonNodeJsonProvider();
//        final Configuration config = Configuration.builder().jsonProvider(provider).build();
//        List<Project> projects = JsonPath.parse(jsonAsStream, config).read("$.projects[*]");

//        JsonSurfer.jackson().collectAll()surf(new InputStreamReader(jsonAsStream), configuration);
//        Project project = null;
    }
}
