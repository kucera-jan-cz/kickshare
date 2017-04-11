package com.github.kickshare.security;

import static com.github.kickshare.db.h2.Tables.USER_AUTH;

import com.github.kickshare.db.h2.tables.daos.UserAuthDao;
import com.github.kickshare.db.h2.tables.pojos.UserAuth;
import lombok.AllArgsConstructor;
import org.jooq.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Jan.Kucera
 * @since 8.4.2017
 */
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private Configuration jooqConfig;

    @Override
    public CustomUser loadUserByUsername(final String username) throws UsernameNotFoundException {
        //@TODO - make it in Spring??
        UserAuthDao dao = new UserAuthDao(jooqConfig);
        UserAuth auth = dao.fetchOne(USER_AUTH.NAME, username);
//        List<UserAuth> auths = using(jooqConfig)
//                .select()
//                .from(USER_AUTH)
//                .where(USER_AUTH.NAME.eq(username))
//                .fetchInto(UserAuth.class);
////                        (auth ->{
////                    new CustomUser(
////                    auth.field(USER_AUTH.USER_ID, String.class)
////                    );
////                });
//        UserAuth auth = auths.get(0);
        return new CustomUser(
                auth.getName(), auth.getPassword(), auth.getUserId()
//                result.field(USER_AUTH.PASSWORD), result.field(USER_AUTH.USER_ID)
        );

    }
}
