package com.github.kickshare.service.impl;

import java.util.List;

import com.github.kickshare.db.dao.NotificationRepository;
import com.github.kickshare.domain.Notification;
import com.github.kickshare.mapper.NotificationMapper;
import com.github.kickshare.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Jan.Kucera
 * @since 24.7.2017
 */
@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> getUserNotification(final Long backerId) {
        return NotificationMapper.MAPPER.toDomain(notificationRepository.getUserNotification(backerId, 10));
    }

    @Override
    public List<Notification> getNotifications(final Long notificationId) {
        return NotificationMapper.MAPPER.toDomain(notificationRepository.getNotifications(notificationId, 100));
    }
}
