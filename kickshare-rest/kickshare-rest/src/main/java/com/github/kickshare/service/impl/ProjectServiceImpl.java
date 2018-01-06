package com.github.kickshare.service.impl;

import static com.github.kickshare.db.jooq.Tables.CATEGORY;
import static com.github.kickshare.db.jooq.Tables.PROJECT;
import static com.github.kickshare.mapper.EntityMapper.photo;
import static com.github.kickshare.mapper.EntityMapper.project;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.github.kickshare.db.dao.ProjectRepository;
import com.github.kickshare.db.jooq.tables.daos.ProjectPhotoDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.ProjectPhotoDB;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.service.ProjectService;
import com.github.kickshare.service.entity.SearchOptions;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private ProjectRepository projectRepository;
    private ProjectPhotoDaoDB photoDao;
    private DSLContext dsl;

    @Override
    @Transactional
    public ProjectInfo findProjectInfo(Long projectId) {
        final com.github.kickshare.db.jooq.tables.pojos.ProjectDB project = projectRepository.findById(projectId);
        final ProjectPhotoDB projectPhoto = photoDao.fetchOneByProjectIdDB(projectId);
        ProjectInfo info = new ProjectInfo();
        info.setProject(project().toDomain(project));
        info.setPhotoUrl(projectPhoto.getThumb());
        info.setPhoto(photo().toDomain(projectPhoto));
        return info;
    }

    @Override
    @Transactional
    public void saveProjects(final List<ProjectInfo> projects) {
        for (ProjectInfo info : projects) {
            Long id = projectRepository.createReturningKey(project().toDB(info.getProject()));
            com.github.kickshare.db.jooq.tables.pojos.ProjectPhotoDB photo = photo().toDB(info.getPhoto());
            photo.setProjectId(id);
            photoDao.insert(photo);
        }
    }

    @Transactional
    @Override
    public List<ProjectInfo> searchProjects(SearchOptions options) throws IOException {
        final List<com.github.kickshare.db.jooq.tables.pojos.ProjectDB> projects = projectRepository.searchProjects(options);
        final List<ProjectInfo> infos = projects.stream().map(this::toProjectInfo).collect(Collectors.toList());
        return infos;
    }

    @Override
    @Transactional
    public List<ProjectInfo> findProjectInfoByName(final Integer categoryId, final String name) {
        final List<com.github.kickshare.db.jooq.tables.pojos.ProjectDB> projects = searchProjectsByName(categoryId, name);
        final List<ProjectInfo> infos = projects.stream().map(this::toProjectInfo).collect(Collectors.toList());
        return infos;
    }

    private ProjectInfo toProjectInfo(com.github.kickshare.db.jooq.tables.pojos.ProjectDB project) {
        final ProjectPhotoDB projectPhoto = photoDao.fetchOneByProjectIdDB(project.getId());
        ProjectInfo info = new ProjectInfo();
        info.setProject(project().toDomain(project));
        info.setPhotoUrl(projectPhoto.getThumb());
        info.setPhoto(photo().toDomain(projectPhoto));
        return info;
    }


    private List<com.github.kickshare.db.jooq.tables.pojos.ProjectDB> searchProjectsByName(Integer categoryId, String name) {
        return dsl.select(PROJECT.fields())
                .from(PROJECT)
                .join(CATEGORY).on(CATEGORY.ID.eq(PROJECT.CATEGORY_ID))
                .where(PROJECT.NAME.like('%' + name + '%'))
                .and(
                        CATEGORY.ID.eq(categoryId).or(CATEGORY.PARENT_ID.eq(categoryId))
                )

                .fetchInto(com.github.kickshare.db.jooq.tables.pojos.ProjectDB.class);
    }
}
