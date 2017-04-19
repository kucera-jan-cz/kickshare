package com.github.kickshare.kickstarter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.github.kickshare.domain.Project;

/**
 * @author Jan.Kucera
 * @since 23.3.2017
 */

public interface ProjectService {

    List<Project> findProjects() throws IOException;

    List<Project> findProjects(String term, String category) throws IOException;

    Optional<Project> findById(Long id) throws IOException;
}