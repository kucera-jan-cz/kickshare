package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.BACKER_RATING;
import static com.github.kickshare.db.jooq.Tables.LEADER_RATING;

import com.github.kickshare.db.jooq.tables.pojos.BackerRatingDB;
import com.github.kickshare.db.jooq.tables.pojos.LeaderRatingDB;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 12.1.2018
 */
@Repository
@AllArgsConstructor
public class RatingRepositoryImpl implements RatingRepository {
    private DSLContext dsl;

    @Override
    public void rateLeader(final LeaderRatingDB rating) {
        insertIgnoringDuplicates(LEADER_RATING, rating);
    }

    @Override
    public void rateBacker(final BackerRatingDB rating) {
        insertIgnoringDuplicates(BACKER_RATING, rating);
    }

    @Override
    public void deleteLeaderRating(final Long authorId, final Long groupId, final Long leaderId) {
        dsl
                .delete(LEADER_RATING)
                .where(
                        LEADER_RATING.AUTHOR_ID.eq(authorId)
                                .and(LEADER_RATING.GROUP_ID.eq(groupId))
                                .and(LEADER_RATING.LEADER_ID.eq(leaderId))
                ).execute();
    }

    @Override
    public void deleteBackerRating(final Long authorId, final Long groupId, final Long backerId) {
        dsl
                .delete(BACKER_RATING)
                .where(
                        BACKER_RATING.AUTHOR_ID.eq(authorId)
                                .and(BACKER_RATING.GROUP_ID.eq(groupId))
                                .and(BACKER_RATING.BACKER_ID.eq(backerId))
                ).execute();
    }

    private <R extends UpdatableRecord<R>, P> void insertIgnoringDuplicates(Table<R> table, P entity) {
        InsertQuery<R> insert = dsl.insertQuery(table);
        insert.addRecord(dsl.newRecord(table, entity));
        insert.onDuplicateKeyIgnore(true);
        insert.execute();
    }
}
