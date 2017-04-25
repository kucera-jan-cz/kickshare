package com.github.kickshare.rest;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.kickstarter.ProjectService;
import com.github.kickshare.mapper.ExtendedMapper;
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
    private final ExtendedMapper dozer;

    public ProjectEndpoint(final KickshareRepository repository, final ProjectService kickstarter, ExtendedMapper dozer) {
        this.repository = repository;
        this.kickstarter = kickstarter;
        this.dozer = dozer;
    }


    @GetMapping("/search")
    public List<ProjectInfo> searchProjects(@RequestParam final String name, @RequestParam final Integer categoryId) throws IOException {
        List<ProjectInfo> projects = dozer.map(kickstarter.findProjects(name, categoryId), ProjectInfo.class);
        LOGGER.debug("Found projects: {}", projects);
        return projects;
    }

    @GetMapping("/{projectId}/projectInfo")
    public ProjectInfo getProjectInfo(@PathVariable Long projectId) {
        return repository.findProjectInfo(projectId);
    }

    @GetMapping("/{projectId}/groupInfos")
    public List<GroupInfo> getGroupInfos(@PathVariable Long projectId) {
        return repository.findAllGroupInfo(projectId);
    }
}
