package com.github.kickshare.service;

import com.github.kickshare.ext.service.configuration.KickstarterConfiguration;
import com.github.kickshare.service.sse.SSENotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Jan.Kucera
 * @since 24.7.2017
 */
@Configuration
@Import(KickstarterConfiguration.class)
@ComponentScan(basePackages = { "com.github.kickshare.service", "com.github.kickshare.gmail" })

public class ServicesConfiguration {

    @Bean
    public SSENotificationService sseNotificationService(@Autowired final NotificationService service,
            @Value("${kickshare.flyway.schemas}") final String schemas) {
        return new SSENotificationService(service, schemas.split(","));
    }
}
