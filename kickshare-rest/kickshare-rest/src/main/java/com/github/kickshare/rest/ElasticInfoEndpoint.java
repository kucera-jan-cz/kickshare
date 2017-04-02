package com.github.kickshare.rest;

import com.github.kickshare.db.dao.ProjectRepository;
import com.github.kickshare.service.SearchService;
import com.github.kickshare.service.entity.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 14.3.2017
 */
@RestController("elasticInfoEndpoint")
public class ElasticInfoEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticInfoEndpoint.class);

    private SearchService searchService;
    private ProjectRepository projectRepository;

    @Autowired
    public ElasticInfoEndpoint(final SearchService searchService, final ProjectRepository projectRepository) {
        this.searchService = searchService;
        this.projectRepository = projectRepository;
    }

    @RequestMapping("/elastic/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("No Elastic yet");
    }

    @GetMapping("/cities/{id}")
    public City getCityById(@PathVariable String id) {
        return searchService.getCityById(id);
    }

//    @GetMapping("/projects/{id}")
//    public Project getProject(@PathVariable Long id) {
//        Project project = projectRepository.findOne(id);
//        return project;
//    }
}
