package com.github.kickshare.service.impl;

import com.github.kickshare.db.dao.TokenRepository;
import com.github.kickshare.db.jooq.enums.TokenType;
import com.github.kickshare.db.jooq.tables.pojos.TokenRequest;
import com.github.kickshare.gmail.GMailService;
import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.security.ExtendedJdbcUserDetailsManager;
import com.github.kickshare.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan.Kucera
 * @since 23.9.2017
 */
@Service
@AllArgsConstructor
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    private final TokenRepository tokenRepository;
    private final GMailService gmail;
    private final ExtendedJdbcUserDetailsManager userDetailsManager;
    private final UserService userService;

    @Transactional
    public void sendPasswordResetMail(String token) {
        //2. send email
        //3. change request token to pending token (PASSWORD_MAIL_WAITING)
        final TokenRequest tokenRequest = tokenRepository.findById(token);
        BackerDetails user = (BackerDetails) userDetailsManager.loadUserById(tokenRequest.getUserId());
        gmail.sendPasswordResetMail(user.getUsername(), tokenRequest.getToken());
        tokenRequest.setTokenType(TokenType.PASSWORD_MAIL_WAITING);
        tokenRepository.update(tokenRequest);
    }

    @Transactional
    public void resetPassword(String token) {
        final TokenRequest tokenRequest = tokenRepository.findById(token);
        BackerDetails user = (BackerDetails) userDetailsManager.loadUserById(tokenRequest.getUserId());
        final String temporaryPassword = RandomStringUtils.randomAlphanumeric(16);
        userService.changePassword(user, temporaryPassword);
        gmail.sendPasswordGeneratedMail(user.getUsername(), temporaryPassword);
        tokenRepository.deleteById(token);
    }
}
