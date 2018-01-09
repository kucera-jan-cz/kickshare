package com.github.kickshare.ext.service.configuration;

import com.github.kickshare.ext.service.common.ResourceBuilder;
import com.github.kickshare.ext.service.common.ResourceStream;
import com.github.kickshare.ext.service.kickstarter.backer.KickstarterBackerService;
import com.github.kickshare.ext.service.kickstarter.backer.impl.KickstarterHtmlBackerService;
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
@ComponentScan("com.github.kickshare.ext.service.kickstarter.*")
public class KickstarterConfiguration {
    @Bean
    public ClientHttpRequestFactory requestFactory() {
        return new SimpleClientHttpRequestFactory();
    }

    @Bean
    public KickstarterBackerService backerService(ClientHttpRequestFactory httpRequestFactory) {
        final ResourceBuilder resourceBuilder = new ResourceStream();
        return new KickstarterHtmlBackerService(resourceBuilder, httpRequestFactory);
    }
}
