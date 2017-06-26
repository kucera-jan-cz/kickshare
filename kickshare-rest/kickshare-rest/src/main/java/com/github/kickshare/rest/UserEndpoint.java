package com.github.kickshare.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.kickshare.db.dao.BackerRepository;
import com.github.kickshare.db.jooq.tables.daos.BackerDao;
import com.github.kickshare.db.jooq.tables.pojos.Backer;
import com.github.kickshare.domain.Group;
import com.github.kickshare.rest.user.domain.UserInfo;
import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.service.GroupServiceImpl;
import com.github.kickshare.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);
    private BackerRepository backerRepository;
    private UserDetailsManager userManager;
    private BackerDao backerDao;
    private PasswordEncoder encoder;
    private GroupServiceImpl groupService;
    private UserService userService;

    @GetMapping("/{userId}/groups")
    public List<Group> getUserGroups(@PathVariable final Long userId) {
        return groupService.getUserGroups(userId);
    }

    @GetMapping("/verify/{token}")
    public void verifyUser(@PathVariable final String token, final HttpServletResponse response) {
        if(userService.verifyUser(token)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.encodeRedirectURL("/authenticate");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping("/{userId}/info")
    public UserInfo getInfo(@PathVariable final String userId) {
//        Backer backer = backerDao.fetchOneById(userId);
//        addressDao.fetchOneById(backer.)
        return null;
    }

    @PostMapping
    public UserDetails createUser(@RequestBody UserInfo user) {
        //@TODO create backer
        LOGGER.info("Creating user: {}", user);
        Long id = backerRepository.createReturningKey(new Backer(null, user.getEmail(), "Testing", "Backer", new Float(5.0), new Float(5.0)));
        //@TODO - implement enabled mail verification
        BackerDetails userToStore = new BackerDetails(user.getEmail(), encoder.encode("user"), id, false);
        userManager.createUser(userToStore);
        return userManager.loadUserByUsername(user.getEmail());
    }


}
