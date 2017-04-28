package com.github.kickshare.db.dao;

import com.github.kickshare.db.h2.tables.daos.BackerDao;
import com.github.kickshare.db.h2.tables.pojos.Backer;
import com.github.kickshare.db.h2.tables.records.BackerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 26.4.2017
 */
@Repository
public class BackerRepositoryImpl extends AbstractRepository<BackerRecord, Backer, Long> {

    @Autowired
    public BackerRepositoryImpl(final BackerDao dao) {
        super(dao);
    }
}
