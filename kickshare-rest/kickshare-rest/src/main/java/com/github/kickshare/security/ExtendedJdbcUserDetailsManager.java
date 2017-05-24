package com.github.kickshare.security;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.util.Assert;

/**
 * @author Jan.Kucera
 * @since 21.5.2017
 */
public class ExtendedJdbcUserDetailsManager extends JdbcUserDetailsManager {
    public static final String DEF_CREATE_USER_SQL = "insert into users (id, username, password, enabled) values (?,?,?,?)";
    public static final String DEF_USERS_BY_USERNAME_QUERY = "select id, username,password,enabled from users where username = ?";

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

    @Override
    protected List<UserDetails> loadUsersByUsername(String email) {
        return getJdbcTemplate().query(DEF_USERS_BY_USERNAME_QUERY, new String[]{ email },
                (ResultSet rs, int rowNum) -> {
                    Long id = rs.getLong(1);
                    String username = rs.getString(2);
                    String password = rs.getString(3);
                    boolean enabled = rs.getBoolean(4);
                    return new BackerDetails(id, username, password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
                }
        );
    }

    @Override
    protected UserDetails createUserDetails(String username,
            UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromUserQuery.getUsername();
        Long id = ((BackerDetails) userFromUserQuery).getId();
        return new BackerDetails(id, returnUsername, userFromUserQuery.getPassword(),
                userFromUserQuery.isEnabled(), true, true, true, combinedAuthorities);
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
