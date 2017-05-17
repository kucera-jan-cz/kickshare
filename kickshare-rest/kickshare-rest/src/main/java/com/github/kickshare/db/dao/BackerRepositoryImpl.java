package com.github.kickshare.db.dao;

import static com.github.kickshare.db.h2.Tables.BACKER_LOCATIONS;
import static com.github.kickshare.db.h2.Tables.CITY;

import com.github.kickshare.db.h2.tables.daos.BackerDao;
import com.github.kickshare.db.h2.tables.pojos.Backer;
import com.github.kickshare.db.h2.tables.records.BackerRecord;
import com.github.kickshare.domain.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 26.4.2017
 */
@Repository
public class BackerRepositoryImpl extends AbstractRepository<BackerRecord, Backer, Long> implements BackerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackerRepositoryImpl.class);

    @Autowired
    public BackerRepositoryImpl(final BackerDao dao) {
        super(dao);
    }

    public City getPermanentAddress(Long backerId) {
        City city = this.dsl.select()
                .from(CITY)
                .join(BACKER_LOCATIONS).on(CITY.ID.eq(BACKER_LOCATIONS.CITY_ID))
                .where(BACKER_LOCATIONS.BACKER_ID.eq(backerId))
                    .and(BACKER_LOCATIONS.IS_PERMANENT_ADDRESS.isTrue())
                .fetchOneInto(City.class);
        LOGGER.info("Retrieved permanent city for backer ({}): {}", backerId, city);
        return city;
    }

}
