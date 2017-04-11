package com.github.kickshare.rest;

import java.util.List;

import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.ProjectInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectEndpoint {
    private KickshareRepository repository;

    @GetMapping("/{projectId}/projectInfo")
    public ProjectInfo getProjectInfo(@PathVariable Long projectId) {
        return repository.findProjectInfo(projectId);
    }

    @GetMapping("/{projectId}/groupInfos")
    public List<GroupInfo> getGroupInfos(@PathVariable Long projectId) {
        return repository.findAllGroupInfo(projectId);
    }
}
