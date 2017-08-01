package com.github.kickshare.service;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.Project;
import com.github.kickshare.domain.ProjectInfo;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
public interface ProjectService {
    Long registerProject(Project project) throws IOException;

    List<GroupInfo> findAllGroupInfo(Long projectId);

    List<ProjectInfo> searchGroups(SearchOptions options) throws IOException;
}

