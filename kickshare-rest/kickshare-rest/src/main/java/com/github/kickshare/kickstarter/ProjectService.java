package com.github.kickshare.kickstarter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.github.kickshare.kickstarter.entity.Project;
import com.github.kickshare.kickstarter.exception.AuthenticationException;


/**
 * @author Jan.Kucera
 * @since 23.3.2017
 */

public interface ProjectService {

    List<Project> findProjects() throws IOException;

    //@TODO - consider using enum for categories
    List<Project> findProjects(String term, Integer category) throws IOException;

    Optional<Project> findById(Long id) throws IOException;

    Long verify(String user, String password) throws IOException, AuthenticationException;
}
