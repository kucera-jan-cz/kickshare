package com.github.kickshare.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Jan.Kucera
 * @since 26.6.2017
 */
public interface UserService {
    UserDetails createUser(String email, Integer cityId);

    boolean verifyUser(String token);
}
