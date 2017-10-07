package com.github.kickshare.db.dao;

import com.github.kickshare.db.jooq.tables.pojos.TokenRequest;
import com.github.kickshare.db.jooq.tables.records.TokenRequestRecord;

/**
 * @author Jan.Kucera
 * @since 29.9.2017
 */
public interface TokenRepository extends EnhancedDAO<TokenRequestRecord, TokenRequest, String> {
}
