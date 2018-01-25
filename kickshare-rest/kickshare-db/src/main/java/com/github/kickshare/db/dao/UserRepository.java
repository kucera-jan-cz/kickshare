package com.github.kickshare.db.dao;

import com.github.kickshare.db.dao.common.EnhancedDAO;
import com.github.kickshare.db.jooq.tables.pojos.UsersDB;
import com.github.kickshare.db.jooq.tables.records.UsersRecordDB;

/**
 * @author Jan.Kucera
 * @since 17.5.2017
 */
public interface UserRepository extends EnhancedDAO<UsersRecordDB, UsersDB, Long> {
    UsersDB getUserByToken(String token);
}
