package com.github.kickshare.db.dao;

import static com.github.kickshare.db.jooq.Tables.NOTIFICATION;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.NotificationDB;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectSeekStep1;
import org.springframework.stereotype.Repository;

/**
 * @author Jan.Kucera
 * @since 24.7.2017
 */
@Repository
@AllArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private DSLContext dsl;

    @Override
    public List<NotificationDB> getUserNotification(final Long backerId, final int size) {
        return dsl.select()
                .from(NOTIFICATION)
                .where(NOTIFICATION.BACKER_ID.eq(backerId))
                .orderBy(NOTIFICATION.ID.desc())
                .limit(size)
                .fetchInto(NotificationDB.class);
    }

    @Override
    public List<NotificationDB> getNotifications(final Long notificationId, final int size) {
        SelectSeekStep1<Record, Long> select = dsl.select()
                .from(NOTIFICATION)
                .orderBy(NOTIFICATION.ID.asc());
        if (notificationId > 0) {
            return select.seekAfter(notificationId)
                    .limit(size)
                    .fetchInto(NotificationDB.class);
        } else {
            return select.limit(size)
                    .fetchInto(NotificationDB.class);
        }
    }
}
