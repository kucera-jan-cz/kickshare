package com.github.kickshare.kickstarter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * @author Jan.Kucera
 * @since 7.4.2017
 */
@Configuration
@ComponentScan("com.github.kickshare.kickstarter")
public class KSConfiguration {
    @Bean
    public ClientHttpRequestFactory requestFactory() {
        return new SimpleClientHttpRequestFactory();
    }
}
