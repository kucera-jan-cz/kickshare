package com.github.kickshare.rest.user;

import com.github.kickshare.rest.user.domain.UserInfo;
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
@RequestMapping("/users")
public class UserEndpoint {

    @GetMapping("/{userId}/info")
    public UserInfo getInfo(@PathVariable String userId) {
        return null;
    }

    @PostMapping
    public String createUser(@RequestBody UserInfo user) {
        return null;
    }

}
