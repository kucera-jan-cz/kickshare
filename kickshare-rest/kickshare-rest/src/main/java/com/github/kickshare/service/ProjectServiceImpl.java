package com.github.kickshare.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.db.dao.ProjectRepository;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.domain.Project;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.mapper.ExtendedMapper;
import lombok.AllArgsConstructor;
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
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private static final Class<com.github.kickshare.db.jooq.tables.pojos.Project> DB_TYPE = com.github.kickshare.db.jooq.tables.pojos.Project.class;
    private static final List<com.github.kickshare.db.jooq.tables.pojos.Project> EMPTY = Collections.emptyList();

    private ProjectRepository projectRepository;
    private com.github.kickshare.kickstarter.ProjectService ksService;
    private ExtendedMapper mapper;
    private KickshareRepository repository;


    @Override
    public Long registerProject(Project project) throws IOException {
//        Predicate<Project> equalFilter = p -> p.getId().equals(project.getId()) && p.getName()
//                .equals(project.getName());
//        if (projectRepository.existsById(project.getId())) {
//            LOGGER.debug("Project {} with ID {} exists", project.getName(), project.getId());
//            return project.getId();
//        } else {
//            Project ksProject = ksService.findProjects(project.getName(), "Games").stream().filter(equalFilter)
//                    .findAny().get();
//            return projectRepository.createReturningKey(mapper.map(ksProject, DB_TYPE));
//        }
        return 1L;
    }

    @Override
    @Transactional
    public List<GroupInfo> findAllGroupInfo(final Long projectId) {
        return repository.findAllGroupInfo(projectId);
    }

    public List<Project> findProjects(String text) throws ExecutionException, InterruptedException {
//        List<com.github.kickshare.db.jooq.tables.pojos.Project> dbItems = projectRepository.findProjects();
//        List<Long> dbIds = dbItems.stream().map(com.github.kickshare.db.jooq.tables.pojos.Project::getId).collect(Collectors.toList());
//        CompletableFuture<List<com.github.kickshare.db.jooq.tables.pojos.Project>> ks2db
//                = CompletableFuture.supplyAsync(() -> ksService.findProjects()).thenApply(this::listTransform);
//        List<com.github.kickshare.db.jooq.tables.pojos.Project> ksItems = withDefault(ks2db, EMPTY, Duration.ofSeconds(1)).get();
//
//
//        List<com.github.kickshare.db.jooq.tables.pojos.Project> newItems = ksItems.stream().filter(i -> !dbIds.contains(i.getId())).collect(Collectors.toList());
//        dbItems.addAll(newItems);
//
//        CompletableFuture.supplyAsync(() -> projectRepository.save(newItems));
//
//        return mapper.convertValue(dbItems, new TypeReference<List<Project>>() {
//        });
        return Collections.emptyList();
    }


//    private List<com.github.kickshare.db.jooq.tables.pojos.Project> listTransform(List<Project> list) {
//        List<com.github.kickshare.db.jooq.tables.pojos.Project> db =
//                list.stream().map(this::dbTransform)
//                        .filter(Optional::isPresent)
//                        .map(Optional::get)
//                        .collect(Collectors.toList());
//        return db;
//    }
//
//    private Optional<com.github.kickshare.db.jooq.tables.pojos.Project> dbTransform(final Project project) {
//        try {
//            com.github.kickshare.db.jooq.tables.pojos.Project transformed = mapper.convertValue(project, com.github.kickshare.db.jooq.tables.pojos.Project.class);
//            return Optional.of(transformed);
//        } catch (RuntimeException ex) {
//            LOGGER.warn("Object mapping failed: ", ex);
//            return Optional.empty();
//        }
//    }
//
//    public static <T> CompletableFuture<T> withDefault(final CompletableFuture<T> cf, final T defaultValue, final Duration timeout) {
//        return (CompletableFuture<T>) CompletableFuture.anyOf(
//                cf.exceptionally(ignoredException -> defaultValue),
//                delayedValue(defaultValue, timeout));
//    }
//
//    public static <T> CompletableFuture<T> delayedValue(final T value, final Duration delay) {
//        final CompletableFuture<T> result = new CompletableFuture<>();
//        EXECUTOR.schedule(() -> result.complete(value),
//                delay.toMillis(), TimeUnit.MILLISECONDS);
//        return result;
//    }

    @Transactional
    @Override
    public List<ProjectInfo> searchGroups(SearchOptions options) throws IOException {
        List<Project> projects = mapper.map(this.repository.searchProjects(options), Project.class);
        return projects.stream().map(p -> new ProjectInfo(null, p, null)).collect(Collectors.toList());
    }
}
