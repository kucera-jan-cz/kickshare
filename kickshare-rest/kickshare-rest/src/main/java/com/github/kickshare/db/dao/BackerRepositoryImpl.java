package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.ADDRESS;
import static com.github.kickshare.db.jooq.Tables.BACKER;
import static com.github.kickshare.db.jooq.Tables.BACKER_2_GROUP;
import static com.github.kickshare.db.jooq.Tables.CITY;
import static com.github.kickshare.db.jooq.Tables.GROUP;

import com.github.kickshare.db.jooq.enums.GroupRequestStatus;
import com.github.kickshare.db.jooq.tables.daos.BackerDao;
import com.github.kickshare.db.jooq.tables.pojos.Backer;
import com.github.kickshare.db.jooq.tables.records.BackerRecord;
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
//        City city = this.dsl.select()
//                .from(CITY)
//                .join(BACKER_LOCATION).on(CITY.ID.eq(BACKER_LOCATION.CITY_ID))
//                .where(BACKER_LOCATION.BACKER_ID.eq(backerId))
//                .fetchOneInto(City.class);

        City city = this.dsl.select()
                .from(CITY)
                .join(ADDRESS).on(CITY.ID.eq(ADDRESS.CITY_ID))
                .where(ADDRESS.BACKER_ID.eq(backerId))
                .fetchOneInto(City.class);
        LOGGER.info("Retrieved permanent city for backer ({}): {}", backerId, city);
        return city;

    }

    public boolean ownGroup(Long leaderId, Long groupId) {
        return this.dsl.fetchExists(
                this.dsl.selectOne()
                        .from(GROUP)
                        .where(GROUP.LEADER_ID.eq(leaderId))
                        .and(GROUP.ID.eq(groupId))
        );
    }

    public boolean isGroupMember(final Long backerId, final Long groupId) {
        return this.dsl.fetchExists(
                this.dsl.selectOne()
                        .from(BACKER_2_GROUP)
                        .where(BACKER_2_GROUP.BACKER_ID.eq(backerId))
                        .and(BACKER_2_GROUP.STATUS.eq(GroupRequestStatus.APPROVED))
                        .and(GROUP.ID.eq(groupId))
        );
    }

    public Backer findByEmail(final String email) {
        return this.dsl
                .select()
                .from(BACKER)
                .where(BACKER.EMAIL.eq(email))
                .fetchOneInto(Backer.class);
    }
}
