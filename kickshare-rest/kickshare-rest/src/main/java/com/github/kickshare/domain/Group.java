package com.github.kickshare.domain;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
@Data
@AllArgsConstructor
public class Group {
    private Long id;

    @NotNull
    private String name;

    @JsonProperty("leader_id")
    private Long leaderId;

    @NotNull
    @JsonProperty("project_id")
    private Long projectId;

    @NotNull
    @JsonProperty("is_local")
    private Boolean isLocal;
}
