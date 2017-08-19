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
import com.github.kickshare.kickstarter.entity.CampaignProject;
import com.github.kickshare.kickstarter.entity.CampaignProjectPhoto;
import com.github.kickshare.kickstarter.entity.User;
import com.github.kickshare.kickstarter.exception.AuthenticationException;
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
@Component
public class KickstarterCampaignServiceImpl implements KickstarterCampaignService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KickstarterCampaignServiceImpl.class);
    public static final String KS_PRODUCTION_ID = "2II5GGBZLOOZAA5XBU1U0Y44BU57Q58L8KOGM7H0E0YFHP3KTG";
    private ClientHttpRequestFactory requestFactory;
    private ObjectMapper mapper;
    private final UriComponentsBuilder termUriBuilder = KickstarterCampaignServiceImpl.createTermSearchBuilder();
    private final URI xauthUri = KickstarterCampaignServiceImpl.createXAuthBuilder().build().toUri();

    @Override
    @Deprecated
    public List<CampaignProject> findProjects() throws IOException {
        return Collections.emptyList();
    }

    @Override
    @Deprecated
    public Optional<CampaignProject> findById(final Long id) throws IOException {
        return Optional.empty();
    }

    //@TODO - better include whole Category object
    public List<CampaignProject> findProjects(String term, Integer category) throws IOException {
        URI uri = termUriBuilder.buildAndExpand(term, category).toUri();

        InputStream jsonAsStream = executeGet(uri);
        JsonNode root = mapper.readTree(jsonAsStream);
        return parseProjects(root);
    }

    @Override
    public Long verify(String user, String password) throws IOException, AuthenticationException {
        ClientHttpResponse response = post(xauthUri, new User(user, password));
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AuthenticationException();
        }
        JsonNode responseRoot = mapper.readTree(response.getBody());
        return responseRoot.path("user").path("id").asLong();
    }

    private List<CampaignProject> parseProjects(JsonNode root) {
        ArrayNode projectsNode = (ArrayNode) root.get("projects");
        List<CampaignProject> projects = new ArrayList<>();
        for (Iterator<JsonNode> it = projectsNode.elements(); it.hasNext(); ) {
            CampaignProject project = parseProject((ObjectNode) it.next());
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

    private ClientHttpResponse post(URI uri, Object body) throws IOException {
        ClientHttpRequest request = requestFactory.createRequest(uri, HttpMethod.POST);
        mapper.writeValue(request.getBody(), body);
        ClientHttpResponse response = request.execute();
        return response;
    }

    private CampaignProject parseProject(ObjectNode projectNode) {
        JsonNode photoNode = projectNode.get("photo");
        Long id = projectNode.get("id").longValue();
        CampaignProjectPhoto photo = new CampaignProjectPhoto(id,
                photoNode.path("thumb").asText(null),
                photoNode.path("small").asText(null),
                photoNode.path("little").asText(null),
                photoNode.path("ed").asText(null),
                photoNode.path("med").asText(null),
                photoNode.path("full").asText(null)
        );
        return new CampaignProject(
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

    public static UriComponentsBuilder createXAuthBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.kickstarter.com")
                .path("/xauth/access_token")
                .queryParam("client_id", KS_PRODUCTION_ID);
    }
}
