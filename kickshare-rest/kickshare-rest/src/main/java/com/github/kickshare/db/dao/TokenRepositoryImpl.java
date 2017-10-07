package com.github.kickshare.db.dao;

import com.github.kickshare.db.jooq.tables.daos.TokenRequestDao;
import com.github.kickshare.db.jooq.tables.pojos.TokenRequest;
import com.github.kickshare.db.jooq.tables.records.TokenRequestRecord;
import org.springframework.stereotype.Service;

/**
 * @author Jan.Kucera
 * @since 29.9.2017
 */
@Service
public class TokenRepositoryImpl extends AbstractRepository<TokenRequestRecord, TokenRequest, String> implements TokenRepository {

    public TokenRepositoryImpl(TokenRequestDao dao) {
        super(dao);
    }
}
