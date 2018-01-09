package com.github.kickshare.ext.service.kickstarter.backer.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kickshare.ext.service.common.ResourceBuilder;
import com.github.kickshare.ext.service.exception.ServiceNotAvailable;
import com.github.kickshare.ext.service.kickstarter.backer.KickstarterBackerService;
import com.github.kickshare.ext.service.kickstarter.backer.entity.Backer;
import com.github.kickshare.ext.service.kickstarter.campaign.entity.CampaignProject;
import com.github.kickshare.ext.service.kickstarter.campaign.entity.User;
import com.github.kickshare.ext.service.kickstarter.campaign.exception.AuthenticationException;
import com.github.kickshare.ext.service.kickstarter.common.HtmlUtil;
import com.github.kickshare.ext.service.kickstarter.parser.ProjectParser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CleanerTransformations;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Jan.Kucera
 * @since 8.1.2018
 */
public class KickstarterHtmlBackerService implements KickstarterBackerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KickstarterHtmlBackerService.class);
    private static final String KS_PRODUCTION_ID = "2II5GGBZLOOZAA5XBU1U0Y44BU57Q58L8KOGM7H0E0YFHP3KTG";
    private static final String PROJECT_LISTING_XPATH = "//div[@id='profile_projects_list']/div/div/ul/li[contains(@class, 'page')]";
    private static final String BACKER_NODE_XPATH = "//div[child::div[@id='profile_avatar']]";
    private final URI xauthUri = KickstarterHtmlBackerService.createXAuthBuilder().build().toUri();

    private final ResourceBuilder resourceBuilder;
    private final ClientHttpRequestFactory requestFactory;
    private final DomSerializer serializer;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectParser projectParser = new ProjectParser();

    public KickstarterHtmlBackerService(final ResourceBuilder resourceBuilder, final ClientHttpRequestFactory requestFactory) {
        this.resourceBuilder = resourceBuilder;
        this.requestFactory = requestFactory;
        final CleanerProperties properties = new CleanerProperties();
        final CleanerTransformations transformations = new CleanerTransformations();
        properties.setCleanerTransformations(transformations);
        this.serializer = new DomSerializer(properties);
    }

    @Override
    public Backer getBackerProjects(final String username) {
        try (InputStream ins = resourceBuilder.apply(username)) {
            TagNode tagNode = new HtmlCleaner().clean(ins);
            Document dom4j = new DOMReader().read(serializer.createDOM(tagNode));
            return parseBacker(username, dom4j);
        } catch (ParserConfigurationException | IOException ex) {
            throw new ServiceNotAvailable("Failed to retrieve and parse rewards", ex);
        }
    }

    private static UriComponentsBuilder createXAuthBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.kickstarter.com")
                .path("/xauth/access_token")
                .queryParam("client_id", KS_PRODUCTION_ID);
    }

    private Backer parseBacker(final String id, final Document root) {
        Element backer = (Element) root.selectSingleNode(BACKER_NODE_XPATH);
        String fullName = parseBackerFullName(backer);
        OffsetDateTime joined = parseBackerJoined(backer);
        Element projectListing = (Element) root.selectSingleNode(PROJECT_LISTING_XPATH);
        //@TODO - finish loading next page
        boolean isComplete = Boolean.parseBoolean(projectListing.attribute("data-last_page").getValue());
        List<Element> projectElements = projectListing.selectNodes("./div");
        List<CampaignProject> projects = projectElements.stream()
                .map(it -> it.attribute("data-project").getValue())
                .map(StringEscapeUtils::unescapeHtml4)
                .map(this::parseProject)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new Backer(id, fullName, joined, projects);
    }

    private String parseBackerFullName(final Element backerElement) {
        Element header = (Element) backerElement.selectSingleNode("./descendant::h2");
        String fullName = HtmlUtil.removeEmptyCharacters(header.getText());
        return fullName;
    }

    private OffsetDateTime parseBackerJoined(final Element backerElement) {
        Element time = (Element) backerElement.selectSingleNode("./descendant::time");
        String datetimeAsText = time.attribute("datetime").getValue();
        TemporalAccessor temporal = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(datetimeAsText);
        return OffsetDateTime.from(temporal);
    }

    private CampaignProject parseProject(final String projectAsJson) {
        try {
            ObjectNode root = (ObjectNode) mapper.readTree(projectAsJson);
            return projectParser.apply(root);
        } catch (IOException e) {
            LOGGER.warn("Failed to parse backer's projects from JSON: ", e);
            return null;
        }
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

    private ClientHttpResponse post(URI uri, Object body) throws IOException {
        ClientHttpRequest request = requestFactory.createRequest(uri, HttpMethod.POST);
        mapper.writeValue(request.getBody(), body);
        ClientHttpResponse response = request.execute();
        return response;
    }
}
