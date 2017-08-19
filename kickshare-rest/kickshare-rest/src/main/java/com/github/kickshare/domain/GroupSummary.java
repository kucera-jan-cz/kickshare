package com.github.kickshare.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupSummary {
    @Deprecated
    @JsonProperty("group_id")
    private Long id;

    private String name;
    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("is_local")
    private Boolean isLocal;

    @JsonProperty("photo_url")
    private String photoUrl;

    //Group related
    @JsonProperty("leader_id")
    private Long leaderId;

    @JsonProperty("leader_name")
    private String leaderName;

    @JsonProperty("leader_rating")
    private Integer leaderRating;

    @JsonProperty("participant_count")
    private Integer numOfParticipants;

    private Group group;
    private Project project;
    //@TODO - consider whether it's really needed
    private ProjectPhoto photo;
}
