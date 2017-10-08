package com.github.kickshare.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author Jan.Kucera
 * @since 8.4.2017
 */
public class BackerDetails extends User {
    private final Long id;
    //@TODO - token will be probably deleted in favor of password generation
    private final String token;

    public BackerDetails(final String username, final String password, final Long id, final boolean enabled) {
        super(username, password, enabled, true, true, true, Arrays.asList(new SimpleGrantedAuthority("VIEW_DATA")));
        this.id = id;
        this.token = UUID.randomUUID().toString();
    }

    public BackerDetails(final Long id, final String username, final String password, final boolean enabled, final boolean accountNonExpired,
            final boolean credentialsNonExpired, final boolean accountNonLocked,
            final Collection<? extends GrantedAuthority> authorities, final String token) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.token = token;
    }

    public BackerDetails(final BackerDetails orig, final String password) {
        super(orig.getUsername(), password, orig.isEnabled(), orig.isAccountNonExpired(), orig.isCredentialsNonExpired(), orig.isAccountNonLocked(),
                orig.getAuthorities());
        this.id = orig.id;
        this.token = orig.token;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
