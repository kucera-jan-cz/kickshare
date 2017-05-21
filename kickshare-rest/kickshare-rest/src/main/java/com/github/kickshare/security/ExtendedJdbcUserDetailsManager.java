package com.github.kickshare.security;

import java.sql.PreparedStatement;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.util.Assert;

/**
 * @author Jan.Kucera
 * @since 21.5.2017
 */
public class ExtendedJdbcUserDetailsManager extends JdbcUserDetailsManager {
    public static final String DEF_CREATE_USER_SQL = "insert into users (id, username, password, enabled) values (?,?,?,?)";

    @Override
    public void createUser(final UserDetails userDetails) {
        validateUserDetails(userDetails);
        BackerDetails user = (BackerDetails) userDetails;
        getJdbcTemplate().update(DEF_CREATE_USER_SQL, (PreparedStatement ps) -> {
                    ps.setLong(1, user.getId());
                    ps.setString(2, user.getUsername());
                    ps.setString(3, user.getPassword());
                    ps.setBoolean(4, user.isEnabled());
                }
        );

        if (getEnableAuthorities()) {
            insertUserAuthorities(user);
        }
    }


    private void insertUserAuthorities(UserDetails user) {
        for (GrantedAuthority auth : user.getAuthorities()) {
            getJdbcTemplate().update(DEF_INSERT_AUTHORITY_SQL, user.getUsername(),
                    auth.getAuthority());
        }
    }

    private void validateUserDetails(UserDetails user) {
        Assert.isTrue(BackerDetails.class.isInstance(user), "Expecting user details with id");
    }
}
