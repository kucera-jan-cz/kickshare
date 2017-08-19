package com.github.kickshare.rest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.github.kickshare.db.multischema.SchemaContextHolder;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.kickstarter.KickstarterCampaignService;
import com.github.kickshare.kickstarter.exception.AuthenticationException;
import com.github.kickshare.mapper.ExtendedMapper;
import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.service.GroupServiceImpl;
import com.github.kickshare.service.ProjectService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 22.5.2017
 */
@RestController
@RequestMapping("/kickstarter")
@AllArgsConstructor
public class KickstarterEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(KickstarterEndpoint.class);

    private final KickstarterCampaignService kickstarter;
    private final GroupServiceImpl groupService;
    private final ProjectService projectService;
    private final ExtendedMapper dozer;

    @GetMapping("/search")
    public List<ProjectInfo> searchKickstarterProjects(
            @RequestParam final String name,
            @RequestParam(defaultValue = "34") final Integer categoryId,
            @RequestParam(defaultValue = "false") final boolean store) throws IOException {
        Validate.inclusiveBetween(3, 100, StringUtils.length(name), "Name parameter must be at least 3 characters long");
        List<ProjectInfo> projects = searchKickstarter(name, categoryId);
        if(!projects.isEmpty() && store) {
            saveProjectsToDB(projects);
        }
        LOGGER.debug("Found projects: {}", projects);
        return projects;
    }

    @PostMapping("/backer/verify")
    public void verifyBacker(@RequestParam String email, @RequestParam String password, @AuthenticationPrincipal BackerDetails user)
            throws IOException, AuthenticationException {
        Long kickstarterId = kickstarter.verify(email, password);
        groupService.saveLeader(user.getId(), email, kickstarterId);
    }

    private void saveProjectsToDB(final List<ProjectInfo> projects) {
        LOGGER.debug("Storing searched projects asynchronously");
        final String schema = SchemaContextHolder.getSchema();
        CompletableFuture.runAsync(() -> {
            SchemaContextHolder.setSchema(schema);
            try {
                projectService.saveProjects(projects);
                LOGGER.debug("Successfully saved projects");
            } catch (RuntimeException ex) {
                LOGGER.warn("Failed to save projects: ", ex);
            }
        });
    }

    private List<ProjectInfo> searchKickstarter(final String name, @RequestParam final Integer categoryId) throws IOException {
        final List<ProjectInfo> projects = dozer.map(kickstarter.findProjects(name, categoryId), ProjectInfo.class);
        LOGGER.debug("Found kickstarter projects: \n{}", projects);
        return projects;
    }
}
