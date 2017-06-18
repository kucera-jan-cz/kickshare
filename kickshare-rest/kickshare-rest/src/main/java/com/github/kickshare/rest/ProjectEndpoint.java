package com.github.kickshare.rest;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.kickstarter.ProjectService;
import com.github.kickshare.mapper.ExtendedMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
@RestController
@RequestMapping("/projects")
public class ProjectEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectEndpoint.class);

    private final KickshareRepository repository;
    private final ProjectService kickstarter;
    private final com.github.kickshare.service.ProjectService projectService;
    private final ExtendedMapper dozer;

    public ProjectEndpoint(final KickshareRepository repository, final ProjectService kickstarter,
            final com.github.kickshare.service.ProjectService projectService, ExtendedMapper dozer) {
        this.repository = repository;
        this.kickstarter = kickstarter;
        this.projectService = projectService;
        this.dozer = dozer;
    }


    @GetMapping("/search")
    public List<ProjectInfo> searchProjects(@RequestParam final String name, @RequestParam final Integer categoryId,
            @RequestParam(defaultValue = "false") final Boolean useKickstarter) throws IOException {
        Validate.inclusiveBetween(3, 100, StringUtils.length(name), "Name parameter must be at least 3 characters long");
        List<ProjectInfo> projects = repository.findProjectInfoByName(name);
        LOGGER.debug("Found projects: {}", projects);
        if (useKickstarter && projects.isEmpty()) {
            return searchKickstarter(name, categoryId);
        }
        return projects;
    }

    @GetMapping("/{projectId}/projectInfo")
    public ProjectInfo getProjectInfo(@PathVariable Long projectId) {
        return repository.findProjectInfo(projectId);
    }

    @GetMapping("/{projectId}/groupInfos")
    public List<GroupInfo> getGroupInfos(@PathVariable Long projectId) {
        return projectService.findAllGroupInfo(projectId);
    }

    @GetMapping("/{projectId}")
    public ProjectInfo getProject(@PathVariable Long projectId) {
        return repository.findProjectInfo(projectId);
    }

    @GetMapping("/{projectId}/groups")
    public List<GroupInfo> getGroups(@PathVariable Long projectId) {
        return repository.findAllGroupInfo(projectId);
    }

    private List<ProjectInfo> searchKickstarter(final String name, @RequestParam final Integer categoryId) throws IOException {
        final List<ProjectInfo> projects = dozer.map(kickstarter.findProjects(name, categoryId), ProjectInfo.class);
        LOGGER.debug("Found kickstarter projects: \n{}", projects);
        return projects;
    }
}
