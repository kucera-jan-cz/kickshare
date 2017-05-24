package com.github.kickshare;

import com.github.kickshare.db.DAOConfiguration;
import com.github.kickshare.db.JooqConfiguration;
import com.github.kickshare.kickstarter.KSConfiguration;
import com.github.kickshare.mapper.MappingConfiguration;
import com.github.kickshare.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricExportAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {
        FlywayAutoConfiguration.class, JmxAutoConfiguration.class,
        MetricExportAutoConfiguration.class, CacheAutoConfiguration.class })
@Import({ JooqConfiguration.class, DAOConfiguration.class, SecurityConfig.class, MappingConfiguration.class, KSConfiguration.class, })
@ComponentScan(basePackages = { "com.github.kickshare.rest", "com.github.kickshare.service", "com.github.kickshare.db" })
@EnableConfigurationProperties
public class KickshareRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KickshareRestApplication.class, args);
    }
}
