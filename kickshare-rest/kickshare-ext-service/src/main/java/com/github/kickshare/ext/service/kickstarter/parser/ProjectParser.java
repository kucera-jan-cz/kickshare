package com.github.kickshare.ext.service.kickstarter.parser;

import java.time.Instant;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kickshare.ext.service.kickstarter.campaign.entity.CampaignProject;
import com.github.kickshare.ext.service.kickstarter.campaign.entity.CampaignProjectPhoto;

/**
 * @author Jan.Kucera
 * @since 8.1.2018
 */
public class ProjectParser implements Function<ObjectNode, CampaignProject> {

    @Override
    public CampaignProject apply(final ObjectNode projectNode) {
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
        Integer categoryId = projectNode.get("category").get("id").intValue();
        return new CampaignProject(
                id,
                categoryId,
                projectNode.get("name").textValue(),
                projectNode.get("blurb").textValue(),
                projectNode.path("urls").path("web").path("project").textValue(),
                Instant.ofEpochSecond(projectNode.get("deadline").longValue()),
                photo
        );
    }
}
