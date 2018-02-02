package com.github.kickshare.rest;

import java.util.List;

import com.github.kickshare.rest.resolver.SeekPageResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Jan.Kucera
 * @since 12.12.2017
 */
@Configuration
@ComponentScan("com.github.kickshare.rest")
public class RESTConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SeekPageResolver());
    }
}
