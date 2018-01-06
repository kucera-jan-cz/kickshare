package com.github.kickshare.rest;

import static com.github.kickshare.mapper.EntityMapper.project;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.github.kickshare.domain.GroupSummary;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.ext.service.kickstarter.campaign.KickstarterCampaignService;
import com.github.kickshare.service.GroupServiceImpl;
import com.github.kickshare.service.entity.SearchOptions;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ProjectEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectEndpoint.class);

    private final KickstarterCampaignService kickstarter;
    private final com.github.kickshare.service.ProjectService projectService;
    private final GroupServiceImpl groupService;


    @GetMapping
    public List<ProjectInfo> searchProjects(@RequestParam final String name, @RequestParam(name="category_id") final Integer categoryId,
            @RequestParam(defaultValue = "false") final Boolean useKickstarter) throws IOException {
        Validate.inclusiveBetween(3, 100, StringUtils.length(name), "Name parameter must be at least 3 characters long");
        List<ProjectInfo> projects = projectService.findProjectInfoByName(categoryId, name);
        LOGGER.debug("Found projects: {}", projects);
        if (useKickstarter && projects.isEmpty()) {
            return searchKickstarter(name, categoryId);
        }
        return projects;
    }

    @GetMapping("/search")
    public List<ProjectInfo> searchProjects(@RequestParam Map<String, String> params) throws IOException {
        final SearchOptions options = SearchOptions.toOptions(params);
        return projectService.searchProjects(options);
    }

    @GetMapping("/{projectId}")
    public ProjectInfo getProject(@PathVariable Long projectId) {
        return projectService.findProjectInfo(projectId);
    }

    @GetMapping("/{projectId}/groups")
    public List<GroupSummary> getGroups(@PathVariable Long projectId) {
        return groupService.findAllGroupInfo(projectId);
    }

    private List<ProjectInfo> searchKickstarter(final String name, @RequestParam final Integer categoryId) throws IOException {
        final List<ProjectInfo> projects = project().toDomain(kickstarter.findProjects(name, categoryId));
        LOGGER.debug("Found kickstarter projects: \n{}", projects);
        return projects;
    }
}
