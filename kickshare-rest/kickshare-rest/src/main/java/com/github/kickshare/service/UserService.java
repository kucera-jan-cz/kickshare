package com.github.kickshare.service;

import com.github.kickshare.domain.Address;
import com.github.kickshare.domain.Backer;
import com.github.kickshare.security.BackerDetails;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Jan.Kucera
 * @since 26.6.2017
 */
public interface UserService {
    UserDetails createUser(String email, Integer cityId);

    BackerDetails createUser(Backer backer, String password, Address address);

    boolean verifyUser(String token);
}
