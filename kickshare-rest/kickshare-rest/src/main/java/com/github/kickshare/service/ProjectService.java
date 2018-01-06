package com.github.kickshare.service;

import java.io.IOException;
import java.util.List;

import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.service.entity.SearchOptions;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
public interface ProjectService {

    ProjectInfo findProjectInfo(Long projectId);

    void saveProjects(final List<ProjectInfo> projects);

    List<ProjectInfo> searchProjects(SearchOptions options) throws IOException;

    List<ProjectInfo> findProjectInfoByName(final Integer categoryId, final String name);
}

