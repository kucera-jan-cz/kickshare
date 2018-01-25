package com.github.kickshare.db.dao;

import com.github.kickshare.db.dao.common.EnhancedDAO;
import com.github.kickshare.db.jooq.tables.pojos.BackerDB;
import com.github.kickshare.db.jooq.tables.pojos.CityDB;
import com.github.kickshare.db.jooq.tables.records.BackerRecordDB;

/**
 * @author Jan.Kucera
 * @since 17.5.2017
 */
public interface BackerRepository extends EnhancedDAO<BackerRecordDB, BackerDB, Long> {
    CityDB getPermanentAddress(Long backerId);

    boolean ownGroup(Long leaderId, Long groupId);

    boolean isGroupMember(final Long backerId, final Long groupId);

    BackerDB findByEmail(final String email);
}
