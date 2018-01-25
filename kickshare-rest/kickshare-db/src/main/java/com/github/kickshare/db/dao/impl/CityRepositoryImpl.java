package com.github.kickshare.db.dao.impl;

import static com.github.kickshare.db.jooq.Tables.BACKER_LOCATION;
import static com.github.kickshare.db.jooq.Tables.CITY;

import java.util.List;

import com.github.kickshare.db.dao.CityRepository;
import com.github.kickshare.db.dao.common.AbstractRepository;
import com.github.kickshare.db.jooq.tables.pojos.CityDB;
import com.github.kickshare.db.jooq.tables.records.CityRecordDB;
import org.jooq.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 23.11.2017
 */
@Repository
public class CityRepositoryImpl extends AbstractRepository<CityRecordDB, CityDB, Integer> implements CityRepository {

    @Autowired
    public CityRepositoryImpl(final DAO<CityRecordDB, CityDB, Integer> dao) {
        super(dao);
    }

    public List<CityDB> searchCitiesByName(String name) {
        final List<CityDB> cities = dsl.select()
                .from(CITY)
                .where(CITY.NAME.like('%' + name + '%'))
                .fetchInto(CityDB.class);
        return cities;
    }

    @Override
    public List<CityDB> getCityByBackerId(final Long backerId) {
        return dsl.select()
                .from(CITY)
                .join(BACKER_LOCATION).on(CITY.ID.eq(BACKER_LOCATION.CITY_ID))
                .where(BACKER_LOCATION.BACKER_ID.eq(backerId))
                .fetchInto(CityDB.class);
    }
}
