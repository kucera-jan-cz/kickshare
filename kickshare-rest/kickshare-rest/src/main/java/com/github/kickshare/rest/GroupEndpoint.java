package com.github.kickshare.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.db.jooq.enums.GroupRequestStatus;
import com.github.kickshare.domain.Backer;
import com.github.kickshare.domain.Group;
import com.github.kickshare.domain.GroupDetail;
import com.github.kickshare.domain.Post;
import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.security.permission.GroupMember;
import com.github.kickshare.security.permission.GroupOwner;
import com.github.kickshare.service.GroupSearchOptions;
import com.github.kickshare.service.GroupServiceImpl;
import com.github.kickshare.service.entity.CityGrid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private KickshareRepository repository;
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
        GroupSearchOptions options = GroupSearchOptions.toOptions(params);
        final List<CityGrid> cityGrids = repository.searchCityGrid(options);
        FeatureCollection collection = new FeatureCollection();
        collection.addAll(cityGrids.stream().map(GroupEndpoint::point).collect(Collectors.toList()));
        return collection;
    }

    @GetMapping("/search")
    public List<GroupDetail> searchGroups(@RequestParam Map<String, String> params) {
        GroupSearchOptions options = GroupSearchOptions.toOptions(params);
        return groupService.searchGroups(options);
    }


    @PostMapping
    public Group create(@RequestBody @Valid Group group, @AuthenticationPrincipal BackerDetails user) throws IOException {
        final Long projectId = group.getProjectId();
        final String name = group.getName();
        final boolean isLocal = group.getIsLocal();
        Long groupId = groupService.createGroup(projectId, name, user.getId(), isLocal, group.getLimit());
        return groupService.getGroup(groupId);
    }

    @PostMapping("/{groupId}/users")
    public void registerParticipant(@PathVariable Long groupId, @AuthenticationPrincipal BackerDetails user) {
        groupService.registerBacker(groupId, user.getId());
    }

    @GroupOwner
    @PostMapping("/{groupId}/users/{backerId}/approve")
    public void approveBacker(@PathVariable Long groupId, @PathVariable Long backerId, @AuthenticationPrincipal BackerDetails user) {
        Validate.isTrue(groupService.isGroupOwner(user.getId(), groupId), "Access violation: no rights for altering group");
        groupService.updateGroupRequestStatus(groupId, backerId, GroupRequestStatus.APPROVED);
    }

    @GroupOwner
    @PostMapping("/{groupId}/users/{backerId}/decline")
    public void declineBacker(@PathVariable Long groupId, @PathVariable Long backerId, @AuthenticationPrincipal BackerDetails user) {
        Validate.isTrue(groupService.isGroupOwner(user.getId(), groupId), "Access violation: no rights for altering group");
        groupService.updateGroupRequestStatus(groupId, backerId, GroupRequestStatus.DECLINED);
    }

    @GetMapping("/{groupId}")
    public GroupDetail getGroupInfo(@PathVariable Long groupId) {
        return groupService.getGroupDetail(groupId);
    }

    @GroupMember
    @GetMapping("/{groupId}/users")
    public List<Backer> getUsers(@PathVariable Long groupId, @AuthenticationPrincipal BackerDetails user) {
        LOGGER.info("{}", user);
        return groupService.getGroupUsers(groupId);
    }

    @GroupMember
    @GetMapping("/{groupId}/posts")
    public Page<Post> getPosts(@PathVariable Long groupId,
            //@ModelAttribute final SeekPageRequest<Long> pageInfo,
            Pageable pageInfo,
            @AuthenticationPrincipal BackerDetails user) {
        LOGGER.info("{}", user);
        LOGGER.info("{}", pageInfo);
        return this.groupService.getPosts(groupId, pageInfo);
    }

    @GroupMember
    @PostMapping("/{groupId}/posts")
    public Post createPost(@PathVariable Long groupId, @AuthenticationPrincipal BackerDetails user, @RequestBody @Valid Post post) {
        post.setBackerId(user.getId());
        post.setGroupId(groupId);
        return this.groupService.insertPost(post);
    }

    @GroupMember
    @PatchMapping("/{groupId}/posts/{postId}")
    public ResponseEntity<?> update(@PathVariable Long groupId, @PathVariable Long postId, @RequestBody @Valid Post post) {
        post.setPostId(postId);
        this.groupService.updatePost(post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
