package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.dao.common.EnhancedDAO;
import com.github.kickshare.db.jooq.tables.pojos.CityDB;
import com.github.kickshare.db.jooq.tables.records.CityRecordDB;

/**
 * @author Jan.Kucera
 * @since 17.5.2017
 */
public interface CityRepository extends EnhancedDAO<CityRecordDB, CityDB, Integer> {
    List<CityDB> searchCitiesByName(String name);

    List<CityDB> getCityByBackerId(Long backerId);
}
