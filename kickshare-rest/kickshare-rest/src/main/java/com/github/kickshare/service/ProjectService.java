package com.github.kickshare.service;

import java.io.IOException;

import com.github.kickshare.domain.Project;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
public interface ProjectService {
    Long registerProject(Project project) throws IOException;
}
