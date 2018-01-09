package com.github.kickshare.ext.service.kickstarter.campaign.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kickshare.ext.service.kickstarter.campaign.KickstarterCampaignService;
import com.github.kickshare.ext.service.kickstarter.campaign.entity.CampaignProject;
import com.github.kickshare.ext.service.kickstarter.parser.ProjectParser;
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
//@TODO - rename to CampaignService
public class KickstarterCampaignServiceImpl implements KickstarterCampaignService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KickstarterCampaignServiceImpl.class);
    private final UriComponentsBuilder termUriBuilder = KickstarterCampaignServiceImpl.createTermSearchBuilder();
    private final ProjectParser projectParser = new ProjectParser();
    private ClientHttpRequestFactory requestFactory;
    private ObjectMapper mapper;

    //@TODO - better include whole Category object
    @Override
    public List<CampaignProject> findProjects(String term, Integer category) throws IOException {
        URI uri = termUriBuilder.buildAndExpand(term, category).toUri();

        InputStream jsonAsStream = executeGet(uri);
        JsonNode root = mapper.readTree(jsonAsStream);
        return parseProjects(root);
    }


    private List<CampaignProject> parseProjects(JsonNode root) {
        ArrayNode projectsNode = (ArrayNode) root.get("projects");
        List<CampaignProject> projects = new ArrayList<>();
        for (Iterator<JsonNode> it = projectsNode.elements(); it.hasNext(); ) {
            CampaignProject project = projectParser.apply((ObjectNode) it.next());
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
