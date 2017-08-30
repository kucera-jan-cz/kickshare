package com.github.kickshare.service.impl;

import static com.github.kickshare.db.jooq.Tables.PROJECT;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.db.dao.ProjectRepository;
import com.github.kickshare.db.jooq.tables.daos.ProjectPhotoDao;
import com.github.kickshare.db.jooq.tables.pojos.ProjectPhoto;
import com.github.kickshare.domain.Project;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.mapper.ExtendedMapper;
import com.github.kickshare.mapper.ProjectPhotoMapper;
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
    private ProjectPhotoDao photoDao;
    private ExtendedMapper mapper;
    private DSLContext dsl;
    private KickshareRepository repository;


    @Override
    public Long registerProject(Project project) throws IOException {
//        Predicate<CampaignProject> equalFilter = p -> p.getId().equals(project.getId()) && p.getName()
//                .equals(project.getName());
//        if (projectRepository.existsById(project.getId())) {
//            LOGGER.debug("CampaignProject {} with ID {} exists", project.getName(), project.getId());
//            return project.getId();
//        } else {
//            CampaignProject ksProject = ksService.findProjects(project.getName(), "Games").stream().filter(equalFilter)
//                    .findAny().get();
//            return projectRepository.createReturningKey(mapper.map(ksProject, DB_TYPE));
//        }
        return 1L;
    }

    @Override
    @Transactional
    public ProjectInfo findProjectInfo(Long projectId) {
        final com.github.kickshare.db.jooq.tables.pojos.Project project = projectRepository.findById(projectId);
        final ProjectPhoto projectPhoto = photoDao.fetchOneByProjectId(projectId);
        ProjectInfo info = new ProjectInfo();
        info.setProject(mapper.map(project, com.github.kickshare.domain.Project.class));
        info.setPhotoUrl(projectPhoto.getThumb());
        info.setPhoto(mapper.map(projectPhoto, com.github.kickshare.domain.ProjectPhoto.class));
        return info;
    }

    @Override
    @Transactional
    public void saveProjects(final List<ProjectInfo> projects) {
        for (ProjectInfo info : projects) {
            com.github.kickshare.db.jooq.tables.pojos.Project project = mapper.map(info.getProject(), com.github.kickshare.db.jooq.tables.pojos.Project.class);
            Long id = projectRepository.createReturningKey(project);
            com.github.kickshare.db.jooq.tables.pojos.ProjectPhoto photo = ProjectPhotoMapper.INSTANCE.toDB(info.getPhoto());
            photo.setProjectId(id);
            photoDao.insert(photo);
        }
    }

    @Transactional
    @Override
    public List<ProjectInfo> searchProjects(SearchOptions options) throws IOException {
        final List<com.github.kickshare.db.jooq.tables.pojos.Project> projects = repository.searchProjects(options);
        final List<ProjectInfo> infos = projects.stream().map(this::toProjectInfo).collect(Collectors.toList());
        return infos;
    }

    @Override
    @Transactional
    public List<ProjectInfo> findProjectInfoByName(final String name) {
        final List<com.github.kickshare.db.jooq.tables.pojos.Project> projects = searchProjectsByName(name);
        final List<ProjectInfo> infos = projects.stream().map(this::toProjectInfo).collect(Collectors.toList());
        return infos;
    }

    private ProjectInfo toProjectInfo(com.github.kickshare.db.jooq.tables.pojos.Project project) {
        final ProjectPhoto projectPhoto = photoDao.fetchOneByProjectId(project.getId());
        ProjectInfo info = new ProjectInfo();
        info.setProject(mapper.map(project, com.github.kickshare.domain.Project.class));
        info.setPhotoUrl(projectPhoto.getThumb());
        info.setPhoto(ProjectPhotoMapper.INSTANCE.toDomain(projectPhoto));
        return info;
    }


    private List<com.github.kickshare.db.jooq.tables.pojos.Project> searchProjectsByName(String name) {
        return dsl.select().from(PROJECT).where(PROJECT.NAME.like('%' + name + '%')).fetchInto(com.github.kickshare.db.jooq.tables.pojos.Project.class);
    }
}
