package com.github.kickshare.security;

import java.util.List;

import com.github.kickshare.common.io.Lists;
import lombok.AllArgsConstructor;
import org.jooq.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Jan.Kucera
 * @since 8.4.2017
 */
@AllArgsConstructor
public class BackerDetailsService implements UserDetailsService {
    private Configuration jooqConfig;
    private ExtendedJdbcUserDetailsManager extendedManager;

    @Override
    public BackerDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        //@TODO - verify that this works as expected
        List<UserDetails> users = extendedManager.loadUsersByUsername(username);
        return (BackerDetails) Lists.first(users);
//        UserAuthDao dao = new UserAuthDao(jooqConfig);
//        UserAuth auth = dao.fetchOne(USER_AUTH.NAME, username);
//        return new BackerDetails(
//                auth.getName(), auth.getPassword(), auth.getUserId(), true
//        );


//        return new BackerDetails(auth.getUserId(), auth.getName(), auth.getPassword(), auth.)

    }
}
