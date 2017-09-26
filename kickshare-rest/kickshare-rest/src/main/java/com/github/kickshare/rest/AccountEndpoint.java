package com.github.kickshare.rest;

import javax.servlet.http.HttpServletResponse;

import com.github.kickshare.domain.Backer;
import com.github.kickshare.rest.user.domain.UserInfo;
import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.service.MailService;
import com.github.kickshare.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 25.9.2017
 */
@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountEndpoint {
    private UserService userService;
    private MailService mailService;

    @GetMapping("/activate/{token}")
    public void verifyUser(@PathVariable final String token, final HttpServletResponse response) {
        if (userService.verifyUser(token)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.encodeRedirectURL("/authenticate");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping("/reset/{token}")
    public void passwordReset(@PathVariable final String token, final HttpServletResponse response) {

    }

    @PostMapping("/reset")
    public void requestPasswordReset(@RequestParam("email") final String userEmail) {
        mailService.sendPasswordResetMail(userEmail);
    }

    @PostMapping
    public UserDetails createAccount(@RequestBody UserInfo user) {
        Backer backer = new Backer(null, user.getEmail(), user.getName(), user.getSurname(), null, null);
        BackerDetails userDetail = userService.createUser(backer, user.getPassword(), user.getAddress());
        //@TODO - figure out whether to risk failed mail or rather persist the activation
        mailService.sendActivationMail(user.getEmail(), userDetail.getToken());
        return userDetail;
    }
}
