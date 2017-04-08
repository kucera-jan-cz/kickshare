package com.github.kickshare.security;

import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author Jan.Kucera
 * @since 8.4.2017
 */
public class CustomUser extends User {
    private final Long id;

    public CustomUser(final String username, final String password, final Long id) {
        super(username, password,
                Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ACTUATOR")));
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
