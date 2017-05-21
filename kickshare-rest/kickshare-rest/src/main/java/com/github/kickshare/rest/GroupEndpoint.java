package com.github.kickshare.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.github.kickshare.db.dao.GroupRepository;
import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.db.h2.tables.Backer;
import com.github.kickshare.domain.Group;
import com.github.kickshare.domain.GroupInfo;
import com.github.kickshare.mapper.ExtendedMapper;
import com.github.kickshare.rest.group.domain.CreateGroupRequest;
import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.service.GeoBoundary;
import com.github.kickshare.service.GroupSearchOptions;
import com.github.kickshare.service.GroupServiceImpl;
import com.github.kickshare.service.Location;
import com.github.kickshare.service.ProjectService;
import com.github.kickshare.service.entity.CityGrid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@RestController
@RequestMapping("/groups")
@AllArgsConstructor
public class GroupEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupEndpoint.class);
    private static final Feature[] POINTS = {
            point(49.1951F, 16.6068F, "Brno", true),
            point(49.1951F, 16.6068F, "Brno", false),
            point(49.1951F, 16.6068F, "Brno", false),
            point(50.083333F, 14.416667F, "Praha", false),
            point(49.0520F, 15.8086F, "České Budějovice", true),
            point(49.5955F, 17.25175F, "Olomouc", false),
            point(49.83332F, 18.25F, "Ostrava", true),
            point(49.33818F, 15.0043F, "Benešov", false),
            point(50.16667F, 13F, "Karlovy Vary", true),
    };
    public static final long USER_ID = 1L;
    //Search for groups using user's location, using distance near (slider), tags, potentially campaign's name
    private ProjectService projectService;
    private GroupRepository groupRepository;
    private KickshareRepository repository;
    private ExtendedMapper dozer;
    private GroupServiceImpl groupService;

    @RequestMapping(value = "/search/jsonp", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeatureCollection getData(
            @RequestParam String callback,
            @RequestParam(required = false) Float lat,
            @RequestParam(required = false) Float lon) {
        LOGGER.warn("{}: {}", lat, lon);
        int items = RandomUtils.nextInt(1, POINTS.length);
        FeatureCollection collection = new FeatureCollection();
        for (int i = 0; i < items; i++) {
            collection.add(POINTS[RandomUtils.nextInt(0, POINTS.length)]);
        }
        return collection;
    }

    @RequestMapping(value = "/search/data.jsonp", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeatureCollection getData(
            @RequestParam String callback,
            @RequestParam Map<String, String> params
    ) throws IOException {
        GroupSearchOptions options = toOptions(params);
        final List<CityGrid> cityGrids = repository.searchCityGrid(options);
        FeatureCollection collection = new FeatureCollection();
        collection.addAll(cityGrids.stream().map(GroupEndpoint::point).collect(Collectors.toList()));
        return collection;
    }

    @PostMapping
    public Long create(@RequestBody @Valid Group group, @AuthenticationPrincipal BackerDetails user) throws IOException {
        final Long projectId = group.getProjectId();
        final String name = group.getName();
        final boolean isLocal = group.getIsLocal();
        return groupService.createGroup(projectId, name, user.getId(), isLocal);
    }

    //@TODO - simple post would be better
    @PostMapping("/create")
    public Long createGroup(@RequestBody @Valid CreateGroupRequest request,
            @AuthenticationPrincipal BackerDetails user) throws IOException {
        LOGGER.info("{}", user);
        Long projectId = projectService.registerProject(request.getProject());
        com.github.kickshare.db.h2.tables.pojos.Group group = new com.github.kickshare.db.h2.tables.pojos.Group(null, user.getId(), projectId,
                request.getName(), null, null, true);
        return groupRepository.createReturningKey(group);
    }

    @PostMapping("/{groupId}/users")
    public void registerParticipant(@PathVariable Long groupId) {
        groupRepository.registerUser(groupId, USER_ID);
    }

    @GetMapping
    public List<Group> getByProjectId(@RequestParam("project_id") Long projectId) {
//        return dozer.map(groupRepository.findAllByProjectId(projectId), ;
        return null;
    }

    @GetMapping("/{groupId}")
    public GroupInfo getUsers(@PathVariable Long groupId) {
        return groupRepository.getGroupInfo(groupId);
    }

    @GetMapping("/{groupId}/users")
    public List<Backer> getUsers(@PathVariable Long groupId,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User customUser) {
        LOGGER.info("{}", customUser);
        List<com.github.kickshare.db.h2.tables.pojos.Backer> list = groupRepository.findAllUsers(groupId);
        List<Backer> users = dozer.map(list, Backer.class);
        return users;
    }

    /**
     export interface Group {
     name: string
     project_id: number
     group_id: number
     leader_name: string
     leader_rating: number
     is_local: boolean
     participant_count: number
     }
     * @param params
     * @return
     */

    private GroupSearchOptions toOptions(Map<String, String> params) {
        GroupSearchOptions.GroupSearchOptionsBuilder builder = GroupSearchOptions.builder();
        builder.searchLocalOnly(Boolean.valueOf(params.get("only_local")));
        builder.projectName(params.get("name"));
        Location leftTop = new Location(
                Float.parseFloat(params.get("ne_lat")),
                Float.parseFloat(params.get("ne_lon"))
        );
        Location rightBottom = new Location(
                Float.parseFloat(params.get("sw_lat")),
                Float.parseFloat(params.get("sw_lon"))
        );
        builder.geoBoundary(new GeoBoundary(leftTop, rightBottom));
        return builder.build();
    }

    public static Feature point(final CityGrid city) {
        Feature feature = new Feature();
        feature.setGeometry(new Point(city.getLocation().getLon(), city.getLocation().getLat()));
        feature.setProperty("type", city.getType().name());
        return feature;
    }

    private static Feature point(Float lat, Float lon, String name, boolean isLocal) {
        Feature feature = new Feature();
        feature.setGeometry(new Point(lon, lat));
        String type = (isLocal) ? "LOCAL" : "GLOBAL";
        feature.setProperty("type", type);
        return feature;
    }

}
