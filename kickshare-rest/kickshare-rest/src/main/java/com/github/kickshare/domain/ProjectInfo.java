package com.github.kickshare.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
public class ProjectInfo {
    @JsonProperty("photo_url")
    private String photoUrl;

    @lombok.experimental.Delegate
    @JsonIgnore
    //@TODO - decide whether this is desired approach, maybe switch to
    //@TODO - group/groupDetail/groupSummary?
    private Project project;

    private ProjectPhoto photo;

    public ProjectInfo() {
        this.project = new Project();
    }

    @JsonIgnore
    public Project getProject() {
        return project;
    }
}
