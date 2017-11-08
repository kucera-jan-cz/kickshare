package com.github.kickshare.db.dao;

import com.github.kickshare.db.jooq.tables.daos.TokenRequestDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.TokenRequestDB;
import com.github.kickshare.db.jooq.tables.records.TokenRequestRecordDB;
import org.springframework.stereotype.Service;

/**
 * @author Jan.Kucera
 * @since 29.9.2017
 */
@Service
public class TokenRepositoryImpl extends AbstractRepository<TokenRequestRecordDB, TokenRequestDB, String> implements TokenRepository {

    public TokenRepositoryImpl(TokenRequestDaoDB dao) {
        super(dao);
    }
}
