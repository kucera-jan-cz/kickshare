package com.github.kickshare.db.dao;

import com.github.kickshare.db.h2.tables.pojos.Backer;
import com.github.kickshare.db.h2.tables.records.BackerRecord;
import com.github.kickshare.domain.City;

/**
 * @author Jan.Kucera
 * @since 17.5.2017
 */
public interface BackerRepository extends EnhancedDAO<BackerRecord, Backer, Long> {
    public City getPermanentAddress(Long backerId);
}
