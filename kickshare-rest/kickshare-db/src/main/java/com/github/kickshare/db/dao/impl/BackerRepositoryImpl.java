package com.github.kickshare.db.dao.impl;

import static com.github.kickshare.db.jooq.Tables.ADDRESS;
import static com.github.kickshare.db.jooq.Tables.BACKER;
import static com.github.kickshare.db.jooq.Tables.BACKER_2_GROUP;
import static com.github.kickshare.db.jooq.Tables.CITY;
import static com.github.kickshare.db.jooq.Tables.GROUP;

import com.github.kickshare.db.dao.BackerRepository;
import com.github.kickshare.db.dao.common.AbstractRepository;
import com.github.kickshare.db.jooq.enums.GroupRequestStatusDB;
import com.github.kickshare.db.jooq.tables.daos.BackerDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.BackerDB;
import com.github.kickshare.db.jooq.tables.pojos.CityDB;
import com.github.kickshare.db.jooq.tables.records.BackerRecordDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 26.4.2017
 */
@Repository
public class BackerRepositoryImpl extends AbstractRepository<BackerRecordDB, BackerDB, Long> implements BackerRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackerRepositoryImpl.class);

    @Autowired
    public BackerRepositoryImpl(final BackerDaoDB dao) {
        super(dao);
    }

    public CityDB getPermanentAddress(Long backerId) {
//        CityDB city = this.dsl.select()
//                .from(CITY)
//                .join(BACKER_LOCATION).on(CITY.ID.eq(BACKER_LOCATION.CITY_ID))
//                .where(BACKER_LOCATION.BACKER_ID.eq(backerId))
//                .fetchOneInto(CityDB.class);

        CityDB city = this.dsl.select()
                .from(CITY)
                .join(ADDRESS).on(CITY.ID.eq(ADDRESS.CITY_ID))
                .where(ADDRESS.BACKER_ID.eq(backerId))
                .fetchOneInto(CityDB.class);
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
                        .and(BACKER_2_GROUP.STATUS.eq(GroupRequestStatusDB.APPROVED))
                        .and(BACKER_2_GROUP.GROUP_ID.eq(groupId))
        );
    }

    public BackerDB findByEmail(final String email) {
        return this.dsl
                .select()
                .from(BACKER)
                .where(BACKER.EMAIL.eq(email))
                .fetchOneInto(BackerDB.class);
    }
}
