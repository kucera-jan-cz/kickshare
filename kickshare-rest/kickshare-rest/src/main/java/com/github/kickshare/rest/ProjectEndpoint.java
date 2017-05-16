package com.github.kickshare.rest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.db.multischema.SchemaContextHolder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
@RestController
public class ProjectEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectEndpoint.class);

    private final KickshareRepository repository;
    private final ProjectService kickstarter;
    private final ExtendedMapper dozer;

    public ProjectEndpoint(final KickshareRepository repository, final ProjectService kickstarter, ExtendedMapper dozer) {
        this.repository = repository;
        this.kickstarter = kickstarter;
        this.dozer = dozer;
    }

    @GetMapping("/kickstarter/search")
    public List<ProjectInfo> searchKickstarterProjects(
            @RequestParam final String name,
            @RequestParam(defaultValue = "34") final Integer categoryId,
            @RequestParam(defaultValue = "false") final boolean store) throws IOException {
        Validate.inclusiveBetween(3, 100, StringUtils.length(name), "Name parameter must be at least 3 characters long");
        List<ProjectInfo> projects = searchKickstarter(name, categoryId);
        if(!projects.isEmpty() && store) {
            LOGGER.debug("Storing searched projects asynchronously");
            final String schema = SchemaContextHolder.getSchema();
            CompletableFuture.runAsync(() -> {
                SchemaContextHolder.setSchema(schema);
                try {
                    repository.saveProjects(projects);
                    LOGGER.debug("Successfully saved projects");
                } catch (RuntimeException ex) {
                    LOGGER.warn("Failed to save projects: ", ex);
                }
            });
        }
        LOGGER.debug("Found projects: {}", projects);
        return projects;
    }

    @GetMapping("/projects/search")
    public List<ProjectInfo> searchProjects(@RequestParam final String name, @RequestParam final Integer categoryId, @RequestParam(defaultValue = "false") final Boolean useKickstarter) throws IOException {
        Validate.inclusiveBetween(3, 100, StringUtils.length(name), "Name parameter must be at least 3 characters long");
        List<ProjectInfo> projects = repository.findProjectInfoByName(name);
        LOGGER.debug("Found projects: {}", projects);
        if(useKickstarter && projects.isEmpty()) {
            return searchKickstarter(name, categoryId);
        }
        return projects;
    }

    @GetMapping("/projects/{projectId}/projectInfo")
    public ProjectInfo getProjectInfo(@PathVariable Long projectId) {
        return repository.findProjectInfo(projectId);
    }

    @GetMapping("/projects/{projectId}/groupInfos")
    public List<GroupInfo> getGroupInfos(@PathVariable Long projectId) {
        return repository.findAllGroupInfo(projectId);
    }

    private List<ProjectInfo> searchKickstarter(final String name, @RequestParam final Integer categoryId) throws IOException {
        final List<ProjectInfo> projects = dozer.map(kickstarter.findProjects(name, categoryId), ProjectInfo.class);
        LOGGER.debug("Found kickstarter projects: \n{}", projects);
        return projects;
    }
}
