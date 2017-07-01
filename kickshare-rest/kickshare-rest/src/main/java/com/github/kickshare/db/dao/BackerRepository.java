package com.github.kickshare.db.dao;

import com.github.kickshare.db.jooq.tables.pojos.Backer;
import com.github.kickshare.db.jooq.tables.records.BackerRecord;
import com.github.kickshare.domain.City;

/**
 * @author Jan.Kucera
 * @since 17.5.2017
 */
public interface BackerRepository extends EnhancedDAO<BackerRecord, Backer, Long> {
    City getPermanentAddress(Long backerId);

    boolean ownGroup(Long leaderId, Long groupId);

    boolean isGroupMember(final Long backerId, final Long groupId);
}
