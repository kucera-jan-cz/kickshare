package com.github.kickshare.db.dao;

import com.github.kickshare.db.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 30.3.2017
 */
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    public Project findById(Long id);
}
