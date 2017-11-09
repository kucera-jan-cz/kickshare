package com.github.kickshare.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jan.Kucera
 * @since 4.4.2017
 */
@Configuration
@ComponentScan("com.github.kickshare.mapper")
public class MappingConfiguration {

    @Autowired
    public void initEntityMapper(EntityMapper mapper) {
        EntityMapper.setInstance(mapper);
    }
}
