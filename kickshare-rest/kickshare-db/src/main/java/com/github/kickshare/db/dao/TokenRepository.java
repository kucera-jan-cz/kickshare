package com.github.kickshare.db.dao;

import com.github.kickshare.db.dao.common.EnhancedDAO;
import com.github.kickshare.db.jooq.tables.pojos.TokenRequestDB;
import com.github.kickshare.db.jooq.tables.records.TokenRequestRecordDB;

/**
 * @author Jan.Kucera
 * @since 29.9.2017
 */
public interface TokenRepository extends EnhancedDAO<TokenRequestRecordDB, TokenRequestDB, String> {
}
