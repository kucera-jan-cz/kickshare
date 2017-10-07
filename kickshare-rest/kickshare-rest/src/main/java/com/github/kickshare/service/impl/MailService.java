package com.github.kickshare.service.impl;

import com.github.kickshare.db.dao.BackerRepository;
import com.github.kickshare.db.dao.TokenRepository;
import com.github.kickshare.db.dao.UserRepository;
import com.github.kickshare.db.jooq.tables.pojos.TokenRequest;
import com.github.kickshare.db.jooq.tables.pojos.Users;
import com.github.kickshare.gmail.GMailService;
import lombok.AllArgsConstructor;
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
    private final BackerRepository backerRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final GMailService gmail;

    @Transactional
    public void resetPassword(String token) {
        //@TODO - implement it as token unique id
        final TokenRequest tokenRequest = tokenRepository.findById(token);
        Users user =  userRepository.findById(tokenRequest.getUserId());
        final String temporaryPassword = "";
        user.setPassword(temporaryPassword);
        userRepository.update(user);
        gmail.sendPasswordGeneratedMail(user.getUsername(), temporaryPassword);
        tokenRepository.deleteById(token);
    }
}
