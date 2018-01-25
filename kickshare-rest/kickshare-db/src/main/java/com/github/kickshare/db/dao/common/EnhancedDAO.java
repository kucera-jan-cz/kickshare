package com.github.kickshare.db.dao.common;

import org.jooq.DAO;
import org.jooq.TableRecord;

/**
 * @author Jan.Kucera
 * @since 6.4.2017
 */
public interface EnhancedDAO<R extends TableRecord<R>, P, T> extends DAO<R, P, T> {
    T createReturningKey(P entity);

    R createReturning(P entity);

    void insertIgnoringDuplicates(P entity);
}
