package com.github.kickshare.service;

import com.github.kickshare.service.sse.SSENotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jan.Kucera
 * @since 24.7.2017
 */
@Configuration
@ComponentScan(basePackages = { "com.github.kickshare.service"})
public class ServicesConfiguration {
    @Bean
    public SSENotificationService sseNotificationService(@Autowired final NotificationService service, @Value("${kickshare.flyway.schemas}") final String schemas) {
        return new SSENotificationService(service, schemas.split(","));
    }
}
