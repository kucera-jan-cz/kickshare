package com.github.kickshare.rest.group.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.github.kickshare.domain.Project;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
@Data
public class CreateGroupRequest {
    @NotNull
    @Valid
    private Project project;

    @NotNull
    @Size(min = 4)
    private String name;
}
