package com.github.kickshare.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDetail {
    private Project project;
    private ProjectPhoto photo;
    private Group group;
    private User leader;
    private List<User> backers;
}
