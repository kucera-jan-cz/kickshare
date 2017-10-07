package com.github.kickshare;

import com.github.kickshare.db.DatabaseConfiguration;
import com.github.kickshare.kickstarter.KSConfiguration;
import com.github.kickshare.mapper.MappingConfiguration;
import com.github.kickshare.security.SecurityConfig;
import com.github.kickshare.service.ServicesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricExportAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {
        FlywayAutoConfiguration.class, JmxAutoConfiguration.class, DataSourceAutoConfiguration.class,
        MetricExportAutoConfiguration.class, CacheAutoConfiguration.class, SessionAutoConfiguration.class })
@Import({ DatabaseConfiguration.class, SecurityConfig.class, MappingConfiguration.class, KSConfiguration.class,
        ServicesConfiguration.class })
@ComponentScan(basePackages = { "com.github.kickshare.rest", "com.github.kickshare.db" })
@EnableConfigurationProperties
@EnableScheduling
public class KickshareRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KickshareRestApplication.class, args);
    }
}
