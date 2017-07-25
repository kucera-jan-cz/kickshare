package com.github.kickshare.service;

import java.util.List;

import com.github.kickshare.domain.Notification;

/**
 * @author Jan.Kucera
 * @since 24.7.2017
 */
public interface NotificationService {
    List<Notification> getUserNotification(Long backerId);

    List<Notification> getNotifications(Long notificationId);
}
