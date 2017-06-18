package com.github.kickshare.security;

import static com.github.kickshare.db.jooq.Tables.USER_AUTH;

import com.github.kickshare.db.jooq.tables.daos.UserAuthDao;
import com.github.kickshare.db.jooq.tables.pojos.UserAuth;
import lombok.AllArgsConstructor;
import org.jooq.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Jan.Kucera
 * @since 8.4.2017
 */
@AllArgsConstructor
public class BackerDetailsService implements UserDetailsService {
    private Configuration jooqConfig;

    @Override
    public BackerDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        UserAuthDao dao = new UserAuthDao(jooqConfig);
        UserAuth auth = dao.fetchOne(USER_AUTH.NAME, username);
        return new BackerDetails(
                auth.getName(), auth.getPassword(), auth.getUserId()
        );

    }
}
