package com.github.kickshare.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author Jan.Kucera
 * @since 8.4.2017
 */
public class BackerDetails extends User {
    private final Long id;

    public BackerDetails(final String username, final String password, final Long id) {
        super(username, password,
                Arrays.asList(new SimpleGrantedAuthority("VIEW_DATA")));
        this.id = id;
    }

    public BackerDetails(final Long id, final String username, final String password, final boolean enabled, final boolean accountNonExpired,
            final boolean credentialsNonExpired, final boolean accountNonLocked,
            final Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
