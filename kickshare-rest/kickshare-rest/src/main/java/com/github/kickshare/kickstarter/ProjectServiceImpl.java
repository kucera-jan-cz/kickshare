package com.github.kickshare.kickstarter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kickshare.kickstarter.entity.Project;
import com.github.kickshare.kickstarter.entity.ProjectPhoto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Jan.Kucera
 * @since 23.3.2017
 */
@AllArgsConstructor
@Service
@Component("ks.kickstarter.projectService")
public class ProjectServiceImpl implements ProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private ClientHttpRequestFactory requestFactory;
    private ObjectMapper mapper;
    private final UriComponentsBuilder termUriBuilder = ProjectServiceImpl.createTermSearchBuilder();
    private final UriComponentsBuilder advancedSearchUriBuilder = ProjectServiceImpl.createTermSearchBuilder();

    @Override
    @Deprecated
    public List<Project> findProjects() throws IOException {
        return Collections.emptyList();
    }

    @Override
    @Deprecated
    public Optional<Project> findById(final Long id) throws IOException {
        return Optional.empty();
    }

    //@TODO - better include whole Category object
    public List<Project> findProjects(String term, Integer category) throws IOException {
        URI uri = termUriBuilder.buildAndExpand(term, category).toUri();

        InputStream jsonAsStream = executeGet(uri);
        JsonNode root = mapper.readTree(jsonAsStream);
        return parseProjects(root);
    }

    private List<Project> parseProjects(JsonNode root) {
        ArrayNode projectsNode = (ArrayNode) root.get("projects");
        List<Project> projects = new ArrayList<>();
        for (Iterator<JsonNode> it = projectsNode.elements(); it.hasNext(); ) {
            Project project = parseProject((ObjectNode) it.next());
            LOGGER.info("{}", project);
            projects.add(project);
        }
        return projects;
    }


    private InputStream executeGet(URI uri) throws IOException {
        ClientHttpRequest request = requestFactory.createRequest(uri, HttpMethod.GET);
        ClientHttpResponse response = request.execute();
        return response.getBody();
    }

    private Project parseProject(ObjectNode projectNode) {
        JsonNode photoNode = projectNode.get("photo");
        Long id = projectNode.get("id").longValue();
        ProjectPhoto photo = new ProjectPhoto(id, photoNode.get("thumb").textValue(), photoNode.get("small").textValue());
        return new Project(
                id,
                projectNode.get("name").textValue(),
                projectNode.get("blurb").textValue(),
                projectNode.path("urls").path("web").path("project").textValue(),
                Instant.ofEpochSecond(projectNode.get("deadline").longValue()),
                photo
        );
    }

    public static UriComponentsBuilder createIdSearchBuilder() {
        //https://www.kickstarter.com/discover/advanced?format=json&term=Perfect+crime&category_id=games
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("www.kickstarter.com")
                .path("/discover/advanced")
                .queryParam("format", "json")
                .queryParam("term", "{term}")
                .queryParam("category_id", "{category_id}");
    }

    public static UriComponentsBuilder createTermSearchBuilder() {
        //https://www.kickstarter.com/discover/advanced?format=json&term=Perfect+crime&category_id=games
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("www.kickstarter.com")
                .path("/discover/advanced")
                .queryParam("format", "json")
                .queryParam("term", "{term}")
                .queryParam("category_id", "{category_id}");
    }

}
