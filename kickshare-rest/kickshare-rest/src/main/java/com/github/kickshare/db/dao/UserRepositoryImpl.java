package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.USERS;

import com.github.kickshare.db.jooq.tables.daos.UsersDao;
import com.github.kickshare.db.jooq.tables.pojos.Users;
import com.github.kickshare.db.jooq.tables.records.UsersRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 26.6.2017
 */
@Repository
public class UserRepositoryImpl extends AbstractRepository<UsersRecord, Users, String> implements UserRepository {
    @Autowired
    public UserRepositoryImpl(final UsersDao dao) {
        super(dao);
    }

    @Override
    public Users getUserByToken(String token) {
        return this.dsl.select().from(USERS).where(USERS.TOKEN.eq(token)).fetchOneInto(Users.class);
    }
}
