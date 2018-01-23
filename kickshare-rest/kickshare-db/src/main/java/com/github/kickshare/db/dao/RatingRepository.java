package com.github.kickshare.db.dao;

import com.github.kickshare.db.jooq.tables.pojos.BackerRatingDB;
import com.github.kickshare.db.jooq.tables.pojos.LeaderRatingDB;

/**
 * @author Jan.Kucera
 * @since 12.1.2018
 */
public interface RatingRepository {

    void rateLeader(LeaderRatingDB rating);

    void rateBacker(BackerRatingDB rating);

    void deleteLeaderRating(final Long authorId, final Long groupId, final Long leaderId);

    void deleteBackerRating(final Long authorId, final Long groupId, final Long backerId);
}
