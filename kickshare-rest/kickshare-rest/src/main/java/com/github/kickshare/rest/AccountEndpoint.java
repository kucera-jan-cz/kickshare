package com.github.kickshare.rest;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.kickshare.db.dao.TokenRepository;
import com.github.kickshare.db.jooq.enums.TokenType;
import com.github.kickshare.db.jooq.tables.pojos.TokenRequest;
import com.github.kickshare.domain.Backer;
import com.github.kickshare.gmail.GMailService;
import com.github.kickshare.rest.user.domain.UserInfo;
import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.service.UserService;
import com.github.kickshare.service.impl.MailService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountEndpoint.class);
    private GMailService gmailService;
    private UserService userService;
    private MailService mailService;
    private TokenRepository tokenRepository;
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    private ConsumerTokenServices consumerTokenServices;


    @GetMapping("/activate/{token}")
    public void verifyUser(@PathVariable final String token, final HttpServletResponse response) {
        if (userService.verifyUser(token)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.encodeRedirectURL("/authenticate");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @PostMapping("/reset")
    public void requestPasswordReset(@RequestParam("email") final String userEmail) {
        //1. create request - (PASSWORD_MAIL)
        ///--- SCHEDULE service
        //2. send email
        //3. change request token to pending token (PASSWORD_MAIL_WAITING)
        String token = UUID.randomUUID().toString();
        Backer backer = userService.getUserByEmail(userEmail);
        TokenRequest request = new TokenRequest(token, backer.getId(), TokenType.PASSWORD_MAIL);
        tokenRepository.insert(request);
    }


    @GetMapping("/reset")
    public void passwordReset(@RequestParam final String token, @RequestParam("email") final String userEmail) {
        //1. generate random password
        //2. store password request
        ///--- SCHEDULE service
        //3. change the password
        //4. send mail
        //5. delete token
        TokenRequest request = tokenRepository.findById(token);
        if (TokenType.PASSWORD_MAIL.equals(request.getTokenType())) {
            request.setTokenType(TokenType.PASSWORD_RESET);
            tokenRepository.update(request);
        } else {
            LOGGER.warn("Invalid type type ({}) for token: {}, mail: {}", request.getTokenType(), token, userEmail);
        }
    }

    @PostMapping("/password")
//    @PreAuthorize("isAuthenticated()")
    public void changePassword(@RequestParam final String password, @AuthenticationPrincipal BackerDetails user) {
        Validate.notNull(user);
        //@TODO validate user enabled and whether isAuthenticated is valid choice
        userService.changePassword(user, password);
    }

    @PostMapping
    public UserDetails createAccount(@RequestBody UserInfo user) {
        Backer backer = new Backer(null, user.getEmail(), user.getName(), user.getSurname(), null, null);
        BackerDetails userDetail = userService.createUser(backer, user.getPassword(), user.getAddress());
        //@TODO - figure out whether to risk failed mail or rather persist the activation
        gmailService.sendActivationMail(user.getEmail(), userDetail.getToken());
        return userDetail;
    }

    @GetMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping("/logout")
    public void logout(Principal principal, HttpServletRequest request, HttpServletResponse response) throws IOException {

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
        OAuth2AccessToken accessToken = authorizationServerTokenServices.getAccessToken(oAuth2Authentication);
        consumerTokenServices.revokeToken(accessToken.getValue());

        String redirectUrl = "http://localhost:4200/cz/dashboard";

        response.setStatus(200);
//        response.sendRedirect(redirectUrl);

        return;
    }
}
