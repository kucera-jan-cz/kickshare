package com.github.kickshare.db.dao;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.NotificationDB;

/**
 * @author Jan.Kucera
 * @since 24.7.2017
 */
public interface NotificationRepository {
    List<NotificationDB> getUserNotification(final Long backerId, final int size);

    List<NotificationDB> getNotifications(final Long notificationId, final int size);
}
