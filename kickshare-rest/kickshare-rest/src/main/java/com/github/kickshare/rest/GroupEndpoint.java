package com.github.kickshare.rest;

import static com.github.kickshare.mapper.EntityMapper.map;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.github.kickshare.db.dao.KickshareRepository;
import com.github.kickshare.db.jooq.enums.GroupRequestStatusDB;
import com.github.kickshare.db.pojos.CityGridDB;
import com.github.kickshare.db.util.SeekPageRequest;
import com.github.kickshare.domain.Backer;
import com.github.kickshare.domain.Group;
import com.github.kickshare.domain.GroupDetail;
import com.github.kickshare.domain.Post;
import com.github.kickshare.domain.Rating;
import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.security.permission.GroupMember;
import com.github.kickshare.security.permission.GroupOwner;
import com.github.kickshare.service.GroupServiceImpl;
import com.github.kickshare.service.entity.SearchOptions;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @Deprecated
    public FeatureCollection getData(
            @RequestParam String callback,
            @RequestParam(required = false) Float lat,
            @RequestParam(required = false) Float lon) {
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
        SearchOptions options = SearchOptions.toOptions(params);
        final List<CityGridDB> cityGrids = repository.searchCityGrid(map().toDB(options));
        FeatureCollection collection = new FeatureCollection();
        collection.addAll(cityGrids.stream().map(GroupEndpoint::point).collect(Collectors.toList()));
        return collection;
    }

    @GetMapping("/search")
    public List<GroupDetail> searchGroups(@RequestParam Map<String, String> params) {
        SearchOptions options = SearchOptions.toOptions(params);
        return groupService.searchGroups(options);
    }

    @GetMapping("/suggest")
    public String suggestGroupName(@RequestParam Long projectId, @RequestParam Integer cityId, @AuthenticationPrincipal BackerDetails user) {
        return groupService.suggestGroupName(projectId, cityId);
    }

    @PostMapping
    public Group create(@RequestBody @Valid Group group, @AuthenticationPrincipal BackerDetails user) {
        final Long projectId = group.getProjectId();
        final String name = group.getName();
        final boolean isLocal = group.getIsLocal();
        Long groupId = groupService.createGroup(projectId, name, user.getId(), isLocal, group.getLimit());
        return groupService.getGroup(groupId);
    }

    @PostMapping("/{groupId}/users/{backerId}")
    public void registerParticipant(@PathVariable Long groupId, @PathVariable Long backerId, @AuthenticationPrincipal BackerDetails user) {
        //@TODO - validation is missing here
        Validate.isTrue(user.getId().equals(backerId), "Invalid backer id");
        groupService.registerBacker(groupId, user.getId());
    }

    @GroupMember
    @DeleteMapping("/{groupId}/users/{backerId}")
    public void deleteParticipant(@PathVariable Long groupId, @PathVariable Long backerId, @AuthenticationPrincipal BackerDetails user) {
        //@TODO - implement validation for group owner and closing down group when money has been send
        Group group = groupService.getGroup(groupId);
        boolean isLeader = group.getLeaderId().equals(user.getId());
        boolean isMember = user.getId().equals(backerId);
        Validate.isTrue(isLeader || isMember, "Illegal request - not leader nor backer id fit");
        groupService.removeBacker(groupId, backerId);
    }

    @GroupOwner
    @PostMapping("/{groupId}/users/{backerId}/approve")
    public void approveBacker(@PathVariable Long groupId, @PathVariable Long backerId, @AuthenticationPrincipal BackerDetails user) {
        Validate.isTrue(groupService.isGroupOwner(user.getId(), groupId), "Access violation: no rights for altering group");
        groupService.updateGroupRequestStatus(groupId, backerId, GroupRequestStatusDB.APPROVED);
    }

    @GroupOwner
    @PostMapping("/{groupId}/users/{backerId}/decline")
    public void declineBacker(@PathVariable Long groupId, @PathVariable Long backerId, @AuthenticationPrincipal BackerDetails user) {
        Validate.isTrue(groupService.isGroupOwner(user.getId(), groupId), "Access violation: no rights for altering group");
        groupService.updateGroupRequestStatus(groupId, backerId, GroupRequestStatusDB.DECLINED);
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

    @GetMapping("/{groupId}/users/requests")
    public List<Backer> getUserRequests(@PathVariable Long groupId, @AuthenticationPrincipal BackerDetails user) {
        LOGGER.info("{}", user);
        return groupService.getGroupUserRequests(groupId);
    }

    @GroupMember
    @PutMapping("/{groupId}/users/{userId}/rate")
    public ResponseEntity<?> createRating(@PathVariable Long groupId, @PathVariable Long userId, @RequestBody @Valid Rating rating,
            @AuthenticationPrincipal BackerDetails user) {
        Validate.isTrue(!user.getId().equals(userId), "User can't rate himself!");
        this.groupService.rate(user.getId(), groupId, userId, rating.getRating(), rating.getMessage());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GroupMember
    @DeleteMapping("/{groupId}/users/{userId}/rate")
    public ResponseEntity<?> deleteRating(@PathVariable Long groupId, @PathVariable Long userId, @AuthenticationPrincipal BackerDetails user) {
        Validate.isTrue(!user.getId().equals(userId), "User can't delete his ratings!");
        this.groupService.deleteRate(user.getId(), groupId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GroupMember
    @GetMapping("/{groupId}/posts")
    public Page<Post> getPosts(@PathVariable Long groupId,
            //@ModelAttribute final
             SeekPageRequest seekPage,
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

    public static Feature point(final CityGridDB city) {
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
