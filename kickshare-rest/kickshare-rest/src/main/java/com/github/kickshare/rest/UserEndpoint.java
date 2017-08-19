package com.github.kickshare.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.kickshare.db.multischema.SchemaContextHolder;
import com.github.kickshare.domain.Backer;
import com.github.kickshare.domain.Group;
import com.github.kickshare.domain.Notification;
import com.github.kickshare.rest.user.domain.UserInfo;
import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.service.GroupServiceImpl;
import com.github.kickshare.service.NotificationService;
import com.github.kickshare.service.UserService;
import com.github.kickshare.service.sse.SSENotificationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);
    private SSENotificationService sseNotificationService;
    private NotificationService notificationService;
    private GroupServiceImpl groupService;
    private UserService userService;

    @GetMapping("/{userId}/groups")
    public List<Group> getUserGroups(@PathVariable final Long userId) {
        return groupService.getUserGroups(userId);
    }

    @GetMapping("/verify/{token}")
    public void verifyUser(@PathVariable final String token, final HttpServletResponse response) {
        if (userService.verifyUser(token)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.encodeRedirectURL("/authenticate");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(path = "/{userId}/notifications/stream", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public SseEmitter stream(@AuthenticationPrincipal BackerDetails user) {
        String country = SchemaContextHolder.getSchema();
        return sseNotificationService.register(country, user.getId());
    }

    @RequestMapping(path = "/{userId}/notifications/latest", method = RequestMethod.GET)
    public List<Notification> getLastNotifications(@AuthenticationPrincipal BackerDetails user) {
        //@TODO implement paging?
        return notificationService.getUserNotification(user.getId());
    }

    @PostMapping
    public UserDetails createUser(@RequestBody UserInfo user) {
        Backer backer = new Backer(null, user.getEmail(), user.getName(), user.getSurname(), null, null);
        return userService.createUser(backer, user.getPassword(), user.getAddress());
    }


}
