package com.github.kickshare.service.sse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.kickshare.db.multischema.SchemaContextHolder;
import com.github.kickshare.domain.Notification;
import com.github.kickshare.service.NotificationService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author Jan.Kucera
 * @since 24.7.2017
 */
public class SSENotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SSENotificationService.class);
    private final NotificationService service;
    private final String[] countries;
    private final Map<String, Map<Long, SseEmitter>> emittersMap;
    private final Map<String, Long> latestNotificationMap = new ConcurrentHashMap<>();

    public SSENotificationService(final NotificationService service, final String[] countries) {
        this.service = service;
        this.countries = countries;
        this.emittersMap = new HashMap<>();
        for (String country : countries) {
            this.emittersMap.put(country, new ConcurrentHashMap<>());
            this.latestNotificationMap.put(country, -1L);
        }
    }

    public SseEmitter register(@NonNull final String country, final @NonNull Long userId) {
        SseEmitter emitter = new SseEmitter();
        emitter.onCompletion(() -> emittersMap.get(country).remove(userId));
        emitter.onCompletion(() -> emittersMap.get(country).remove(userId));

        SseEmitter previousEmitter = emittersMap.get(country).putIfAbsent(userId, emitter);
        if (previousEmitter != null) {
            previousEmitter.complete();
        }
        return emitter;
    }

    @Scheduled(initialDelay = 2000, fixedRate = 5_000)
    private void checkNewNotifications() throws IOException {
        for (String country : countries) {
            Map<Long, SseEmitter> emittersPerCountry = emittersMap.get(country);
            if (emittersPerCountry.isEmpty()) {
                continue;
            }
            Long latestId = latestNotificationMap.get(country);
            Set<Long> ids = emittersPerCountry.keySet();

            String previousSchema = SchemaContextHolder.setSchema(country);
            List<Notification> notifications = service.getNotifications(latestId);
            SchemaContextHolder.setSchema(previousSchema);
            for (Notification notification : notifications) {
                Long id = notification.getId();
                if (ids.contains(id)) {
                    sendNotification(emittersPerCountry.get(id), notification);
                }
                latestId = Math.max(latestId, id);
            }
            latestNotificationMap.put(country, latestId);
        }
    }

    private void sendNotification(SseEmitter emitter, Object notification) {
        if (emitter == null) {
            return;
        }
        try {
            emitter.send(notification);
        } catch (IOException e) {
            LOGGER.warn("Failed to notify user", e);
            emitter.complete();
        }
    }
}
