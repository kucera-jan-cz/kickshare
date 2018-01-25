package com.github.kickshare.db.dao.impl;

import static com.github.kickshare.db.jooq.Tables.USERS;

import com.github.kickshare.db.dao.UserRepository;
import com.github.kickshare.db.dao.common.AbstractRepository;
import com.github.kickshare.db.jooq.tables.daos.UsersDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.UsersDB;
import com.github.kickshare.db.jooq.tables.records.UsersRecordDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 26.6.2017
 */
@Repository
public class UserRepositoryImpl extends AbstractRepository<UsersRecordDB, UsersDB, Long> implements UserRepository {
    @Autowired
    public UserRepositoryImpl(final UsersDaoDB dao) {
        super(dao);
    }

    @Override
    public UsersDB getUserByToken(String token) {
        return this.dsl.select().from(USERS).where(USERS.TOKEN.eq(token)).fetchOneInto(UsersDB.class);
    }
}
