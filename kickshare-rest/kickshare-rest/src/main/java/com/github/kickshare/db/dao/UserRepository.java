package com.github.kickshare.db.dao;

import com.github.kickshare.db.jooq.tables.pojos.Users;
import com.github.kickshare.db.jooq.tables.records.UsersRecord;

/**
 * @author Jan.Kucera
 * @since 17.5.2017
 */
public interface UserRepository extends EnhancedDAO<UsersRecord, Users, String> {
    Users getUserByToken(String token);
}
